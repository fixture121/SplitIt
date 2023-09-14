import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpenseList {
    public static ArrayList<ExpenseList> globalList = new ArrayList<ExpenseList>();
    
    private static final Pattern LIST_PATTERN = Pattern.compile("(#:\\d+?:\\S+?:\\S+\\s*){1}(\\$:\\d+?:\\S+?:\\d+?\\.\\d+\\s*)*(&\\s*)");
    private static final Pattern HEADER_PATTERN = Pattern.compile("#:\\d+?:\\S+?:\\S+\\s*");
    private static final Pattern BODY_PATTERN = Pattern.compile("\\$:\\d+?:\\S+?:\\d+?\\.\\d+\\s*");

    private static int claimedIDs = 1;
    private static File expenseFile = new File("expenses.data");
    private static FileWriter writer;

    private int id;
    private String name;
    private Account owner;
    private ArrayList<Expense> expenses;

    public ExpenseList(String name, int id) {
        if (claimedIDs <= id) {
            claimedIDs = id + 1;
        }

        this.id = id;
        this.name = name;
        this.expenses = new ArrayList<Expense>();
    }

    public ExpenseList(String name, Account owner) {
        this.id = claimedIDs++;
        this.name = name;
        this.owner = owner;
        this.expenses = new ArrayList<Expense>();

        globalList.add(this);
    }

    public ExpenseList(String name, Account owner, ArrayList<Expense> expenses) {
        this.id = claimedIDs++;
        this.name = name;
        this.owner = owner;
        this.expenses = expenses;

        globalList.add(this);
    }

    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
        for (Expense expense : expenses) {
            expense.setOwner(owner);
        }
    }

    public double getTotal() {
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

    public void addExpense(Expense e) {
        this.expenses.add(e);
    }

    public void removeExpense(int id) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                this.expenses.remove(e);
            }
        }
    }

    public void removeExpense(Expense e) {
        this.expenses.remove(e);
    }
    
    public String generateListIDString() {
        return String.format("%s:%04d:", Account.BODY_START, id);
    }
    
    private String generateExpenseListHeader() {
        StringJoiner joiner = null;
        
        try {
            joiner = new StringJoiner(Account.DELIM);
            joiner.add(Account.HEADER_START).add(String.valueOf(id)).add(name).add(owner.getName());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return joiner.toString();
    }

    public String generateExpenseListString() throws Exception {
        String listString = "";

        try {
            listString += generateExpenseListHeader() + "\n";
            for (Expense e : expenses) {
                listString += e + "\n";
            }
            listString += Account.END_START + "\n";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return listString;
    }

    public String printExpenseListData() throws Exception {
        String listData = "";

        try {
            listData = generateExpenseListString();
            expenseFile.createNewFile();
            writer = new FileWriter(expenseFile, true);
            writer.write(listData);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            writer.close();
        }

        return listData;
    }

    public static void printAllExpenseLists() throws Exception {
        try {
            writer = new FileWriter(expenseFile, false);
            writer.close();

            for (ExpenseList list : globalList) {
                list.printExpenseListData();
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            writer.close();
        }
    }

    public static ExpenseList findExpenseList(int id) {
        ExpenseList result = null;
        
        for (ExpenseList expenseList : globalList) {
            if (expenseList.getId() == id) {
                result = expenseList;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        String listString = "";
        
        try {
            listString = generateExpenseListString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listString;
    }

    private static ExpenseList readListData(String listString) throws Exception {
        ExpenseList readList = null;

        Matcher headerMatcher = HEADER_PATTERN.matcher(listString);
        Matcher bodyMatcher = BODY_PATTERN.matcher(listString);

        String headerFields[] = new String[4];
        
        ArrayList<String> bodyRows = new ArrayList<String>();
        String bodyFields[] = new String[4];

        try {
            if (!headerMatcher.find()) {
                throw new Exception("Error: Invalid list header data found.");
            }
            
            headerFields = headerMatcher.group().split(Account.DELIM);
            readList = new ExpenseList(headerFields[2], Integer.parseInt(headerFields[1]));

            while (bodyMatcher.find()) {
                bodyRows.add(bodyMatcher.group());
            }
            
            for (String row : bodyRows) {
                bodyFields = row.split(Account.DELIM);
                readList.addExpense(new Expense(bodyFields[2], 
                Double.parseDouble(bodyFields[3]), 
                Integer.parseInt(bodyFields[1])));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return readList;
    }

    public static void repopulateExpenseLists() throws Exception {
        globalList.clear();
        Matcher listMatcher;

        String expenseFileString = "";
        try {
            expenseFile.createNewFile();
            expenseFileString = new String(Files.readAllBytes(expenseFile.toPath()));
            listMatcher = LIST_PATTERN.matcher(expenseFileString);

            while (listMatcher.find()) {
                globalList.add(readListData(listMatcher.group()));
            }

            printAllExpenseLists();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
