package week03exercises;

public class Student {
    // Non-static variables: Each instance of Student has its own copy
    private String name;
    private int studentID;

    // Static variable: Shared among all instances of Student
    private static int numStudents = 0;

    public static Student createStudent(String name, int studentID) {
        Student student = new Student();

        //TODO Set student's name and ID appropriately
        student.name = name;
        student.studentID = studentID;

        //TODO Increment total number of students
        numStudents++;

        return student;
    }

    public void displayDetails() {
        System.out.println("Name: " + name + ", Student ID: " + studentID);
    }

    public static int getNumStudents() {
        return numStudents;
    }
}
