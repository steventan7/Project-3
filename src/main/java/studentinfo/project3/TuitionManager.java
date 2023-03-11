package studentinfo.project3;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.text.DecimalFormat;
import java.io.File;

/**
 * This class contains the tuition manager that will manage commands inputted by the user
 * @author David Fabian, Steven Tan
 */
public class TuitionManager {
    public static final String [] COMMANDS = new String [] {"LS", "AR", "AN", "AT", "AI", "R",
            "E", "D", "S", "P", "PS", "PC", "PE", "PT", "C", "S", "SE"};
    public static final String QUIT = "Q";
    public static final String [] BOOLEANS = new String [] {"true", "false"};
    public static final String [] STUDENTTYPE = new String [] {"Resident", "Tri-state",
            "International student", "Non-Resident"};
    public static final String STUDYABROAD = "study abroad";
    public static String [] STATES = new String [] {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
            "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN",
            "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA",
            "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
    public static final int SIXTEENYEARS = 16;
    public static final int PROFILETOKENCOUNT = 3;
    public static final int ACADEMICTOKENCOUNT = 2;
    public static final int FULLTIMECREDITMIN = 12;
    public static final int MAXAMOUNT = 10000;
    private static Roster studentRoster;
    private static Enrollment studentEnrollment;

    /**
     * This method initiates the console-based interactive user interface that manages the student roster.
     * It reads a sequence of line commands entered by the user and writes the results on the console.
     * A line command entered by the user will begin with an operation code followed by the data tokens needed to
     * complete the operations for managing the student roster.
     * An operation code will be represented with a single character or two characters in uppercase letters.
     */
    public void run() {
        studentRoster = new Roster();
        studentEnrollment = new Enrollment();
        System.out.println("Tuition Manager is running...\n");
        Scanner userInstruction = new Scanner(System.in);
        StringTokenizer inputTokens = getNextInputCommand(userInstruction);
        String parser = inputTokens.nextToken();
        while (!parser.equals(QUIT)) {
            commandReader(parser, inputTokens);
            inputTokens = getNextInputCommand(userInstruction);
            parser = inputTokens.nextToken();
        }
        System.out.println("Tuition Manager terminated.");
        userInstruction.close();
    }

    /**
     * Takes in a command String and data tokens to decide which method to execute, if the command String is a
     * valid command. Prints an error if not.
     * @param command command String to determine which process to execute.
     * @param tokens StringTokenizer object that contains the data input after the command.
     */
    private void commandReader(String command, StringTokenizer tokens) {
        if (command.equals(COMMANDS[0])) {
            loadList(tokens);
        } else if (command.equals(COMMANDS[1])) {
            addResident(tokens);
        } else if (command.equals(COMMANDS[2])) {
            addNonResident(tokens);
        } else if (command.equals(COMMANDS[3])) {
            addTristate(tokens);
        } else if (command.equals(COMMANDS[4])) {
            addInternational(tokens);
        } else if (command.equals(COMMANDS[5])) {
            removeStudent(tokens);
        } else if(command.equals(COMMANDS[6])) {
            enrollStudent(tokens);
        } else if (command.equals(COMMANDS[7])) {
            dropStudent(tokens);
        } else if(command.equals(COMMANDS[8])) {
            awardScholarship(tokens);
        } else if (command.equals(COMMANDS[9])) {
            printRosterByName(tokens);
        } else if(command.equals(COMMANDS[10])) {
            printRosterByStanding(tokens);
        } else if (command.equals(COMMANDS[11])) {
            printRosterBySchoolMajor(tokens);
        } else if(command.equals(COMMANDS[12])) {
            printEnrolledStudents(tokens);
        } else if (command.equals(COMMANDS[13])) {
            printTuition(tokens);
        } else if(command.equals(COMMANDS[14])) {
            changeStudentMajor(tokens);
        } else if(command.equals(COMMANDS[15])) {
            printStudentsInSchool(tokens);
        } else if(command.equals(COMMANDS[16])) {
            completeSemester(tokens);
        } else {
            System.out.println(command + " is an invalid command!");
        }
    }

    /**
     * Loads the list of students in a text file with all the details of a student on each line and adds them
     * to the student roster.
     * @param tokens StringTokenizer object that contains the text file path.
     */
    private void loadList(StringTokenizer tokens) {
        if(tokens.countTokens() != 1) {
            System.out.println("Invalid command arguments: Load List command only has file path as an argument.");
            return;
        }
        Scanner openFile;
        try {
            openFile = new Scanner(new File(tokens.nextToken()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return;
        }
        StringTokenizer data;
        char studentType;
        while(openFile.hasNextLine()) {
            data = new StringTokenizer(openFile.nextLine(), ",");
            studentType = data.nextToken().charAt(0);
            if(studentType == COMMANDS[1].charAt(1)) {
                studentRoster.add(new Resident(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken()), 0));
            } else if(studentType == COMMANDS[2].charAt(1)) {
                studentRoster.add(new NonResident(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken())));
            } else if(studentType == COMMANDS[3].charAt(1)) {
                studentRoster.add(new TriState(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken()), data.nextToken()));
            } else {
                studentRoster.add(new International(readProfile(data), Major.valueOf(data.nextToken().toUpperCase()),
                        Integer.parseInt(data.nextToken()), Boolean.parseBoolean(data.nextToken().toLowerCase())));
            }
        }
        System.out.println("Students loaded to the roster.");
    }

