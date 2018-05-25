package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Hospitalization;
import utils.LongLimit;

import java.util.List;

public interface HospitalizationDao extends GenericDao<Hospitalization> {
    List<Hospitalization> selectAllInRange(LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfHospitalizations() throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<Hospitalization> selectHospitalizationsByPatientIdInRange(long patientId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfHospitalizationsWithPatientId(long patientId) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<Hospitalization> selectHospitalizationsByAcceptedDoctorIdInRange(long doctorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfHospitalizationsWithAcceptedDoctorId(long doctorId) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    List<Hospitalization> selectHospitalizationsByDischargedDoctorIdInRange(long doctorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long selectCountOfHospitalizationsWithDischargedDoctorId(long doctorId) throws UnknownSqlException, ErrorMessageKeysContainedException;;

    long getIdOfCurrentHospitalizationByPatientId(long patientId);
}
