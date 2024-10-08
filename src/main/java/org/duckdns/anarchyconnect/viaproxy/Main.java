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
    System.out.println("ViaProxyConnect: User has entered the website!");
    HttpClient httpClient = MinecraftAuth.createHttpClient();
    try {
        fetchJavaSession(httpClient, exchange);
    } catch (Exception e) {
        Logger.LOGGER.error("Error during Java session: " + e.getMessage());
        throw new RuntimeException(e);
    }
}

private void fetchJavaSession(HttpClient httpClient, HttpExchange exchange) {
    StepFullJavaSession.FullJavaSession javaSession = MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.getFromInput(httpClient, msaDeviceCode -> {
        System.out.println("Fetched Java Code!");
        String javaDeviceCode = msaDeviceCode.getUserCode();
        try {
            fetchBedrockSession(httpClient, exchange, javaDeviceCode);
        } catch (Exception e) {
            Logger.LOGGER.error("Error during Bedrock session: " + e.getMessage());
            throw new RuntimeException(e);
        }
    });
}

private void fetchBedrockSession(HttpClient httpClient, HttpExchange exchange, String javaDeviceCode) {
    StepFullBedrockSession.FullBedrockSession bedrockSession = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.getFromInput(httpClient, bedrockMsaDeviceCode -> {
        System.out.println("Fetched Bedrock Code!");
        String bedrockDeviceCode = bedrockMsaDeviceCode.getUserCode();
        sendResponse(exchange, javaDeviceCode, bedrockDeviceCode);
    });
}

private void sendResponse(HttpExchange exchange, String javaDeviceCode, String bedrockDeviceCode) {
    String response = "<html><body>Java Device Code: " + javaDeviceCode + "<br>Bedrock Device Code: " + bedrockDeviceCode + "</body></html>";
    try {
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    } catch (IOException e) {
        Logger.LOGGER.error("Error sending response: " + e.getMessage());
        throw new RuntimeException(e);
    }
}


}