    /**
     * Takes in a StringTokenizer object to verify the student information, create
     * a Resident object, and add it to the student roster if the student's
     * profile is not in the roster. Prints out error messages for invalid
     * input, if any.
     * @param tokens StringTokenizer object that contains user information
     *               about the Resident to add.
     */
    private void addResident(StringTokenizer tokens) {
        if (tokens.countTokens() == PROFILETOKENCOUNT + ACADEMICTOKENCOUNT) {
            Profile newProfile = readProfile(tokens);
            String newMajor = tokens.nextToken().toUpperCase();
            String creds = tokens.nextToken();
            if (newProfile != null && isMajor(newMajor)) {
                int numCreds = readCompletedCredits(creds);
                if (numCreds >= 0) {
                    Student newResident = new Resident(newProfile, Major.valueOf(newMajor), numCreds, 0);
                    if (studentRoster.add(newResident)) {
                        System.out.println(newResident.studentProfile() + " added to the roster.");
                    } else {
                        System.out.println(newResident.studentProfile() + " is already in the roster.");
                    }
                }
            }
        } else if (tokens.countTokens() < PROFILETOKENCOUNT + ACADEMICTOKENCOUNT) {
            System.out.println("Missing data in line command.");
        } else {
            System.out.println("Unnecessary arguments given.");
        }
    }
    /**
     * Takes in a StringTokenizer object to verify the student information, create
     * a NonResident object, and add it to the student roster if the student's
     * profile is not in the roster. Prints out error messages for invalid
     * input, if any.
     * @param tokens StringTokenizer object that contains user information
     *               about the NonResident to add.
     */
    private void addNonResident(StringTokenizer tokens) {
        if (tokens.countTokens() == PROFILETOKENCOUNT + ACADEMICTOKENCOUNT) {
            Profile newProfile = readProfile(tokens);
            String newMajor = tokens.nextToken().toUpperCase();
            String creds = tokens.nextToken();
            if(newProfile != null && isMajor(newMajor)) {
                int numCreds = readCompletedCredits(creds);
                if(readCompletedCredits(creds) >= 0) {
                    Student newNResident = new NonResident(newProfile, Major.valueOf(newMajor), numCreds);
                    if (studentRoster.add(newNResident)) {
                        System.out.println(newNResident.studentProfile() + " added to the roster.");
                    } else {
                        System.out.println(newNResident.studentProfile() + " is already in the roster.");
                    }
                }
            }
        } else if(tokens.countTokens() < PROFILETOKENCOUNT + ACADEMICTOKENCOUNT) {
            System.out.println("Missing data in line command.");
        } else {
            System.out.println("Unnecessary arguments given.");
        }
    }
    /**
     * Takes in a StringTokenizer object to verify the student information, create
     * a TriState object, and add it to the student roster if the student's
     * profile is not in the roster. Prints out error messages for invalid
     * input, if any.
     * @param tokens StringTokenizer object that contains user information
     *               about the TriState student to add.
     */
    private void addTristate(StringTokenizer tokens) {
        if (tokens.countTokens() == PROFILETOKENCOUNT + ACADEMICTOKENCOUNT + 1) {
            Profile newProfile = readProfile(tokens);
            String newMajor = tokens.nextToken().toUpperCase();
            String creds = tokens.nextToken();
            String state = tokens.nextToken();
            if(newProfile != null && isMajor(newMajor) && isState(state)) {
                int numCreds = readCompletedCredits(creds);
                if(readCompletedCredits(creds) >= 0) {
                    Student newTristate = new TriState(newProfile, Major.valueOf(newMajor), numCreds, state);
                    if (studentRoster.add(newTristate)) {
                        System.out.println(newTristate.studentProfile() + " added to the roster.");
                    } else {
                        System.out.println(newTristate.studentProfile() + " is already in the roster.");
                    }
                }
            }
        } else if(tokens.countTokens() > PROFILETOKENCOUNT + ACADEMICTOKENCOUNT + 1) {
            System.out.println("Unnecessary arguments given.");
        } else if(tokens.countTokens() == PROFILETOKENCOUNT + ACADEMICTOKENCOUNT) {
            System.out.println("Missing the state code.");
        } else if(tokens.countTokens() == PROFILETOKENCOUNT) {
            System.out.println("Missing data in command line.");
        } else {
            System.out.println("Missing data in line command.");
        }
    }
    /**
     * Takes in a StringTokenizer object to verify the student information, create
     * a International object, and add it to the student roster if the student's
     * profile is not in the roster. Prints out error messages for invalid
     * input, if any.
     * @param tokens StringTokenizer object that contains user information
     *               about the International student to add.
     */
    private void addInternational(StringTokenizer tokens) {
        if (tokens.countTokens() == PROFILETOKENCOUNT + ACADEMICTOKENCOUNT ||
                tokens.countTokens() == PROFILETOKENCOUNT + ACADEMICTOKENCOUNT + 1) {
            Profile newProfile = readProfile(tokens);
            String newMajor = tokens.nextToken().toUpperCase();
            String creds = tokens.nextToken();
            String isAbroad = "false";
            if(tokens.hasMoreTokens()) {
                isAbroad = tokens.nextToken();
                if(!isAbroad.equalsIgnoreCase(BOOLEANS[0]) && !isAbroad.equalsIgnoreCase(BOOLEANS[1])) {
                    System.out.println("Study Abroad Invalid: " + isAbroad + " is not valid!");
                }
            }
            if(newProfile != null && isMajor(newMajor)) {
                int numCreds = readCompletedCredits(creds);
                if(readCompletedCredits(creds) >= 0) {
                    Student newNResident = new International(newProfile, Major.valueOf(newMajor), numCreds,
                            Boolean.parseBoolean(isAbroad));
                    if (studentRoster.add(newNResident)) {
                        System.out.println(newNResident.studentProfile() + " added to the roster.");
                    } else {
                        System.out.println(newNResident.studentProfile() + " is already in the roster.");
                    }
                }
            }
        } else if(tokens.countTokens() < PROFILETOKENCOUNT + ACADEMICTOKENCOUNT) {
            System.out.println("Missing data in line command.");
        } else {
            System.out.println("Unnecessary arguments given.");
        }
    }

