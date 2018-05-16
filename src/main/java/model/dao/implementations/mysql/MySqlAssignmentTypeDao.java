package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import model.dao.interfaces.AssignmentTypeDao;
import model.dao.interfaces.GenericDaoSupport;
import model.entities.AssignmentType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlAssignmentTypeDao
        extends GenericDaoSupport<AssignmentType> implements AssignmentTypeDao {

    private static MySqlAssignmentTypeDao instance;

    public static MySqlAssignmentTypeDao getInstance() {
        if (instance == null) {
            synchronized (MySqlAssignmentTypeDao.class) {
                if (instance == null)
                    instance = new MySqlAssignmentTypeDao();
            }
        }
        return instance;
    }

    private MySqlAssignmentTypeDao() {

    }

    @Override
    public long insert(AssignmentType assignmentType) {
        long assignmentTypeId = insertEntity(assignmentType,
                "INSERT INTO assignment_type (name, hospitalization_required) VALUES(?, ?)");
        assignmentType.setId(assignmentTypeId);
        return assignmentTypeId;
    }

    @Override
    public void update(AssignmentType assignmentType) {
        updateEntity(assignmentType, "UPDATE assignment_type SET name = ?, hospitalization_required = ? " +
                "WHERE id = " + assignmentType.getId());
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM assignment_type WHERE id = " + id);
    }

    @Override
    public List<AssignmentType> selectAll() {
        return selectEntities("SELECT * FROM assignment_type");
    }

    @Override
    public AssignmentType selectById(long id) {
        return selectEntity("SELECT * FROM assignment_type WHERE id = " + id);
    }

    @Override
    public List<AssignmentType> selectByRoleId(long roleId) {
        return selectEntities("SELECT * FROM assignment_type WHERE id IN " +
                "(SELECT assignment_type_id FROM role_to_assignment_type WHERE role_id = ?)", roleId);
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, AssignmentType assignmentType) {
        try {
            preparedStatement.setString(1, assignmentType.getName());
            preparedStatement.setBoolean(2, assignmentType.isHospitalizationRequired());
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }

    @Override
    protected AssignmentType getSingleResult(ResultSet resultSet) {
        try {
            AssignmentType assignmentType = new AssignmentType();
            assignmentType.setId(resultSet.getLong("id"));
            assignmentType.setName(resultSet.getString("name"));
            assignmentType.setHospitalizationRequired(resultSet.getBoolean("hospitalization_required"));
            return assignmentType;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }
}
