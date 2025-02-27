package com.skydio.mpp.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.getAppTheme

@Composable
fun LoginEmailUi(
    state: LoginState,
    vm: LoginViewModel = viewModel { LoginViewModel() },
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getAppTheme().colors.appBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SkydioText(
                text = "Login",
                style = TextStyle.Default.copy(fontSize = 32.sp),
                color = Color.White,
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.size(32.dp))

            Button(
                onClick = {
                    if (state != LoginState.LoadingEmail) {
                        //println("$email")
                        vm.requestLoginCode(email)
                    }
                }
            ) {
                if (state != LoginState.LoadingEmail) {
                    CircularProgressIndicator()
                } else {
                    SkydioText(
                        text = "Get login code",
                        style = TextStyle.Default.copy(fontSize = 14.sp),
                        color = Color.White,
                    )
                }
            }

        }
    }
}
