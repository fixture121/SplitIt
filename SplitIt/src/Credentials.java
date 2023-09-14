import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.StringJoiner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Credentials {
    private static SecretKeyFactory keyFactory;
    private static SecureRandom rand;
    public static ArrayList<Credentials> globalList = new ArrayList<Credentials>();

    private static boolean initialized = false;
    private static final int ITERATIONS = 32768;
    private static final int KEY_SIZE = 128;

    private String username;
    private String hash;
    private String salt;
    private Account owner;
    
    public Credentials(String username, String password) throws Exception {
        try {
            initialize();
            setUsername(username);
            generateSalt();
            this.hash = hashPassword(password);
            globalList.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public Credentials(String username, String password, Account account) throws Exception {
        this.owner = account;

        try {
            initialize();
            setUsername(username);
            generateSalt();
            this.hash = hashPassword(password);
            globalList.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public Credentials(String username, String hash, Account account, String salt) throws Exception {
        this.username = username;
        this.hash = hash;
        this.owner = account;
        this.salt = salt;

        try {
            initialize();
            globalList.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    
    /** 
     * @return String
     */
    public String getUsername() {
        return username;
    }
    
    private void setUsername(String username) throws Exception {
        if (username.isEmpty() || username.length() > 20) {
            throw new Exception("Name must be between 1-20 characters long.");
        }

        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    private static void initialize() throws Exception {
        if (initialized) {
            return;
        }

        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            rand = new SecureRandom();
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected String generateCredentialString() throws Exception {
        StringJoiner joiner = null;
        
        try {
            joiner = new StringJoiner(":");
            joiner.add(Account.HEADER_START).add(username).add(hash).add(salt);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return joiner.toString();
    }

    private void generateSalt() throws Exception {
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        byte[] saltBytes = new byte[16];
        rand.nextBytes(saltBytes);
        
        this.salt = "WIP";

        // this.salt = new String(saltBytes, StandardCharsets.UTF_8).replaceAll("\\s+","").replaceAll("\\s", "");;
        // this.salt = this.salt.replaceAll(":","a");
    }

    private String hashPassword(String password) throws Exception {
        String newHash = "";
        PBEKeySpec pass;

        byte[] defaultSalt = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p' };
        
        try {
            pass = new PBEKeySpec(password.toCharArray(), defaultSalt, ITERATIONS, KEY_SIZE);
            newHash = new String(keyFactory.generateSecret(pass).getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }

        newHash = newHash.replaceAll(":","a").replaceAll("\\s", "");
        return newHash;
    }

    private boolean isMatchingPassword(String loginPass) throws Exception {
        boolean result = false;
        try {
            String loginHash = hashPassword(loginPass);
            if(hash.equals(loginHash)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    public static Account matchCredentials(String loginUser, String loginPass) throws Exception {
        Account matchingAccount = null;

        for (Credentials creds : globalList) {
            if (creds.getUsername().equals(loginUser)) {
                try {
                    if (creds.isMatchingPassword(loginPass)) {
                        matchingAccount = creds.getOwner();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        return matchingAccount;
    }

    @Override
    public String toString() {
        String credString = "";

        try {
            credString = generateCredentialString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return credString;
    }
}