    /**
     * Takes in the user's input arguments to enroll the student specified for the semester. Adds
     * the student to the enrollment list if the student eligible regarding their credits and is in
     * the student roster. Prints error messages for invalid input, if any.
     * @param tokens StringTokenizer object that contains the details of the student to enroll.
     */
    private void enrollStudent(StringTokenizer tokens) {
        if (tokens.countTokens() == PROFILETOKENCOUNT + 1) {
            Profile profileToEnroll = readProfile(tokens);
            if(profileToEnroll != null) {
                Student checkStudent = studentRoster.student(new Resident(profileToEnroll, Major.CS,
                        0, 0));
                if(checkStudent == null) {
                    System.out.println("Cannot enroll: " + profileToEnroll + " is not in the roster.");
                    return;
                }
                int creditsEnrolling = readEnrolledCredits(checkStudent, tokens.nextToken());
                if(creditsEnrolling != -1) {
                    EnrollStudent newEnrolled = new EnrollStudent(profileToEnroll, creditsEnrolling);
                    studentEnrollment.add(newEnrolled);
                    System.out.println(newEnrolled.studentProfile() + " enrolled " + creditsEnrolling + " credits");
                }
            }
        } else if(tokens.countTokens() < PROFILETOKENCOUNT + 1) {
            System.out.println("Missing data in line command.");
        } else {
            System.out.println("Unnecessary arguments given.");
        }
    }

