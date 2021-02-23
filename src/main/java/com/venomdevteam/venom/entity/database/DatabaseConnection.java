package com.venomdevteam.venom.entity.database;

import com.venomdevteam.venom.main.Venom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final Venom venom;

    private final String JDBC_URL;
    private final String JDBC_USR;
    private final String JDBC_PASS;

    private Connection connection;

    public String MEMBER_DATA_TABLE = "data.member_data";
    public String USER_DATA_TABLE = "data.user_data";
    public String GUILD_DATA_TABLE = "data.guild_data";
    public String TAG_DATA_TABLE = "data.tag_data";

    public DatabaseConnection(Venom venom) {
        this.venom = venom;

        this.JDBC_URL = venom.getConfig().getString("database.url");
        this.JDBC_USR = venom.getConfig().getString("database.user");
        this.JDBC_PASS = venom.getConfig().getString("database.password");

        MEMBER_DATA_TABLE = venom.getConfig().getString("database.tables.member_data");
        USER_DATA_TABLE = venom.getConfig().getString("database.tables.user_data");
        GUILD_DATA_TABLE = venom.getConfig().getString("database.tables.guild_data");
        TAG_DATA_TABLE = venom.getConfig().getString("database.tables.tag_data");

    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection(this.JDBC_URL, this.JDBC_USR, this.JDBC_PASS);
            venom.getLogger().info("Connected to database successfully");
        } catch (SQLException e) {
            venom.getLogger().error(e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                venom.getLogger().info("Disconnected from the database...");
            } catch (SQLException e) {
                venom.getLogger().error(e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
