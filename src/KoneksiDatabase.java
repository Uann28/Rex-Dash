import java.sql.*;
import java.util.*;

class KoneksiDatabase {

    private static final String HOST    = "localhost";
    private static final int    PORT    = 3306;
    private static final String DB_NAME = "rex_dash";
    private static final String USER    = "root"; 
    private static final String PASS    = "";     

    private static Connection cache;

    public static Connection getConnection() throws SQLException {
        if (cache != null && !cache.isClosed()) return cache;

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("Driver MySQL tidak ditemukan", e);
            }

            String urlRoot = String.format(
                    "jdbc:mysql://%s:%d/?serverTimezone=UTC",
                    HOST, PORT
            );

            try (Connection c = DriverManager.getConnection(urlRoot, USER, PASS);
                 Statement st = c.createStatement()) {

                // bikin database kalau belum ada
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "`");
            }

            String urlDb = String.format(
                    "jdbc:mysql://%s:%d/%s?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true",
                    HOST, PORT, DB_NAME
            );

            cache = DriverManager.getConnection(urlDb, USER, PASS);

            inisialisasi(cache);

            return cache;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void inisialisasi(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL" +
                    ") ENGINE=InnoDB");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS scores (" +
                    "score_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "score INT NOT NULL, " +        
                    "timestamp DATETIME NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB");

        }
    }

    public static boolean registerUser(String usn, String pass) {
        try {
            Connection c = getConnection();
            try (PreparedStatement cek = c.prepareStatement(
                    "SELECT user_id FROM users WHERE username=?")) {
                cek.setString(1, usn);
                try (ResultSet r = cek.executeQuery()) {
                    if (r.next()) return false; // sudah ada
                }
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO users(username,password) VALUES(?,?)")) {
                ps.setString(1, usn);
                ps.setString(2, pass);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int loginUser(String usn, String pass) {
        try {
            Connection c = getConnection();
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT user_id FROM users WHERE username=? AND password=?")) {
                ps.setString(1, usn);
                ps.setString(2, pass);
                try (ResultSet r = ps.executeQuery()) {
                    return r.next() ? r.getInt(1) : -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean saveScore(int userId, int score) {
        try {
            Connection c = getConnection();
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO scores(user_id,score,timestamp) VALUES (?,?,NOW())")) {
                ps.setInt(1, userId);
                ps.setInt(2, score);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getHighScoreForUser(int userId) {
        String sql = "SELECT MAX(score) FROM scores WHERE user_id = ?";
        try (Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static List<Object[]> getLeaderboardTop10() {
        List<Object[]> rows = new ArrayList<>();
        String sql =
                "SELECT u.username, s.score, s.timestamp " +
                "FROM scores s JOIN users u ON s.user_id=u.user_id " +
                "ORDER BY s.score DESC, s.timestamp ASC LIMIT 10";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString(1),  
                        rs.getInt(2),      
                        rs.getTimestamp(3) 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