    /**
     * Drops the student that is enrolled for the semester, if in the enrollment list. Prints
     * error messages for invalid input or if the student is not in the enrollment list.
     * @param tokens the StringTokenizer object that contains the details of the student to drop.
     */
    private void dropStudent(StringTokenizer tokens) {
        if(tokens.countTokens() == PROFILETOKENCOUNT) {
            Profile profileToDrop = readProfile(tokens);
            if(profileToDrop != null) {
                EnrollStudent studentToDrop = new EnrollStudent(profileToDrop, 0);
                if(studentEnrollment.contains(studentToDrop)) {
                    studentEnrollment.remove(studentToDrop);
                    System.out.println(studentToDrop.studentProfile() + " dropped.");
                    return;
                }
                System.out.println(profileToDrop + " is not enrolled.");
            }
        } else {
            System.out.println("Invalid command arguments: Drop command is followed" +
                    " by (FirstName) (LastName) (DOB: mm/dd/yyyy)");
        }
    }

    /**
     * Awards the specified student with the amount inputted by the user. Prints error messages for invalid
     * input, if the student is ineligible for the scholarship, or if the student is not in the roster.
     * @param tokens StringTokenizer object that contains the details of the student to award a scholarship.
     */
    private void awardScholarship(StringTokenizer tokens) {
        if(tokens.countTokens() == PROFILETOKENCOUNT + 1) {
            Profile profileToAward = readProfile(tokens);
            String scholarship = tokens.nextToken();
            if(isNumeric(scholarship)) {
                int amount = Integer.parseInt(scholarship);
                Resident residentToAward = isScholarshipEligible(profileToAward);
                if(0 < amount && amount <= MAXAMOUNT) {
                    if (profileToAward != null && residentToAward != null) {
                        residentToAward.setScholarship(Integer.parseInt(scholarship));
                        System.out.println(profileToAward + ": scholarship amount updated.");
                    }
                } else {
                    System.out.println(amount + ": invalid amount.");
                }
            } else {
                System.out.println("Amount is not an integer.");
            }
        } else if(tokens.countTokens() == PROFILETOKENCOUNT) {
            Profile profileToCheck = readProfile(tokens);
            if(profileToCheck != null &&
                    !studentRoster.contains(new NonResident(profileToCheck, Major.CS, 0))) {
                System.out.println(profileToCheck + " is not in the roster.");
                return;
            }
            System.out.println("Missing data in line command.");
        } else if(tokens.countTokens() < PROFILETOKENCOUNT) {
            System.out.println("Missing data in line command.");
        } else {
            System.out.println("Unnecessary command line arguments.");
        }
    }

