package com.hcy.download

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.arialyy.aria.core.Aria

/**
 * function: 下载服务
 *
 * @author HCY
 * @date 20201120 11:17
 */
class DownloadService : Service() {

    companion object {
        /**
         * 下载链接
         */
        var DOWNLOAD_URL: String = ""
    }


    override fun onCreate() {
        super.onCreate()
        Aria.download(this)
            .register()
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

//        Aria.download(this)
//            .load(DOWNLOAD_URL)
//            .setFilePath()

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}