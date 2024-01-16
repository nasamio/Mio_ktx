//import android.util.Log
//import com.mio.base.Tag.TAG
//import net.schmizz.sshj.DefaultConfig
//import net.schmizz.sshj.SSHClient
//import net.schmizz.sshj.transport.verification.PromiscuousVerifier
//import java.io.IOException
//
//object SshjHelper {
//    var host: String = "117.50.190.141"
//    var port: Int = 22
//    var username: String = "ubuntu"
//    var password: String = "Qq123456"
//    lateinit var sshClient: SSHClient
//
//    fun connect() {
//        try {
//            val client = SSHClient(DefaultConfig())
//            client.addHostKeyVerifier(PromiscuousVerifier())
//            client.connect(host, port)
//            client.authPassword(username, password)
//            sshClient = client
//            Log.d(TAG, "connect: is connected: ${client.isConnected}")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun executeCommand(command: String) {
//        val session = sshClient.startSession()
//        val cmd = session.exec(command)
//        cmd.join()
//        session.close()
//    }
//
//    fun listDirectory(directory: String) {
//        val sftpClient = sshClient.newSFTPClient()
//        val files = sftpClient.ls(directory)
//        for (file in files) {
//            println(file.name)
//            Log.d(TAG, "listDirectory: file.name: ${file.name}")
//        }
//        sftpClient.close()
//    }
//}
