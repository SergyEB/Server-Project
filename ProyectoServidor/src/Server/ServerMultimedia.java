/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the servidor.
 *
 * @author sergi
 */
public class ServerMultimedia {

    private static final int LOCALPORT = 8000;
    private static final int LIMIT_OF_THREADS = 8;
    private static final ExecutorService executor = Executors.newFixedThreadPool(LIMIT_OF_THREADS);

    private static Authentication userAuthentication = new Authentication();

    /**
     * Method main.
     *
     * @param args
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(LOCALPORT, 0,
                InetAddress.getLocalHost())) {

            System.out.println("Servidor escuchando en localhost en el puerto "
                    + LOCALPORT);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Dirección IP del servidor: "
                    + inetAddress.getHostAddress());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: "
                        + clientSocket.getInetAddress());
                Runnable clientHandler = new ClientHandler(clientSocket,
                        userAuthentication);
                executor.execute(clientHandler);
            }

        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
    /**
     * This class handle the client requests.
     */
    static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private final Authentication userAuthentication;
        /**
         * Clienthandler constructor.
         * @param clientSocket
         * @param userAuthentication 
         */
        public ClientHandler(Socket clientSocket,
                Authentication userAuthentication) {
            this.clientSocket = clientSocket;
            this.userAuthentication = userAuthentication;
        }
        
        /**
         * Make a thread for the client.
         */
        public void run() {

            try {
                PrintWriter outPut = new PrintWriter(clientSocket
                        .getOutputStream(), true);
                BufferedReader inPut = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream fileOut = clientSocket.getOutputStream();
                String line;

                while ((line = inPut.readLine()) != null) {
                    System.out.println("Mensaje del cliente: " + line);
                    String[] pieces = line.split("-");
                    boolean auth = Boolean.parseBoolean(pieces[2]);
                    boolean isAdmin = Boolean.parseBoolean(pieces[1]);
                    if (auth == false) {
                        handlingLogSignRequest(line, outPut);
                    } else {

                        manageClientMenu(line, outPut, fileOut, isAdmin);
                    }
                }
            } catch (IOException e) {

                System.err.println("Error durante la comunicación con el "
                        + "cliente: " + e.getMessage());

            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("adios");
                    System.err.println("Error al cerrar el socket del cliente: "
                            + e.getMessage());
                }
            }
        }
/**
 * Handle the login and signup requets.
 * @param line client message
 * @param outPut
 * @throws IOException
 * @throws FileNotFoundException 
 */
        private void handlingLogSignRequest(String line, PrintWriter outPut)
                throws IOException, FileNotFoundException {

            System.out.println("Autenticacion: " + line);

            String[] pieces = line.split(",");
            String logSign = pieces[0];
            String userName = pieces[1];
            String password = pieces[2];
            String adminAuth = pieces[3].substring(1);
            String[] splitAdminAuth = adminAuth.split("-");
            String isAdmin = splitAdminAuth[0];
            String auth = splitAdminAuth[1];
            System.out.println("rol:" + isAdmin);
            try {
                switch (logSign) {
                    case "new":
                        userAuthentication.userRegister(userName, password,
                                isAdmin);
                        outPut.println("Autorizacion exitosa true");
                        break;

                    case "former":

                        String authentication = userAuthentication.
                                verifyUser(userName, password);

                        String[] authenticationParts = authentication.split(",");
                        System.out.println("authenticationParts: "
                                + authentication);
                        if (authentication.startsWith("true")) {
                            outPut.println("true-" + authenticationParts[1]);
                        } else {
                            outPut.println("false- ");
                        }

                        break;
                    default:
                        throw new AssertionError();
                }
            } catch (Exception e) {
            }

        }
        /**
         * Handle the menu requets.
         * @param line client message
         * @param outPut
         * @param fileOut
         * @param admin
         * @throws IOException 
         */
        private void manageClientMenu(String line, PrintWriter outPut,
                OutputStream fileOut, boolean admin) throws IOException {
            if (line.startsWith("G")) {

                char option = line.charAt(1);

                switch (option) {
                    case 'V':

                        List<File> videos = MediaController.getVideos();

                        if (admin) {
                            List<File> videosAdmin = MediaController.
                                    getVideosAdmin();
                            videos.addAll(videosAdmin);
                        }

                        sendListFiles(outPut, videos);

                        break;

                    case 'M':
                        List<File> songs = MediaController.getSongs();

                        if (admin) {
                            List<File> songsAdmin = MediaController.
                                    getSongsAdmin();
                            songs.addAll(songsAdmin);
                        }

                        sendListFiles(outPut, songs);

                        break;

                    case 'D':
                        List<File> docs = MediaController.getDocs();

                        if (admin) {

                            List<File> docAdmin = MediaController.getDocAdmin();
                            docs.addAll(docAdmin);

                        }

                        sendListFiles(outPut, docs);

                        break;
                    default:
                        throw new AssertionError();
                }
            } else if (line.startsWith("D")) {

                String[] messageParts = line.split("_");

                String typeFile = messageParts[1];

                String nameFile = messageParts[2];

                String docFilePath = "";

                String docAdminFilePath = "";

                switch (typeFile) {
                    case "doc":
                        docFilePath = "multimedia/documentos/" + nameFile;
                        docAdminFilePath = "multimedia/docs_admin/" + nameFile;
                        sendFile(docFilePath, docAdminFilePath, admin, fileOut);
                        break;

                    case "song":
                        docFilePath = "multimedia/canciones/" + nameFile;
                        docAdminFilePath = "multimedia/canciones_admin/"
                                + nameFile;
                        sendFile(docFilePath, docAdminFilePath, admin, fileOut);
                        break;

                    case "video":
                        docFilePath = "multimedia/videos/" + nameFile;
                        docAdminFilePath = "multimedia/videos_admin/"
                                + nameFile;
                        sendFile(docFilePath, docAdminFilePath, admin, fileOut);
                        break;

                    default:
                        throw new AssertionError();
                }
            }
        }
/**
 * Sends the files list.
 * @param outPut
 * @param fileList 
 */
        private void sendListFiles(PrintWriter outPut, List<File> fileList) {
            StringBuilder sb = new StringBuilder();

            for (File file : fileList) {
                sb.append(file.getName()).append(';');
            }

            sb.append("end list");

            outPut.println(sb.toString());
        }

        private void sendFile(String FilePath, String adminFilePath,
                boolean admin, OutputStream fileOut) throws IOException {
            File file = new File(FilePath);
            if (!file.exists()) {

                if (admin) {

                    file = new File(adminFilePath);

                    if (!file.exists()) {
                        System.out.println("archivo no existe");
                        return;
                    }
                }
            }

            try {
                Files.copy(file.toPath(), fileOut);
                fileOut.flush();
                System.out.println("archivo enviado");
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                try {
                    
                    if (fileOut != null) {
                        fileOut.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
