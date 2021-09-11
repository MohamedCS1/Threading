package com.example.threading

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Activity_one : AppCompatActivity() {

    var tv_progress: TextView? = null
    var myprogress: ProgressBar? = null
    var bu_runonuithread: Button? = null
    var bu_runwithexecutorandhandler:Button? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one)

        tv_progress = findViewById(R.id.tv_progress_download)
        myprogress = findViewById(R.id.pb_download)
        bu_runonuithread = findViewById(R.id.bu_runuithread)
        bu_runwithexecutorandhandler = findViewById(R.id.bu_runwithexecutorandhandler)

        tv_progress!!.visibility = View.INVISIBLE
        myprogress!!.visibility = View.INVISIBLE

        bu_runonuithread!!.setOnClickListener {
            bu_runwithexecutorandhandler!!.isClickable = false
            val tr = Thread(object : Runnable {
                override fun run() {

                    for (i in 0..100) {
                        Thread.sleep(500)
                        runOnUiThread(object : Runnable {

                            @SuppressLint("SetTextI18n")
                            override fun run() {
                                tv_progress!!.visibility = View.VISIBLE
                                myprogress!!.visibility = View.VISIBLE
                                myprogress!!.incrementProgressBy(10)
                                tv_progress!!.text = "${myprogress!!.progress}/100"
                            }

                        })

                    }
                    runOnUiThread(object : Runnable {
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun run() {

                            bu_runwithexecutorandhandler!!.isClickable = true
                            tv_progress!!.visibility = View.INVISIBLE
                            myprogress!!.visibility = View.INVISIBLE
                            tv_progress!!.text = ""
                            myprogress!!.setProgress(0, false)
                        }

                    })
                }

            })

            tr.start()

        }

        bu_runwithexecutorandhandler!!.setOnClickListener {
            bu_runonuithread!!.isClickable = false

            val exexutors = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            fun update_progress(){
//                in UI
                handler.post(object :Runnable
                {
                    @SuppressLint("SetTextI18n")
                    override fun run() {
                        tv_progress!!.visibility = View.VISIBLE
                        myprogress!!.visibility = View.VISIBLE
                        myprogress!!.incrementProgressBy(10)
                        tv_progress!!.text = "${myprogress!!.progress}/100"
                    }

                })

            }
            fun clear(){
//                in UI
                handler.post(object :Runnable
                {
                    override fun run() {

                        bu_runonuithread!!.isClickable = true
                        tv_progress!!.visibility = View.INVISIBLE
                        myprogress!!.visibility = View.INVISIBLE
                        tv_progress!!.text = ""
                        myprogress!!.setProgress(0, false)
                    }
                })

            }

            exexutors.execute {
//              in Background
                for (i in 0..100)
                {
                    Thread.sleep(500)
                    update_progress()
                }
                clear()
            }

        }
    }

}