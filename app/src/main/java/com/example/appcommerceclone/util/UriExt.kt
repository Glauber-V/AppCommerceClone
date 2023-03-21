package com.example.appcommerceclone.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.appcommerceclone.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

object UriExt {

    fun getUri(@ApplicationContext context: Context): Uri {
        val tmpFile: File = File.createTempFile(TimeExt.getCurrentTime(), ".png", context.cacheDir)
        return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}