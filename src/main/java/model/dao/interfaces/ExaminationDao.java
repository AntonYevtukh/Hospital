package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Examination;
import utils.LongLimit;

import java.util.List;

public interface ExaminationDao extends GenericDao<Examination> {
    List<Examination> selectAllInRange(LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfExaminations() throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<Examination> selectExaminationsByPatientIdInRange(long patientId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfExaminationsWithPatientId(long patientId) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<Examination> selectExaminationsByDoctorIdInRange(long doctorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfExaminationsWithDoctorId(long doctorId) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<Examination> selectIntermediateExaminationsByHospitalizationIdInRange(long doctorId, LongLimit longLimit);

    long selectCountOfIntermediateExaminationsWithHospitalizationId(long hospitalizationId);
}
