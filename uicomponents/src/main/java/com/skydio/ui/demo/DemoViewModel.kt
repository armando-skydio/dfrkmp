package com.skydio.ui.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.components.widget.toggles.*
import com.skydio.ui.components.widget.inputs.*
import com.skydio.ui.components.widget.indicators.*
import com.skydio.ui.components.widget.buttons.ButtonState
import com.skydio.ui.components.widget.buttons.SkydioButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class DemoViewModel : ViewModel() {

    // MARK: UI State

    // theme

    val themeState = AppTheme.Light

    // text input

    val textRequiredCheckboxState: StateFlow<CheckboxState> = MutableStateFlow(
        CheckboxState(value = false, label = "Input Required")
    )

    val textHasErrorCheckboxState: StateFlow<CheckboxState> = MutableStateFlow(
        CheckboxState(value = false, label = "Input Has Error")
    )

    val textInputState = MutableStateFlow(
        TextInputState(
            value = "Example Input",
            header = "Example Input",
            placeholder = "Input Header",
            isRequired = textRequiredCheckboxState.value.isChecked
        )
    )

    // buttons

    val buttonsEnabledCheckboxState: StateFlow<CheckboxState> = MutableStateFlow(
        CheckboxState(value = true, label = "Buttons Enabled")
    )

    val allButtonTypeStates = MutableStateFlow(
        SkydioButton.Style.values().map {
            ButtonState(
                label = it.name,
                style = it,
                isEnabled = buttonsEnabledCheckboxState.value.isChecked
            )
        }
    )

    // progress indicators

    val progressPausedCheckboxState: StateFlow<CheckboxState> = MutableStateFlow(
        CheckboxState(value = false, label = "Progress Is Paused")
    )

    val progressHasErrorCheckboxState: StateFlow<CheckboxState> = MutableStateFlow(
        CheckboxState(value = false, label = "Progress Has Error")
    )

    val detailedProgressState = MutableStateFlow(ProgressIndicatorState())
    val percentageProgressState = MutableStateFlow(ProgressIndicatorState())

    // MARK: Startup

    init {
        viewModelScope.launch { updateProgressIndicators() }
    }

    // MARK: UI Updates

    // theme

    // text input

    fun onTextInputChanged(text: String) {
        textInputState.value = textInputState.value.copy(header = text)
    }

    fun onTextRequiredToggled(isChecked: Boolean) {
        textInputState.value = textInputState.value.copy(isRequired = isChecked)
    }

    fun onTextErrorToggled(isChecked: Boolean) {
        val error = if (isChecked) "Invalid Input" else null
        textInputState.value = textInputState.value.copy(errorText = error)
    }

    // buttons

    fun onButtonsEnabledChanged(enabled: Boolean) {
        allButtonTypeStates.value = allButtonTypeStates.value.map {
            it.copy(isEnabled = enabled)
        }
    }

    // progress indicator

    fun onProgressPausedToggled(isChecked: Boolean) {
        detailedProgressState.value = detailedProgressState.value.copy(isPaused = isChecked)
        percentageProgressState.value = percentageProgressState.value.copy(isPaused = isChecked)
    }

    fun onProgressErrorToggled(isChecked: Boolean) {
        val error = if (isChecked) "Error downloading sample data" else null
        detailedProgressState.value = detailedProgressState.value.copy(errorMessage = error)
        percentageProgressState.value = percentageProgressState.value.copy(errorMessage = error)
    }

    private suspend fun updateProgressIndicators() {
        val totalItems = 5
        var currentItems = 0
        while (coroutineContext.isActive) {
            delay(1000)

            if (progressPausedCheckboxState.value.value) continue
            if (progressHasErrorCheckboxState.value.value) continue

            currentItems++
            if (currentItems > totalItems) currentItems = 0

            val details = ProgressIndicatorDetail(
                leftText = "${currentItems * 100} MB / ${totalItems * 100} MB",
                rightText = "$currentItems / $totalItems")
            detailedProgressState.value = detailedProgressState.value.copy(
                progressPercent = currentItems.toFloat() / totalItems,
                detailedProgress = details)
            percentageProgressState.value = percentageProgressState.value.copy(
                progressPercent = currentItems.toFloat() / totalItems)
        }
    }

}
