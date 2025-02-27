package com.skydio.ui.components.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget.BaseViewState

/**
 * Base class for UI elements that can both be used solely in a Composable function, or directly
 * embedded in XML. Be sure to set the [viewState] otherwise the view will be empty.
 * The [viewState] can be set via data binding (see [setComposeViewState]).
 * Setting/Unsetting the view model will automatically clean up the composable and re-make it.
 *
 * Example implementation:
 *      class ExView(...): SkydioComposeView<ExView.ExState>(...) {
 *
 *              interface ExState {...}
 *
 *              @Composable
 *              override fun Content(state: ExState) = ExView(state)
 *      }
 *
 *      @Composable
 *      fun ExView(state: ExView.ExState) = Column {...}
 *
 * Using this example, an XML-compatible view, ExView, is defined, and backed solely by the
 * composable function ExView(ExView.ExModel) implementation.
 */
abstract class AbstractSkydioComposeWidget<ViewState : BaseViewState> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0,
    private val defaultViewState: ViewState
) : AbstractComposeView(context, attrs, defaultStyleAttr) {

    // MARK: View State

    /**
     * All view states must implement, at a minimum, [isEnabled] and [isVisible].
     * This is to assist in the interoperability if these Compose views with existing
     * XML layouts, in particular the ability to set the view's visibility to [View.GONE] is key.
     *
     * Note that, like the [UserEditableViewState.value] field, the [isVisible] and [isEnabled]
     * variables are editable internally. This is in order to properly support [setEnabled]
     * and [setVisibility], and by extensions to support the classic XML paradigm.
     */
    abstract class BaseViewState {

        /**
         * This value will always reflect the latest state of the UI, as it is internally
         * updated by the parent view when [setEnabled] is called on the [View].
         */
        abstract var isEnabled: Boolean
            internal set

        /**
         * This value will always reflect the latest state of the UI, as it is internally
         * updated by the parent view when [setVisibility] is called on the [View].
         */
        abstract var isVisible: Boolean
            internal set
    }

    /**
     * Although [ViewState]s should be fully immutable, this can lead to a confusing situation
     * when the view can be edited by the user (such as a [SkydioTextInput]).
     *
     * For example, when storing a [ViewState] in a ViewModel a user may update the view's input
     * while the state in the ViewModel would still reflect the old value and not the current value.
     *
     * Rather than let the state object diverge from the UI, this class allows
     * [AbstractSkydioComposeWidget]s to update the user-editable field internally so that everything
     * stays up-to-date.
     *
     * To enforce the practice of immutability in compose states, the setter for the [value] is
     * internal, meaning only classes within the uicomponents module can actually update the value,
     * while consumers (such as the flyby module) will only have access to a fully-immutable state.
     *
     * Note that it is the responsibility of subclasses that use a [UserEditableViewState] to
     * actually update the value (see [SkydioTextInput] for an example).
     */
    abstract class UserEditableViewState<T> : BaseViewState() {

        /**
         * This value will always reflect the latest state of the UI, as it is internally
         * auto-updated by the parent widget.
         */
        abstract var value: T
            internal set
    }

    /**
     * The backing data model used to compose the view.
     * When this source changes, the previous composition is disposed and recreated.
     */
    var viewState: ViewState = defaultViewState
        set(value) {
            if (value == field) return
            field = value
            viewStateDidChange()
        }

    /**
     * Attempts to set an arbitrary object as the [AbstractSkydioComposeWidget.viewState].
     * This will throw an exception if the [viewState] is not an instance of [ViewState].
     */
    @Suppress("UNCHECKED_CAST")
    internal fun setViewStateOrThrow(viewState: Any?) {
        if (viewState == null) clearViewState()
        else this.viewState = viewState as ViewState
    }

    /**
     * Clears the view state and sets it to [defaultViewState].
     */
    internal fun clearViewState() {
        this.viewState = defaultViewState
    }

    private fun viewStateDidChange() {
        val isVisible = viewState.isVisible
        super.setVisibility(if (isVisible) View.VISIBLE else View.GONE)
        super.setEnabled(viewState.isEnabled)
        disposeComposition()
    }

    final override fun setEnabled(enabled: Boolean) {
        val wasEnabled = viewState.isEnabled
        viewState.isEnabled = enabled
        if (wasEnabled != enabled) viewStateDidChange()
    }

    final override fun setVisibility(visibility: Int) {
        val wasVisible = viewState.isVisible
        viewState.isVisible = visibility == View.VISIBLE
        if (visibility == View.VISIBLE != wasVisible) viewStateDidChange()
    }

    // MARK: Content

    @Composable
    abstract fun Content(state: ViewState)

    @Composable
    final override fun Content() {
        val isDefaultState = viewState === defaultViewState
        return if (isDefaultState) DefaultOverrideView() ?: Content(viewState)
        else Content(viewState)
    }

    /**
     * An optional overridable function to create a default view when the [viewState] is
     * the [defaultViewState].
     */
    @Composable
    protected open fun DefaultOverrideView(): Unit? = null

}


