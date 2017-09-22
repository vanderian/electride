package sk.vander.lib.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

/**
 * A "view server" adaptation which automatically hooks itself up to all activities.
 */
interface ActivityHierarchyServer : Application.ActivityLifecycleCallbacks {

  class Proxy : ActivityHierarchyServer {
    private val servers = ArrayList<ActivityHierarchyServer>()

    fun addServer(server: ActivityHierarchyServer) = servers.add(server)

    fun removeServer(server: ActivityHierarchyServer) = servers.remove(server)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) =
        servers.forEach { it.onActivityCreated(activity, savedInstanceState) }

    override fun onActivityStarted(activity: Activity) =
        servers.forEach { it.onActivityStarted(activity) }

    override fun onActivityResumed(activity: Activity) =
        servers.forEach { it.onActivityResumed(activity) }

    override fun onActivityPaused(activity: Activity) =
        servers.forEach { it.onActivityPaused(activity) }

    override fun onActivityStopped(activity: Activity) =
        servers.forEach { it.onActivityStopped(activity) }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) =
        servers.forEach { it.onActivitySaveInstanceState(activity, outState) }

    override fun onActivityDestroyed(activity: Activity) =
        servers.forEach { it.onActivityDestroyed(activity) }
  }

  open class Empty : ActivityHierarchyServer {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}
  }

  companion object {
    /**
     * An [ActivityHierarchyServer] which does nothing.
     */
    val NONE: ActivityHierarchyServer = Empty()
  }
}
