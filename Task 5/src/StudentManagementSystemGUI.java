import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

class StudentManagementSystem {
    private ArrayList<Student> students;
    private static final String FILE_PATH = "Students.txt";
    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        loadFromFile(FILE_PATH);
    }

    public void addStudent(Student student) {
        students.add(student);
        saveToFile(FILE_PATH);
    }

    public void removeStudent(int rollNumber) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRollNumber() == rollNumber) {
                students.remove(i);
                saveToFile(FILE_PATH);
                return;
            }
        }
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    private void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Student student : students) {
                writer.println(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile(String filename) {
        students.clear(); // Clear existing data before loading from file
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    int rollNumber = Integer.parseInt(parts[1]);
                    String grade = parts[2];
                    students.add(new Student(name, rollNumber, grade));
                }
            }
        } catch (FileNotFoundException e) {

        }
    }
}

public class StudentManagementSystemGUI extends JFrame {
    private StudentManagementSystem sms;

    private JTextField nameField, rollNumberField, gradeField;
    private JTextArea displayArea;

    public StudentManagementSystemGUI() {
        sms = new StudentManagementSystem();

        setTitle("Student Management System");
        setSize(600, 600); // Taille agrandie
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        inputPanel.add(rollNumberField);
        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        inputPanel.add(addButton);

        JButton removeButton = new JButton("Remove Student");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStudent();
            }
        });
        inputPanel.add(removeButton);

        JButton searchButton = new JButton("Search Student");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        inputPanel.add(searchButton);

        JButton displayButton = new JButton("Display Students");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllStudents();
            }
        });
        inputPanel.add(displayButton);

        add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addStudent() {
        String name = nameField.getText();
        int rollNumber = Integer.parseInt(rollNumberField.getText());
        String grade = gradeField.getText();

        sms.addStudent(new Student(name, rollNumber, grade));
        displayArea.append("Student added:\nName: " + name + "\nRoll Number: " + rollNumber + "\nGrade: " + grade + "\n\n");

        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
    }

    private void removeStudent() {
        String rollNumberText = rollNumberField.getText();
        if (rollNumberText.isEmpty()) {
            displayArea.append("Please enter the roll number to remove the student.\n");
            return;
        }
        int rollNumber = Integer.parseInt(rollNumberText);
        sms.removeStudent(rollNumber);
        displayArea.append("Student with roll number " + rollNumber + " removed.\n\n");

        rollNumberField.setText("");
    }

    private void searchStudent() {
        String rollNumberText = rollNumberField.getText();
        if (rollNumberText.isEmpty()) {
            displayArea.append("Please enter the roll number to search for the student.\n");
            return;
        }
        int rollNumber = Integer.parseInt(rollNumberText);
        Student student = sms.searchStudent(rollNumber);
        if (student != null) {
            displayArea.append("Student found:\n" + student + "\n\n");
        } else {
            displayArea.append("Student with roll number " + rollNumber + " not found.\n\n");
        }

        rollNumberField.setText("");
    }

    private void displayAllStudents() {
        displayArea.setText("");
        for (Student student : sms.getStudents()) {
            displayArea.append(student + "\n\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementSystemGUI().setVisible(true);
            }
        });
    }
}
