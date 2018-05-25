package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.User;
import utils.LongLimit;

import java.util.List;

public interface UserDao extends GenericDao<User> {

    void updateHospitalizedStatus(boolean hospitalizedStatus, long userId);

    User selectShortById(long id);

    User selectByLogin(String login) throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<User> selectShortInRange(LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<User> selectAllShort() throws UnknownSqlException, ErrorMessageKeysContainedException;

    long selectCountOfUsers() throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<User> selectShortByRoleIdInRange(long roleId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<User> selectAllShortByRoleId(long roleId) throws UnknownSqlException, ErrorMessageKeysContainedException;

    long selectCountOfUsersWithRole(long roleId) throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<User> selectAllShortByRoleIdAndHospitalizationStatus(long roleId, boolean hospitalized);
}
