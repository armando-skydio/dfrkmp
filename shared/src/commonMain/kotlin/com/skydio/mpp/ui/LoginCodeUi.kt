package com.skydio.mpp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydio.mpp.login.LoginViewModel
import org.koin.compose.koinInject

@Composable
fun LoginCodeUi(
    state: LoginState,
    vm: LoginViewModel = koinInject(),
) {
    var code by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = TextStyle.Default.copy(fontSize = 32.sp),
                color = Color.Black,
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Login code") },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )


            if (state == LoginState.ErrorCode) {
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    text = "Something went wrong!",
                    style = TextStyle.Default.copy(fontSize = 20.sp),
                    color = Color.Red,
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            Button(
                onClick = {
                    if (state != LoginState.LoadingCode) {
                        vm.verifyLoginCode(code)
                    }
                }
            ) {
                if (state !== LoginState.LoadingCode) {
                    Text(
                        text = "Login",
                        style = TextStyle.Default.copy(fontSize = 14.sp),
                        color = Color.White,
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color.White)
                }
            }

        }
    }
}
