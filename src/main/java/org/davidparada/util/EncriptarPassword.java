package org.davidparada.util;

import org.mindrot.jbcrypt.BCrypt;

public class EncriptarPassword {

    public static String generarHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificarPassword(String passwordPlano, String hash) {
        return BCrypt.checkpw(passwordPlano, hash);
    }
}