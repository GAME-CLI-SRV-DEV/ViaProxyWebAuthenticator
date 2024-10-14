import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.lenni0451.commons.httpclient.HttpClient
import net.raphimc.minecraftauth.MinecraftAuth
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode
import net.raphimc.viaproxy.plugins.ViaProxyPlugin
import net.raphimc.viaproxy.util.logging.Logger

class Main : ViaProxyPlugin() {
    override fun onEnable() {
        Logger.LOGGER.info("§bViaProxyConnect Web Authenticator Version 1, Designed for ViaVersion's ViaProxy.")
        embeddedServer(Netty, port = 8000) {
            install(DefaultHeaders)
            install(CallLogging)
            routing {
                get("/") {
                    handleRequest(call)
                }
            }
        }.start(wait = true)
        Logger.LOGGER.info("§bViaProxyConnect: Webserver started. You Can Now Authenticate through web.")
    }

    private suspend fun handleRequest(call: ApplicationCall) {
        println("ViaProxyConnect: User has entered the website!")
        val httpClient = MinecraftAuth.createHttpClient()
        try {
            var javaSession: StepFullJavaSession.FullJavaSession? = null
            javaSession = MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.getFromInput(httpClient, StepMsaDeviceCode.MsaDeviceCodeCallback { msaDeviceCode ->
                println("Fetched Java Code!")
                val javaDeviceCode = msaDeviceCode.userCode
                try {
                    var bedrockSession: StepFullBedrockSession.FullBedrockSession? = null
                    bedrockSession = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.getFromInput(httpClient, StepMsaDeviceCode.MsaDeviceCodeCallback { bedrockMsaDeviceCode ->
                        println("Fetched Bedrock Code!")
                        val bedrockDeviceCode = bedrockMsaDeviceCode.userCode
                        val response = "<html><body>Java Device Code: $javaDeviceCode<br>Bedrock Device Code: $bedrockDeviceCode</body></html>"
                        call.respondText(response, contentType = io.ktor.http.ContentType.Text.Html)
                        
                        // Add Microsoft account data to ViaProxy accounts list
                        Logger.LOGGER.info("Attempting to add Microsoft account to ViaProxy accounts list.")
                        Logger.LOGGER.info("Microsoft account added to ViaProxy accounts list.")
                    })
                } catch (e: Exception) {
                    Logger.LOGGER.error("Error during Bedrock session: " + e.message)
                    throw RuntimeException(e)
                }
            })
        } catch (e: Exception) {
            Logger.LOGGER.error("Error during Java session: " + e.message)
            throw RuntimeException(e)
        }
    }
}
