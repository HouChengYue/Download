package com.hcy.download

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DownLoadActivity : AppCompatActivity() {
    private val _times = "_times"
    private val PERMISSION_REQUEST_CODE = 0XF0
    private var urls = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        不需要布局
//        setContentView(R.layout.activity_down_load)
        intent?.apply {
            urls = (getCharSequenceArrayListExtra("Data")
                ?: arrayListOf<String>()) as ArrayList<String>
        }
        times = getSharedPreferences(TAG, MODE_PRIVATE).getInt(_times, 0)
        checkPermissions {
            toServer()
        }
    }

    //<editor-fold desc="检查权限">

    private val dailogOne by lazy {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_msg))
            .setCancelable(false)
            .setNegativeButton(
                getString(R.string.sure)
            ) { dialog, which ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                }
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.cancel)) { dailog, which ->
                dailog.dismiss()
            }
            .create()
    }
    private val dailogTwo by lazy {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_msg2))
            .setCancelable(false)
            .setNegativeButton(
                getString(R.string.toAuthorize)
            ) { dialog, _ ->
                goAppDetailSettingIntent(this)
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.cancel)) { dailog, _ ->
                dailog.dismiss()
            }
            .create()
    }

    private var times = 0
    private fun checkPermissions(next: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    next
                }
                shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> {
                    dailogOne.show()
                }
                times < 2 -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                    times++
                    getSharedPreferences(TAG, MODE_PRIVATE).edit().putInt(_times, times).apply()
                }
                else -> {
                    dailogTwo.show()
                }
            }
        }

    }
    //</editor-fold>

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    toServer()
                } else {
                    checkPermissions { toServer() }
                }
            }
            else -> {

            }
        }

    }


    fun goAppDetailSettingIntent(context: Context) {
        try {
            val localIntent = Intent()
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            when (Build.VERSION.SDK_INT) {
                in 0 until 9 -> {
                    localIntent.action = Intent.ACTION_VIEW
                    localIntent.setClassName(
                        "com.android.settings",
                        "com.android.setting.InstalledAppDetails"
                    )
                    localIntent.putExtra(
                        "com.android.settings.ApplicationPkgName",
                        context.packageName
                    )
                }
                in 9 until 11 -> {
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts("package", context.packageName, null)
                }
                else -> {
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts("package", context.packageName, null)
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri = Uri.fromParts("package", context.packageName, null)
//                    intent.data = uri
                }
            }
            context.startActivity(localIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toServer() {
        times=0
        getSharedPreferences(TAG, MODE_PRIVATE).edit().putInt(_times, times).apply()
        startService(Intent(this, DownloadService::class.java))
    }

    private val TAG = "DownLoadActivity"


    companion object {
        @JvmStatic
        fun start(context: Context, urls: ArrayList<String>) {
            val starter = Intent(context, DownLoadActivity::class.java)
                .putExtra("Data", urls)
            context.startActivity(starter)
        }

    }


}
