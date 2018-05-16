package services;

import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.AssignmentTypeDao;
import model.dao.interfaces.DaoFactory;
import model.dao.interfaces.RoleDao;
import model.database.ConnectionProvider;
import model.database.TransactionManager;
import model.entities.Role;

import java.sql.SQLException;
import java.util.*;

public class RoleService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();

    private RoleService() {
    };

    public static long addRole(Role role) {
        try {
            RoleDao roleDao = daoFactory.createRoleDao();
            long roleId = roleDao.insert(role);
            return roleId;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteRole(long roleId) {
        try {
            RoleDao roleDao = daoFactory.createRoleDao();
            roleDao.delete(roleId);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateRole(Role role) {
        try {
            RoleDao roleDao = daoFactory.createRoleDao();
            TransactionManager.beginTransaction();
            roleDao.update(role);
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        }
    }
    
    static public List<Role> getRoles() {
        RoleDao roleDao = daoFactory.createRoleDao();
        AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
        try {
            List<Role> roles = roleDao.selectAll();
            roles.sort(Comparator.comparing(Role::getId));
            roles.forEach((Role role) -> role.setAllowedAssignmentTypes(assignmentTypeDao.selectByRoleId(role.getId())));
            return roles;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
        }
        return null;
    }
}
