import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StudentDBRepository implements StudentRepository{
    String dbUrl = "jdbc:postgresql://database-1.ca9yujkt2z1o.eu-central-1.rds.amazonaws.com:5432/timesheets";
    String dbUsername = "exxetadev102";
    String dbPassword = "password";
    @Override
    public Optional<Student> getStudentWithUserName(String userName) {

        try (Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)
        ) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE userName='" + userName + "'");
            if (rs.next()) {
                System.out.println(rs.getString("username"));
                Student s = new Student(rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
                return Optional.of(s);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void addStudent(String firstName, String lastName, String userName) {
        executeSqlUpdate("INSERT INTO students(userName,firstName,lastName) VALUES('" + userName + "','" + firstName + "','" + lastName + "') " +
                "ON CONFLICT(userName) DO NOTHING");
    }

    @Override
    public void deleteStudent(Student student) {
        executeSqlUpdate("DELETE FROM students WHERE userName='" + student.getUserName() + "'");
    }

    @Override
    public List<Student> getStudents() {
        List<Student> studentList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                Student s = new Student(rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
                studentList.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }

    private void executeSqlUpdate(String sql) {
        try (Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
