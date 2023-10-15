package discordbot.manager.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zaxxer.hikari.HikariDataSource;

import discordbot.Constants;

public abstract class MysqlConnection {
    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static final MysqlConnection INSTANCE = new MysqlConnection(
            Constants.MYSQL_DATABASE,
            Constants.MYSQL_SERVER,
            Constants.MYSQL_PORT,
            Constants.MYSQL_USERNAME,
            Constants.MYSQL_PASSWORD) {
    };

    private final String database;
    private final String hostname;
    private final int port;

    private final String username;
    private final String password;

    private HikariDataSource dataSource;

    public MysqlConnection(String database, String hostname, int port,
            String username, String password) {

        this.database = database;
        this.hostname = hostname;
        this.port = port;

        this.username = username;
        this.password = password;
    }

    public String getURL() {
        return "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?useSSL=false";
    }

    public void connectMySQL() {
        final HikariDataSource database = new HikariDataSource();
        database.setJdbcUrl(this.getURL());
        database.setUsername(this.username);
        database.setPassword(this.password);
        this.dataSource = database;

        System.out.println("Connected to MySQL Database.");
    }

    public void disconnectMySQL(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public boolean executeStatement(String statement) {
        try (Connection connection = MysqlConnection.INSTANCE.getConnection()) {
            final boolean result = this.executeStatement(connection, statement);
            this.disconnectMySQL(connection);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet queryStatement(String statement) {
        try (Connection connection = MysqlConnection.INSTANCE.getConnection()) {
            final ResultSet result = this.queryStatement(connection, statement);
            this.disconnectMySQL(connection);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Importante meter dentro de un try catch siempre */
    public boolean executeStatement(Connection connection, String statement) throws Exception {
        if (connection == null) {
            throw new Exception("MySQL Connection is null.");
        }

        final PreparedStatement preparedStatement = connection.prepareStatement(statement);
        return preparedStatement.execute();
    }

    /* Importante meter dentro de un try catch siempre */
    public ResultSet queryStatement(Connection connection, String statement) throws Exception {
        if (connection == null) {
            throw new Exception("MySQL Connection is null.");
        }

        final PreparedStatement preparedStatement = connection.prepareStatement(statement);
        return preparedStatement.executeQuery();
    }
}