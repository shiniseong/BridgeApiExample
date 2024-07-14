package com.example.testbridgeapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.testbridgeapi.controller.router
import com.example.testbridgeapi.controller.serializer.serializeToJson
import com.example.testbridgeapi.ui.theme.TestBridgeApiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.imtsoft.bridgeApi.util.generateRejectScript
import org.imtsoft.bridgeApi.util.generateResolveScript

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestBridgeApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WebViewScreen()
                }
            }
        }
    }
}

private fun WebView.resolveAsyncPromise(promiseId: String, responseJsonString: String) {
    post { evaluateJavascript(generateResolveScript(promiseId, responseJsonString), null) }
}

private fun WebView.rejectAsyncPromise(promiseId: String, errorJsonString: String) {
    post { evaluateJavascript(generateRejectScript(promiseId, errorJsonString), null) }
}

class BridgeApi(private val webView: WebView) {
    @JavascriptInterface
    fun bridgeRequest(promiseId: String, apiCommonRequestJsonString: String) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                Log.d("BridgeApi", "bridgeRequest: $apiCommonRequestJsonString")
                val result = router.bridgeRequest(apiCommonRequestJsonString)
                Log.d("BridgeApi", "bridgeRequest result: $result")
                webView.resolveAsyncPromise(promiseId, result)
            } catch (e: Exception) {
                Log.e("BridgeApi", "bridgeRequest error: ${e.message}", e)
                webView.rejectAsyncPromise(promiseId, e.serializeToJson())
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen() {
    val mUrl = "http://192.168.219.100:5173/"
    AndroidView(
        factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                loadUrl(mUrl)
                val bridgeApi = BridgeApi(this)
                addJavascriptInterface(bridgeApi, "BridgeApi")
            }
        },
        update = {
            it.loadUrl(mUrl)
        }
    )
}