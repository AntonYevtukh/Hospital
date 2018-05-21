package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Role;

import java.util.List;

public interface RoleDao extends GenericDao<Role> {

    List<Role> selectByUserId(long userId) throws UnknownSqlException, ErrorMessageKeysContainedException;
    List<Role> selectByAssignmentTypeId(long assignmentTypeId) throws UnknownSqlException, ErrorMessageKeysContainedException;
}
