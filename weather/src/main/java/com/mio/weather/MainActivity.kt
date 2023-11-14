package com.mio.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mio.weather.ui.theme.Mio_ktxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mio_ktxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

/**
 * 通过State进行控制的Loading、Content、Error页面
 *
 * @param playState 数据State
 * @param onErrorClick 错误时的点击事件
 * @param content 数据加载成功时应显示的可组合项
 */
@Composable
fun <T> LcePage(
    playState: PlayState<T>,
    onErrorClick: () -> Unit,
    content: @Composable (T) -> Unit
) = when (playState) {
    PlayLoading -> {
        LoadingContent()
    }
    is PlayError -> {
        ErrorContent(onErrorClick = onErrorClick)
    }
    is PlaySuccess<T> -> {
        content(playState.data)
    }
}

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    onErrorClick: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.weather_error)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Gray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(130.dp),
            progress = progress
        )
        Button(onClick = onErrorClick) {
            Text(text = stringResource(id = R.string.bad_network_view_tip))
        }
    }
}


fun LoadingContent() {

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mio_ktxTheme {
        Greeting("Android")
    }
}