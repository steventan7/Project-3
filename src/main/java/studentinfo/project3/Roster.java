package studentinfo.project3;

/**
 * This class contains methods that perform actions that replicate a student roster
 *
 * @author Steven Tan, David Fabian
 */
public class Roster {
    private Student[] roster;
    private int size;

    /**
     * Constructs an instance of the Roster object
     * Holds the student roster array and the current size of the student roster
     */
    public Roster() {
        this.roster = new Student[4];
        this.size = 0;
    }

    /**
     * Searches whether a given student is in the roster
     * If the student is not found in the roster, -1 is returned.
     * If the student is found in the roster, the index of the student is returned.
     * @param student the student that is to be found
     * @return -1 the student is not found, the index of the student otherwise
     */
    private int find(Student student) {
        final int NOTFOUND = -1;
        if(size == 0) return NOTFOUND;
        for (int i = 0; i < size; i++) {
            if (roster[i].equals(student)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Increases the array capacity by 4
     * This method will be called when the roster is full after a student has been recently added
     */
    private void grow() {
        Student[] temp = new Student[size + 4];
        for (int i = 0; i < size; i++) {
            temp[i] = this.roster[i];
        }
        roster = temp;
    }

    /**
     * Adds a student to the end of the roster
     * If a student has been added and the roster is full, the size of the roster is increased by 4
     * @param student the student that is to be added
     * @return true the student has been found, false if the student has not been added otherwise
     */
    public boolean add(Student student){
        if (size == 0) {
            roster[0] = student;
            size++;
            return true;
        }
        if(find(student) != -1) return false;
        roster[size] = student;
        size++;
        if(size == roster.length) grow();
        return true;
    }

    /**
     * Removes the student requested in the parameter
     * After a student is removed, all the students after the index of the student removed are shifted over
     * by 1
     * @param student the student that is to be removed
     * @return true the student has been successfully removed, false if the student is not in the roster
     */
    public boolean remove(Student student){
        int index = find(student);
        if (index == -1) return false;
        while(index < size - 1) {
            roster[index] = roster[index + 1];
            index++;
        }
        roster[index] = null;
        size--;
        return true;
    }

    /**
     * Checks if a student is in the roster
     * @param student the student that is to be added
     * @return true the student is in the roster, false if the student is not in the roster otherwise
     */
    public boolean contains(Student student){
        if (find(student) == -1) {
            return false;
        }
        return true;
    }

    /**
     * Prints the students by their profiles
     * This method sorts the students by the profiles. This is done by comparing the student's first and
     * last names, as well as their date of births.
     * @return a String representation of all students in the roster sorted by profile
     */
    public String print() {
        for(int i = 0; i < size - 1; i++) {
            int min = i;
            for(int j = i + 1; j < size; j++) {
                int compareStudents = roster[j].compareTo(roster[min]);
                if (compareStudents < 0) min = j;
            }
            Student temp = roster[min];
            roster[min] = roster[i];
            roster[i] = temp;
        }

        String rosterList = "";
        for(int i = 0; i < size; i++) {
            rosterList += roster[i] + "#";
        }
        return rosterList;
    }

    /**
     * Prints the students in order of their academic standing
     * This method sorts the students by their academic standing. This is done by comparing the number of credits
     * completed
     * @return a String representation of all students listed by standing
     */
    public String printByStanding() {
        for(int i = 0; i < size - 1; i++) {
            int min = i;
            for(int j = i + 1; j < size; j++) {
                String student1 = roster[j].studentStanding();
                String student2 = roster[min].studentStanding();
                if(student1.compareTo(student2) == 0) {
                    int compareProfile = roster[j].studentProfile().compareTo(roster[min].studentProfile());
                    if(compareProfile < 0) min = j;
                }
                if(student1.compareTo(student2) < 0) min = j;
            }
            Student temp = roster[min];
            roster[min] = roster[i];
            roster[i] = temp;
        }

        String rosterList = "";
        for(int i = 0; i < size; i++) {
            rosterList += roster[i] + "#";
        }
        return rosterList;
    }

    /**
     * Prints the students in order of their school
     * This method sorts the students by their school. This is done by comparing the student's majors and schools
     * @return a String representation of all students listed by school
     */
    public String printBySchool() {
        for(int i = 0; i < size - 1; i++) {
            int min = i;
            for(int j = i + 1; j < size; j++) {
                Major student1 = roster[j].major();
                Major student2 = roster[min].major();
                int compareSchools = student1.schoolName().compareTo(student2.schoolName());
                if(compareSchools == 0) {
                    int compareMajors = student1.name().compareTo(student2.name());
                    if(compareMajors < 0) min = j;
                }
                if (compareSchools < 0) min = j;
            }
            Student temp = roster[min];
            roster[min] = roster[i];
            roster[i] = temp;
        }

        String rosterList = "";
        for(int i = 0; i < size; i++) {
            rosterList += roster[i] + "#";
        }
        return rosterList;
    }

    /**
     * Prints the students in order by last name, first, DOB by a specified school
     * This method sorts the students by their profile. This is done by comparing the student's majors and schools
     * Then it prints the students that are in the specified school
     * @param school the specified school for the listing
     * @return a String representation of all students listed within a specific school
     */
    public String listBySchool(String school) {
        for(int i = 0; i < size - 1; i++) {
            int min = i;
            for(int j = i + 1; j < size; j++) {
                int compareStudents = roster[j].compareTo(roster[min]);
                if (compareStudents < 0) min = j;
            }
            Student temp = roster[min];
            roster[min] = roster[i];
            roster[i] = temp;
        }

        String rosterList = "";
        for(int i = 0; i < size; i++) {
            if(roster[i].major().schoolName().toUpperCase().equals(school.toUpperCase())) {
                rosterList += roster[i] + "#";
            }
        }
        return rosterList;
    }

    /**
     * Prints all the students in the roster that can graduate with the number of credits currently completed.
     * @return a String representation of all students that can graduate
     */
    public String printGrads() {
        String rosterList = "";
        for(int i = 0; i < size; i++) {
            if(roster[i].canGraduate()) {
                rosterList += roster[i] + "#";
            }
        }
        return rosterList;
    }

    /**
     * Changes a student's major to the specified new major
     * This method finds a specified student. If the student does not exist, nothing occurs.
     * If the student is found, the student's major instance is changed to the new major
     * @param student the student whose major is being changed
     * @param major the student's new major
     */
    public void changeMajor(Student student, String major) {
        int index = find(student);
        if (index == -1) return;
        String s = major.toUpperCase();
        roster[index].changeStudentMajor(s);
    }

    /**
     * Checks whether the roster array is empty.
     * @return true if the size of the roster is 0; false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retrieves the student on the roster list with an identical Profile as the given student.
     * @param findStudent the Student object that is used to find an identical one in the roster.
     * @return the instance identical to the one given; if there is none that is identical, null is returned.
     */
    public Student student(Student findStudent) {
        int index = find(findStudent);
        if(index != -1) {
            return roster[index];
        }
        return null;
    }
}
