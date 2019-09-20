package com.tino.textflowlayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textFlowLayout.setData(arrayListOf("11111111","22222222","3333","44","55555555555","66666","77777777777777777","888888888888888"))
    }
}
