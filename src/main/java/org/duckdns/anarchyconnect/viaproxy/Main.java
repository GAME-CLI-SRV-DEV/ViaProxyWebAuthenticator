package org.duckdns.anarchyconnect.viaproxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.lenni0451.commons.httpclient.HttpClient;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode;
import net.raphimc.viaproxy.plugins.ViaProxyPlugin;
import net.raphimc.viaproxy.proxy.session.UserOptions;
import net.raphimc.viaproxy.saves.impl.accounts.BedrockAccount;
import net.raphimc.viaproxy.saves.impl.accounts.MicrosoftAccount;
import net.raphimc.viaproxy.util.logging.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main extends ViaProxyPlugin implements HttpHandler {
    @Override
    public void onEnable() {
        Logger.LOGGER.info("§bViaProxyConnect Web Authenticator Version 1, Designed for ViaVersion's ViaProxy.");
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", this);
            server.setExecutor(null); // creates a default executor
            server.start();
            Logger.LOGGER.info("§bViaProxyConnect: Webserver started. You Can Now Authenticate through web.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Your Java code to be triggered
        System.out.println("ViaProxyConnect: User has entered the website!");
        // Prepare the response
        HttpClient httpClient = MinecraftAuth.createHttpClient();
        try {
            StepFullJavaSession.FullJavaSession javaSession = MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.getFromInput(httpClient, new StepMsaDeviceCode.MsaDeviceCodeCallback(msaDeviceCode -> {
                // Method to generate a verification URL and a code for the user to enter on that page
                System.out.println("Fetched Java Code!");
                String javaDeviceCode = msaDeviceCode.getUserCode();
                try {
                    StepFullBedrockSession.FullBedrockSession bedrockSession = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.getFromInput(httpClient, new StepMsaDeviceCode.MsaDeviceCodeCallback(bedrockMsaDeviceCode -> {
                        // Method to generate a verification URL and a code for the user to enter on that page
                        System.out.println("Fetched Bedrock Code!");
                        String bedrockDeviceCode = bedrockMsaDeviceCode.getUserCode();
                        String response = "<html><body>Java Device Code: " + javaDeviceCode + "<br>Bedrock Device Code: " + bedrockDeviceCode + "</body></html>";
                        try {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                    System.out.println("DEBUG - Username: " + javaSession.getMcProfile().getName());
                    System.out.println("DEBUG - Access token: " + javaSession.getMcProfile().getMcToken().getAccessToken());
                    System.out.println("DEBUG - Player certificates: " + javaSession.getPlayerCertificates());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
