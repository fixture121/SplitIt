import java.util.StringJoiner;

public class Expense {
    private static int claimedIDs = 0;
    
    private int id;
    private String name;
    private Account owner;
    private double amount;
    
    public Expense(String name, double amount, int id) {
        if (claimedIDs <= id) {
            claimedIDs = id + 1;
        }

        this.id = id;
        this.name = name;
        this.amount = amount;
    }
    
    public Expense(String name, Account owner, double amount) {
        this.id = claimedIDs++;
        this.name = name;
        this.owner = owner;
        this.amount = amount;
    }

    
    /** 
     * @return int
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public double getAmount() {
        return amount;
    }

    private String generateExpenseString() throws Exception {
        StringJoiner joiner = null;
        
        try {
            joiner = new StringJoiner(":");
            joiner.add(Account.BODY_START).add(String.valueOf(id)).add(name).add(String.valueOf(amount));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return joiner.toString();
    }

    @Override
    public String toString() {
        String expenseString = "";

        try {
            expenseString = generateExpenseString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return expenseString;
    }
}
