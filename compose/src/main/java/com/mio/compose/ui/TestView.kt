package com.mio.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mio.compose.R
import com.mio.compose.ui.theme.Mio_ktxTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, DelicateCoroutinesApi::class)
@Composable
fun HomeScreen() {
    val bottomState =
        androidx.compose.material.rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)

    ModalBottomSheetLayout(
        sheetContent = {
            Text("Hi there!")
        },
        sheetState = bottomState,
        modifier = Modifier.clickable {
            GlobalScope.launch {
                if (bottomState.isVisible) {
                    bottomState.hide()
                } else {
                    bottomState.show()
                }
            }
        }
    ) {
        // Content of the screen
        Image(painterResource(R.drawable.img11), contentDescription = null)
    }
    LaunchedEffect(bottomState) {
        bottomState.show()
    }
}


//@Preview
@Composable
fun JetpackCompose() {
    Card {
        var expanded by remember { mutableStateOf(false) }
        Column(Modifier.clickable { expanded = !expanded }) {
            Image(painterResource(R.drawable.img11), contentDescription = null)
            AnimatedVisibility(expanded) {
                Text(
                    "Hello",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun Greeting() {

    val name = "你好"
    val modifier = Modifier
    Text(
        text = "Hello $name!",
        modifier = modifier,

        )
}

//@Preview(showBackground = true)
@Composable
fun test() {
    Text(
        text = "1233",
        modifier = Modifier
    )
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mio_ktxTheme {
        Greeting()
    }
}