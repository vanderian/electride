package sk.vander.electride.ui.routes.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import sk.vander.electride.db.entity.Route
import sk.vander.lib.ui.bindView
import sk.vander.lib.ui.widget.adapter.ViewHolder

/**
 * @author marian on 22.9.2017.
 */
class RouteViewHolder(root: View) : ViewHolder<RouteItem, Route>(root) {
  val text: TextView by bindView<TextView>(android.R.id.text1)

  @SuppressLint("SetTextI18n")
  override fun bind(item: RouteItem) {
    text.text = "${item.route}\n"
    disposable.addAll(
        itemView.clicks().subscribe { itemEvent.onNext(item.route.route()) }
    )
  }
}