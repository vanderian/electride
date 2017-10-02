package sk.vander.lib.ui.screen

import android.app.Activity
import android.content.Intent
import sk.vander.lib.ui.widget.adapter.AdapterModel
import kotlin.reflect.KClass

/**
 * @author marian on 24.9.2017.
 */
interface Navigation

object GoBack : Navigation
data class NextScreen(val screen: Screen<*, *, *>) : Navigation
data class NextStage(val clazz: KClass<out Activity>) : Navigation
data class WithResult(val intent: Intent, val requestCode: Int) : Navigation

data class Result(
    val request: Int,
    val success: Boolean = false
)


object Empty: Screen.State

data class ListState<out T: AdapterModel>(
    val items: List<T> = emptyList(),
    val loading: Boolean = true,
    val empty: Boolean = false
) : Screen.State

