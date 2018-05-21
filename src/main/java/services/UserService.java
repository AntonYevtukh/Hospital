package services;

import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.*;
import model.database.TransactionManager;
import model.entities.Role;
import model.entities.User;
import utils.EncryptUtil;
import utils.LongLimit;
import utils.PageContent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();

    private UserService() {

    }

    public static long registerUser(User user) {
        try {
            UserDao userDao = daoFactory.createUserDao();
            PhotoDao photoDao = daoFactory.createPhotoDao();
            user.setPassword(EncryptUtil.encryptString(user.getPassword()));
            TransactionManager.beginTransaction();
            long photoId = photoDao.insert(user.getPhoto());
            user.getPhoto().setId(photoId);
            long userId = userDao.insert(user);
            TransactionManager.commitTransaction();
            return userId;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        }
    }

    public static void updateUser(User user) {
        try {
            UserDao userDao = daoFactory.createUserDao();
            PhotoDao photoDao = daoFactory.createPhotoDao();
            if (!user.getPassword().isEmpty()) {
                user.setPassword(EncryptUtil.encryptString(user.getPassword()));
            } else {
                User storedUser = userDao.selectById(user.getId());
                user.setPassword(storedUser.getPassword());
            }
            TransactionManager.beginTransaction();
            if (user.getPhoto().getContent() != null) {
                if (user.getPhoto().getId() == 0) {
                    long photoId = photoDao.insert(user.getPhoto());
                    user.getPhoto().setId(photoId);
                } else {
                    photoDao.update(user.getPhoto());
                }
            }
            userDao.update(user);
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        }
    }

    public static void deleteUser(long userId) {
        try {
            UserDao userDao = daoFactory.createUserDao();
            PhotoDao photoDao = daoFactory.createPhotoDao();
            User user = userDao.selectById(userId);
            TransactionManager.beginTransaction();
            userDao.delete(userId);
            photoDao.delete(user.getPhoto().getId());
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            TransactionManager.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public static User getUserById(long userId) {
        try {
            UserDao userDao = daoFactory.createUserDao();
            PhotoDao photoDao = daoFactory.createPhotoDao();
            RoleDao roleDao = daoFactory.createRoleDao();
            AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
            TransactionManager.beginTransaction();
            User user = userDao.selectById(userId);
            List<Role> roles = roleDao.selectByUserId(userId);
            roles.forEach((Role role) ->
                    role.setAllowedAssignmentTypes(assignmentTypeDao.selectByRoleId(role.getId())));
            Map<Long, Role> roleMap = roles.stream().
                    collect(Collectors.toMap((Role role) -> role.getId(), Function.identity()));
            user.setRoleMap(roleMap);
            if (user.getPhoto().getId() != 0) {
                user.setPhoto(photoDao.selectById(user.getPhoto().getId()));
            }
            user.setPassword("");
            TransactionManager.commitTransaction();
            return user;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw new ErrorMessageKeysContainedException(List.of("user.not_found"));
        }
    }

    public static long signIn(String login, String password) {
        try {
            UserDao userDao = daoFactory.createUserDao();
            TransactionManager.beginTransaction();
            User user = userDao.selectByLogin(login);
            if (EncryptUtil.encryptString(password).equals(user.getPassword())) {
                return user.getId();
            }
            throw new ErrorMessageKeysContainedException(List.of("error.password_wrong"));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw new ErrorMessageKeysContainedException(List.of("error.login_not_found"));
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        }
    }

    public static PageContent<User> getUsersForPage(int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        UserDao userDao = daoFactory.createUserDao();
        List<User> content = userDao.selectShortInRange(longLimit);
        long countOfUsers = userDao.selectCountOfUsers();
        int totalPages = (int)((countOfUsers / itemsPerPage) + (countOfUsers % itemsPerPage == 0 ? 0 : 1));
        PageContent<User> userPageContent = new PageContent<>();
        userPageContent.setContent(content);
        userPageContent.setPage(page);
        userPageContent.setTotalPages(totalPages);
        return userPageContent;
    }

    public static PageContent<User> getUsersForPageByRole(long roleId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        UserDao userDao = daoFactory.createUserDao();
        List<User> content = userDao.selectShortByRoleIdInRange(roleId, longLimit);
        long countOfUsers = userDao.selectCountOfUsersWithRole(roleId);
        int totalPages = (int)((countOfUsers / itemsPerPage) + (countOfUsers % itemsPerPage == 0 ? 0 : 1));
        PageContent<User> userPageContent = new PageContent<>();
        userPageContent.setContent(content);
        userPageContent.setPage(page);
        userPageContent.setTotalPages(totalPages);
        return userPageContent;
    }

    public static List<User> getAllUsersByRole(long roleId) {
        UserDao userDao = daoFactory.createUserDao();
        List<User> users = userDao.selectAllShortByRoleId(roleId);
        return users;
    }
}
