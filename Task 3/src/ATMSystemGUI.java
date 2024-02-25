import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class BankAccount {
    private String accountID;
    private double balance;

    public BankAccount(String accountID, double initialBalance) {
        this.accountID = accountID;
        this.balance = initialBalance;
    }

    public String getAccountID() {
        return accountID;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}

class ATM {
    private ArrayList<BankAccount> accounts;

    public ATM() {
        this.accounts = new ArrayList<>();
        loadAccountsFromFile("accounts.txt");
    }

    public BankAccount getUserAccount(String accountID) {
        for (BankAccount account : accounts) {
            if (account.getAccountID().equals(accountID)) {
                return account;
            }
        }
        return null;
    }

    private void loadAccountsFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 2) {
                    String accountID = parts[0];
                    double initialBalance = Double.parseDouble(parts[1]);
                    accounts.add(new BankAccount(accountID, initialBalance));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveAccountsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (BankAccount account : accounts) {
                writer.println(account.getAccountID() + "," + account.getBalance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deposit(String accountID, double amount) {
        BankAccount userAccount = getUserAccount(accountID);
        if (userAccount != null) {
            userAccount.deposit(amount);
            saveAccountsToFile("accounts.txt");
        }
    }

    public boolean withdraw(String accountID, double amount) {
        BankAccount userAccount = getUserAccount(accountID);
        if (userAccount != null) {
            boolean success = userAccount.withdraw(amount);
            if (success) {
                saveAccountsToFile("accounts.txt");
            }
            return success;
        }
        return false;
    }
}

public class ATMSystemGUI extends JFrame {
    private ATM atm;

    private JTextField accountIDField, amountField;
    private JTextArea outputArea;

    private static final int WITHDRAW_OPTION = 1;
    private static final int DEPOSIT_OPTION = 2;
    private static final int CHECK_BALANCE_OPTION = 3;

    public ATMSystemGUI() {
        atm = new ATM();

        setTitle("ATM System");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Account ID:"));
        accountIDField = new JTextField();
        inputPanel.add(accountIDField);
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processOption(WITHDRAW_OPTION);
            }
        });
        inputPanel.add(withdrawButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processOption(DEPOSIT_OPTION);
            }
        });
        inputPanel.add(depositButton);

        JButton checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processOption(CHECK_BALANCE_OPTION);
            }
        });
        inputPanel.add(checkBalanceButton);

        add(inputPanel, BorderLayout.WEST);

        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void processOption(int option) {
        String accountID = accountIDField.getText();
        String amountText = amountField.getText();

        // Vérifier si les champs ne sont pas vides
        if (accountID.isEmpty() || amountText.isEmpty()) {
            outputArea.append("Account ID and amount cannot be empty.\n");
            return;
        }

        // Vérifier si le compte existe
        BankAccount userAccount = atm.getUserAccount(accountID);
        if (userAccount == null) {
            outputArea.append("Account ID not found.\n");
            return;
        }

        double amount = Double.parseDouble(amountText);

        // Traiter l'opération en fonction de l'option
        switch (option) {
            case WITHDRAW_OPTION:
                // Effectuer le retrait
                if (atm.withdraw(accountID, amount)) {
                    outputArea.append("Withdrawal successful. New balance: " + userAccount.getBalance() + "\n");
                } else {
                    outputArea.append("Insufficient funds. Withdrawal failed.\n");
                }
                break;

            case DEPOSIT_OPTION:
                // Effectuer le dépôt
                atm.deposit(accountID, amount);
                outputArea.append("Deposit successful. New balance: " + userAccount.getBalance() + "\n");
                break;

            case CHECK_BALANCE_OPTION:
                // Vérifier le solde
                outputArea.append("Account balance: " + userAccount.getBalance() + "\n");
                break;

            default:
                // Autres options, si nécessaire
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATMSystemGUI().setVisible(true);
            }
        });
    }
}
