//package com.socketProcessor;
//
//import com.base.Main;
//import com.base.SimulatorProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.net.BindException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.ArrayList;
//
//public class ServerInitializer extends Thread {
//    private static Logger logger = LoggerFactory.getLogger(ServerInitializer.class);
//    private Socket socket;
//    private ClientHandler clientHandler;
//    private ArrayList<ClientHandler> connectedClients = new ArrayList<ClientHandler>();
//
//    public void run() {
//        try {
//            Main.variables.serverSocket = new ServerSocket(Main.simProp.getPortNumber());
//            logger.info("Server has started.");
//            logger.info("Waiting for client to connect...");
//            while (true) {
//                socket = Main.variables.serverSocket.accept();
//                clientHandler = new ClientHandler(socket);
//                clientHandler.start();
//            }
//        } catch (BindException e) {
//            logger.debug("Unable to start the simulator on port number " + Main.simProp.getPortNumber());
//        } catch (SocketException e){
//            logger.debug("Server stopped.");
//        }
//        catch (IOException e) {
//            logger.warn("Unable to create the server socket due to error " + e);
//            throw new RuntimeException(e);
//        }
//    }
//}
