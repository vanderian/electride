package sk.vander.electride

import autodagger.AutoComponent
import sk.vander.electride.db.DataModule
import sk.vander.electride.ui.ViewModelsModule

@AutoComponent(
    modules = arrayOf(
        InjectorFactoriesModules.Activities::class,
        InjectorFactoriesModules.Services::class,
        ViewModelsModule::class,
        DataModule::class
    )
)
annotation class MainComponent