package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Entity;

import java.util.List;

public interface GenericDao<E extends Entity> {
    long insert(E entity) throws UnknownSqlException, ErrorMessageKeysContainedException;
    void update(E entity) throws UnknownSqlException, ErrorMessageKeysContainedException;
    void delete(long id) throws UnknownSqlException, ErrorMessageKeysContainedException;
    List<E> selectAll() throws UnknownSqlException, ErrorMessageKeysContainedException;
    E selectById(long id) throws UnknownSqlException, ErrorMessageKeysContainedException;
}
