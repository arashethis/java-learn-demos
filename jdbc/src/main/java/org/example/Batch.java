package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Batch {
    public static void main(String[] args) throws IOException, SQLException {
        String databaseSettingsFilePath = "database.properties";

        Properties dbProperties = getDatabaseProperties(databaseSettingsFilePath);
        String jdbcUrl = dbProperties.getProperty("jdbc.url");
        String jdbcUser = dbProperties.getProperty("jdbc.user");
        String jdbcPassword = dbProperties.getProperty("jdbc.password");

        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

        batchInsertStudent(conn, List.of("小甲", "小乙", "小丙", "小丁"), 2, "M", 99);

        deleteStudents(conn);
    }


    private static void batchInsertStudent(Connection conn, List<String> names, int grade, String gender, int score) throws SQLException {
        boolean isAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (grade, name, gender, score) VALUES (?, ?, ?, ?)")) {
            for (String name : names) {
                stmt.setInt(1, grade);
                stmt.setString(2, name);
                stmt.setString(3, gender);
                stmt.setInt(4, score);
                stmt.addBatch();
            }

            int[] ns = stmt.executeBatch();
            for (int n : ns) {
                System.out.println("Inserted " + n + " students");
            }
        }

        conn.commit();
        conn.setAutoCommit(isAutoCommit);
    }

    private static int deleteStudents(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id > 10")) {
            int n = ps.executeUpdate();
            return n;
        }
    }

    private static Properties getDatabaseProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Batch.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new IOException(filePath + "文件未找到");
            }
            properties.load(input);
        }
        return properties;
    }
}