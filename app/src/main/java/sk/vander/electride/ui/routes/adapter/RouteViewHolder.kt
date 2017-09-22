package sk.vander.electride.ui.routes.adapter

import android.view.View
import android.widget.TextView
import sk.vander.lib.ui.bindView
import sk.vander.lib.ui.widget.adapter.ViewHolder

/**
 * @author marian on 22.9.2017.
 */
class RouteViewHolder(root: View) : ViewHolder<RouteItem, Unit>(root) {
  val text: TextView by bindView<TextView>(android.R.id.text1)

  override fun bind(item: RouteItem) {
    text.text = item.route.toString()
  }
}