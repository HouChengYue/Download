package com.hcy.download

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.arialyy.aria.core.Aria
import java.util.jar.Manifest

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
        val checkCallingPermission =
            checkCallingPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)



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