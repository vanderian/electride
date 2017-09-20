package sk.vander.lib.ui

import android.content.Context
import android.content.ContextWrapper
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * @author marian on 20.9.2017.
 */
object ActivityFinder {

  fun get(view: View) = get(view.context)

  fun get(context: Context): AppCompatActivity {
    if (context is AppCompatActivity) {
      return context
    } else if (context is ContextWrapper) {
      return get(context.baseContext)
    } else {
      throw IllegalStateException("No Activity found for $context")
    }
  }
}