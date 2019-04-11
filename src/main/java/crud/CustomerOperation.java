package crud;

import lombok.extern.log4j.Log4j;
import models.Customer;
import db.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j
public class CustomerOperation {
    private static final String SELECT_ID = "SELECT * FROM customers WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM customers";
    private static final String INSERT = "INSERT INTO customers(name, age) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE customers SET name = ?, age = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM customers WHERE id = ?";

    public Customer selectById(int id) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            assert connection != null;
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Customer customer = createCustomer(resultSet);
            return customer;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        } finally {
            resultSet.close();
        }
    }

    public List<Customer> selectAll() throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            assert connection != null;
            resultSet = statement.executeQuery(SELECT_ALL);
            List<Customer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(createCustomer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            resultSet.close();
        }
        return Collections.emptyList();
    }

    public void deleteById(int id) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void insert(Customer object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setInt(2, object.getAge());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void update(Customer object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setInt(2, object.getAge());
            preparedStatement.setInt(3, object.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    private Customer createCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age"));
    }
}
