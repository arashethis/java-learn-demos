package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Query {
    public static void main(String[] args) throws IOException, SQLException {
        String databaseSettingsFilePath = "database.properties";

        Properties dbProperties = getDatabaseProperties(databaseSettingsFilePath);
        String jdbcUrl = dbProperties.getProperty("jdbc.url");
        String jdbcUser = dbProperties.getProperty("jdbc.user");
        String jdbcPassword = dbProperties.getProperty("jdbc.password");

        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

        List<Student> students = queryStudents(conn);
        students.forEach(System.out::println);

        System.out.println("\n--------------------------------\n");

        List<Student> students2 = queryStudentsPrepareStatement(conn);
        students2.forEach(System.out::println);
    }

    private static List<Student> queryStudents(Connection conn) throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE grade = 1")) {
                while (rs.next()) {
                    students.add(extractRow(rs));
                }
            }
        }
        return students;
    }

    private static List<Student> queryStudentsPrepareStatement(Connection conn) throws SQLException {
        List<Student> students = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE grade = ?")) {
            stmt.setInt(1, 2);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractRow(rs));
                }
            }
        }
        return students;
    }

    private static Student extractRow(ResultSet rs) throws SQLException {
        Student student = new Student();

        student.setId(rs.getInt("id"));
        student.setGrade(rs.getInt("grade"));
        student.setName(rs.getString("name"));
        student.setGender(rs.getString("gender"));
        student.setScore(rs.getInt("score"));

        return student;
    }

    private static Properties getDatabaseProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Query.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new IOException(filePath + "文件未找到");
            }
            properties.load(input);
        }
        return properties;
    }
}