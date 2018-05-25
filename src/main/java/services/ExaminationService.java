package services;

import enums.HospitalizationRelation;
import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.*;
import model.database.TransactionManager;
import model.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LongLimit;
import utils.PageContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExaminationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExaminationService.class);
    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();
    private static ExaminationDao examinationDao = daoFactory.createExaminationDao();
    private static DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
    private static UserDao userDao = daoFactory.createUserDao();
    private static AssignmentDao assignmentDao = daoFactory.createAssignmentDao();
    private static AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
    private static HospitalizationDao hospitalizationDao = daoFactory.createHospitalizationDao();
    private static RoleDao roleDao = daoFactory.createRoleDao();

    private ExaminationService() {

    }

    public static long addExamination(Examination examination) {
        try {
            TransactionManager.beginTransaction();
            Hospitalization hospitalization = new Hospitalization();
            long patientId = examination.getPatient().getId();
            long currentPatientHospitalizationId = hospitalizationDao.getIdOfCurrentHospitalizationByPatientId(patientId);
            if (examination.getHospitalizationRelation() == HospitalizationRelation.INITIAL) {
                hospitalization.setId(hospitalizationDao.insert(hospitalization));
                userDao.updateHospitalizedStatus(true, patientId);
            } else if (examination.getHospitalizationRelation() == HospitalizationRelation.DISCHARGE) {
                hospitalization.setId(currentPatientHospitalizationId);
                userDao.updateHospitalizedStatus(false, patientId);
            } else if (examination.getHospitalizationRelation() == HospitalizationRelation.INTERMEDIATE) {
                hospitalization.setId(currentPatientHospitalizationId);
            }
            examination.setHospitalization(hospitalization);
            long examinationId = examinationDao.insert(examination);
            TransactionManager.commitTransaction();
            return examinationId;
        } catch (UnknownSqlException e) {
            TransactionManager.rollbackTransaction();
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
            TransactionManager.beginTransaction();
            Examination examination = examinationDao.selectById(examinationId);
            if (examination.getHospitalizationRelation() == HospitalizationRelation.SINGLE ||
                    examination.getHospitalizationRelation() == HospitalizationRelation.INTERMEDIATE) {
                examinationDao.delete(examinationId);
            } else
                throw new ErrorMessageKeysContainedException(List.of("examination.undeletable"));
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            TransactionManager.rollbackTransaction();
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
        long countOfExaminations = examinationDao.selectCountOfExaminations();
        int totalPages = (int)((countOfExaminations / itemsPerPage) +
                (countOfExaminations % itemsPerPage == 0 ? 0 : 1));
        PageContent<Examination> examinationPageContent = new PageContent<>();
        examinationPageContent.setContent(content);
        examinationPageContent.setPage(page);
        examinationPageContent.setTotalPages(totalPages);
        return examinationPageContent;
    }

    public static PageContent<Examination> getIntermediateExaminationsForPageByHospitalizationId(
            long hospitalizationId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Examination> content = examinationDao.selectIntermediateExaminationsByHospitalizationIdInRange(
                hospitalizationId, longLimit);
        content.forEach(ExaminationService::fillExamination);
        long countOfIntermediateExaminationsWithHospitalizationId =
                examinationDao.selectCountOfIntermediateExaminationsWithHospitalizationId(hospitalizationId);
        int totalPages = (int)((countOfIntermediateExaminationsWithHospitalizationId / itemsPerPage) +
                (countOfIntermediateExaminationsWithHospitalizationId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Examination> examinationPageContent = new PageContent<>();
        examinationPageContent.setContent(content);
        examinationPageContent.setPage(page);
        examinationPageContent.setTotalPages(totalPages);
        return examinationPageContent;
    }

    public static Map<String, Object> getDropDownsData() {
        TransactionManager.beginTransaction();
        Map<String, Object> outerMap = new HashMap<>();
        try {

            List<Role> roles = roleDao.selectAll();
            List<AssignmentType> assignmentTypes = assignmentTypeDao.selectAll();
            Map<Long, List<Role>> assignmentTypeIdsToRoleMap;
            Map<Long, List<User>> roleIdsToExecutorMap;
            assignmentTypeIdsToRoleMap = assignmentTypeDao.selectAll().stream().collect(Collectors.toMap(
                    (AssignmentType assignmentType) -> assignmentType.getId(),
                    (AssignmentType assignmentType) -> roleDao.selectByAssignmentTypeId(assignmentType.getId())));
            roleIdsToExecutorMap = roleDao.selectAll().stream().collect(Collectors.toMap(
                    (Role role) -> role.getId(),
                    (Role role) -> userDao.selectAllShortByRoleIdAndHospitalizationStatus(role.getId(), false)));
            System.out.println("Map: " + roles);
            outerMap.put("roles", roles);
            outerMap.put("assignmentTypes", assignmentTypes);
            outerMap.put("assignmentTypeIdsToRoleMap", assignmentTypeIdsToRoleMap);
            outerMap.put("roleIdsToExecutorMap", roleIdsToExecutorMap);
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            LOGGER.error("Error fetching drop-down data: ", e);
            TransactionManager.rollbackTransaction();
        }
        return outerMap;
    }

    static void fillExamination(Examination examination) {
        examination.setPatient(userDao.selectById(examination.getPatient().getId()));
        examination.setDoctor(userDao.selectById(examination.getDoctor().getId()));
        examination.setDiagnoses(diagnoseDao.selectByExaminationId(examination.getId()));
        if (examination.getHospitalization() != null) {
            examination.setHospitalization(hospitalizationDao.selectById(examination.getHospitalization().getId()));
        }
    }
}
