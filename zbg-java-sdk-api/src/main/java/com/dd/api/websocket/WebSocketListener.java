package com.dd.api.websocket;


/**
 * @author zhangzp
 */
public class WebSocketListener {

    public void onOpen(WebSocket webSocket) {

    }

    public void onMessage(WebSocket webSocket, String message) {
    }

    /**
     * Invoked when the remote peer has indicated that no more incoming messages will be
     * transmitted.
     */
    public void onClosing(WebSocket webSocket, int code, String reason) {
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    public void onClosed(WebSocket webSocket, int code, String reason) {
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    public void onFailure(WebSocket webSocket, Throwable t) {
    }

    /**
     * Called when a pong frame is received.
     *
     * @param ws The WebSocket instance this event is occuring on.
     **/
    public void onPong(WebSocket webSocket) {
    }
}
