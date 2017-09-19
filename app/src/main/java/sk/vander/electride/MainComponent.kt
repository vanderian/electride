package sk.vander.electride

import autodagger.AutoComponent

@AutoComponent(
    modules = arrayOf(
        ActivityFactoriesModule::class
    )
)
annotation class MainComponent