    /**
     * Prints all the students enrolled in the current semester. Prints error messages for
     * unnecessary and invalid input.
     * @param tokens StringTokenizer object that contains the user's input arguments, if any.
     */
    private void printEnrolledStudents(StringTokenizer tokens) {
        if(tokens.countTokens() != 0) {
            System.out.println("Invalid command arguments: PE command has no following arguments.");
            return;
        }
        if(rosterHasEnrollments()) {
            System.out.println("** Enrollment **");
            studentEnrollment.print();
            System.out.println("* end of enrollment *");
        }
    }

    /**
     * Prints the amount of tuition owed by the currently enrolled students alongside with their details.
     * @param tokens StringTokenizer object that contains user input arguments, if any.
     */
    private void printTuition(StringTokenizer tokens) {
        if(tokens.countTokens() == 0 && rosterHasEnrollments()) {
            System.out.println("** Tuition due **");
            for(int j = 0; j < studentEnrollment.size(); j++) {
                Student dummyStudent = new Resident(studentEnrollment.list()[j].studentProfile(), Major.CS,
                        0, 0);
                int enrolledCredits = studentEnrollment.list()[j].credits();
                String tuitionString = DecimalFormat.getCurrencyInstance().format(studentRoster
                        .student(dummyStudent).tuitionDue(enrolledCredits));
                System.out.println(studentEnrollment.list()[j].studentProfile() + " ("
                        + studentType(studentRoster.student(dummyStudent)) + ") enrolled "
                        + studentEnrollment.list()[j].credits() + " credits: tuition due: " + tuitionString);
            }
            System.out.println("* end of tuition due *");
        } else if (tokens.countTokens() != 0){
            System.out.println("Invalid command arguments: PT command has no following arguments.");
        }
    }

    /**
     * Takes in a Scanner object to verify the profile information, check if the
     * profile is in the roster, and removes the corresponding Student object if
     * the profile is in the roster. Prints out error messages for invalid input,
     * if any.
     * @param tokens StringTokenizer object that contains user information about
     *               the student to remove.
     */
    private void removeStudent(StringTokenizer tokens) {
        if(tokens.countTokens() == PROFILETOKENCOUNT) {
            Profile profileToRemove = readProfile(tokens);
            Student studentToRemove = new Resident(profileToRemove, Major.CS, 0, 0);
            if (studentToRemove.studentProfile() != null) {
                if (studentRoster.remove(studentToRemove)) {
                    System.out.println(studentToRemove.studentProfile() + " removed from the roster.");
                } else {
                    System.out.println(studentToRemove.studentProfile() + " is not in the roster.");
                }
            }
        } else {
            System.out.println("Invalid command arguments: Remove command is followed" +
                    " by (FirstName) (LastName) (DOB: mm/dd/yyyy)");
        }
    }

    /**
     * Takes in a Scanner object to verify the school name and prints out the
     * student in the roster part of the specified school. Prints out error
     * messages for invalid input, if any.
     * @param tokens StringTokenizer object that contains the school name.
     */
    private void printStudentsInSchool(StringTokenizer tokens) {
        if(tokens.countTokens() != 1) {
            System.out.println("Invalid command argument: PC command is followed" +
                    " by (School)");
            return;
        }
        String schoolName = tokens.nextToken();
        if(isSchool(schoolName)) {
            System.out.println("* Students in " + schoolName + " *");
            studentRoster.listBySchool(schoolName);
            System.out.println("* end of list *");
        }
    }

    /**
     * Prints the students currently in the roster lexicographically in the order
     * of last, first name, and DOB, if the roster is not empty. Otherwise, it
     * will print a message stating the roster is empty. Also, it only prints if
     * there are no invalid arguments following the command. If so, it prints an
     * error message.
     * @param tokens StringTokenizer object used to ensure there aren't any invalid arguments.
     */
    private void printRosterByName(StringTokenizer tokens) {
        if(tokens.countTokens() != 0) {
            System.out.println("Invalid command arguments: P command has no following arguments.");
            return;
        }
        if(rosterHasStudents()) {
            System.out.println("** Student roster sorted by last name, first name, DOB **");
            studentRoster.print();
            System.out.println("* end of roster *");
        }
    }

