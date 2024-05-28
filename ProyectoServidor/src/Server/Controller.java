/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the contoller class.
 *
 * @author sergi
 */
public class Controller {

    private final Socket socket;
    private final PrintWriter outPut;
    private final BufferedReader inPut;
    private final OutputStream binaryOut;
    private final InputStream binaryIn;

    /**
     * Controller constructor.
     *
     * @param socket
     * @param outPut
     * @param inPut
     * @param binaryOut
     * @param binaryIn
     */
    public Controller(Socket socket, PrintWriter outPut, BufferedReader inPut,
            OutputStream binaryOut, InputStream binaryIn) {
        this.socket = socket;
        this.outPut = outPut;
        this.inPut = inPut;
        this.binaryOut = binaryOut;
        this.binaryIn = binaryIn;
    }

    /**
     * getSocket method.
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Sends message to the server.
     *
     * @param message
     */
    public void sendMessage(String message) {
        if (outPut != null) {
            System.out.println("Enviando mensaje al servidor: " + message);
            outPut.println(message);
            outPut.flush();
        } else {
            System.out.println("PrintWriter es null");
        }

    }

    /**
     * Receives message from the server.
     *
     * @return
     * @throws IOException
     */
    public String receiveMessage() throws IOException {
        if (inPut != null) {
            String response = inPut.readLine();
            System.out.println("Mensaje recibido del servidor: " + response);
            return response;
        } else {
            System.err.println("BufferedReader no inicializado");
            return null;
        }
    }

    /**
     * Receives files from the server.
     * @param filePath 
     */
    public void receiveFileFromServer(String filePath) {
        System.out.println("Recibiendo archivo: " + filePath);
        Path path = Paths.get(filePath);
        int count = 0;

        while (Files.exists(path)) {

            count++;
            String fileName = path.getFileName().toString();
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            String newFileName = baseName + "_" + count + extension;
            path = Paths.get(path.getParent().toString(), newFileName);
        }

        try {
            //binaryOut.close();
            Files.copy(binaryIn, path);

            System.out.println("Archivo recibido y guardado en: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                binaryIn.close(); // Cerrar el flujo de entrada
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Closes the comunication.
     */
    public void close() {
        try {
            inPut.close();
            outPut.close();
            socket.close();
            System.out.println("Se cerro la conexion de descarga");
        } catch (IOException e) {
        }
    }

}
