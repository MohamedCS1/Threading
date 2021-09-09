package com.example.threading

import android.annotation.SuppressLint
import android.os.*
import android.renderscript.Sampler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.core.os.postDelayed
import java.lang.Exception
import java.time.Clock

class MainActivity : AppCompatActivity() {

    var tv_number_runuithread:TextView? = null
    var bu_start_runuithread:Button? = null


    var tv_number_post:TextView? = null
    var bu_start_post:Button? = null


    var tv_number_handler:TextView? = null
    var bu_start_hander:Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_number_runuithread = findViewById(R.id.tv_number_runuithread)
        bu_start_runuithread = findViewById(R.id.bu_start_runuithread)

        tv_number_post = findViewById(R.id.tv_number_post)
        bu_start_post = findViewById(R.id.bu_start_post)

        tv_number_handler = findViewById(R.id.tv_number_handler)
        bu_start_hander = findViewById(R.id.bu_star_handler)


        val th = Thread(object :Runnable{
            override fun run() {
                for (i in 0..1000)
                {
                    Thread.sleep(300)
                    runOnUiThread(object :Runnable{
                        override fun run() {
                            tv_number_runuithread!!.text = i.toString()
                        }

                    })
                }
            }

        })


        val th1 = Thread(object :Runnable
        {
            override fun run() {
                for (i in 0..1000)
                {
                    Thread.sleep(300)
                    tv_number_post!!.post(object :Runnable{
                        override fun run() {
                            tv_number_post!!.text = i.toString()
                        }

                    })

                }

            }

        })


        val handler = Myhandler()


        val th2 = Thread(object :Runnable{
            override fun run() {
                for (i in 0..1000)
                {   val msg = Message()
                    Thread.sleep(300)
                    msg.arg1 = i
                    handler.sendMessage(msg)
                }
            }

        })


        bu_start_runuithread!!.setOnClickListener {
            th.start()
            bu_start_runuithread!!.isClickable = false
        }

        bu_start_post!!.setOnClickListener {
            th1.start()
            bu_start_post!!.isClickable = false
        }

        bu_start_hander!!.setOnClickListener {
            th2.start()
            bu_start_hander!!.isClickable = false
        }
    }


    @SuppressLint("HandlerLeak")
    inner class Myhandler:Handler()
    {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
         tv_number_handler!!.text = msg.arg1.toString()
        }
    }

}