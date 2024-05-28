/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This class has the entire authentication logic.
 *
 * @author sergi
 */
public class Authentication {

    private static final String usersList = "user_list.txt";

    public BufferedWriter writer = null;

    /**
     * Registers a new user.
     *
     * @param userName
     * @param password
     * @param isAdmin
     */
    void userRegister(String userName, String password, String isAdmin) {
        try {
            writer = new BufferedWriter(new FileWriter(usersList, true));

            byte[] salt = createSalt();

            String encryptedPassword = encryptPassword(password, salt);

            String stringSalt = stringSalt(salt);

            writer.write(userName + "," + encryptedPassword + "," + stringSalt
                    + "," + isAdmin);
            writer.newLine();

            System.out.println("El usuario " + userName + " ha sido registrado"
                    + " con exito...");
        } catch (IOException | NoSuchAlgorithmException e) {

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el BufferedWriter: "
                            + e.getMessage());
                }
            }
        }
    }

    /**
     * Create a private key for the encrypt method.
     *
     * @return The key.
     */
    private byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Encrypt the password.
     *
     * @param password
     * @param salt is the key.
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String encryptPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        messageDigest.update(salt);

        byte[] encryptedPasswordBytes = messageDigest.digest(password.getBytes());

        return Base64.getEncoder().encodeToString(encryptedPasswordBytes);
    }

    /**
     * Parses the key to string.
     *
     * @param salt
     * @return
     */
    private String stringSalt(byte[] salt) {
        String stringSalt = Base64.getEncoder().encodeToString(salt);
        return stringSalt;
    }

    /**
     * Verify a former user.
     *
     * @param userName
     * @param password
     * @return
     */
    public String verifyUser(String userName, String password) {

        try (BufferedReader rd = new BufferedReader(new FileReader(usersList));) {
            String line;

            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                String[] registeredParts = line.split(",");
                String registeredName = registeredParts[0];
                String registeredHashPass = registeredParts[1];

                byte[] registeredSalt = decodeSalt(registeredParts[2]);
                String isAdmin = registeredParts[3];

                if (registeredName.equals(userName) && verifyPass(password,
                        registeredHashPass, registeredSalt)) {
                    return "true," + isAdmin;
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error al leer el archivo de usuarios: "
                    + e.getMessage());
        }

        return "false";

    }

    /**
     * Verify the password.
     *
     * @param password
     * @param registeredHashPass
     * @param registeredSalt
     * @return
     * @throws NoSuchAlgorithmException
     */
    private boolean verifyPass(String password, String registeredHashPass,
            byte[] registeredSalt) throws NoSuchAlgorithmException {

        String keyHashedPass = encryptPassword(password, registeredSalt);
        System.out.println("Verify Password: "
                + keyHashedPass.equals(registeredHashPass));

        return keyHashedPass.equals(registeredHashPass);
    }

    /**
     * Decode the key.
     *
     * @param encodedSalt
     * @return the key decoded.
     */
    private byte[] decodeSalt(String encodedSalt) {

        return Base64.getDecoder().decode(encodedSalt);
    }

}
