package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ConnectionPool {
    static Map<String, DataSource> globalDataSourceMap = new HashMap<>();

    public static void main(String[] args) throws IOException, SQLException {
        String databaseSettingsFilePath = "database.properties";

        Properties dbProperties = getDatabaseProperties(databaseSettingsFilePath);
        String jdbcUrl = dbProperties.getProperty("jdbc.url");
        String jdbcUser = dbProperties.getProperty("jdbc.user");
        String jdbcPassword = dbProperties.getProperty("jdbc.password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUser);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("connectionTimeout", 1000);
        config.addDataSourceProperty("idleTimeout", 30000);
        config.addDataSourceProperty("maximumPoolSize", 10);

        DataSource ds = new HikariDataSource(config);
        globalDataSourceMap.put("dataSource", ds);

        List<Student> students = queryStudent();
        students.forEach(System.out::println);
    }


    private static List<Student> queryStudent() throws SQLException {
        List<Student> students = new ArrayList<>();
        DataSource ds = globalDataSourceMap.get("dataSource");

        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE grade = ?")) {
                stmt.setInt(1, 2);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        students.add(extractRow(rs));
                    }
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
        try (InputStream input = Batch.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new IOException(filePath + "文件未找到");
            }
            properties.load(input);
        }
        return properties;
    }
}