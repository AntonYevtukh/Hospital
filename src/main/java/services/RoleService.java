package services;

import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.DaoFactory;
import model.dao.interfaces.RoleDao;
import model.database.ConnectionProvider;
import model.entities.Role;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RoleService {

    private RoleService() {
    };

    static public Set<Role> getRolesFromDatabase() {
        DaoFactory daoFactory = MySqlDaoFactory.getInstance();
        RoleDao roleDao = daoFactory.createRoleDao();
        try {
            ConnectionProvider.bindConnection();
            Set<Role> roles = new TreeSet<>(Comparator.comparing(Role::getId));
            roles.addAll(roleDao.selectAll());
            ConnectionProvider.unbindConnection();
            return roles;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
        }
        return null;
    }
}
