package com.dd.api.websocket;

import lombok.extern.java.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzp
 */
@Log
public class WebSocketClient implements com.dd.api.websocket.WebSocket {
    private String url = "wss:/kline.zbgpro.net/websocket";

    private WebSocket webSocket = null;
    private WebSocketListener listener;

    public WebSocketClient(WebSocketListener listener) {
        this.listener = listener;
    }

    public WebSocketClient(String url, WebSocketListener listener) {
        this.url = url;
        this.listener = listener;
    }

    private void init() {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();

            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                this.listener.onFailure(this, new RuntimeException("Only WS(S) is supported."));
                return;
            }
            final OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            this.webSocket = client.newWebSocket(request, new WebSocketAdapter(this, listener));
        } catch (Exception e) {
            this.listener.onFailure(this, e);
        }
    }


    public void send(String msg) {
        this.webSocket.send(msg);
    }

    @Override
    public void connect() {
        this.init();
    }

    @Override
    public void close() {
        this.webSocket.close(200, "User exist");
    }

    @Override
    public void ping() {
        String message = "{\"action\":\"PING\"}";
        this.send(message);
    }

    @Override
    public void subscribe(String topic, int size) {
        String msg = String.format("{\"action\":\"ADD\", \"dataType\":%s, \"dataSize\":%d}", topic, size);

        log.info("Subscribe topic : " + msg);

        this.send(msg);
    }

    @Override
    public void unsubscribe(String topic) {
        String msg = String.format("{\"action\":\"DEL\", \"dataType\":%s}", topic);
        log.info("Unsubscribe topic : " + msg);

        this.send(msg);
    }

    private class WebSocketAdapter extends okhttp3.WebSocketListener {
        private com.dd.api.websocket.WebSocket socket;
        private WebSocketListener listener;

        public WebSocketAdapter(com.dd.api.websocket.WebSocket socket, WebSocketListener listener) {
            this.socket = socket;
            this.listener = listener;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            listener.onOpen(socket);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            if (text != null && text.contains("\"action\":\"PING\"")) {
                this.listener.onPong(socket);
            } else {
                this.listener.onMessage(socket, text);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            this.listener.onClosing(socket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            this.listener.onClosed(socket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            this.listener.onFailure(socket, t);
        }
    }
}
