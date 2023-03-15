package studentinfo.project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TuitionManagerController {
    @FXML
    private Button addStudent, removeStudent, changeMajor, loadSchedule;

    @FXML
    private RadioButton international, tristate, neither, ny, ct;

    @FXML
    private CheckBox studyabroad;

    @FXML
    private TextField firstname, lastname, creditscompleted;

    @FXML
    private TextArea output;

    @FXML
    private DatePicker dob1;

    @FXML
    private ToggleGroup major, isResident, state, homeplace;

    public static final String [] COMMANDS = new String [] {"R", "N", "T", "I"};
    public static final int SIXTEENYEARS = 16;
    private static Roster studentRoster = new Roster();
    private static Enrollment studentEnrollment = new Enrollment();

    /**
     * Adds a student to studentRoster when the user clicks on the "Add" button if the user has appropriately filled
     * the specified data fields
     */
    @FXML
    void addStudent(ActionEvent event) {
        try {
            String typeOfStudent = ((RadioButton) isResident.getSelectedToggle()).getText();
            if (typeOfStudent.equals("Resident")) {
                addResident();
            } else {
                String typeOfNonResidentStudent = ((RadioButton) homeplace.getSelectedToggle()).getText();
                if (typeOfNonResidentStudent.equals("Tri-State")) {
                    addTriState();
                } else if (typeOfNonResidentStudent.equals("International")) {
                    addInternational();
                } else {
                    addNonResident();
                }
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid data input");
            alert.setHeaderText("Please ensure that all inputs are valid.");
            alert.setContentText("Please enter proper input.");
            alert.showAndWait();
        }
    }

    /**
     * Creates a Resident object, and adds it to the student roster if the student's profile is not in the roster.
     */
    private void addResident() {
        Profile newProfile = readProfile();
        if (newProfile != null) {
            int numCreds = readCompletedCredits(creditscompleted.getText());
            if (numCreds >= 0) {
                Student student = new Resident(newProfile,
                        Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()), numCreds, 0);
                if (studentRoster.add(student)) {
                    output.setText(student.studentProfile() + " added to the roster.");
                } else {
                    output.setText(student.studentProfile() + " is already in the roster.");
                }
            }
        }
    }

    /**
     * Creates an International object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addInternational() {
        Profile newProfile = readProfile();
        if (newProfile != null) {
            int numCreds = readCompletedCredits(creditscompleted.getText());
            if (numCreds >= 0) {
                Student student = new International(newProfile,
                        Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()),
                        Integer.parseInt(creditscompleted.getText()), studyabroad.isSelected());
                if (studentRoster.add(student)) {
                    output.setText(student.studentProfile() + " added to the roster.");
                } else {
                    output.setText(student.studentProfile() + " is already in the roster.");
                }
            }
        }
    }

    /**
     * Creates a NonResident object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addNonResident() {
        Profile newProfile = readProfile();
        if (newProfile != null) {
            int numCreds = readCompletedCredits(creditscompleted.getText());
            if (numCreds >= 0) {
                Student student = new NonResident(newProfile, Major.valueOf(((RadioButton)
                        major.getSelectedToggle()).getText()), Integer.parseInt(creditscompleted.getText()));
                if (studentRoster.add(student)) {
                    output.setText(student.studentProfile() + " added to the roster.");
                } else {
                    output.setText(student.studentProfile() + " is already in the roster.");
                }
            }
        }
    }

    /**
     * Creates a TriState object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addTriState() {
        Profile newProfile = readProfile();
        if (newProfile != null) {
            int numCreds = readCompletedCredits(creditscompleted.getText());
            if (numCreds >= 0) {
                Student student = new TriState(newProfile,
                        Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()),
                        Integer.parseInt(creditscompleted.getText()),
                        ((RadioButton) state.getSelectedToggle()).getText());
                if (studentRoster.add(student)) {
                    output.setText(student.studentProfile() + " added to the roster.");
                } else {
                    output.setText(student.studentProfile() + " is already in the roster.");
                }
            }
        }
    }

    /**
     * This method checks the validity of a String command inputted by the user relating to
     * the number of completed credits
     * @param input the input command String that is being read by the Scanner
     * @return number of credits taken if the input is valid, -1 if the input is invalid
     */
    private int readCompletedCredits(String input) {
        if(isNumeric(input)) {
            int cred = Integer.parseInt(input);
            if(cred >= 0) {
                return cred;
            }
            output.setText("Credits completed invalid: cannot be negative!");
        } else {
            output.setText("Credits completed invalid: not an integer!");
        }
        return -1;
    }

    /**
     * Used to give the Student's birthDate in the proper mm/day/year format
     * @return a String representation of the birthDate
     */
    private String birthDate() {
        String[] date = dob1.getValue().toString().split("-");
        String birthDate = date[1] + "/" + date[2] + "/" + date[0];
        return birthDate;
    }
    /**
     * Removes the specified student from studentRoster when the user clicks on the "Remove" button
     * If the student is not found or the specified details are incorrect, an error message appears
     */
    @FXML
    void removeStudent(ActionEvent event) {
        Profile profileToRemove = readProfile();
        Student studentToRemove = new Resident(profileToRemove, Major.CS, 0, 0);
        if (studentToRemove.studentProfile() != null) {
            if (studentRoster.remove(studentToRemove)) {
                output.setText(studentToRemove.studentProfile() + " removed from the roster.");
            } else {
                output.setText(studentToRemove.studentProfile() + " is not in the roster.");
            }
        }
    }

    /**
     * This method checks the validity of a String command inputted by the user relating to the Profile class
     * @return a Profile object if the components inputted are valid and null if invalid
     */
    private Profile readProfile() {
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String studentDOB = birthDate();
        if(!isDate(studentDOB)) {
            return null;
        }
        Date studentDob = new Date(studentDOB);
        Date today = new Date();
        Date dateCompare = new Date(today.month() + "/" + today.day() + "/" + (today.year() - SIXTEENYEARS));
        if(studentDob.compareTo(dateCompare) > 0) {
            output.setText("DOB invalid: " + studentDob + " younger than 16 years old.");
            return null;
        }
        return new Profile(firstName, lastName, studentDOB);
    }

    /**
     * This method changes the student's major, given the student's profile and the new major
     */
    @FXML
    void changeStudentMajor(ActionEvent event) {
        Profile checkProfile = readProfile();
        String newMajor = ((RadioButton) major.getSelectedToggle()).getText();
        Student targetStudent = new Resident(checkProfile, Major.CS, 0, 0);
        if(studentRoster.contains(targetStudent)) {
            studentRoster.changeMajor(targetStudent, newMajor);
            output.setText(checkProfile + " major changed to " + newMajor);
        } else {
            output.setText(checkProfile + " is not in the roster.");
        }
    }
    /**
     * Checks whether the argument String is in the correct calendar date
     * format (mm/dd/yyyy) with numeric values for day, month, and year.
     * Prints error messages for invalid input.
     * @param input a String to be checked for proper calendar Date formatting.
     * @return true if the String is properly formatted as described; false
     * otherwise.
     */
    private boolean isDate(String input) {
        String [] nums = input.split("/");
        if(nums.length != 3) {
            output.setText("DOB invalid: " + input + " must be in format mm/dd/yyy!");
            return false;
        }
        for(String number : nums) {
            if(!isNumeric(number)) {
                output.setText("DOB invalid: " + number + " not a number!");
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks the validity of a String command inputted by the user relating to whether it is numeric
     * @param input the input command String that is being read by the Scanner
     * @return true if the input is a numeric number, false if it is not a numeric number.
     */
    private boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the Resident or NonResident buttons has been selected. If the Resident button is selected, the
     * buttons relating to Non-Resident students are disabled and unselected
     */
    @FXML
    void checkIfResident(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) isResident.getSelectedToggle();
        if((selectedRadioButton).getText().equals("Non-Resident")) {
            neither.setDisable(false);
            ct.setDisable(false);
            ny.setDisable(false);
            tristate.setDisable((false));
            studyabroad.setDisable(false);
            international.setDisable(false);
            neither.setDisable(false);
        } else {
            neither.setDisable(true);
            neither.setSelected(false);
            ct.setDisable(true);
            ct.setSelected(false);
            ny.setDisable(true);
            ny.setSelected(false);
            tristate.setDisable((true));
            tristate.setSelected(false);
            studyabroad.setDisable(true);
            studyabroad.setSelected(false);
            international.setDisable(true);
            international.setSelected(false);
        }
    }

    /**
     * Checks if the Tri-State or International button has been selected. If the Tri-State button is selected, the
     * Study Abroad button is disabled. If the International button is selected, the NY and CT buttons are disabled.
     */
    @FXML
    void checkIfTriStateOrInternational(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) homeplace.getSelectedToggle();
        if((selectedRadioButton).getText().equals("Tri-State")) {
            studyabroad.setDisable(true);
            studyabroad.setSelected(false);
            ct.setDisable(false);
            ny.setDisable(false);
        } else if((selectedRadioButton).getText().equals("International")){
            studyabroad.setDisable(false);
            ct.setDisable(true);
            ct.setSelected(false);
            ny.setDisable(true);
            ny.setSelected(false);
        } else {
            studyabroad.setDisable(true);
            studyabroad.setSelected(false);
            ct.setDisable(true);
            ct.setSelected(false);
            ny.setDisable(true);
            ny.setSelected(false);
        }
    }

    @FXML
    void importFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Source File for the Import");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File sourceFile = chooser.showOpenDialog(stage);
        loadList(sourceFile);
    }

    /**
     * Loads the list of students in a text file with all the details of a student on each line and adds them
     * to the student roster.
     * @param file File object that contains the text file path.
     */
    private void loadList(File file) {
        Scanner openFile;
        try {
            openFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            output.setText("File not found!");
            return;
        }
        StringTokenizer data;
        char studentType;
        while(openFile.hasNextLine()) {
            data = new StringTokenizer(openFile.nextLine(), ",");
            studentType = data.nextToken().charAt(0);
            if(studentType == COMMANDS[0].charAt(0)) {
                output.setText("Resident...");
                studentRoster.add(new Resident(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken()), 0));
            } else if(studentType == COMMANDS[1].charAt(0)) {
                output.setText("Non-Resident...");
                studentRoster.add(new NonResident(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken())));
            } else if(studentType == COMMANDS[2].charAt(0)) {
                studentRoster.add(new TriState(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken()), data.nextToken()));
            } else {
                studentRoster.add(new International(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken()), Boolean.parseBoolean(data.nextToken().toLowerCase())));
            }
        }
        output.setText("Students loaded to the roster.");
    }

    /**
     * This method checks the validity of a String command inputted by the user relating to the Profile class
     * @param tokens StringTokenizer that contains Profile information
     * @return a Profile object if the components inputted by the string command are valid and null if invalid
     */
    private Profile readProfile(StringTokenizer tokens) {
        String firstName = tokens.nextToken();
        String lastName = tokens.nextToken();
        String studentDOB = tokens.nextToken();
        return new Profile(firstName, lastName, studentDOB);
    }

    /**
     * Prints the student roster in the output TextArea. If the roster is empty, the output TextArea says that
     * the roster is empty
     */
    @FXML
    void printRoster(ActionEvent event) {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.print().split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }
}
