package crud;

import lombok.extern.log4j.Log4j;
import models.Developer;
import db.util.ConnectionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Log4j
public class DeveloperOperation {
    private static final String pathToTheQuery = "src/main/resources/queries/";

    private static final String SELECT_ID = "SELECT * FROM developers WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM developers";
    private static final String INSERT = "INSERT INTO developers(name, age, salary) VALUES(?, ?, ?)";
    private static final String UPDATE = "UPDATE developers SET name = ?, age = ?, salary = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM developers WHERE id = ?";

    public Developer selectById(int id) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Developer developer = createDeveloper(resultSet);
            return developer;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        } finally {
            resultSet.close();
        }
    }

    public List<Developer> selectAll() throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(SELECT_ALL);
            List<Developer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(createDeveloper(resultSet));
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

    public void insert(Developer object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setInt(2, object.getAge());
            preparedStatement.setDouble(3, object.getSalary());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void update(Developer object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setInt(2, object.getAge());
            preparedStatement.setDouble(3, object.getSalary());
            preparedStatement.setInt(4, object.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public List<Developer> getlistOfDevelopersByProject(String projectName) throws SQLException {
        return selectAllByCondition(projectName, pathToTheQuery + "listOfDevelopersByProject.sql");
    }

    public List<Developer> getListOfDevelopersBySkillLevel(String skillLevel) throws SQLException {
        return selectAllByCondition(skillLevel, pathToTheQuery + "listOfMiddleDevelopers.sql");
    }

    public List<Developer> getListOfDevelopersByIndustry(String branchDevelopment) throws SQLException {
        return selectAllByCondition(branchDevelopment, pathToTheQuery + "listOfJavaDevelopers.sql");
    }

    public double getSalaryOfDevelopersByProject(String projectName) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            String sql = null;
            try {
                sql = new Scanner(new File(pathToTheQuery + "salaryOfDevelopersBySeparateProject.sql"))
                        .useDelimiter("\\A").next();
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            }
            preparedStatement.setString(1, projectName);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            double salary = resultSet.getDouble(1);
            return salary;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return 0;
        } finally {
            resultSet.close();
        }
    }

    private List<Developer> selectAllByCondition(String conditionalField, String pathToSql) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            String sql = null;
            try {
                sql = new Scanner(new File(pathToSql)).useDelimiter("\\A").next();
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            }
            preparedStatement.setString(1, conditionalField);
            resultSet = preparedStatement.executeQuery();

            List<Developer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(createDeveloper(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            resultSet.close();
        }
        return Collections.emptyList();
    }

    private Developer createDeveloper(ResultSet resultSet) throws SQLException {
        return new Developer(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age"),
                resultSet.getDouble("salary"));
    }
}
