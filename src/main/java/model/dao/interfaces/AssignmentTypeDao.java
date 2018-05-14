package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.AssignmentType;

import java.sql.SQLException;
import java.util.List;

public interface AssignmentTypeDao extends GenericDao<AssignmentType> {
    List<AssignmentType> selectByRoleId(long roleId) throws UnknownSqlException, ErrorMessageKeysContainedException;
}
