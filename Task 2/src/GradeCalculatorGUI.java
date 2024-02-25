import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GradeCalculatorGUI extends JFrame {

    private JTextField[] marksFields;
    private JButton calculateButton;
    private JLabel totalMarksLabel;
    private JLabel averagePercentageLabel;
    private JLabel gradeLabel;

    public GradeCalculatorGUI() {
        setTitle("Grade Calculator");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        marksFields = new JTextField[6];
        for (int i = 0; i < marksFields.length; i++) {
            panel.add(new JLabel("Subject " + (i + 1) + ":"));
            marksFields[i] = new JTextField();
            panel.add(marksFields[i]);
        }

        calculateButton = new JButton("Calculate");
        panel.add(new JLabel(""));
        panel.add(calculateButton);

        totalMarksLabel = new JLabel("Total Marks: ");
        panel.add(totalMarksLabel);

        averagePercentageLabel = new JLabel("Average Percentage: ");
        panel.add(averagePercentageLabel);

        gradeLabel = new JLabel("Grade: ");
        panel.add(gradeLabel);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateResults();
            }
        });

        add(panel);
    }

    private void calculateResults() {
        int totalMarks = 0;
        int numSubjects = marksFields.length;
        double averagePercentage;

        for (int i = 0; i < numSubjects; i++) {
            try {
                int marks = Integer.parseInt(marksFields[i].getText());
                if (marks < 0 || marks > 100) {
                    JOptionPane.showMessageDialog(this, "Invalid marks entered. Marks should be between 0 and 100.");
                    return;
                }
                totalMarks += marks;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric marks.");
                return;
            }
        }

        averagePercentage = (double) totalMarks / numSubjects;

        totalMarksLabel.setText("Total Marks: " + totalMarks);
        averagePercentageLabel.setText("Average Percentage: " + String.format("%.2f%%", averagePercentage));
        gradeLabel.setText("Grade: " + calculateGrade(averagePercentage));
    }

    private String calculateGrade(double averagePercentage) {
        if (averagePercentage >= 90) {
            return "A+";
        } else if (averagePercentage >= 80) {
            return "A";
        } else if (averagePercentage >= 70) {
            return "B";
        } else if (averagePercentage >= 60) {
            return "C";
        } else if (averagePercentage >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GradeCalculatorGUI().setVisible(true);
            }
        });
    }
}
