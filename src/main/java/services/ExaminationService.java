package services;

import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.*;
import model.database.TransactionManager;
import model.entities.*;
import utils.LongLimit;
import utils.PageContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExaminationService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();
    private static ExaminationDao examinationDao = daoFactory.createExaminationDao();
    private static DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
    private static UserDao userDao = daoFactory.createUserDao();
    private static AssignmentDao assignmentDao = daoFactory.createAssignmentDao();
    private static AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();

    private ExaminationService() {

    }

    public static long addExamination(Examination examination) {
        try {
            long examinationId = examinationDao.insert(examination);
            return examinationId;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateExamination(Examination examination) {
        try {
            examinationDao.update(examination);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteExamination(long examinationId) {
        try {
            examinationDao.delete(examinationId);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Examination getExaminationById(long examinationId) {
        try {
            TransactionManager.beginTransaction();
            Examination examination = examinationDao.selectById(examinationId);
            examination.setPatient(userDao.selectById(examination.getPatient().getId()));
            examination.setDoctor(userDao.selectById(examination.getDoctor().getId()));
            examination.setDiagnoses(diagnoseDao.selectByExaminationId(examination.getId()));
            List<Assignment> assignments = assignmentDao.selectAllAssignmentsByExamination(examinationId);
            assignments.forEach((Assignment assignment) -> {
                assignment.setPatient(examination.getPatient());
                assignment.setDoctor(examination.getDoctor());
                assignment.setExecutor(userDao.selectShortById(assignment.getExecutor().getId()));
                assignment.setAssignmentType(assignmentTypeDao.selectById(assignment.getAssignmentType().getId()));
            });
            examination.setAssignments(assignments);
            TransactionManager.commitTransaction();
            return examination;
        } catch (UnknownSqlException e) {
            TransactionManager.rollbackTransaction();
            e.printStackTrace();
            throw e;
        } catch (EntityNotFoundException e) {
            TransactionManager.rollbackTransaction();
            e.printStackTrace();
            throw new ErrorMessageKeysContainedException(List.of("examination.not_found"));
        }
    }

    public static PageContent<Examination> getExaminationsForPageByPatientId(long patientId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Examination> content = examinationDao.selectExaminationsByPatientIdInRange(patientId, longLimit);
        content.forEach(ExaminationService::fillExamination);
        long countOfExaminationsWithPatientId = examinationDao.selectCountOfExaminationsWithPatientId(patientId);
        int totalPages = (int)((countOfExaminationsWithPatientId / itemsPerPage) + 
                (countOfExaminationsWithPatientId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Examination> examinationPageContent = new PageContent<>();
        examinationPageContent.setContent(content);
        examinationPageContent.setPage(page);
        examinationPageContent.setTotalPages(totalPages);
        return examinationPageContent;
    }

    public static PageContent<Examination> getExaminationsForPageByDoctorId(long doctorId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Examination> content = examinationDao.selectExaminationsByDoctorIdInRange(doctorId, longLimit);
        content.forEach(ExaminationService::fillExamination);
        long countOfExaminationsWithDoctorId = examinationDao.selectCountOfExaminationsWithDoctorId(doctorId);
        int totalPages = (int)((countOfExaminationsWithDoctorId / itemsPerPage) +
                (countOfExaminationsWithDoctorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Examination> examinationPageContent = new PageContent<>();
        examinationPageContent.setContent(content);
        examinationPageContent.setPage(page);
        examinationPageContent.setTotalPages(totalPages);
        return examinationPageContent;
    }

    public static PageContent<Examination> getExaminationsForPage(int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Examination> content = examinationDao.selectAllInRange(longLimit);
        content.forEach(ExaminationService::fillExamination);
        long countOfExaminationsWithDoctorId = examinationDao.selectCountOfExaminations();
        int totalPages = (int)((countOfExaminationsWithDoctorId / itemsPerPage) +
                (countOfExaminationsWithDoctorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Examination> examinationPageContent = new PageContent<>();
        examinationPageContent.setContent(content);
        examinationPageContent.setPage(page);
        examinationPageContent.setTotalPages(totalPages);
        return examinationPageContent;
    }

    public static Map<String, Object> getDropDownsData() {
        RoleDao roleDao = daoFactory.createRoleDao();
        AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
        UserDao userDao = daoFactory.createUserDao();
        Map<String, Object> outerMap = new HashMap<>();
        List<Role> roles = roleDao.selectAll();
        List<AssignmentType> assignmentTypes = assignmentTypeDao.selectAll();
        Map<Long, List<Role>> assignmentTypeIdsToRoleMap;
        Map<Long, List<User>> roleIdsToExecutorMap;
        assignmentTypeIdsToRoleMap = assignmentTypeDao.selectAll().stream().collect(Collectors.toMap(
                (AssignmentType assignmentType) -> assignmentType.getId(),
                (AssignmentType assignmentType) -> roleDao.selectByAssignmentTypeId(assignmentType.getId())));
        roleIdsToExecutorMap = roleDao.selectAll().stream().collect(Collectors.toMap(
                (Role role) -> role.getId(),
                (Role role) -> userDao.selectAllShortByRoleId(role.getId())));
        outerMap.put("roles", roles);
        outerMap.put("assignmentTypes", assignmentTypes);
        outerMap.put("assignmentTypeIdsToRoleMap", assignmentTypeIdsToRoleMap);
        outerMap.put("roleIdsToExecutorMap", roleIdsToExecutorMap);
        return outerMap;
    }

    private static void fillExamination(Examination examination) {
        examination.setPatient(userDao.selectById(examination.getPatient().getId()));
        examination.setDoctor(userDao.selectById(examination.getDoctor().getId()));
        examination.setDiagnoses(diagnoseDao.selectByExaminationId(examination.getId()));
    }
}
