public class App {

    public static void newLine() {
        System.out.println("\n======================================\n");
    }

    
    /** 
     * @param title
     */
    public static void newLine(String title) {
        System.out.println("\n==================== " + title + " ====================\n");
    }

    private static void readCheck() throws Exception {
        ExpenseList.repopulateExpenseLists();
        Account.repopulateAccounts();

        ExpenseList.globalList.forEach((list) -> System.out.println(list));

        Account.globalList.forEach((account) -> System.out.println(account));
    }

    private static void writeCheck() throws Exception {
        newLine("Account Creation");
        
        String userOne = "Mo";
        String passOne = "pass";

        Account account = new Account(userOne, passOne);

        try {
            System.out.println(account.getCredentials());
        } catch (Exception e) {
            e.printStackTrace();
        }

        newLine("Empty Expense List");

        ExpenseList list = new ExpenseList("Monthly", account);
        System.out.println(list);

        newLine("New Expenses");

        Expense expOne = new Expense("Food", account, 25.04);
        Expense expTwo = new Expense("Gas", account, 53.04);
        Expense expThree = new Expense("Rent", account, 842.04);

        System.out.println(expOne);
        System.out.println(expTwo);
        System.out.println(expThree);

        newLine("Adding Expenses to List");

        list.addExpense(expOne);
        list.addExpense(expTwo);
        list.addExpense(expThree);

        System.out.println(list.printExpenseListData());

        newLine("Adding Expense List to Account");

        account.addExpenseList(list);

        System.out.println(account.printAccountData());

        newLine("Printing Account With Multiple Lists");

        Expense newExpOne = new Expense("Hotel", account, 525.04);
        Expense newExpTwo = new Expense("Travago", account, 53.04);
        ExpenseList newList = new ExpenseList("Vacation", account);

        newList.addExpense(newExpOne);
        newList.addExpense(newExpTwo);
        account.addExpenseList(newList);

        System.out.println(account.printAccountData());

        newLine("Printing All Accounts");

        Expense lastExpOne = new Expense("Travel", account, 525.04);
        Expense lastExpTwo = new Expense("Snacks", account, 53.04);
        ExpenseList lastList = new ExpenseList("Road Trip", account);
        
        String userTwo = "Toufiq";
        String passTwo = "password";

        Account accountTwo = new Account(userTwo, passTwo);

        lastList.addExpense(lastExpOne);
        lastList.addExpense(lastExpTwo);
        accountTwo.addExpenseList(lastList);

        newLine("Logging In Correctly");

        String loginUser = new String("Mo");
        String loginPass = new String("pass");

        Account match = Account.login(loginUser, loginPass);
        if (match != null) {
            System.out.println(match);
        }

        newLine("Logging In Incorrectly");

        String loginBadUser = new String("aMo");
        String loginBadPass = new String("apass");

        Account matchBad = Account.login(loginBadUser, loginBadPass);
        if (matchBad != null) {
            System.out.println(match);
        }

        Account.printAllAccounts();
        ExpenseList.printAllExpenseLists();
    }

    // public static void main(String[] args) throws Exception {

    //     try {
    //         readCheck();
    //         writeCheck();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    // }
}
