package model.dao.implementations.mysql;

import enums.Gender;
import exceptions.EntitySQLParseException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.QueryPreparationException;
import exceptions.UnknownSqlException;
import model.dao.interfaces.GenericDaoSupport;
import model.dao.interfaces.UserDao;
import model.database.ConnectionProvider;
import model.entities.Photo;
import model.entities.Role;
import model.entities.User;
import utils.LongLimit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class MySqlUserDao extends GenericDaoSupport<User> implements UserDao {

    private static MySqlUserDao instance;
    private static final String INSERT_TEMPLATE =
            "INSERT INTO user(login, password, first_name, last_name, middle_name, " +
            "date_of_birth, gender, passport_number, email, phone, address, language, items_per_page, photo_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TEMPLATE =
            "UPDATE user SET login = ?, password = ?, first_name = ?, last_name = ?, middle_name = ?, date_of_birth = ?, " +
            "gender = ?, passport_number = ?, email = ?, phone = ?, address = ?, language = ?, items_per_page = ?, photo_id = ? " +
            "WHERE id = ";


    public static MySqlUserDao getInstance() {
        if (instance == null) {
            synchronized (MySqlUserDao.class) {
                if (instance == null)
                    instance = new MySqlUserDao();
            }
        }
        return instance;
    }

    private MySqlUserDao() {

    }

    @Override
    public long insert(User user) {
        checkUniqueFields(user);
        long userId = insertEntity(user, INSERT_TEMPLATE);
        user.setId(userId);
        updateUserRoles(user);
        return userId;
    }

    @Override
    public void update(User user) {
        checkUniqueFields(user);
        updateEntity(user, UPDATE_TEMPLATE + user.getId());
        updateUserRoles(user);
    }

    @Override
    public void updateHospitalizedStatus(boolean hospitalizedStatus, long userId) {
        Connection connection = ConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE user SET hospitalized = ? WHERE id = ?"
        )){
            preparedStatement.setBoolean(1, hospitalizedStatus);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM user WHERE id = " + id);
    }

    @Deprecated
    @Override
    public List<User> selectAll() {
        return selectEntities("SELECT * FROM user ORDER BY last_name, first_name, middle_name", this::getSingleResult);
    }

    @Override
    public User selectById(long id) {
        return selectEntity("SELECT * FROM user WHERE id = " + id, this::getSingleResult);
    }

    @Override
    public User selectShortById(long id) {
        return selectEntity("SELECT * FROM user WHERE id = " + id, this::getSingleShortResult);
    }

    @Override
    public User selectByLogin(String login) {
        return selectEntity("SELECT * FROM user WHERE login = ?", login);
    }

    @Override
    public List<User> selectShortInRange(LongLimit longLimit) {
        return selectEntities("SELECT id, first_name, last_name, middle_name, passport_number, hospitalized FROM user " +
                        "ORDER BY last_name, first_name, middle_name LIMIT ?, ?",
                this::getSingleShortResult, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public List<User> selectAllShort() {
        return selectShortInRange(new LongLimit(0L, Long.MAX_VALUE));
    }

    @Override
    public long selectCountOfUsers() {
        return selectCountOfEntities("SELECT count(*) FROM user");
    }

    @Override
    public List<User> selectShortByRoleIdInRange(long roleId, LongLimit longLimit) {
        return selectEntities("SELECT id, first_name, last_name, middle_name, passport_number, hospitalized FROM user " +
                        "WHERE id in (SELECT user_id FROM user_to_role where role_id = ?) " +
                        "ORDER BY last_name, first_name, middle_name LIMIT ?, ?",
                this::getSingleShortResult, roleId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public List<User> selectAllShortByRoleId(long roleId) {
        return selectShortByRoleIdInRange(roleId, new LongLimit(0L, Long.MAX_VALUE));
    }

    @Override
    public long selectCountOfUsersWithRole(long roleId) {
        return selectCountOfEntities("SELECT count(*) FROM user WHERE user.id in (" +
                                "SELECT user_id FROM user_to_role where role_id = ?)", roleId);

    }

    @Override
    public List<User> selectAllShortByRoleIdAndHospitalizationStatus(long roleId, boolean hospitalized) {
        return selectEntities("SELECT id, first_name, last_name, middle_name, passport_number, hospitalized FROM user " +
                        "WHERE id IN (SELECT user_id FROM user_to_role WHERE role_id = ?) AND hospitalized = ? " +
                "ORDER BY last_name, first_name, middle_name", this::getSingleShortResult, roleId, hospitalized);
    }

    private User getSingleShortResult(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(             resultSet.getLong("id"));
            user.setFirstName(      resultSet.getString("first_name"));
            user.setLastName(       resultSet.getString("last_name"));
            user.setMiddleName(     resultSet.getString("middle_name"));
            user.setPassportNumber( resultSet.getString("passport_number"));
            user.setHospitalized(   resultSet.getBoolean("hospitalized"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    @Override
    protected User getSingleResult(ResultSet resultSet) {
        try {
            User user = getSingleShortResult(resultSet);
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setDateOfBirth(resultSet.getDate("date_of_birth"));
            user.setGender(Gender.getGenderByCode(resultSet.getByte("gender")));
            user.setEmail(resultSet.getString("email"));
            user.setPhone(resultSet.getString("phone"));
            user.setAddress(resultSet.getString("address"));
            user.setLanguage(resultSet.getString("language"));
            user.setItemsPerPage(resultSet.getInt("items_per_page"));
            user.setPhoto(new Photo(resultSet.getLong("photo_id")));
            return user;
        } catch (SQLException e) {
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, User user) {
        try {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getMiddleName());
            preparedStatement.setDate(6, user.getDateOfBirth());
            preparedStatement.setByte(7, user.getGender().getCode());
            preparedStatement.setString(8, user.getPassportNumber());
            preparedStatement.setString(9, user.getEmail());
            preparedStatement.setString(10, user.getPhone());
            preparedStatement.setString(11, user.getAddress());
            preparedStatement.setString(12, user.getLanguage());
            preparedStatement.setInt(13, user.getItemsPerPage());
            if (!Objects.isNull(user.getPhoto()) && user.getPhoto().getId() != 0)
                preparedStatement.setLong(14, user.getPhoto().getId());
            else
                preparedStatement.setObject(14, null);
            return preparedStatement;
        } catch (SQLException e) {
            throw new QueryPreparationException(e.getMessage());
        }
    }

    private void checkUniqueFields(User user) {
        Connection connection = ConnectionProvider.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT count(id) FROM user WHERE login = (?) AND id != (?) UNION ALL " +
                        "SELECT count(id) from user where passport_number = (?) AND id != (?)"
        )){
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.setString(3, user.getPassportNumber());
            preparedStatement.setLong(4, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            List<String> notUniqueErrorMessageKeys = new ArrayList<>(2);
            if (resultSet.getInt(1) > 0)
                notUniqueErrorMessageKeys.add("validation.login_not_unique");
            resultSet.next();
            if (resultSet.getInt(1) > 0)
                notUniqueErrorMessageKeys.add("validation.passport_number_not_unique");
            if (!notUniqueErrorMessageKeys.isEmpty()) {
                throw new ErrorMessageKeysContainedException(notUniqueErrorMessageKeys);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnknownSqlException(e.getMessage());
        }
    }

    private void updateUserRoles(User user)
            {
        Connection connection = ConnectionProvider.getConnection();
        StringJoiner queryBuilder = new StringJoiner(", ",
                "DELETE FROM User_To_Role WHERE user_id = ?; " +
                        "INSERT INTO User_To_Role (user_id, role_id) VALUES ",
                ";"
        );
        user.getRoleMap().forEach((Long id, Role role) -> queryBuilder.add("(?,?)"));

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryBuilder.toString()
        )) {
            int i = 2;
            preparedStatement.setLong(1, user.getId());
            for (Long roleId: user.getRoleMap().keySet()) {
                preparedStatement.setLong(i++, user.getId());
                preparedStatement.setLong(i++, roleId);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnknownSqlException(e.getMessage());
        }
    }
}
