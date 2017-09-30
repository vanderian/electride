package sk.vander.electride

import autodagger.AutoComponent
import sk.vander.electride.db.DataModule
import sk.vander.electride.ui.ModelsModule

@AutoComponent(
    modules = arrayOf(
        InjectorFactoriesModules.Activities::class,
        InjectorFactoriesModules.Services::class,
        ModelsModule::class,
        DataModule::class
    )
)
annotation class MainComponent