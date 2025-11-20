//package com.greenrobotdev.linklibrary.utils
//
//import app.cash.molecule.AndroidUiDispatcher
//import com.arkivanov.essenty.instancekeeper.InstanceKeeper
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlin.coroutines.CoroutineContext
//
//actual open class ViewModel : InstanceKeeper.Instance, CoroutineScope {
//  actual override val coroutineContext: CoroutineContext = AndroidUiDispatcher.Companion.Main + SupervisorJob()
//  override fun onDestroy() { coroutineContext.cancel() }
//}