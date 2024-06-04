import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MoneyTrackOk {
    private static int debit_balance = 500;
    private static int credit_balance = 0;
    private static final int CREDIT_LIMIT = 30000; // Maximum credit limit
    private static final String USERS_FILE = "users.txt"; // File to store user credentials
    private static String name;

    public static void main(String[] args) {
        

        // Create the main frame
        JFrame frame = new JFrame("Money Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new CardLayout());

        // Create the panels
        JPanel loginPanel = createLoginPanel(frame);
        JPanel registerPanel = createRegisterPanel(frame);
        JPanel mainPanel = createMainPanel(frame);
        JPanel debitPanel = createDebitPanel(frame);
        JPanel creditPanel = createCreditPanel(frame);
        
        // Add the panels to the frame
        frame.add(loginPanel, "Login");
        frame.add(registerPanel, "Register");
        frame.add(mainPanel, "Main");
        frame.add(debitPanel, "Debit");
        frame.add(creditPanel, "Credit");

        // Show the login panel
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");

        // Set frame visible
        frame.setVisible(true);
    }

    // Initialize balances by reading from the files, or create the files if they don't exist
    public static void initializeBalances() {
        debit_balance = readBalance(getName() + "debit_balance.txt", debit_balance);
        credit_balance = readBalance(getName() + "credit_balance.txt", credit_balance);
    }

    // Read the balance from the file, or return the default balance if the file doesn't exist
    public static int readBalance(String fileName, int defaultBalance) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            saveBalance(fileName, defaultBalance);
        }
        return defaultBalance;
    }

    // Save the current balance to the file
    public static void saveBalance(String fileName, int balance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(balance));
        } catch (IOException e) {
            System.out.println("An error occurred while saving the balance.");
            e.printStackTrace();
        }
    }

    // Save a new user to the file
    public static void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while saving user data.");
            e.printStackTrace();
        }
    }

    // Check if the user exists in the file
    public static boolean checkUserExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while checking user data.");
            e.printStackTrace();
        }
        return false;
    }

    // Authenticate user by checking credentials
    public static boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while authenticating user.");
            e.printStackTrace();
        }
        return false;
    }

    // Create login panel
    public static JPanel createLoginPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panel.setBackground(new Color(135, 206, 235));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        userField.setBackground(new Color(251, 246, 237));
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        passField.setBackground(new Color(251, 246, 237));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (authenticateUser(username, password)) {
                setName(username);
                initializeBalances();
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Register");
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    // Create register panel
    public static JPanel createRegisterPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panel.setBackground(new Color(135, 206, 235));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        userField.setBackground(new Color(251, 246, 237));
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        passField.setBackground(new Color(251, 246, 237));
        JLabel confirmLabel = new JLabel("Confirm Password:");
        JPasswordField confirmField = new JPasswordField();
        confirmField.setBackground(new Color(251, 246, 237));

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (checkUserExists(username)) {
                JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                saveUser(username, password);
                JOptionPane.showMessageDialog(frame, "Registration successful!");
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(confirmLabel);
        panel.add(confirmField);
        panel.add(registerButton);
        panel.add(backButton);

        return panel;
    }

    // Create main panel
    public static JPanel createMainPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panel.setBackground(new Color(135, 206, 235));

        JButton debitButton = new JButton("Debit Balance");
        debitButton.setBackground(new Color(144, 238, 144));
        debitButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Debit");
        });

        JButton creditButton = new JButton("Credit Balance");
        creditButton.setBackground(new Color(252, 238, 167));
        creditButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Credit");
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 71, 76));
        logoutButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");
        });

        panel.add(debitButton);
        panel.add(creditButton);
        panel.add(logoutButton);

        return panel;
    }

    // Create debit panel
    public static JPanel createDebitPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 6, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panel.setBackground(new Color(135, 206, 235));

        JLabel balanceLabel = new JLabel("Balance: $" + debit_balance, SwingConstants.CENTER);
        JButton cashInButton = new JButton("Cash In");
        cashInButton.setBackground(new Color(144, 238, 144));
        JButton cashOutButton = new JButton("Cash Out");
        cashOutButton.setBackground(new Color(255, 71, 76));
        JButton historyButton = new JButton("View History");
        historyButton.setBackground(new Color(252, 238, 167));
        JButton backButton = new JButton("Back");
        JButton exitButton = new JButton("Exit");



        cashInButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter amount to cash in:");

            try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
                
            } catch (final Exception et) {
        
            }




            try {
                int amount = Integer.parseInt(input);
                if (amount < 0) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a positive number.");
                } else {
                    debit_balance += amount;
                    saveTransaction(getName() + "debit_history.txt", "Cash in of $" + amount);
                    saveBalance(getName() + "debit_balance.txt", debit_balance);
                    balanceLabel.setText("Balance: $" + debit_balance);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a number.");
            }
        });

        cashOutButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter amount to cash out:");
            try {
                int amount = Integer.parseInt(input);
                if (amount < 0) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a positive number.");
                } else if (amount > debit_balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient Balance, please try again.");
                } else {
                    debit_balance -= amount;
                    saveTransaction(getName() + "debit_history.txt", "Cash out of $" + amount);
                    saveBalance(getName() + "debit_balance.txt", debit_balance);
                    balanceLabel.setText("Balance: $" + debit_balance);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a number.");
            }
        });

        historyButton.addActionListener(e -> {
            String history = readTransactionHistory(getName() + "debit_history.txt");
            JOptionPane.showMessageDialog(frame, history.isEmpty() ? "No transactions yet." : history);
        });

        backButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        panel.add(balanceLabel);
        panel.add(cashInButton);
        panel.add(cashOutButton);
        panel.add(historyButton);
        panel.add(backButton);
        panel.add(exitButton);

        return panel;
    }

    // Create credit panel
    public static JPanel createCreditPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 6, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        panel.setBackground(new Color(135, 206, 235));

        JLabel balanceLabel = new JLabel("Balance: $" + credit_balance, SwingConstants.CENTER);
        JButton cashInButton = new JButton("Add Credit");
        cashInButton.setBackground(new Color(255, 71, 76));
        JButton cashOutButton = new JButton("Decrease Credit");
        cashOutButton.setBackground(new Color(144, 238, 144));
        JButton historyButton = new JButton("View History");
        historyButton.setBackground(new Color(252, 238, 167));
        JButton backButton = new JButton("Back");
        JButton exitButton = new JButton("Exit");

        cashInButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter amount to add:");
            
            try {
                int amount = Integer.parseInt(input);
                if (amount < 0) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a positive number.");
                } else if (credit_balance + amount > CREDIT_LIMIT) {
                    JOptionPane.showMessageDialog(frame, "Transaction declined! Exceeds credit limit of $" + CREDIT_LIMIT);
                } else {
                    credit_balance += amount;
                    saveTransaction( getName() + "_credit_history.txt", "Add credit of $" + amount);
                    saveBalance( getName() + "credit_balance.txt", credit_balance);
                    balanceLabel.setText("Balance: $" + credit_balance);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a number.");
            }
        });

        cashOutButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter amount to decrease:");
            try {
                int amount = Integer.parseInt(input);
                if (amount < 0) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a positive number.");
                } else if (amount > credit_balance) {
                    JOptionPane.showMessageDialog(frame, "Your payment is more than the remaining balance!");
                } else {
                    credit_balance -= amount;
                    saveTransaction(getName() + "credit_history.txt", "Decrease credit of $" + amount);
                    saveBalance(getName() + "credit_balance.txt", credit_balance);
                    balanceLabel.setText("Balance: $" + credit_balance);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a number.");
            }
        });

        historyButton.addActionListener(e -> {
            String history = readTransactionHistory(getName() + "credit_history.txt");
            JOptionPane.showMessageDialog(frame, history.isEmpty() ? "No transactions yet." : history);
        });

        backButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        panel.add(balanceLabel);
        panel.add(cashInButton);
        panel.add(cashOutButton);
        panel.add(historyButton);
        panel.add(backButton);
        panel.add(exitButton);

        return panel;
    }

    // Save transaction history
    public static void saveTransaction(String fileName, String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(transaction);
            writer.newLine();
        } catch (IOException ex) {
            System.out.println("An error has occurred, and the file cannot be written to.");
            ex.printStackTrace();
        }
    }

    // Read transaction history
    public static String readTransactionHistory(String fileName) {
        StringBuilder history = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the transaction history.");
            e.printStackTrace();
        }
        return history.toString();
    }

    public static String getName() {
        return name;
    }

    // Setter
    public static void setName(String newName) {
        name = newName;
    }
}
    
