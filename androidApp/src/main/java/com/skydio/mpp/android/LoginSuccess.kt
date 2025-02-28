package com.skydio.mpp.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.getAppTheme

@Composable
fun LoginSuccess(auth: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getAppTheme().colors.appBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(vertical = 32.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SkydioText(
                text = "Login success",
                style = TextStyle.Default.copy(fontSize = 32.sp),
                color = Color.White,
            )

            Spacer(modifier = Modifier.size(32.dp))

            SkydioText(
                text = auth,
                style = TextStyle.Default.copy(fontSize = 16.sp),
                color = Color.White,
            )

        }
    }
}
