import javafx.collections.*;

import java.io.*;

public class DataSource {
   public static ObservableList<Student> getAllStudents() {
      ObservableList<Student> students = FXCollections.observableArrayList();

      students.add(new Student(100100100, "Janet", "Combes", 2.85));
      students.add(new Student(100100101, "Abichal", "Kaur", 1.71));
      students.add(new Student(100100102, "Cecile", "Lalonde", 3.60));
      students.add(new Student(100100103, "Pablo", "Rodriguez", 2.19));
      students.add(new Student(100100104, "Flora", "Ivanovic", 2.45));
      students.add(new Student(100100105, "Mahmoud", "Ashfaq", 3.15));
      students.add(new Student(100100106, "Stephen", "McCullough", 1.55));
      students.add(new Student(100100107, "Zhilong", "Fu", 1.35));
      students.add(new Student(100100108, "Sadiva", "Krupal", 2.64));
      students.add(new Student(100100109, "Carmine", "Dipaolo", 3.13));
      students.add(new Student(100100110, "Sarah", "Morrissey", 2.40));
      students.add(new Student(100100111, "Pavel", "Zakharov", 1.95));

      return students;
   }
}
