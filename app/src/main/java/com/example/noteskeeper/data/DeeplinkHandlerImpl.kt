package com.example.noteskeeper.data

import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

class DeeplinkHandlerImpl:DeeplinkHandler,DefaultLifecycleObserver {
    override fun handleDeepLink(deeplink: Intent?,lifecycleOwner: LifecycleOwner) {

        if (lifecycleOwner.lifecycle .currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // connect if not connected
        }

    }

    /*override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }*/



}