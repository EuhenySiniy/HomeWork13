package crud;

import models.Company;
import db.util.ConnectionUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanyOperation {
    private static final Logger LOGGER = Logger.getLogger(CompanyOperation.class.getName());

    private static final String SELECT_ID = "SELECT * FROM companies WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM companies";
    private static final String INSERT = "INSERT INTO companies(name, address) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE companies SET name = ?, address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM companies WHERE id = ?";

    public Company selectById(int id) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            assert connection != null;
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Company company = createCompany(resultSet);
            return company;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return null;
        } finally {
            resultSet.close();
        }
    }

    public List<Company> selectAll() throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            assert connection != null;
            resultSet = statement.executeQuery(SELECT_ALL);
            List<Company> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(createCompany(resultSet));
            }
            return result;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
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
            LOGGER.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void insert(Company object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, object.getName);
            preparedStatement.setString(2, object.getAddress());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void update(Company object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getAddress());
            preparedStatement.setInt(3, object.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    private Company createCompany(ResultSet resultSet) throws SQLException {
        return new Company(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("address"));
    }
}
