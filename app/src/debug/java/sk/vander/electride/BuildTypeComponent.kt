package sk.vander.electride

import autodagger.AutoComponent
import autodagger.AutoInjector
import sk.vander.lib.annotations.ApplicationScope

@AutoComponent(
    modules = arrayOf(
        DebugAppModule::class
    ),
    includes = MainComponent::class
)
@AutoInjector
@ApplicationScope
annotation class BuildTypeComponent