# SplitIt - PROG24178

A Java-based expense tracking application

Project Overview

Our application, Split It, allows multiple users to register and log in, with user credentials stored securely using encryption techniques. Users can create expense lists, assign expenses to different users, and calculate individual user totals. The application utilizes dynamic arrays to manage user accounts and their corresponding credentials. Additionally, file I/O operations are employed to save and load user data. This report outlines the implementation and technical aspects of our project.



Technical Implementation

GUI
The GUI is designed using JavaFX. The UI comprises several screens, including the Welcome Screen, Login Screen, Registration Screen, Account Screen, List Screen, and Add Expense Screen. Users click on buttons to navigate through the interface.


Account Information
User credentials are managed using the UserCredentials class. In order to safely store passwords, an irreversible hashing method (PBKDF2) ciphers the password into a SecretKey using Java’s built-in SecretKeyFactory and an array of 16 pseudorandom bits for salting.

When an account is created, its credentials are stored in the accounts.data class in a bitstream as a component of that user’s information, which is stored in multiple lines. The first line contains five colon-delimited fields consisting of a # character (to identify it as the first line of a user information block), the account’s username, the account’s hashed password, the salt used to hash the password, and the number of expense lists owned by that user. The last field is necessary for informing the program how many subsequent lines will contain information about that account’s expense lists.

Those lines consist of two colon-delimited fields.The first field is a $ character, which simply acts as a verification that the following data relates an account to an expense list whose details are stored in the expenses.data file. The second field contains a randomly generated five-digit integer representing an expense list’s unique ID, so that the program can find its details in expenses.data.

Whenever a new expense list is created, its ID is generated and stored into a global static ArrayList within the ExpenseList class that tracks reserved ID numbers. This way, the program avoids random number collisions through recursively calling the ExpenseList class’ assignID() method where necessary.


Login & Registration
Whenever a user makes a login attempt, their entered credentials are used to construct a new UserCredentials object using a constructor specialized for login attempts. The username provided is then compared to each saved account’s username in accounts.data until either a match is found or it isn’t. In the latter case, an alert will inform the user of an invalid login attempt. In the former, the program then retrieves the matching account’s salt and attempts to hash the entered password using it.

The PBKDF2 algorithm is known for its reliability and consistency when generating hashes from passwords. This is important, as hashing is not a reversible process, and the program matches the entered password with saved ones by comparing their hashed products, because the plain text password is not saved anywhere in the program for security purposes. Assuming a successful login takes place, the authenticated user is then welcome to their account overview screen.
