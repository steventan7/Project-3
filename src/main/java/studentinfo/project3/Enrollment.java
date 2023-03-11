package studentinfo.project3;

/**
 * This class contains methods that perform actions that replicate a student enrollment roster
 *
 * @author Steven Tan, David Fabian
 */
public class Enrollment{
    private EnrollStudent[] enrollStudents;
    private int size;

    /**
     * Constructs an instance of the Enrollment object
     * Holds the student enrollment array and the current size of the student enrollment roster
     */
    public Enrollment() {
        this.enrollStudents = new EnrollStudent[4];
        this.size = 0;
    }

    /**
     * Searches whether a given student is in the enrollment roster
     * If the student is not found in the roster, -1 is returned.
     * If the student is found in the roster, the index of the student is returned.
     * @param student the student that is to be found
     * @return -1 the student is not found, the index of the student otherwise
     */
    private int find(EnrollStudent student) {
        final int NOTFOUND = -1;
        if(size == 0) return NOTFOUND;
        for (int i = 0; i < size; i++) {
            if (enrollStudents[i].equals(student)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Increases the array capacity by 4
     * This method will be called when the enrollment roster is full after a student has been recently added
     */
    private void grow() {
        EnrollStudent[] temp = new EnrollStudent[size + 4];
        for (int i = 0; i < size; i++) {
            temp[i] = this.enrollStudents[i];
        }
        enrollStudents = temp;
    }

    /**
     * Adds a student to the end of the enrollment roster
     * If a student has been added and the roster is full, the size of the roster is increased by 4
     * @param enrollStudent the student that is to be added
     */
    public void add (EnrollStudent enrollStudent) {
        if (size == 0) {
            enrollStudents[0] = enrollStudent;
            size++;
            return;
        }
        int index = find(enrollStudent);
        if(index != -1) {
            enrollStudents[index].updateCredits(enrollStudent.credits());
            return;
        }
        enrollStudents[size] = enrollStudent;
        size++;
        if(size == enrollStudents.length) grow();
    }

    /**
     * Removes the student requested in the parameter
     * The student in the last index is swapped with the index of the student to be removed
     * @param enrollStudent the student that is to be removed
     */
    public void remove(EnrollStudent enrollStudent) {
        int index = find(enrollStudent);
        if (index == -1) return;
        enrollStudents[index] = enrollStudents[size - 1];
        enrollStudents[size] = null;
        size--;
    }

    /**
     * Checks if a student is in the enrollment roster
     * @param enrollStudent the student that is to be added
     * @return true the student is in the roster, false if the student is not in the roster otherwise
     */
    public boolean contains(EnrollStudent enrollStudent) {
        return find(enrollStudent) != -1;
    }

    /**
     * Returns the list of enrolled students. Only used for printing.
     * @return the array of currently enrolled students.
     */
    public EnrollStudent[] list() {
        return enrollStudents;
    }

    /**
     * Retrieves the enrolled student on the enrollment list with an identical Profile as the given student.
     * @param findStudent the EnrolledStudent object used to find an identical one in the enrollment list.
     * @return the instance identical to the one given; if there is none that is identical, null is returned.
     */
    public EnrollStudent enrolledStudent(EnrollStudent findStudent) {
        int index = find(findStudent);
        if(index != -1) {
            return enrollStudents[index];
        }
        return null;
    }

    /**
     * Prints the list of enrolled students.
     */
    public void print() {
        for(int i = 0; i < size; i++) {
            System.out.println(enrollStudents[i]);
        }
    }

    /**
     * Checks if there are enrolled students.
     * @return true if the Enrollment array is not empty; false otherwise.
     */
    public boolean hasEnrollments() {
        return size > 0;
    }

    /**
     * Returns the number of currently enrolled students.
     * @return the size of this enrollment list.
     */
    public int size() {
        return size;
    }
}
