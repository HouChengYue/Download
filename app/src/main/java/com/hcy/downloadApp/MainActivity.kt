package com.hcy.downloadApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hcy.download.DownLoadActivity

/**
 * function: 用于下载文件使用的
 *
 * @author HCY
 * @date 20201120 11:14
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, DownLoadActivity::class.java))
    }
}