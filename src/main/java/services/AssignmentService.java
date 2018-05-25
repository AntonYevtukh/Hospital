package services;

import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.*;
import model.entities.Assignment;
import utils.LongLimit;
import utils.PageContent;

import java.util.List;

public class AssignmentService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();
    private static AssignmentDao assignmentDao = daoFactory.createAssignmentDao();
    private static AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
    private static UserDao userDao = daoFactory.createUserDao();
    private static RoleDao roleDao = daoFactory.createRoleDao();

    private AssignmentService() {

    }

    public static long addAssignment(Assignment assignment) {
        try {
            long assignmentId = assignmentDao.insert(assignment);
            return assignmentId;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateAssignment(Assignment assignment) {
        try {
            assignmentDao.update(assignment);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteAssignment(long assignmentId) {
        try {
            assignmentDao.delete(assignmentId);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static PageContent<Assignment> getAssignmentsForPage(int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Assignment> content = assignmentDao.selectAllInRange(longLimit);
        content.forEach(AssignmentService::populateAssignment);
        long countOfAssignments = assignmentDao.selectCountOfAssignments();
        int totalPages = (int)((countOfAssignments / itemsPerPage) +
                (countOfAssignments % itemsPerPage == 0 ? 0 : 1));
        PageContent<Assignment> assignmentPageContent = new PageContent<>();
        assignmentPageContent.setContent(content);
        assignmentPageContent.setPage(page);
        assignmentPageContent.setTotalPages(totalPages);
        return assignmentPageContent;
    }

    public static PageContent<Assignment> getAssignmentsForPageByPatientId(long patientId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Assignment> content = assignmentDao.selectAssignmentsByPatientIdInRange(patientId, longLimit);
        content.forEach(AssignmentService::populateAssignment);
        long countOfAssignmentsWithPatientId = assignmentDao.selectCountOfAssignmentsWithPatientId(patientId);
        int totalPages = (int)((countOfAssignmentsWithPatientId / itemsPerPage) + 
                (countOfAssignmentsWithPatientId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Assignment> assignmentPageContent = new PageContent<>();
        assignmentPageContent.setContent(content);
        assignmentPageContent.setPage(page);
        assignmentPageContent.setTotalPages(totalPages);
        return assignmentPageContent;
    }

    public static PageContent<Assignment> getAssignmentsForPageByDoctorId(long doctorId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Assignment> content = assignmentDao.selectAssignmentsByDoctorIdInRange(doctorId, longLimit);
        content.forEach(AssignmentService::populateAssignment);
        long countOfAssignmentsWithDoctorId = assignmentDao.selectCountOfAssignmentsWithDoctorId(doctorId);
        int totalPages = (int)((countOfAssignmentsWithDoctorId / itemsPerPage) +
                (countOfAssignmentsWithDoctorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Assignment> assignmentPageContent = new PageContent<>();
        assignmentPageContent.setContent(content);
        assignmentPageContent.setPage(page);
        assignmentPageContent.setTotalPages(totalPages);
        return assignmentPageContent;
    }

    public static PageContent<Assignment> getAssignmentsForPageByExecutorId(long executorId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Assignment> content = assignmentDao.selectAssignmentsByExecutorIdInRange(executorId, longLimit);
        content.forEach(AssignmentService::populateAssignment);
        long countOfAssignmentsWithExecutorId = assignmentDao.selectCountOfAssignmentsWithExecutorId(executorId);
        int totalPages = (int)((countOfAssignmentsWithExecutorId / itemsPerPage) +
                (countOfAssignmentsWithExecutorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Assignment> assignmentPageContent = new PageContent<>();
        assignmentPageContent.setContent(content);
        assignmentPageContent.setPage(page);
        assignmentPageContent.setTotalPages(totalPages);
        return assignmentPageContent;
    }

    public static List<Assignment> getAllAssignments() {
        List<Assignment> assignments = assignmentDao.selectAll();
        return assignments;
    }

    private static void populateAssignment(Assignment assignment) {
        assignment.setPatient(userDao.selectShortById(assignment.getPatient().getId()));
        assignment.setDoctor(userDao.selectShortById(assignment.getDoctor().getId()));
        assignment.setExecutor(userDao.selectShortById(assignment.getExecutor().getId()));
        assignment.setAssignmentType(assignmentTypeDao.selectById(assignment.getAssignmentType().getId()));
    }
}
