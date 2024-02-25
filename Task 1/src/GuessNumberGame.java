import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuessNumberGame extends JFrame {

    private int generatedNumber;
    private int attempts;
    private int maxAttempts = 10;
    private int roundsWon;

    private JTextField guessField;
    private JLabel feedbackLabel;
    private JButton guessButton;
    private JButton playAgainButton;
    private JButton quitButton;

    public GuessNumberGame() {
        setTitle("Guess the Number Game");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        guessField = new JTextField();
        feedbackLabel = new JLabel("Enter your guess:", SwingConstants.CENTER);
        guessButton = new JButton("Guess");
        playAgainButton = new JButton("Play Again");
        quitButton = new JButton("Quit");

        // Add components to layout
        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(feedbackLabel);
        panel.add(guessField);
        panel.add(guessButton);
        panel.add(playAgainButton);
        panel.add(quitButton);
        add(panel);

        // Initialize game
        initializeGame();

        // Add action listeners
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });

        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeGame();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void initializeGame() {
        generatedNumber = generateRandomNumber(1, 100);
        attempts = 0;
        feedbackLabel.setText("Enter your guess:");
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false);
        quitButton.setEnabled(true);
        guessField.setText("");
    }

    private int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private void checkGuess() {
        try {
            int userGuess = Integer.parseInt(guessField.getText());
            attempts++;

            if (userGuess == generatedNumber) {
                feedbackLabel.setText("Congratulations! You guessed the number in " + attempts + " attempts.");
                guessButton.setEnabled(false);
                playAgainButton.setEnabled(true);
                quitButton.setEnabled(false);
                roundsWon++;
            } else if (userGuess < generatedNumber) {
                feedbackLabel.setText("Too low! Try again.");
            } else {
                feedbackLabel.setText("Too high! Try again.");
            }

            if (attempts == maxAttempts) {
                feedbackLabel.setText("Sorry, you've run out of attempts. The correct number was " + generatedNumber + ".");
                guessButton.setEnabled(false);
                playAgainButton.setEnabled(true);
                quitButton.setEnabled(false);
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Please enter a valid number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuessNumberGame().setVisible(true);
            }
        });
    }
}
