package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddUpdateDelete {
    public static void main(String[] args) throws IOException, SQLException {
        String databaseSettingsFilePath = "database.properties";

        Properties dbProperties = getDatabaseProperties(databaseSettingsFilePath);
        String jdbcUrl = dbProperties.getProperty("jdbc.url");
        String jdbcUser = dbProperties.getProperty("jdbc.user");
        String jdbcPassword = dbProperties.getProperty("jdbc.password");

        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

        int insertCount = insertStudent(conn);
        System.out.println("Inserted " + insertCount + " students");

        List<Integer> ids = insertStudentWithKeys(conn);
        int insertCount2 = ids.remove(0);
        System.out.println("Inserted " + insertCount2 + " students with keys " + ids);

        int updateCount = updateStudent(conn, ids.get(0));
        System.out.println("Updated " + updateCount + " students");

        int deleteCount = deleteStudents(conn);
        System.out.println("Deleted " + deleteCount + " students");
    }


    private static int insertStudent(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (grade, name, gender, score) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "小黄");
            stmt.setString(3, "M");
            stmt.setInt(4, 78);

            int n = stmt.executeUpdate();
            return n;
        }
    }

    private static List<Integer> insertStudentWithKeys(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (grade, name, gender, score) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, 3);
            stmt.setString(2, "小玉");
            stmt.setString(3, "F");
            stmt.setInt(4, 86);

            int n = stmt.executeUpdate();
            ids.add(n);
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
        }
        return ids;
    }

    private static int updateStudent(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE students SET name=? WHERE id=?")) {
            ps.setObject(1, "小小");
            ps.setObject(2, id);
            int n = ps.executeUpdate();
            return n;
        }
    }

    private static int deleteStudents(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id > 10")) {
            int n = ps.executeUpdate();
            return n;
        }
    }

    private static Properties getDatabaseProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = AddUpdateDelete.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new IOException(filePath + "文件未找到");
            }
            properties.load(input);
        }
        return properties;
    }
}