package services;

import exceptions.EntityNotFoundException;
import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.dao.implementations.mysql.MySqlDaoFactory;
import model.dao.interfaces.DaoFactory;
import model.dao.interfaces.DiagnoseDao;
import model.database.TransactionManager;
import model.entities.Diagnose;
import utils.LongLimit;
import utils.PageContent;

import java.util.List;

public class DiagnoseService {

    private static DaoFactory daoFactory = MySqlDaoFactory.getInstance();

    private DiagnoseService() {

    }

    public static long addDiagnose(Diagnose diagnose) {
        try {
            DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
            long diagnoseId = diagnoseDao.insert(diagnose);
            return diagnoseId;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateDiagnose(Diagnose diagnose) {
        try {
            DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
            TransactionManager.beginTransaction();
            diagnoseDao.update(diagnose);
            TransactionManager.commitTransaction();
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            TransactionManager.rollbackTransaction();
            throw e;
        }
    }

    public static void deleteDiagnose(long diagnoseId) {
        try {
            DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
            diagnoseDao.delete(diagnoseId);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Diagnose getDiagnoseById(long diagnoseId) {
        try {
            DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
            Diagnose diagnose = diagnoseDao.selectById(diagnoseId);
            return diagnose;
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ErrorMessageKeysContainedException(List.of("diagnose.not_found"));
        }
    }

    public static PageContent<Diagnose> getDiagnosesForPage(int page, int itemsPerPage) {
        long offset = (page - 1) * itemsPerPage;
        LongLimit longLimit = new LongLimit(offset, itemsPerPage);
        DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
        List<Diagnose> content = diagnoseDao.selectInRange(longLimit);
        long countOfDiagnoses = diagnoseDao.selectCountOfDiagnoses();
        int totalPages = (int)((countOfDiagnoses / itemsPerPage) + (countOfDiagnoses % itemsPerPage == 0 ? 0 : 1));
        PageContent<Diagnose> diagnosePageContent = new PageContent<>();
        diagnosePageContent.setContent(content);
        diagnosePageContent.setPage(page);
        diagnosePageContent.setTotalPages(totalPages);
        return diagnosePageContent;
    }

    public static List<Diagnose> getAllDiagnoses() {
        DiagnoseDao diagnoseDao = daoFactory.createDiagnoseDao();
        List<Diagnose> diagnoses = diagnoseDao.selectAll();
        return diagnoses;
    }
}
