package sk.vander.electride.ui.routes.adapter

import sk.vander.electride.db.entity.Route
import sk.vander.lib.ui.widget.adapter.AdapterModel

/**
 * @author marian on 22.9.2017.
 */
data class RouteItem(val route: Route): AdapterModel {
  override val layoutRes: Int = android.R.layout.simple_list_item_1
  override val id: Long = route.id
}