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

public class TuitionManagerController {
    @FXML
    private Button addStudent, removeStudent, changeMajor, loadSchedule;

    @FXML
    private RadioButton bait, cs, ece, iti, math, resident, nonresident, international, tristate, ny, ct;

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

    public static final String [] STUDENTTYPE = new String [] {"Resident", "Tri-State", "International",
            "Non-Resident"};
    private static Roster studentRoster = new Roster();
    private static Enrollment studentEnrollment = new Enrollment();

    /**
     * Adds a student to studentRoster when the user clicks on the "Add" button if the user has appropriately filled
     * the specified data fields
     */
    @FXML
    void add(ActionEvent event) {
        try {
            String typeOfStudent = ((RadioButton) isResident.getSelectedToggle()).getText();
            if (typeOfStudent.equals("Resident")) {
                addResident();
            } else {

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
     * Removes the specified student from studentRoster when the user clicks on the "Remove" button
     * If the student is not found or the specified details are incorrect, an error message appears
     */
    @FXML
    void remove(ActionEvent event) {

    }

    /**
     * Checks if the Resident or NonResident buttons has been selected. If the Resident button is selected, the
     * buttons relating to Non-Resident students are disabled
     */
    @FXML
    void checkIfResident(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) isResident.getSelectedToggle();
        if((selectedRadioButton).getText().equals("Non-Resident")) {
            ct.setDisable(false);
            ny.setDisable(false);
            tristate.setDisable((false));
            studyabroad.setDisable(false);
            international.setDisable(false);
        } else {
            ct.setDisable(true);
            ny.setDisable(true);
            tristate.setDisable((true));
            studyabroad.setDisable(true);
            international.setDisable(true);
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
            ct.setDisable(false);
            ny.setDisable(false);
        } else {
            studyabroad.setDisable(false);
            ct.setDisable(true);
            ny.setDisable(true);
        }
    }

    @FXML
    void printRoster(ActionEvent event) {
        output.clear();
        String[] split = studentRoster.print().split("\n");
        for(String student: split) {
            output.appendText((student));
            output.appendText("\n");
        }
    }

    /**
     * Used to give the Student's birthDate in the proper mm/day/year format
     * @return a string representation of the birthDate
     */
    private String birthDate() {
        String[] date = dob1.getValue().toString().split("-");
        String birthDate = date[1] + "/" + date[2] + "/" + date[0];
        return birthDate;
    }

    /**
     * Creates a Resident object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addResident() {
        Student student = new Resident(new Profile(firstname.getText(), lastname.getText(), birthDate()),
                Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()),
                Integer.parseInt(creditscompleted.getText()), 0);
        studentRoster.add(student);
    }

    /**
     * Creates an International object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addInternational() {
//        Student student = new International(new Profile(firstname.getText(), lastname.getText(), birthDate()),
//                Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()),
//                Integer.parseInt(creditscompleted.getText()), studyabroad.);
//        studentRoster.add(student);
    }

    /**
     * Creates a NonResident object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addNonResident() {
        Student student = new NonResident(new Profile(firstname.getText(), lastname.getText(), birthDate()),
                Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()),
                Integer.parseInt(creditscompleted.getText()));
        studentRoster.add(student);
    }

    /**
     * Creates a TriState object, and adds it to the student roster if the student's
     * profile is not in the roster.
     */
    private void addTriState() {
        Student student = new TriState(new Profile(firstname.getText(), lastname.getText(), birthDate()),
                Major.valueOf(((RadioButton) major.getSelectedToggle()).getText()),
                Integer.parseInt(creditscompleted.getText()), ((RadioButton) state.getSelectedToggle()).getText());
        studentRoster.add(student);
    }
}
