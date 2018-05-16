package services;

import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.DaoFactory;
import model.dao.interfaces.AssignmentTypeDao;
import model.database.TransactionManager;
import model.entities.AssignmentType;
import utils.LongLimit;
import utils.PageContent;

import java.util.List;

public class AssignmentTypeService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();

    private AssignmentTypeService() {

    }

    public static long addAssignmentType(AssignmentType assignmentType) {
        try {
            AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
            long assignmentTypeId = assignmentTypeDao.insert(assignmentType);
            return assignmentTypeId;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteAssignmentType(long assignmentTypeId) {
        try {
            AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
            assignmentTypeDao.delete(assignmentTypeId);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateAssignmentType(AssignmentType assignmentType) {
        try {
            AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
            TransactionManager.beginTransaction();
            assignmentTypeDao.update(assignmentType);
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        }
    }

    public static AssignmentType getAssignmentTypeById(long assignmentTypeId) {
        try {
            AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
            AssignmentType assignmentType = assignmentTypeDao.selectById(assignmentTypeId);
            return assignmentType;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ErrorMessageKeysContainedException(List.of("assignment_type.not_found"));
        }
    }

    public static List<AssignmentType> getAssignmentTypes() {
        try {
            AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
            System.out.println(assignmentTypeDao.selectAll());
            return assignmentTypeDao.selectAll();
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
