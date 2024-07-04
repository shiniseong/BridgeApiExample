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
import com.example.testbridgeapi.ui.theme.TestBridgeApiTheme
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

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

class BridgeApi(private val webView: WebView) {
    private val executor = Executors.newSingleThreadExecutor()

    @JavascriptInterface
    fun bridgeRequest(promiseId: String, apiCommonRequestJsonString: String) {
        executor.execute {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("BridgeApi", "bridgeRequest: $apiCommonRequestJsonString")
                val result = router.bridgeRequest(apiCommonRequestJsonString)
                Log.d("BridgeApi", "bridgeRequest result: $result")
                resolveAsyncPromise(promiseId, result)
            }
        }
    }

    private fun resolveAsyncPromise(callbackId: String, responseJsonString: String) {
        webView.post {
            webView.evaluateJavascript("resolveAsyncPromise('$callbackId', '$responseJsonString')", null)
        }
    }
}

class SleepApi {
    @JavascriptInterface
    suspend fun sleep(): String {
        Log.d("SleepApi", "sleep")
        return withContext(Dispatchers.Main) {
            delay(10)
            "sleep"
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen() {
    val mUrl = "http://192.168.10.138:5173/"
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
                val sleepApi = SleepApi()
                addJavascriptInterface(bridgeApi, "BridgeApi")
                addJavascriptInterface(sleepApi, "SleepApi")
            }
        },
        update = {
            it.loadUrl(mUrl)
        }
    )
}