package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Diagnose;
import utils.LongLimit;

import java.util.List;

public interface DiagnoseDao extends GenericDao<Diagnose> {
    long selectCountOfDiagnoses();

    List<Diagnose> selectInRange(LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;
    List<Diagnose> selectByExaminationId(long examinationId) throws UnknownSqlException, ErrorMessageKeysContainedException;
}
