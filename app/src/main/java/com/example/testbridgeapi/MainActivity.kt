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

class BridgeApi {
    @JavascriptInterface
    fun bridgeRequest(apiCommonRequestJsonString: String): String {
        Log.d("BridgeApi", "bridgeRequest: $apiCommonRequestJsonString")
        return router.bridgeRequest(apiCommonRequestJsonString)
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
                val bridgeApi = BridgeApi()
                addJavascriptInterface(bridgeApi, "BridgeApi")
            }
        },
        update = {
            it.loadUrl(mUrl)
        }
    )
}