
import fancyui.FancyTable;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DBConn {

    String DB_URL = "jdbc:sqlite:course.db";

    public Connection connect() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite Database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String login(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection conn = connect(); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()){
                String role = rs.getString("role");
                return role;
            }
            return "";
//            String role = rs.getString("role");
//            return rs.next() ? role : "";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public boolean register(String name, String email, String ID, String username, String password) {
        String query = "INSERT INTO user (name,email,id,username,password ) VALUES (?,?,?,?,?) ";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, ID);
            pstmt.setString(4, username);
            pstmt.setString(5, password);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public String getStudentID(String username) {

    String query = "SELECT id FROM user WHERE username = ?";

    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getString("id");
        }

        return "";

    } catch (SQLException e) {
        System.out.println(e.getMessage());
        return "";
    }
}

    public DefaultTableModel getAllInfo() {
        String[] headers = {"Student ID", "Code", "Subject", "Schedule", "Teacher", "Status"};

        DefaultTableModel model = new DefaultTableModel(headers, 0);

        String query = "SELECT * FROM info";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String studentID = rs.getString("StudentID");
                String code = rs.getString("Code");
                String subject = rs.getString("Subject");
                String schedule = rs.getString("Schedule");
                String teacher = rs.getString("Teacher");
                String status = rs.getString("Status");

                String[] row = {studentID, code, subject, schedule, teacher, status};

                model.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return model;
    }

    public boolean saveCourse(String studentID, String code, String subject,
            String schedule, String teacher, String status) {

        String query = "INSERT INTO info "
                + "(StudentID, Code, Subject, Schedule, Teacher, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, studentID);
            pstmt.setString(2, code);
            pstmt.setString(3, subject);
            pstmt.setString(4, schedule);
            pstmt.setString(5, teacher);
            pstmt.setString(6, status);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error saving course: " + e.getMessage());
            return false;
        }
    }
}
