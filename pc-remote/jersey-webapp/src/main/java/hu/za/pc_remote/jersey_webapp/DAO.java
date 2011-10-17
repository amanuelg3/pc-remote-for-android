package hu.za.pc_remote.jersey_webapp;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/16/11
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DAO {
    private static final Logger log = Logger.getLogger(DAO.class);
    private static final String DATASOURCE_CONTEXT = "jdbc/mysqldb";
    private static final String listLayoutsQuery = "SELECT id as id, name as name FROM layouts";
    private static final String getLayoutQuery = "SELECT id as id, name as name, text as text FROM layouts WHERE id = ?";
    private static final String updateLayoutQuery = "UPDATE layouts SET name=?, text=? WHERE id = ?";
    private static final String insertLayoutQuery = "INSERT INTO layouts(name, text) VALUES(?, ?)";
    private static final String deleteLayoutQuery = "DELETE FROM layouts WHERE id = ?";


    public static List<ListItem> getLayouts() {

        List<ListItem> result = new ArrayList<ListItem>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getJNDIConnection();
            preparedStatement = connection.prepareStatement(listLayoutsQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ListItem item = new ListItem();
                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                result.add(item);
            }

        } catch (SQLException se) {
            log.error(se);
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e);
                }
        }
        return result;
    }

    public static Layout getLayout(int id) {

        Layout result = new Layout();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getJNDIConnection();
            preparedStatement = connection.prepareStatement(getLayoutQuery);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.setId(resultSet.getInt("id"));
                result.setName(resultSet.getString("name"));
                result.setText(resultSet.getString("text"));
            }

        } catch (SQLException se) {
            log.error(se);
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e);
                }
        }
        return result;
    }

    public static void updateLayout(Layout layout) {

        List<String> result = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getJNDIConnection();
            preparedStatement = connection.prepareStatement(updateLayoutQuery);
            preparedStatement.setString(1, layout.getName());
            preparedStatement.setString(2, layout.getText());
            preparedStatement.setInt(3, layout.getId());
            preparedStatement.execute();

        } catch (SQLException se) {
            log.error(se);
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e);
                }
        }
    }

    public static void insertLayout(Layout layout) {

        List<String> result = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getJNDIConnection();
            preparedStatement = connection.prepareStatement(insertLayoutQuery);
            preparedStatement.setString(1, layout.getName());
            preparedStatement.setString(2, layout.getText());
            preparedStatement.execute();

        } catch (SQLException se) {
            log.error(se);
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e);
                }
        }
    }

    public static void deleteLayout(int id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getJNDIConnection();
            preparedStatement = connection.prepareStatement(deleteLayoutQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException se) {
            log.error(se);
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e);
                }
        }
    }

    protected static Connection getJNDIConnection() {
        Connection result = null;
        try {
            Context initialContext = new InitialContext();
            if (initialContext == null) {
                log.error("JNDI problem. Cannot get InitialContext.");
            }
            DataSource datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
            if (datasource != null) {
                result = datasource.getConnection();
            } else {
                log.error("Failed to lookup datasource.");
            }
        } catch (NamingException ex) {
            log.error("Cannot get connection: " + ex);
        } catch (SQLException ex) {
            log.error("Cannot get connection: " + ex);
        }
        return result;
    }
}