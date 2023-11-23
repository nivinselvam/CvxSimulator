///*
//This class is used for handling the client requests/responses.
//Each client gets handled in a separate thread.
// */
//package com.socketProcessor;
//
//import com.base.Main;
//import com.ResponseProcessor.RequestAcknowledger;
//import com.codec.RequestDecoder;
//import com.codec.ResponseEncoder;
//import com.ResponseProcessor.ResponseGenerator;
//import com.utilities.PacketsValidationFileGenerator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.LinkedHashMap;
//
//public class ClientHandler extends Thread {
//    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
//    private Socket socket;
//    private DataInputStream dataInputStream;
//    private DataOutputStream dataOutputStream;
//    private byte[] byteArray;
//
//    public ClientHandler(Socket socket) {
//        this.socket = socket;
//    }
//
//    public void run() {
//        logger.info("Client " + socket.getRemoteSocketAddress().toString() + " is connected.");
//        try {
//            logger.info("---------------------------------------------------------------------");
//            logger.info("                       Start of Transaction                          ");
//            logger.info("---------------------------------------------------------------------");
//            String transactionRequestPacket = readDataFromSocket();
//            PacketsValidationFileGenerator fileGenerator = new PacketsValidationFileGenerator();
//            RequestDecoder decoder = new RequestDecoder(transactionRequestPacket);
//            LinkedHashMap<String, String> requestMap = decoder.getFieldsInRequestPacket();
//            fileGenerator.generateValidationPacketsXmlFiles(requestMap, "request");
//            if(Main.simProp.sendAcknowledgement().equalsIgnoreCase("Y")){
//                RequestAcknowledger requestAcknowledger = new RequestAcknowledger(requestMap);
//                writeToSocket(requestAcknowledger.getResponse());
//                if(Main.simProp.sendResponse().equalsIgnoreCase("Y")){
//                    ResponseGenerator responseGenerator = new ResponseGenerator(requestMap);
//                    LinkedHashMap<String, String> responseMap = responseGenerator.getResponseMap();
//                    ResponseEncoder encoder = new ResponseEncoder(responseMap);
//                    fileGenerator.generateValidationPacketsXmlFiles(responseMap, "response");
//                    String transactionResponsePacket = encoder.getEncodedData();
//                    writeToSocket(transactionResponsePacket);
//                    transactionRequestPacket = readDataFromSocket();
//                    logger.debug("Acknowledgement received is : "+ transactionRequestPacket);
//                }else{
//                    logger.info("Response not sent as configured.");
//                    closeSocket();
//                }
//            }else{
//                logger.info("Acknowledgment not sent for request as configured.");
//            }
//            logger.info("---------------------------------------------------------------------");
//            logger.info("                         End of Transaction                          ");
//            logger.info("---------------------------------------------------------------------");
//        } catch (IOException e) {
//            logger.error(e.toString());
//        }
//    }
//
//    /***
//     * This method is used for reading the data from the socket.
//     * Program waits for the data to be available in the socket and reads it as soon as available.
//     * First the length of the total bytes available is read and then followed by the actual data.
//     * @return requestPacket
//     * @throws IOException
//     */
//    private String readDataFromSocket() throws IOException {
//        dataInputStream = new DataInputStream(socket.getInputStream());
//        //For monitoring the socket continuously and read data whenever available.
//        while (true) {
//            logger.info("Waiting for client data...");
//            while (dataInputStream.available() == 0) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    logger.error(e.toString());
//                }
//            }
//
//            logger.debug("Trying to read data from the socket.");
//            int socketDataLength = dataInputStream.available();
//            byteArray = new byte[socketDataLength];
//            dataInputStream.read(byteArray);
//            String requestPacket = new String(byteArray, StandardCharsets.UTF_8);
//            logger.debug("Request received: " + requestPacket);
//            return requestPacket;
//        }
//    }
//
//    /**
//     * This method is used for closing the socket along with the input and output streams.
//     * @param stringDataToBeSent
//     */
//    private void writeToSocket(String stringDataToBeSent) {
//        logger.debug("Data to be written into the socket: \n" + stringDataToBeSent);
//        try {
//            dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            logger.debug("Trying to write data into the socket");
//            byteArray = Main.converter.convertStringToBytes(stringDataToBeSent);
//            if (byteArray != null) {
//                dataOutputStream.write(byteArray);
//                dataOutputStream.flush();
//            }
//
//        } catch (IOException e) {
//            logger.error("Error during conversion from string to bytes\n" + e.toString());
//        }
//    }
//
//    /***
//     * This method is used for closing the socket along with the input and output streams.
//     * @throws IOException
//     */
//    private void closeSocket() throws IOException {
//        logger.debug("Trying to close the client socket...");
//        dataInputStream.close();
//        dataOutputStream.close();
//        socket.close();
//        logger.debug("Client socket closed successfully.");
//    }
//}
