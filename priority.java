import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.sql.*;

public class priority {

    public priority() {
        alertEmployee();
        showPriority();
    }

    /*
     * Check the priority
     * Sort according to priority and time added
     * Alert employee if high priority task isn't completed by 11:59pm friday
     * Add high priority task to worksheet after 9am monday
     */
    private String id;
    private String date;
    private String type;
    private String title;
    private String description;
    private String employee;
    private String PRIORITY;
    private String status;
    private String url = "jdbc:mysql://sql9.freesqldatabase.com:3306/sql9581282";
    private String user = "sql9581282";
    private String SQL_password = "BABN2FhxnM";
    // private Boolean isLoggedIn = false;
    private String username;
    private String password;
    private String code;

    // Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    public int day = calendar.get(Calendar.DAY_OF_WEEK);
    public int hour = calendar.get(Calendar.HOUR_OF_DAY);
    public int minutes = calendar.get(Calendar.MINUTE);
    Connection connection;
    Item newItem = new Item(id, date, type, title, description, employee, PRIORITY, status);
    UserLogin userLogin = new UserLogin();
    LoginInfo loginInfo = new LoginInfo(username, password, PRIORITY, code);
    TA_MasterList MasterList = new TA_MasterList();
    EMP_List emp_List = new EMP_List(code);
    Dictionary<String, Integer> priorityDict = new Hashtable<>() {
        {
            put("High", 1);
            put("Medium", 2);
            put("Low", 3);
            put("", null);
        }
    };

    public void sortPriority() {
        try {
            Connection connection = DriverManager.getConnection(url, user, SQL_password);
            emp_List.UpdateStatuses();
            emp_List.LoadRecords(code);

            for (Item items : emp_List.LoadRecords(code)) {
                if (items.getPriority() != null && items.getPriority().equalsIgnoreCase("High")) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, String.format(
                            "ID: %s\n Date: %s\n Type: %s\n Title: %s\n Description: %s\n Employee: %s\n Priority: %s\n Status: %s\n",
                            items.getid(), items.getDate(), items.getType(), items.getTitle(), items.getDescription(),
                            items.getEmployees(), items.getPriority(), items.getStatus()));
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("[CONNECTING...] Connection Failed\n");
            e.printStackTrace();
        }

    }

    public void alertEmployee() {
        try {
            Connection connection = DriverManager.getConnection(url, user, SQL_password);
            emp_List.LoadRecords(code);
            if (newItem.getPriority().equalsIgnoreCase("High")
                    && (newItem.getStatus().equalsIgnoreCase("Ongoing")
                            || newItem.getStatus().equalsIgnoreCase("Not Started"))) {
                if (day == 6 && hour == 23 && minutes == 59) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                            String.format("[%s] You have a %s priority %s that is %s.\n Task description: %s\n",
                                    newItem.getEmployees(), newItem.getPriority(), newItem.getType(),
                                    newItem.getStatus(),
                                    newItem.getDescription()));
                }

            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("[CONNECTING...] Connection Failed\n");
            e.printStackTrace();
        }

    }

    public Dictionary<String, Integer> getPriorityDict() {
        return priorityDict;
    }

    public void setPriorityDict(Dictionary<String, Integer> priorityDict) {
        this.priorityDict = priorityDict;
    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * Innerpriority
     */
    public class LowToHighPriority implements Comparator<priority> {

        @Override
        public int compare(priority o1, priority o2) {
            return Integer.compare(o1.getPriorityDict().get(newItem.getPriority()), o2.getPriorityDict()
                    .get(newItem.getPriority()));
        }

    }

    public class HighToLowPriority implements Comparator<priority> {

        @Override
        public int compare(priority o1, priority o2) {
            return Integer.compare(o2.getPriorityDict().get(newItem.getPriority()), o1.getPriorityDict()
                    .get(newItem.getPriority()));
        }
    }

    public static void showPriority() {
        priority prio = new priority();
        prio.alertEmployee();
        prio.sortPriority();
    }

    public static void main(String[] args) {
        showPriority();
    }

}
