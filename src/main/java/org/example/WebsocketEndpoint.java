package org.example;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

// https://www.baeldung.com/java-websockets
// https://www.pegaxchange.com/2018/03/23/websocket-client/

@ServerEndpoint(value = "/ws")
public class WebsocketEndpoint {

    private Session session;

    private static Set<WebsocketEndpoint> endpoints = new CopyOnWriteArraySet<>();

    public void print(Object o) {
        System.out.println(new Date() + "|" + o );
    }


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        endpoints.add(this);
        print("connected session[" + session + "]");
        broadcast("session[" + session.getId() + "] connected" );
    }

    @OnMessage
    public void onMessage(Session session, String str) throws IOException {
        print("got message[" + str + "]from session[" + session + "]");
        session.getBasicRemote().sendText("server says acked[" + str + "]");
    }


    @OnClose
    public void onClose(Session session) {
        endpoints.remove(this);
        print("removed session [" + session + "]");
        broadcast("session[" + session.getId() + "] removed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        endpoints.remove(this);
        print("removed session [" + session + "] due to error [" + throwable + "]");
        broadcast("session[" + session.getId() + "] removed due to error[" + throwable.getMessage() + "]");
    }

    private static void broadcast(String str) {
        for (WebsocketEndpoint endpoint : endpoints) {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
