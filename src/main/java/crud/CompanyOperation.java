package crud;

import lombok.extern.log4j.Log4j;
import models.Company;
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
public class CompanyOperation {
    private static final String SELECT_ID = "SELECT * FROM companies WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM companies";
    private static final String INSERT = "INSERT INTO companies(name, address) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE companies SET name = ?, address = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM companies WHERE id = ?";

    public Company selectById(int id) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Company company = createCompany(resultSet);
            return company;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        } finally {
            resultSet.close();
        }
    }

    public List<Company> selectAll() throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(SELECT_ALL);
            List<Company> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(createCompany(resultSet));
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

    public void insert(Company object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getAddress());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void update(Company object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getAddress());
            preparedStatement.setInt(3, object.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
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