    /**
     * Prints the students currently in the roster lexicographically in the order
     * of their standings, if the roster is not empty. Otherwise, it will print a
     * message stating the roster is empty. Also, it only prints if there are no
     * invalid arguments following the command. If so, it prints an error message.
     * @param tokens StringTokenizer object used to ensure there aren't any invalid arguments.
     */
    private void printRosterByStanding(StringTokenizer tokens) {
        if(tokens.countTokens() != 0) {
            System.out.println("Invalid command arguments: PS command has no following arguments.");
            return;
        }
        if(rosterHasStudents()) {
            System.out.println("** Student roster sorted by standing **");
            studentRoster.printByStanding();
            System.out.println("* end of roster *");
        }
    }

    /**
     * Prints the students currently in the roster lexicographically in the order
     * of their school and major, if the roster is not empty. Otherwise, it will
     * print a message stating the roster is empty. Also, it only prints if there
     * are no invalid arguments following the command. If so, it prints an error message.
     * @param tokens StringTokenizer object used to ensure there aren't any invalid arguments.
     */
    private void printRosterBySchoolMajor(StringTokenizer tokens) {
        if(tokens.countTokens() != 0) {
            System.out.println("Invalid command arguments: PC command has no following arguments.");
            return;
        }
        if(rosterHasStudents()) {
            System.out.println("** Student roster sorted by school, major **");
            studentRoster.printBySchool();
            System.out.println("* end of roster *");
        }
    }

