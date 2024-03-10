package com.example.noteskeeper.data

import android.content.Intent
import androidx.lifecycle.LifecycleOwner

interface DeeplinkHandler {

    fun handleDeepLink(deeplink:Intent?,lifecycleOwner: LifecycleOwner)
   // fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner)
}