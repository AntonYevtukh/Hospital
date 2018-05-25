package model.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {

    private static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    private static DataSource dataSource;

    static {
        try {
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext
                    .lookup("java:comp/env");
            String dataResourceName = "jdbc/hospital";
            dataSource = (DataSource) environmentContext
                    .lookup(dataResourceName);
            /*DataSource ds = new DataSource();
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUrl("jdbc:mysql://localhost:3306/flatdb");
            ds.setUsername("root");
            ds.setPassword("root");
            ds.setInitialSize(5);
            ds.setMaxActive(10);
            ds.setMaxIdle(5);
            ds.setMinIdle(2);
            this.dataSource = ds;*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ConnectionProvider() {

    }

    public static void bindConnection() {
        try {
            if (threadLocalConnection.get() == null)
                threadLocalConnection.set(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {

        if (threadLocalConnection.get() != null)
            return threadLocalConnection.get();
        else
            throw new RuntimeException("No connection is binded to current context");

    }

    public static void unbindConnection() {
        try {
            if (threadLocalConnection.get() != null)
                threadLocalConnection.get().close();
            threadLocalConnection.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
