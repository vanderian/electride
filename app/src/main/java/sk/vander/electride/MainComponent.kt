package sk.vander.electride

import autodagger.AutoComponent
import sk.vander.electride.data.DataModule
import sk.vander.electride.db.DatabaseModule
import sk.vander.electride.net.NetModule
import sk.vander.electride.ui.ModelsModule

@AutoComponent(
    modules = arrayOf(
        InjectorFactoriesModules.Activities::class,
        InjectorFactoriesModules.Services::class,
        ModelsModule::class,
        DatabaseModule::class,
        NetModule::class,
        DataModule::class
    )
)
annotation class MainComponent