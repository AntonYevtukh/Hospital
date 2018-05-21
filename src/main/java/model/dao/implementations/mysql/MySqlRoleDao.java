package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import exceptions.UnknownSqlException;
import model.dao.interfaces.GenericDaoSupport;
import model.dao.interfaces.RoleDao;
import model.database.ConnectionProvider;
import model.entities.AssignmentType;
import model.entities.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

public class MySqlRoleDao extends GenericDaoSupport<Role> implements RoleDao {

    private static MySqlRoleDao instance;

    public static MySqlRoleDao getInstance() {
        if (instance == null) {
            synchronized (MySqlRoleDao.class) {
                if (instance == null)
                    instance = new MySqlRoleDao();
            }
        }
        return instance;
    }

    private MySqlRoleDao() {

    }

    @Override
    public long insert(Role role) {
        long roleId = insertEntity(role, "INSERT INTO role (name) VALUES(?)");
        role.setId(roleId);
        updateAllowedAssignmentTypes(role);
        return roleId;
    }

    @Override
    public void update(Role role) {
        updateEntity(role, "UPDATE role SET name = ? WHERE id = " + role.getId());
        updateAllowedAssignmentTypes(role);
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM role WHERE id = " + id);
    }

    @Override
    public List<Role> selectAll() {
        return selectEntities("SELECT * FROM role");
    }

    @Override
    public Role selectById(long id) {
        return selectEntity("SELECT * FROM role WHERE id = " + id);
    }

    @Override
    public List<Role> selectByUserId(long userId) {
        return selectEntities("SELECT * FROM role WHERE id IN " +
        "(SELECT role_id FROM user_to_role WHERE user_id = ?)", userId);
    }

    @Override
    public List<Role> selectByAssignmentTypeId(long assignmentTypeId) {
        return selectEntities("SELECT * FROM role WHERE id IN " +
                "(SELECT role_id FROM role_to_assignment_type WHERE assignment_type_id = ?)", assignmentTypeId);
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Role role) {
        try {
            preparedStatement.setString(1, role.getName());
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }

    @Override
    protected Role getSingleResult(ResultSet resultSet)
    {
        try {
            Role role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
            return role;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    private void updateAllowedAssignmentTypes(Role role) {
        Connection connection = ConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM Role_To_Assignment_Type WHERE role_id = ?; ")) {
            preparedStatement.setLong(1, role.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        }
        if (role.getAllowedAssignmentTypes() != null && !role.getAllowedAssignmentTypes().isEmpty()) {

            StringJoiner queryBuilder = new StringJoiner(", ",
                            "INSERT INTO Role_To_Assignment_Type (role_id, assignment_type_id) VALUES ",
                    ""
            );
            role.getAllowedAssignmentTypes().forEach((AssignmentType assignmentType) -> queryBuilder.add("(?,?)"));

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    queryBuilder.toString()
            )) {
                int i = 1;
                for (AssignmentType assignmentType : role.getAllowedAssignmentTypes()) {
                    preparedStatement.setLong(i++, role.getId());
                    preparedStatement.setLong(i++, assignmentType.getId());
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new UnknownSqlException(e.getMessage());
            }
        }
    }
}