    /**
     * Completes the current semester by awarding the credits enrolled by each student in the enrollment list.
     * If any are eligible to graduate with the amount of current credits, they are printed afterwards.
     * @param tokens StringTokenizer object used to ensure there aren't any invalid arguments.
     */
    private void completeSemester(StringTokenizer tokens) {
        if(tokens.countTokens() != 0) {
            System.out.println("Invalid command arguments: PC command has no following arguments.");
            return;
        }
        System.out.println("Credit completed has been updated.");
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
            System.out.println("** list of students eligible for graduation **");
            studentRoster.printGrads();
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
                if(studentEnrollment.enrolledStudent(dummyObj).credits() >= FULLTIMECREDITMIN) {
                    return (Resident) studentToAward;
                } else {
                    System.out.println(checkProfile + " part time student is not eligible for the scholarship.");
                }
            } else {
                System.out.println(checkProfile + " (" + studentType(studentToAward)
                        + ") is not eligible for the scholarship.");
            }
        } else {
            System.out.println("Student not in roster!");
        }
        return null;
    }

    /**
     * Checks if the roster currently holds students. If not, it will print
     * a message stating it isn't.
     * @return true if the roster currently has students; false otherwise.
     */
    private boolean rosterHasStudents() {
        if(studentRoster.isEmpty()) {
            System.out.println("Student roster is empty!");
            return false;
        }
        return true;
    }

    /**
     * Checks whether the student roster currently has enrollments. If not, it will print a
     * message stating so.
     * @return true if the roster currently has enrolled students; false otherwise.
     */
    private boolean rosterHasEnrollments() {
        boolean nonEmptyRoster = rosterHasStudents();
        if(nonEmptyRoster && studentEnrollment.hasEnrollments()) {
            return true;
        } else if(nonEmptyRoster) {
            System.out.println("Enrollment is empty!");
        }
        return false;
    }

    /**
     * This method changes the student's major, given the student's profile and the new major
     * @param tokens StringTokenizer object consisting of a student's profile and their new major
     */
    private void changeStudentMajor(StringTokenizer tokens) {
        if(tokens.countTokens() != PROFILETOKENCOUNT + 1) {
            System.out.println("Invalid command arguments: List command is followed" +
                    " by (FirstName) (LastName) (DOB: mm/dd/yyyy) (NewMajor)");
            return;
        }
        Profile checkProfile = readProfile(tokens);
        String newMajor = tokens.nextToken();
        if(isMajor(newMajor)) {
            Student targetStudent = new Resident(checkProfile, Major.CS, 0, 0);
            if(studentRoster.contains(targetStudent)) {
                studentRoster.changeMajor(targetStudent, newMajor);
                System.out.println(checkProfile + " major changed to " + newMajor);
            } else {
                System.out.println(checkProfile + " is not in the roster.");
            }
        }
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
        if(!isDate(studentDOB)) {
            return null;
        }
        Date studentDob = new Date(studentDOB);
        if(!studentDob.isValid()) {
            System.out.println("DOB invalid: " + studentDob + " not a valid calendar date!");
            return null;
        }
        Date today = new Date();
        Date dateCompare = new Date(today.month() + "/" + today.day() + "/" + (today.year() - SIXTEENYEARS));
        if(studentDob.compareTo(dateCompare) > 0) {
            System.out.println("DOB invalid: " + studentDob + " younger than 16 years old.");
            return null;
        }
        return new Profile(firstName, lastName, studentDOB);
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
            System.out.println("Credits completed invalid: cannot be negative!");
        } else {
            System.out.println("Credits completed invalid: not an integer!");
        }
        return -1;
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
                System.out.println("(" + studentType(enrolled) + ") " + cred + ": invalid credit hours.");
            } else {
                System.out.println("Credits enrolled invalid: cannot be negative!");
            }
        } else {
            System.out.println("Credits enrolled is not an integer.");
        }
        return -1;
    }

    /**
     * This method checks the validity of a String command inputted by the user relating to the Major class
     * @param code the input command String that is being read by the Scanner
     * @return true if the input is a valid major that is part of the Major class, false if the major does not exist
     */
    private boolean isMajor(String code) {
        for(Major validMajor : Major.values()) {
            if(validMajor.name().equalsIgnoreCase(code)) {
                return true;
            }
        }
        System.out.println("Major code invalid: " + code);
        return false;
    }

    /**
     * This method checks the validity of a String command inputted by the user relating to the name of school
     * @param schoolName the input command String that is being read by the Scanner
     * @return true if the input is a valid major that is a valid school name, false if the school does not exist
     */
    private boolean isSchool(String schoolName) {
        for(Major validMajor : Major.values()) {
            if(validMajor.schoolName().equalsIgnoreCase(schoolName)) {
                return true;
            }
        }
        System.out.println("School doesn't exist: " + schoolName);
        return false;
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
            System.out.println("DOB invalid: " + input + " must be in format mm/dd/yyy!");
            return false;
        }
        for(String number : nums) {
            if(!isNumeric(number)) {
                System.out.println("DOB invalid: " + number + " not a number!");
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
     * Utilizes a predefined list of valid US state abbreviations to check if the given String
     * is a valid two letter abbreviation of a US state.
     * @param checkState String to be compared against all the valid Strings in STATES array.
     * @return true if the given String is a valid US state abbreviation; false otherwise.
     */
    private boolean isState(String checkState) {
        for(String state : STATES) {
            if(checkState.equalsIgnoreCase(state)) {
                return true;
            }
        }
        System.out.println(checkState + ": Invalid state code.");
        return false;
    }

    /**
     * The process of retrieving the next line instruction inputted by the user while the
     * program is running.
     * @param userInput Scanner object that retrieves the next line of user input.
     * @return StringTokenizer containing the data inputted by the user as tokens.
     */
    private StringTokenizer getNextInputCommand(Scanner userInput) {
        StringTokenizer newTokens = new StringTokenizer(userInput.nextLine());
        while(!newTokens.hasMoreTokens()) {
            newTokens = new StringTokenizer(userInput.nextLine());
        }
        return newTokens;
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
}