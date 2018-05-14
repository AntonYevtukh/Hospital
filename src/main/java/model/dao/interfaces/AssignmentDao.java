package model.dao.interfaces;

import exceptions.ErrorMessageKeysContainedException;
import exceptions.UnknownSqlException;
import model.entities.Assignment;
import utils.LongLimit;

import java.util.List;

public interface AssignmentDao extends GenericDao<Assignment> {
    List<Assignment> selectAllInRange(LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    long selectCountOfAssignments() throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<Assignment> selectAssignmentsByPatientIdInRange(long patientId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    long selectCountOfAssignmentsWithPatientId(long patientId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<Assignment> selectAssignmentsByDoctorIdInRange(long doctorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    long selectCountOfAssignmentsWithDoctorId(long doctorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<Assignment> selectAssignmentsByExecutorIdInRange(long executorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    long selectCountOfAssignmentsWithExecutorId(long executorId, LongLimit longLimit) throws UnknownSqlException, ErrorMessageKeysContainedException;

    List<Assignment> selectAllAssignmentsByExamination(long examinationId) throws UnknownSqlException, ErrorMessageKeysContainedException;
}
