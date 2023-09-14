import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {
    public static ArrayList<Account> globalList = new ArrayList<Account>();

    public static final String HEADER_START = "#";
    public static final String BODY_START = "$";
    public static final String END_START = "&";
    public static final String DELIM = ":";
    
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("(#:\\S+?:\\S+?:\\S+\\s*){1}(\\$:\\d{4}:\\s*)*(&\\s*)+");
    private static final Pattern HEADER_PATTERN = Pattern.compile("(#:\\S+?:\\S+?:\\S+\\s*)");
    private static final Pattern BODY_PATTERN = Pattern.compile("\\$:\\d{4}:\\s*");

    private static File accountFile = new File("accounts.data");
    private static FileWriter writer;

    private Credentials credentials;
    private ArrayList<ExpenseList> userLists = new ArrayList<ExpenseList>();

    public Account(String username, String password) {
        try {
        this.credentials = new Credentials(username, password, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        globalList.add(this);
        try {
            printAllAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account(String username, String hash, String salt) {
        try {
            this.credentials = new Credentials(username, hash, this, salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * @return File
     */
    public static File getAccountFile() {
        return accountFile;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public int getListCount() {
        return userLists.size();
    }

    public String getName() {
        return credentials.getUsername();
    }

    public void addExpenseList(ExpenseList list) {
        this.userLists.add(list);
    }

    public void removeExpenseList(int id) {
        for (ExpenseList list : userLists) {
            if (list.getId() == id) {
                userLists.remove(list);
            }
        }
    }

    public void removeExpenseList(ExpenseList list) {
        userLists.remove(list);
    }

    public static void printAllAccounts() throws Exception {
        try {
            accountFile.createNewFile();
            writer = new FileWriter(accountFile, false);
            writer.close();

            for (Account acc : globalList) {
                acc.printAccountData();
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            writer.close();
        }
    }

    private String generateAccountString() throws Exception {
        String accountString = "";

        try {
            accountString += credentials + "\n";
            for (ExpenseList list : userLists) {
                accountString += list.generateListIDString() + "\n";
            }
            accountString += END_START + "\n";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return accountString;
    }
    
    public String printAccountData() throws Exception {
        String accountData = "";

        try {
            accountData = generateAccountString();
            writer = new FileWriter(accountFile, true);
            writer.write(accountData);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            writer.close();
        }

        return accountData;
    }

    @Override
    public String toString() {
        String accountString = "";
        
        try {
            accountString = generateAccountString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountString;
    }

    public static Account login(String username, String password) throws Exception {
        Account match = null;

        try {
            match = Credentials.matchCredentials(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return match;
    }

    private static Account readAccountData(String accountString) throws Exception {
        Account readAccount = null;

        Matcher headerMatcher = HEADER_PATTERN.matcher(accountString);
        Matcher bodyMatcher = BODY_PATTERN.matcher(accountString);

        String headerFields[] = new String[4];
        
        ArrayList<String> bodyRows = new ArrayList<String>();
        String bodyFields[] = new String[2];

        try {
            if (!headerMatcher.find()) {
                throw new Exception("Error: Invalid account header data found.");
            }
            
            String temp = headerMatcher.group();
            // System.out.println(temp);


            headerFields = temp.split(DELIM);
            readAccount = new Account(headerFields[1], headerFields[2], headerFields[3]);

            while (bodyMatcher.find()) {
                bodyRows.add(bodyMatcher.group());
            }

            ExpenseList currentList = null;
            
            for (String row : bodyRows) {
                bodyFields = row.split(DELIM);
                currentList = ExpenseList.findExpenseList(Integer.parseInt(bodyFields[1]));
                currentList.setOwner(readAccount);
                readAccount.addExpenseList(currentList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return readAccount;
    }

    public static void repopulateAccounts() throws Exception {
        globalList.clear();
        Matcher accountMatcher;
        String accountFileString = "";

        String temp = "";

        try {
            accountFile.createNewFile();
            accountFileString = new String(Files.readAllBytes(accountFile.toPath()));
            accountMatcher = ACCOUNT_PATTERN.matcher(accountFileString);

            while (accountMatcher.find()) {
                temp = accountMatcher.group();
                System.out.println(temp);
                globalList.add(readAccountData(temp));
            }

            // printAllAccounts();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
