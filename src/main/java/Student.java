import lombok.Data;

@Data

public class Student implements Comparable {
    private final String firstName;
    private final String lastName;
    private final String userName;

    /**
     * Student vergleicht seinen Nachnamen mit dem eines anderen Studenten
     *
     * @param o Student, mit dem verglichen werden soll
     * @return negativer int, falls der Nachname des aufrufenden Studenten kleiner ist als der des anderen
     */
    @Override
    public int compareTo(Object o) {
        Student other = (Student) o;
        return this.lastName.compareToIgnoreCase(other.lastName);
    }
}

