package org.example;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;


// https://www.baeldung.com/jetty-embedded
// https://github.com/jetty-project/embedded-jetty-websocket-examples/blob/10.0.x/native-jetty-websocket-example/src/main/java/org/eclipse/jetty/demo/EventServer.java


public class JettyServer {

    Server server;

    public void start() throws Exception {

        ThreadPool threadPool = new QueuedThreadPool(10, 1, 10);

        server = new Server(threadPool);

        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8090);
        server.setConnectors(new Connector[] {serverConnector});

//        ServletHandler servletHandler = new ServletHandler();
//        servletHandler.addServletWithMapping(BlockingServlet.class, "/status");
//        server.setHandler(servletHandler);

        ServletContextHandler servletContextHandler = new ServletContextHandler();

        servletContextHandler.addServlet(BlockingServlet.class, "/status");

        WebSocketServerContainerInitializer.configure(servletContextHandler, ((servletContext, wsContainer) -> {
            wsContainer.addEndpoint(WebsocketEndpoint.class);
        }));

        server.setHandler(servletContextHandler);

        server.start();

    }

    public void stop() throws Exception {
        server.stop();
    }
}



