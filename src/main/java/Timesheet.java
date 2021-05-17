import lombok.Data;

@Data
public class Timesheet {
    private final Student student;
    private final int year;
    private final int month;
    boolean fileExists = false;

    /**
     * Setzt den Namen des Timesheetfiles zusammen
     *
     * @return Namen des Timesheetfiles
     */
    public String getFileName() {
        return "Timesheet_" + student.getLastName() + "_" + (month > 9 ? month : "0" + month) + "-" + year + ".xls";
    }

}
