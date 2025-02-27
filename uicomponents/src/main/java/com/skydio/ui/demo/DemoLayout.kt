package com.skydio.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.container.SkydioCard
import com.skydio.ui.components.widget.buttons.SkydioButton
import com.skydio.ui.components.widget.indicators.ProgressIndicatorState
import com.skydio.ui.components.widget.indicators.SkydioCircularProgressIndicator
import com.skydio.ui.components.widget.indicators.SkydioLoadingIndicator
import com.skydio.ui.components.widget.indicators.SkydioProgressIndicator
import com.skydio.ui.components.widget.inputs.SkydioTextInput
import com.skydio.ui.components.widget.selectors.SkydioDropdownSelector
import com.skydio.ui.components.widget.selectors.SkydioTextSegmentControl
import com.skydio.ui.components.widget.toggles.SkydioCheckbox
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray100
import com.skydio.ui.designsystem.Gray900
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.extension.noRippleClickable

@Composable
fun DemoLayout(
    viewModel: DemoViewModel
) {
    val focusManager = LocalFocusManager.current


}

@Composable
fun DemoContent(
    viewModel: DemoViewModel,
    theme: AppTheme = getAppTheme()
) {
    Column(modifier = Modifier.padding(32.dp)) {

        val dividerColor = when (theme) {
            AppTheme.Dark -> Gray100
            AppTheme.Light -> Gray900
        }

        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Use Dark Theme", style = theme.typography.bodyLarge)
        }

        Divider(thickness = 2.dp, color = dividerColor)

        Column(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, top = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // text

            val textInputState = viewModel.textInputState.collectAsState()
            SkydioTextInput(
                state = textInputState.value,
                onTextChanged = { viewModel.onTextInputChanged(it) },
                theme = theme
            )

            val text = remember {
                mutableStateOf(
                    TextFieldValue("")
                )
            }

            SkydioTextInput(
                value = TextFieldValue(
                    text.value.text,
                    selection = TextRange(text.value.text.length)),
                onTextChanged = {
                    text.value = it
                },
                modifier = Modifier.padding(top = 8.dp),
                isEnabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                    }
                ),
                maxCharacters = 10,
                placeholder = "Placeholder"
            )

            val textRequiredState = viewModel.textRequiredCheckboxState.collectAsState()
            SkydioCheckbox(
                state = textRequiredState.value,
                onCheckChanged = viewModel::onTextRequiredToggled,
                theme = theme
            )

            val textErrorState = viewModel.textHasErrorCheckboxState.collectAsState()
            SkydioCheckbox(
                state = textErrorState.value,
                onCheckChanged = viewModel::onTextErrorToggled,
                theme = theme
            )

            // buttons

            Spacer(modifier = Modifier.height(64.dp))

            val allButtonsState = viewModel.allButtonTypeStates.collectAsState()
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                allButtonsState.value.forEach {
                    SkydioButton(state = it, theme = theme)
                }
            }

            val buttonsEnabledState = viewModel.buttonsEnabledCheckboxState.collectAsState()
            SkydioCheckbox(
                state = buttonsEnabledState.value,
                onCheckChanged = viewModel::onButtonsEnabledChanged,
                theme = theme
            )

            // loading progress

            Spacer(modifier = Modifier.height(64.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                val detailedState = viewModel.detailedProgressState.collectAsState()
                SkydioProgressIndicator(state = detailedState.value, theme = theme)

                Spacer(modifier = Modifier.height(8.dp))

                val percentState = viewModel.percentageProgressState.collectAsState()
                SkydioProgressIndicator(state = percentState.value, theme = theme)

                // indeterminate state
                Spacer(modifier = Modifier.height(8.dp))
                SkydioProgressIndicator(state = ProgressIndicatorState(progressPercent = -1f), theme = theme)

                Spacer(modifier = Modifier.height(8.dp))

                val progressPausedState = viewModel.progressPausedCheckboxState.collectAsState()
                SkydioCheckbox(
                    state = progressPausedState.value,
                    onCheckChanged = viewModel::onProgressPausedToggled,
                    theme = theme
                )

                val progressErrorState = viewModel.progressHasErrorCheckboxState.collectAsState()
                SkydioCheckbox(
                    state = progressErrorState.value,
                    onCheckChanged = viewModel::onProgressErrorToggled,
                    theme = theme
                )
            }

            // loading indefinite

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                SkydioLoadingIndicator(modifier = Modifier.size(32.dp), theme = theme)
                SkydioLoadingIndicator(modifier = Modifier.size(48.dp), theme = theme)
                SkydioLoadingIndicator(modifier = Modifier.size(64.dp), theme = theme)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkydioCircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                durationMillis = 5000
            )

            Spacer(modifier = Modifier.height(16.dp))

            SkydioCard(
                modifier = Modifier.width(300.dp),
                headerText = "SkydioTextSegmentControl"
            ) {
                val items = listOf<String>("1", "2", "3")
                SkydioTextSegmentControl(
                    theme = theme,
                    modifier = Modifier.fillMaxWidth(),
                    selectedIndex = 1,
                    items = items,
                    itemToString = { it },
                    onItemSelected = {  }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            SkydioCard(
                modifier = Modifier.width(300.dp),
                headerText = "SkydioDropdownSelector"
            ) {
                val options = listOf("1", "2", "3")
                SkydioDropdownSelector(
                    selectedIndex = 0,
                    options = options,
                    getTextFromOptions = { it },
                    label = "Label: ",
                    onSelectionChanged = {},
                    theme = theme,
                )
            }
        }
    }
}