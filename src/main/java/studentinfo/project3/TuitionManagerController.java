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
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TuitionManagerController {
    @FXML
    private Button addStudent, removeStudent, changeMajor, loadSchedule;
    @FXML
    private RadioButton resident, nonresident, international, tristate, neither, ny, ct;

    @FXML
    private CheckBox studyabroad;

    @FXML
    private TextField firstname, lastname, creditscompleted, enrollingCredits, scholarshipAmount;

    @FXML
    private TextArea output;

    @FXML
    private DatePicker dob1;

    @FXML
    private ToggleGroup major, isResident, state, homeplace;

    public static final String [] COMMANDS = new String [] {"R", "N", "T", "I"};
    public static final String [] STUDENTTYPE = new String [] {"Resident", "Tri-state",
            "International student", "Non-Resident"};
    public static final String STUDYABROAD = "study abroad";
    public static final int SIXTEENYEARS = 16;
    public static final int FULLTIMECREDITMIN = 12;
    public static final int MAXAMOUNT = 10000;
    private static Roster studentRoster = new Roster();
    private static Enrollment studentEnrollment = new Enrollment();

    /**
     * Adds a student to studentRoster when the user clicks on the "Add" button if the user has appropriately filled
     * the specified data fields
     */
    @FXML
    void addStudent() {
        try {
            String typeOfStudent = ((RadioButton) isResident.getSelectedToggle()).getText();
            if (typeOfStudent.equals("Resident")) {
                addResident();
            } else {
                try {
                    String typeOfNonResidentStudent = ((RadioButton) homeplace.getSelectedToggle()).getText();
                    if (typeOfNonResidentStudent.equals("Tri-State")) {
                        addTriState();
                    } else if (typeOfNonResidentStudent.equals("International")) {
                        addInternational();
                    } else {
                        addNonResident();
                    }
                }
                catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid data input");
                    alert.setHeaderText("Student is selected as Non-Resident. Please select TriState, International," +
                            "or Neither.");
                    alert.setContentText("Please enter proper input.");
                    alert.showAndWait();
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
     * Takes in the user's input arguments to enroll the student specified for the semester. Adds
     * the student to the enrollment list if the student eligible regarding their credits and is in
     * the student roster. Prints error messages for invalid input, if any.
     */
    @FXML
    private void enrollStudent() {
        Profile profileToEnroll = readProfile();
        if(profileToEnroll != null) {
            Student checkStudent = studentRoster.student(new Resident(profileToEnroll, Major.CS,
                    0, 0));
            if(checkStudent == null) {
                output.setText("Cannot enroll: " + profileToEnroll + " is not in the roster.");
                return;
            }
            int creditsEnrolling = readEnrolledCredits(checkStudent, enrollingCredits.getText());
            if(creditsEnrolling != -1) {
                EnrollStudent newEnrolled = new EnrollStudent(profileToEnroll, creditsEnrolling);
                studentEnrollment.add(newEnrolled);
                output.setText(newEnrolled.studentProfile() + " enrolled " + creditsEnrolling + " credits");
            }
        }
    }
    /**
     * Drops the student that is enrolled for the semester, if in the enrollment list. Prints
     * error messages for invalid input or if the student is not in the enrollment list.
     */
    @FXML
    private void dropStudent() {
        Profile profileToDrop = readProfile();
        if(profileToDrop != null) {
            EnrollStudent studentToDrop = new EnrollStudent(profileToDrop, 0);
            if(studentEnrollment.contains(studentToDrop)) {
                studentEnrollment.remove(studentToDrop);
                output.setText(studentToDrop.studentProfile() + " dropped.");
                return;
            }
            output.setText(profileToDrop + " is not enrolled.");
        }
    }
    /**
     * Awards the specified student with the amount inputted by the user. Prints error messages for invalid
     * input, if the student is ineligible for the scholarship, or if the student is not in the roster.
     */
    @FXML
    private void awardScholarship() {
        Profile profileToAward = readProfile();
        if(scholarshipAmount.getText().isEmpty()) {
            output.setText("No scholarship amount inputted.");
            return;
        }
        String scholarship = scholarshipAmount.getText();
        if(profileToAward != null && isNumeric(scholarship)) {
            int amount = Integer.parseInt(scholarship);
            Resident residentToAward = isScholarshipEligible(profileToAward);
            output.appendText("\n" + profileToAward);
            if(0 < amount && amount <= MAXAMOUNT) {
                if (residentToAward != null) {
                    residentToAward.setScholarship(Integer.parseInt(scholarship));
                    output.setText(profileToAward + ": scholarship amount updated.");
                }
            } else {
                output.setText(amount + ": invalid amount.");
            }
        } else if(profileToAward != null) {
            output.setText("Amount is not an integer.");
        }
    }
    /**
     * Checks if a student is eligible for a scholarship given their profile. Used in junction with
     * the awardScholarship method. Prints error messages for an invalid Profile, if it is.
     * @param checkProfile Profile for which to check if eligible for a scholarship.
     * @return a Resident object if the student is eligible; NULL otherwise.
     */
    private Resident isScholarshipEligible(Profile checkProfile) {
        Student studentToAward = new Resident(checkProfile, Major.CS, 0, 0);
        studentToAward = studentRoster.student(studentToAward);
        if(studentToAward != null) {
            if(studentToAward.isResident()) {
                EnrollStudent dummyObj = new EnrollStudent(studentToAward.studentProfile(), 0);
                dummyObj = studentEnrollment.enrolledStudent(dummyObj);
                output.appendText("\n" + dummyObj);
                if(dummyObj != null &&
                        studentEnrollment.enrolledStudent(dummyObj).credits() >= FULLTIMECREDITMIN) {
                    return (Resident) studentToAward;
                } else if(dummyObj != null) {
                    output.setText(checkProfile + " part time student is not eligible for the scholarship.");
                } else {
                    output.setText("Student is not enrolled.");
                }
            } else {
                output.setText(checkProfile + " (" + studentType(studentToAward)
                        + ") is not eligible for the scholarship.");
            }
        } else {
            output.setText("Student not in roster!");
        }
        return null;
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
        } else if(!input.isEmpty()) {
            output.setText("Credits completed invalid: not an integer!");
        } else {
            output.setText("Credits completed missing.");
        }
        return -1;
    }

    /**
     * Used to give the Student's birthDate in the proper mm/day/year format
     * @return a String representation of the birthDate
     */
    private String birthDate() {
        if(dob1.getValue() == null) {
            output.setText("Please enter a date.");
            return "";
        }
        String[] date = dob1.getValue().toString().split("-");
        String birthDate = date[1] + "/" + date[2] + "/" + date[0];
        return birthDate;
    }

    /**
     * Removes the specified student from studentRoster when the user clicks on the "Remove" button
     * If the student is not found or the specified details are incorrect, an error message appears
     */
    @FXML
    void removeStudent() {
        Profile profileToRemove = readProfile();
        Student studentToRemove = new Resident(profileToRemove, Major.CS, 0, 0);
        if (studentToRemove.studentProfile() != null) {
            if(isNumeric(enrollingCredits.getText())) {
                EnrollStudent student = new EnrollStudent(profileToRemove, Integer.parseInt(enrollingCredits.getText()));
                if (studentEnrollment.contains(student)) {
                    output.setText("Cannot remove student. Student is currently enrolled!");
                    return;
                }
            }
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
        if(firstName.isEmpty() || lastName.isEmpty()) {
            output.setText("Missing name information.");
            return null;
        }
        String studentDOB = birthDate();
        if(studentDOB.isEmpty() || !isDate(studentDOB)) {
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
    void changeStudentMajor() {
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
    void checkIfResident() {
        RadioButton selectedRadioButton = (RadioButton) isResident.getSelectedToggle();
        if((selectedRadioButton).getText().equals("Non-Resident")) {
            neither.setDisable(false);
            ct.setDisable(false);
            ny.setDisable(false);
            tristate.setDisable((false));
            studyabroad.setDisable(false);
            international.setDisable(false);
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
    void checkIfTriStateOrInternational() {
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

    /**
     * Open a file dialog for user to select text file containing details of students to be added to the student roster.
     * Prints an error if no file is selected.
     */
    @FXML
    void importFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Source File for the Import");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File sourceFile = chooser.showOpenDialog(stage);
        if(sourceFile == null) {
            output.setText("No text file selected.");
            return;
        }
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
    void printRoster() {
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

    /**
     * Prints the student roster in the output TextArea. If the roster is empty, the output TextArea says that
     * the roster is empty
     */
    @FXML
    void printByStanding() {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.printByStanding().split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }

    /**
     * Prints the student roster in the output TextArea. If the roster is empty, the output TextArea says that
     * the roster is empty
     */
    @FXML
    void printBySchool() {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.printBySchool().split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }

    /**
     * Prints the students in order by last name, first, DOB by RBS students
     * This method sorts the students by their profile. This is done by comparing the student's majors and schools
     * Then it prints the students that are in the specified school
     */
    @FXML
    void listByRBS() {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.listBySchool("RBS").split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }

    /**
     * Prints the students in order by last name, first, DOB by SAS students
     * This method sorts the students by their profile. This is done by comparing the student's majors and schools
     * Then it prints the students that are in the specified school
     */
    @FXML
    void listBySAS() {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.listBySchool("SAS").split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }

    /**
     * Prints the students in order by last name, first, DOB by SC&I students
     * This method sorts the students by their profile. This is done by comparing the student's majors and schools
     * Then it prints the students that are in the specified school
     */
    @FXML
    void listBySCI() {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.listBySchool("SC&I").split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }

    /**
     * Prints the students in order by last name, first, DOB by SOE students
     * This method sorts the students by their profile. This is done by comparing the student's majors and schools
     * Then it prints the students that are in the specified school
     */
    @FXML
    void listBySOE() {
        output.clear();
        if(studentRoster.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentRoster.listBySchool("SOE").split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }
    /**
     * Takes in a Student object and a String representing the amount of credits the student with
     * the profile is enrolling with. Prints error messages if the student cannot take these amount of
     * credits or for invalid input.
     * @param enrolled Student object to be enrolled with the specified credits.
     * @param input String representing the amount of enrolling credits.
     * @return a positive integer if the Student can take those amount of credits; -1 otherwise.
     */
    private int readEnrolledCredits(Student enrolled, String input) {
        if(isNumeric(input)) {
            int cred = Integer.parseInt(input);
            if(cred >= 0) {
                if(enrolled.isValid(cred)) {
                    return cred;
                }
                output.setText("(" + studentType(enrolled) + ") " + cred + ": invalid credit hours.");
            } else {
                output.setText("Credits enrolled invalid: cannot be negative!");
            }
        } else {
            output.setText("Credits enrolled is not an integer.");
        }
        return -1;
    }
    /**
     * Checks for the type of specific student given. Return a String representing that type.
     * @param inspectStudent the Student object to be checked for the type.
     * @return String representing the type of Student object given.
     */
    private String studentType(Student inspectStudent) {
        if(inspectStudent instanceof Resident) {
            return STUDENTTYPE[0];
        }
        if(inspectStudent instanceof TriState) {
            TriState promoteStudent = (TriState) inspectStudent;
            return STUDENTTYPE[1] + " " + promoteStudent.state().toUpperCase();
        }
        if(inspectStudent instanceof International) {
            International promoteStudent = (International) inspectStudent;
            if(promoteStudent.isStudyAbroad()) {
                return STUDENTTYPE[2] + STUDYABROAD;
            }
            return STUDENTTYPE[2];
        }
        return STUDENTTYPE[3];
    }

    /**
     * Prints the student roster in the output TextArea. If the roster is empty, the output TextArea says that
     * the roster is empty
     */
    @FXML
    void printEnrolledStudent() {
        output.clear();
        if(studentEnrollment.isEmpty()) {
            output.setText("Roster is empty");
        } else {
            String[] split = studentEnrollment.print().split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
        }
    }

    /**
     * Prints the amount of tuition owed by the currently enrolled students alongside with their details.
     */
    @FXML
    void printTuition() {
        output.clear();
        if(!studentEnrollment.isEmpty()) {
            output.appendText("** Tuition due **");
            output.appendText("\n");
            for(int j = 0; j < studentEnrollment.size(); j++) {
                Student dummyStudent = new Resident(studentEnrollment.list()[j].studentProfile(), Major.CS,
                        0, 0);
                int enrolledCredits = studentEnrollment.list()[j].credits();
                String tuitionString = DecimalFormat.getCurrencyInstance().format(studentRoster
                        .student(dummyStudent).tuitionDue(enrolledCredits));
                output.appendText(studentEnrollment.list()[j].studentProfile() + " ("
                        + studentType(studentRoster.student(dummyStudent)) + ") enrolled "
                        + studentEnrollment.list()[j].credits() + " credits: tuition due: " + tuitionString);
                output.appendText("\n");
            }
            output.appendText("* end of tuition due *");
        } else {
            output.setText("Student enrollment is empty!");
        }
    }

    /**
     * Completes the current semester by awarding the credits enrolled by each student in the enrollment list.
     * If any are eligible to graduate with the amount of current credits, they are printed afterwards.
     */
    @FXML
    void completeSemester() {
        output.clear();
        output.appendText("Credits completed has been updated.");
        output.appendText("\n");
        boolean existsGrads = false;
        for(int j = 0; j < studentEnrollment.size(); j++) {
            Student findStudent = new NonResident(studentEnrollment.list()[j].studentProfile(),
                    Major.CS, 0);
            studentRoster.student(findStudent).updateCredits(studentEnrollment.list()[j].credits());
            if(studentRoster.student(findStudent).canGraduate()) {
                existsGrads = true;
            }
        }
        if(existsGrads) {
            output.appendText("** list of students eligible for graduation **");
            output.appendText("\n");
            String[] split = studentRoster.printGrads().split("#");
            for(String student: split) {
                output.appendText((student));
                output.appendText("\n");
            }
            studentEnrollment.clear();
            output.appendText("Enrollment roster has been cleared.");
        } else {
            output.appendText("No students have graduated...");
        }
    }
}
