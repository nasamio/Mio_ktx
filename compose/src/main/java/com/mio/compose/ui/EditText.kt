package com.mio.compose.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun EditText(
    value: String = "",
    label: String = "用户名",
    onValueChange: (String) -> Unit = {},
    icon: ImageVector = Icons.Filled.Lock,
) {
    Icons.Default.Lock
    Row(
        modifier = Modifier.padding(
            start = 80.dp,
            end = 80.dp
        )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
//                        .weight(.6f, false)
                .padding(vertical = 8.dp),
            label = { Text(label) },
            leadingIcon = { Icon(icon, null) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
//                    onNext = { LocalSoftwareKeyboardController.current?.focusByKey(ImeAction.Next) }
            )
        )
    }
}
