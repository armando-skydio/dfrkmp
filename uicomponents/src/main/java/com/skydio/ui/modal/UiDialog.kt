package com.skydio.ui.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.skydio.ui.components.widget.buttons.PrimaryButton
import com.skydio.ui.components.widget.buttons.SecondaryButton
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

@Composable
fun UiDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    dismissText: String?,
    confirmText: String,
    theme: AppTheme = getAppTheme()
) {
    Dialog(onDismissRequest = { onDismissRequest() }, ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(220.dp)
                .background(theme.colors.defaultWidgetBackgroundColor)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentSize(
                ).align(Alignment.TopCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text(
                    text = dialogTitle,
                    style = theme.typography.headlineMedium
                )
                Text(
                    text = dialogText,
                    style = theme.typography.body
                )
            }
            Column(
                modifier = Modifier.wrapContentSize().align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    dismissText?.also {dismissBtn ->
                        SecondaryButton(
                            label = dismissBtn,
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(32.dp),
                            onClick = {
                                onDismissRequest()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    PrimaryButton(
                        label = confirmText,
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(32.dp),
                        onClick = {
                            onConfirmation()
                        }
                    )
                }
            }
        }
    }
}
