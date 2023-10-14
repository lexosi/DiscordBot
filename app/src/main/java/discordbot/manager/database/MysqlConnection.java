package discordbot.manager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import discordbot.Constants;

public abstract class MysqlConnection {
    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static final MysqlConnection INSTANCE = new MysqlConnection(
            "com.mysql.cj.jdbc.Driver",
            Constants.MYSQL_DATABASE,
            Constants.MYSQL_SERVER,
            Constants.MYSQL_PORT,
            Constants.MYSQL_USERNAME,
            Constants.MYSQL_PASSWORD) {
    };

    private final String driver;

    private final String database;
    private final String hostname;
    private final int port;

    private final String username;
    private final String password;

    private Connection connection;

    public MysqlConnection(String driver, String database,
            String hostname, int port, String username, String password) {

        this.driver = driver;
        this.database = database;
        this.hostname = hostname;
        this.port = port;

        this.username = username;
        this.password = password;
    }

    public String getURL() {
        return "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?useSSL=false";
    }

    public Connection connectMySQL() {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(this.getURL(), this.username, this.password);

            System.out.println("Connected to MySQL Database.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void disconnectMySQL() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Importante meter dentro de un try catch siempre */
    public boolean executeStatement(String statement) throws Exception {
        if (this.connection == null) {
            throw new Exception("MySQL Connection is null.");
        }

        final PreparedStatement preparedStatement = this.connection.prepareStatement(statement);
        return preparedStatement.execute();
    }

    /* Importante meter dentro de un try catch siempre */
    public ResultSet queryStatement(String statement) throws Exception {
        if (this.connection == null) {
            throw new Exception("MySQL Connection is null.");
        }

        final PreparedStatement preparedStatement = this.connection.prepareStatement(statement);
        return preparedStatement.executeQuery();
    }
}