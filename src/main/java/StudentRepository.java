import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> getStudentWithUserName(String userName);

    void addStudent(String firstName, String lastName, String userName);

    void deleteStudent(Student student);

    List<Student> getStudents();
}
