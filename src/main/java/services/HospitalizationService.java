package services;

import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.*;
import model.database.TransactionManager;
import model.entities.Examination;
import model.entities.Hospitalization;
import utils.LongLimit;
import utils.PageContent;

import java.util.List;

public class HospitalizationService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();
    private static ExaminationDao examinationDao = daoFactory.createExaminationDao();
    private static DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
    private static UserDao userDao = daoFactory.createUserDao();
    private static AssignmentDao assignmentDao = daoFactory.createAssignmentDao();
    private static AssignmentTypeDao assignmentTypeDao = daoFactory.createAssignmentTypeDao();
    private static HospitalizationDao hospitalizationDao = daoFactory.createHospitalizationDao();

    private HospitalizationService() {

    }

    public static Hospitalization getHospitalizationById(long hospitalizationId) {
        try {
            TransactionManager.beginTransaction();
            Hospitalization hospitalization = hospitalizationDao.selectById(hospitalizationId);
            fillHospitalization(hospitalization);
            Examination initialExamination = examinationDao.selectById(hospitalization.getInitialExamination().getId());
            ExaminationService.fillExamination(initialExamination);
            Examination dischargeExamination = null;
            if (hospitalization.getDischargeExamination().getId() != 0) {
                dischargeExamination = examinationDao.selectById(hospitalization.getDischargeExamination().getId());
                ExaminationService.fillExamination(dischargeExamination);
            }
            hospitalization.setInitialExamination(initialExamination);
            hospitalization.setDischargeExamination(dischargeExamination);
            TransactionManager.commitTransaction();
            return hospitalization;
        } catch (UnknownSqlException e) {
            TransactionManager.rollbackTransaction();
            e.printStackTrace();
            throw e;
        } catch (EntityNotFoundException e) {
            TransactionManager.rollbackTransaction();
            e.printStackTrace();
            throw new ErrorMessageKeysContainedException(List.of("hospitalization.not_found"));
        }
    }

    public static PageContent<Hospitalization> getHospitalizationsForPageByPatientId(long patientId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Hospitalization> content = hospitalizationDao.selectHospitalizationsByPatientIdInRange(patientId, longLimit);
        content.forEach(HospitalizationService::fillHospitalization);
        long countOfHospitalizationsWithPatientId = hospitalizationDao.selectCountOfHospitalizationsWithPatientId(patientId);
        int totalPages = (int)((countOfHospitalizationsWithPatientId / itemsPerPage) + 
                (countOfHospitalizationsWithPatientId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Hospitalization> hospitalizationPageContent = new PageContent<>();
        hospitalizationPageContent.setContent(content);
        hospitalizationPageContent.setPage(page);
        hospitalizationPageContent.setTotalPages(totalPages);
        return hospitalizationPageContent;
    }

    public static PageContent<Hospitalization> getHospitalizationsForPageByAcceptedDoctorId(long acceptedDoctorId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Hospitalization> content = 
                hospitalizationDao.selectHospitalizationsByAcceptedDoctorIdInRange(acceptedDoctorId, longLimit);
        content.forEach(HospitalizationService::fillHospitalization);
        long countOfHospitalizationsWithAcceptedDoctorId = 
                hospitalizationDao.selectCountOfHospitalizationsWithAcceptedDoctorId(acceptedDoctorId);
        int totalPages = (int)((countOfHospitalizationsWithAcceptedDoctorId / itemsPerPage) +
                (countOfHospitalizationsWithAcceptedDoctorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Hospitalization> hospitalizationPageContent = new PageContent<>();
        hospitalizationPageContent.setContent(content);
        hospitalizationPageContent.setPage(page);
        hospitalizationPageContent.setTotalPages(totalPages);
        return hospitalizationPageContent;
    }

    public static PageContent<Hospitalization> getHospitalizationsForPageByDischargedDoctorId(long acceptedDoctorId, int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Hospitalization> content =
                hospitalizationDao.selectHospitalizationsByDischargedDoctorIdInRange(acceptedDoctorId, longLimit);
        content.forEach(HospitalizationService::fillHospitalization);
        long countOfHospitalizationsWithDischargedDoctorId =
                hospitalizationDao.selectCountOfHospitalizationsWithDischargedDoctorId(acceptedDoctorId);
        int totalPages = (int)((countOfHospitalizationsWithDischargedDoctorId / itemsPerPage) +
                (countOfHospitalizationsWithDischargedDoctorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Hospitalization> hospitalizationPageContent = new PageContent<>();
        hospitalizationPageContent.setContent(content);
        hospitalizationPageContent.setPage(page);
        hospitalizationPageContent.setTotalPages(totalPages);
        return hospitalizationPageContent;
    }

    public static PageContent<Hospitalization> getHospitalizationsForPage(int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        List<Hospitalization> content = hospitalizationDao.selectAllInRange(longLimit);
        content.forEach(HospitalizationService::fillHospitalization);
        long countOfHospitalizationsWithDoctorId = hospitalizationDao.selectCountOfHospitalizations();
        int totalPages = (int)((countOfHospitalizationsWithDoctorId / itemsPerPage) +
                (countOfHospitalizationsWithDoctorId % itemsPerPage == 0 ? 0 : 1));
        PageContent<Hospitalization> hospitalizationPageContent = new PageContent<>();
        hospitalizationPageContent.setContent(content);
        hospitalizationPageContent.setPage(page);
        hospitalizationPageContent.setTotalPages(totalPages);
        return hospitalizationPageContent;
    }

    private static void fillHospitalization(Hospitalization hospitalization) {
        hospitalization.setPatient(userDao.selectById(hospitalization.getPatient().getId()));
        hospitalization.setAcceptedDoctor(userDao.selectShortById(hospitalization.getAcceptedDoctor().getId()));
        if (hospitalization.getDischargedDoctor().getId() != 0) {
            hospitalization.setDischargedDoctor(userDao.selectShortById(hospitalization.getDischargedDoctor().getId()));
        } else {
            hospitalization.setDischargedDoctor(null);
        }
    }
}
