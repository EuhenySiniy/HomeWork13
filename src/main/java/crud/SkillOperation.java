package crud;

import models.Skill;
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

public class SkillOperation {
    private static final Logger LOGGER = Logger.getLogger(SkillOperation.class.getName());

    private static final String SELECT_ID = "SELECT * FROM skills WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM skills";
    private static final String INSERT = "INSERT INTO developers(industry, level) VALUES(?, ?)";
    private static final String UPDATE = "UPDATE developers SET industry = ?, level = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM skills WHERE id = ?";

    public Skill selectById(int id) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            assert connection != null;
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Skill skill = createSkill(resultSet);
            return skill;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return null;
        } finally {
            resultSet.close();
        }
    }

    public List<Skill> selectAll() throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            assert connection != null;
            resultSet = statement.executeQuery(SELECT_ALL);
            List<Skill> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(createSkill(resultSet));
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

    public void insert(Skill object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, object.getIndustry());
            preparedStatement.setString(2, object.getLevel());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    public void update(Skill object) throws SQLException {
        PreparedStatement preparedStatement = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            assert connection != null;
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, object.getIndustry());
            preparedStatement.setString(2, object.getLevel());
            preparedStatement.setInt(3, object.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            preparedStatement.close();
        }
    }

    private Skill createSkill(ResultSet resultSet) throws SQLException {
        return new Skill(resultSet.getInt("id"),
                resultSet.getString("industry"),
                resultSet.getString("level"));
    }
}
