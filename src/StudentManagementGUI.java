import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementGUI extends JFrame {
    private List<Student> students = new ArrayList<>();

    // validation part
    private boolean validateInput(String id, String firstName, String lastName, String gender, String gpa, String level, String address) {
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || gpa.isEmpty() || level.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!");
            return false;
        }
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+") || !address.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Names and Address must contain only letters!");
            return false;
        }
        try {
            double gpaValue = Double.parseDouble(gpa);
            if (gpaValue < 0 || gpaValue > 4) {
                JOptionPane.showMessageDialog(this, "GPA must be between 0 and 4!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "GPA must be a number!");
            return false;
        }
        try {
            Integer.parseInt(level);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Level must be a number!");
            return false;
        }
        return true;
    }

    public StudentManagementGUI() {
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Student Tab
        JPanel addPanel = new JPanel(new GridLayout(8, 2));
        JTextField idField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField gpaField = new JTextField();
        JTextField levelField = new JTextField();
        JTextField addressField = new JTextField();
        JButton addButton = new JButton("Add Student");

        addPanel.add(new JLabel("ID:"));
        addPanel.add(idField);
        addPanel.add(new JLabel("First Name:"));
        addPanel.add(firstNameField);
        addPanel.add(new JLabel("Last Name:"));
        addPanel.add(lastNameField);
        addPanel.add(new JLabel("Gender:"));
        addPanel.add(genderField);
        addPanel.add(new JLabel("GPA:"));
        addPanel.add(gpaField);
        addPanel.add(new JLabel("Level:"));
        addPanel.add(levelField);
        addPanel.add(new JLabel("Address:"));
        addPanel.add(addressField);
        addPanel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                Student student = new Student(
                        idField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        genderField.getText(),
                        Double.parseDouble(gpaField.getText()),
                        Integer.parseInt(levelField.getText()),
                        addressField.getText()
                );

                if (XMLHandler.doesIdExist(idField.getText())) {
                    JOptionPane.showMessageDialog(this, "Error: Student ID already exists!");
                    return;
                }

                if (!validateInput(idField.getText(), firstNameField.getText(), lastNameField.getText(), genderField.getText(), gpaField.getText(), levelField.getText(), addressField.getText())) {
                    return;
                }


                students.add(student);
                XMLHandler.saveStudents(students);
                XMLHandler.sortStudentsById();

                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Update Student tab
        JPanel updatePanel = new JPanel(new GridLayout(8, 2, 10, 10));

        JLabel updateIdLabel = new JLabel("Enter Student ID:");
        JTextField updateIdField = new JTextField();

        JLabel updateFirstNameLabel = new JLabel("First Name:");
        JTextField updateFirstNameField = new JTextField();

        JLabel updateLastNameLabel = new JLabel("Last Name:");
        JTextField updateLastNameField = new JTextField();

        JLabel updateGenderLabel = new JLabel("Gender:");
        JTextField updateGenderField = new JTextField();

        JLabel updateGpaLabel = new JLabel("GPA:");
        JTextField updateGpaField = new JTextField();

        JLabel updateLevelLabel = new JLabel("Level:");
        JTextField updateLevelField = new JTextField();

        JLabel updateAddressLabel = new JLabel("Address:");
        JTextField updateAddressField = new JTextField();

        JButton updateButton = new JButton("Update Student");

        updatePanel.add(updateIdLabel);
        updatePanel.add(updateIdField);
        updatePanel.add(updateFirstNameLabel);
        updatePanel.add(updateFirstNameField);
        updatePanel.add(updateLastNameLabel);
        updatePanel.add(updateLastNameField);
        updatePanel.add(updateGenderLabel);
        updatePanel.add(updateGenderField);
        updatePanel.add(updateGpaLabel);
        updatePanel.add(updateGpaField);
        updatePanel.add(updateLevelLabel);
        updatePanel.add(updateLevelField);
        updatePanel.add(updateAddressLabel);
        updatePanel.add(updateAddressField);
        updatePanel.add(new JLabel()); // Spacer
        updatePanel.add(updateButton);

        updateButton.addActionListener(e -> {
            try {
                String id = updateIdField.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Error: Student ID is required to update.");
                    return;
    
                }

                // Load all students
                List<Student> students = XMLHandler.loadStudents();
                boolean found = false;

                for (Student student : students) {
                    if (student.getId().equals(id)) {
                        found = true;

                        // Update fields if provided
                        if (!updateFirstNameField.getText().trim().isEmpty()) {
                            student.setFirstName(updateFirstNameField.getText().trim());
                        }
                        if (!updateLastNameField.getText().trim().isEmpty()) {
                            student.setLastName(updateLastNameField.getText().trim());
                        }
                        if (!updateGenderField.getText().trim().isEmpty()) {
                            student.setGender(updateGenderField.getText().trim());
                        }
                        if (!updateGpaField.getText().trim().isEmpty()) {
                            double gpa = Double.parseDouble(updateGpaField.getText().trim());
                            if (gpa < 0 || gpa > 4) {
                                JOptionPane.showMessageDialog(this, "Error: GPA must be between 0 and 4.");
                                return;
                            }
                            student.setGpa(gpa);
                        }
                        if (!updateLevelField.getText().trim().isEmpty()) {
                            int level = Integer.parseInt(updateLevelField.getText().trim());
                            student.setLevel(level);
                        }
                        if (!updateAddressField.getText().trim().isEmpty()) {
                            student.setAddress(updateAddressField.getText().trim());
                        }

                        break;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this, "Error: Student ID not found.");
                    return;
                }

                XMLHandler.saveStudents(students);
                XMLHandler.sortStudentsById();

                JOptionPane.showMessageDialog(this, "Student updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid input format.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });


        // Delete Student Tab 
        JPanel deletePanel = new JPanel(); 
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center components with padding 
 
        JLabel deleteIdLabel = new JLabel("Enter Student ID:"); 
        deletePanel.add(deleteIdLabel); 
 
        JTextField deleteIdField = new JTextField(20);  // Smaller width (20 characters wide) 
        deletePanel.add(deleteIdField); 
 
        // Spacer for better visual 
        deletePanel.add(Box.createVerticalStrut(10)); 
 
        JButton deleteButton = new JButton("Delete Student"); 
        deletePanel.add(deleteButton); 
 
        deleteButton.addActionListener(e -> { 
            String id = deleteIdField.getText().trim(); 
            if (id.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Error: Student ID is required to delete."); 
                return; 
            } 
 
            try { 
                // Load all students 
                List<Student> students = XMLHandler.loadStudents(); 
                boolean found = students.removeIf(student -> student.getId().equals(id)); 
 
                if (!found) { 
                    JOptionPane.showMessageDialog(this, "Error: Student ID not found."); 
                    return; 
                } 
 
                // Save updated list back to the XML file 
                XMLHandler.saveStudents(students); 
                JOptionPane.showMessageDialog(this, "Student deleted successfully!"); 
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Error !!" + ex.getMessage()); 
            } 
        });





        // panel for viewing the XML file
        JPanel viewPanel = new JPanel(new BorderLayout());

        JTextArea xmlContentArea = new JTextArea();
        xmlContentArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(xmlContentArea);
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        JButton loadButton = new JButton("Load XML File");
        viewPanel.add(loadButton, BorderLayout.SOUTH);


        loadButton.addActionListener(e -> {
            try {
                List<Student> students = XMLHandler.loadStudents();

                StringBuilder xmlContent = new StringBuilder();
                for (Student student : students) {
                    xmlContent.append("Student ID: ").append(student.getId()).append("\n");
                    xmlContent.append("First Name: ").append(student.getFirstName()).append("\n");
                    xmlContent.append("Last Name: ").append(student.getLastName()).append("\n");
                    xmlContent.append("Gender: ").append(student.getGender()).append("\n");
                    xmlContent.append("GPA: ").append(student.getGpa()).append("\n");
                    xmlContent.append("Level: ").append(student.getLevel()).append("\n");
                    xmlContent.append("Address: ").append(student.getAddress()).append("\n");
                    xmlContent.append("--------------------------------------------\n");
                }

                xmlContentArea.setText(xmlContent.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading XML file: " + ex.getMessage());
            }
        });

        // Panel for searching and sorting students
        JPanel searchPanel = new JPanel(new GridLayout(11, 2, 10, 10));

        JLabel searchIdLabel = new JLabel("Student ID:");
        JTextField searchIdField = new JTextField();

        JLabel searchFirstNameLabel = new JLabel("First Name:");
        JTextField searchFirstNameField = new JTextField();

        JLabel searchLastNameLabel = new JLabel("Last Name:");
        JTextField searchLastNameField = new JTextField();

        JLabel searchGenderLabel = new JLabel("Gender:");
        JTextField searchGenderField = new JTextField();

        JLabel searchGpaLabel = new JLabel("GPA:");
        JTextField searchGpaField = new JTextField();

        JLabel searchLevelLabel = new JLabel("Level:");
        JTextField searchLevelField = new JTextField();

        JLabel searchAddressLabel = new JLabel("Address:");
        JTextField searchAddressField = new JTextField();

        // Sorting inputs
        JLabel sortAttributeLabel = new JLabel("Sort By (id, firstname, lastname, gender, gpa, level, address):");
        JTextField sortAttributeField = new JTextField();

        JLabel sortOrderLabel = new JLabel("Sort Order (asc/desc):");
        JTextField sortOrderField = new JTextField();

        JButton searchButton = new JButton("Search");

        JTextArea searchResultsArea = new JTextArea(10, 30);
        searchResultsArea.setEditable(false);
        JScrollPane resultsScrollPane = new JScrollPane(searchResultsArea);

        // Adding components to the panel
        searchPanel.add(searchIdLabel);
        searchPanel.add(searchIdField);
        searchPanel.add(searchFirstNameLabel);
        searchPanel.add(searchFirstNameField);
        searchPanel.add(searchLastNameLabel);
        searchPanel.add(searchLastNameField);
        searchPanel.add(searchGenderLabel);
        searchPanel.add(searchGenderField);
        searchPanel.add(searchGpaLabel);
        searchPanel.add(searchGpaField);
        searchPanel.add(searchLevelLabel);
        searchPanel.add(searchLevelField);
        searchPanel.add(searchAddressLabel);
        searchPanel.add(searchAddressField);
        searchPanel.add(sortAttributeLabel);
        searchPanel.add(sortAttributeField);
        searchPanel.add(sortOrderLabel);
        searchPanel.add(sortOrderField);
        searchPanel.add(new JLabel()); // Spacer
        searchPanel.add(searchButton);
        searchPanel.add(resultsScrollPane);

        // Search and sort button action listener
        searchButton.addActionListener(e -> {
            try {
                List<Student> students = XMLHandler.loadStudents();

                String searchId = searchIdField.getText().trim();
                String searchFirstName = searchFirstNameField.getText().trim();
                String searchLastName = searchLastNameField.getText().trim();
                String searchGender = searchGenderField.getText().trim();
                String searchGpaText = searchGpaField.getText().trim();
                String searchLevelText = searchLevelField.getText().trim();
                String searchAddress = searchAddressField.getText().trim();

                String sortAttribute = sortAttributeField.getText().trim().toLowerCase();
                String sortOrder = sortOrderField.getText().trim().toLowerCase();

                List<Student> matchedStudents = new ArrayList<>();

                // Filtering logic
                for (Student student : students) {
                    boolean matches = true;

                    if (!searchId.isEmpty() && !student.getId().contains(searchId)) {
                        matches = false;
                    }
                    if (!searchFirstName.isEmpty() && !student.getFirstName().contains(searchFirstName)) {
                        matches = false;
                    }
                    if (!searchLastName.isEmpty() && !student.getLastName().contains(searchLastName)) {
                        matches = false;
                    }
                    if (!searchGender.isEmpty() && !student.getGender().contains(searchGender)) {
                        matches = false;
                    }
                    if (!searchGpaText.isEmpty()) {
                        try {
                            double searchGpa = Double.parseDouble(searchGpaText);
                            if (student.getGpa() != searchGpa) {
                                matches = false;
                            }
                        } catch (NumberFormatException ex) {
                            matches = false;
                        }
                    }
                    if (!searchLevelText.isEmpty()) {
                        try {
                            int searchLevel = Integer.parseInt(searchLevelText);
                            if (student.getLevel() != searchLevel) {
                                matches = false;
                            }
                        } catch (NumberFormatException ex) {
                            matches = false;
                        }
                    }
                    if (!searchAddress.isEmpty() && !student.getAddress().contains(searchAddress)) {
                        matches = false;
                    }

                    if (matches) {
                        matchedStudents.add(student);
                    }
                }

                // Sorting logic
                matchedStudents.sort((s1, s2) -> {
                    int comparison = 0;

                    switch (sortAttribute) {
                        case "id":
                            comparison = s1.getId().compareTo(s2.getId());
                            break;
                        case "firstname":
                            comparison = s1.getFirstName().compareTo(s2.getFirstName());
                            break;
                
                        case "lastname":
                            comparison = s1.getLastName().compareTo(s2.getLastName());
                            break;
                        case "gender":
                            comparison = s1.getGender().compareTo(s2.getGender());
                            break;
                        case "gpa":
                            comparison = Double.compare(s1.getGpa(), s2.getGpa());
                            break;
                        case "level":
                            comparison = Integer.compare(s1.getLevel(), s2.getLevel());
                            break;
                        case "address":
                            comparison = s1.getAddress().compareTo(s2.getAddress());
                            break;
                        default:
                            comparison = s1.getId().compareTo(s2.getId());
                    }

                    if (sortOrder.isEmpty()) return comparison;

                    return sortOrder.equals("asc") ? comparison : -comparison;
                });

                // Displaying results
                StringBuilder resultsContent = new StringBuilder();
                resultsContent.append("Found ").append(matchedStudents.size()).append(" students:\n\n");

                for (Student student : matchedStudents) {
                    resultsContent.append("Student ID: ").append(student.getId()).append("\n");
                    resultsContent.append("First Name: ").append(student.getFirstName()).append("\n");
                    resultsContent.append("Last Name: ").append(student.getLastName()).append("\n");
                    resultsContent.append("Gender: ").append(student.getGender()).append("\n");
                    resultsContent.append("GPA: ").append(student.getGpa()).append("\n");
                    resultsContent.append("Level: ").append(student.getLevel()).append("\n");
                    resultsContent.append("Address: ").append(student.getAddress()).append("\n");
                    resultsContent.append("--------------------------------------------\n");
                }

                searchResultsArea.setText(resultsContent.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error searching or sorting students: " + ex.getMessage());
            }
        });





        tabbedPane.add("View XML", viewPanel);
        tabbedPane.add("Add Student", addPanel);
        tabbedPane.add("Update Student", updatePanel);
        tabbedPane.add("Search Student", searchPanel);
        tabbedPane.add("Search Delete", deletePanel);


        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                StudentManagementGUI frame = new StudentManagementGUI();
                frame.students = XMLHandler.loadStudents();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
