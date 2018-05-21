package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import model.dao.interfaces.AssignmentDao;
import model.dao.interfaces.GenericDaoSupport;
import model.entities.Assignment;
import model.entities.AssignmentType;
import model.entities.Examination;
import model.entities.User;
import utils.LongLimit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlAssignmentDao extends GenericDaoSupport<Assignment> implements AssignmentDao {

    private static MySqlAssignmentDao instance;
    private static final String INSERT_TEMPLATE =
            "INSERT INTO assignment(assignment_type_id, examination_id, executor_id, description, start_date, end_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TEMPLATE =
            "UPDATE assignment SET assignment_type_id = ?, examination_id = ?, executor_id = ?, description = ?, start_date = ?, end_date = ? " +
                    "WHERE id = ";

    public static MySqlAssignmentDao getInstance() {
        if (instance == null) {
            synchronized (MySqlAssignmentDao.class) {
                if (instance == null)
                    instance = new MySqlAssignmentDao();
            }
        }
        return instance;
    }

    private MySqlAssignmentDao() {

    }

    @Override
    public long insert(Assignment assignment) {
        return insertEntity(assignment, INSERT_TEMPLATE);
    }

    @Override
    public void update(Assignment assignment) {
        updateEntity(assignment, UPDATE_TEMPLATE + assignment.getId());
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM assignment WHERE id = " + id);
    }

    @Override
    public List<Assignment> selectAll() {
        return selectAllInRange(new LongLimit(0L, Long.MAX_VALUE));
    }

    @Override
    public List<Assignment> selectAllInRange(LongLimit longLimit) {
        return selectEntities("SELECT * FROM assignment_full ORDER BY start_date DESC LIMIT ?, ?",
                longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfAssignments() {
        return selectCountOfEntities("SELECT count(*) FROM assignment_full");
    }

    @Override
    public List<Assignment> selectAssignmentsByPatientIdInRange(long patientId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM assignment_full WHERE patient_id = ? " +
                        "ORDER BY start_date DESC LIMIT ?, ?",
                patientId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfAssignmentsWithPatientId(long patientId) {
        return selectCountOfEntities("SELECT count(*) FROM assignment_full WHERE patient_id = ?", patientId);
    }

    @Override
    public List<Assignment> selectAssignmentsByDoctorIdInRange(long doctorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM assignment_full WHERE doctor_id = ? " +
                        "ORDER BY start_date DESC LIMIT ?, ?",
                doctorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfAssignmentsWithDoctorId(long doctorId) {
        return selectCountOfEntities("SELECT count(*) FROM assignment_full WHERE doctor_id = ? ", doctorId);
    }

    @Override
    public List<Assignment> selectAssignmentsByExecutorIdInRange(long executorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM assignment_full WHERE executor_id = ? " +
                        "ORDER BY start_date DESC LIMIT ?, ?",
                executorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfAssignmentsWithExecutorId(long executorId) {
        return selectCountOfEntities("SELECT count(*) FROM assignment_full WHERE executor_id = ? ", executorId);
    }

    @Override
    public Assignment selectById(long id) {
        return selectEntity("SELECT * FROM assignment WHERE id = ?", id);
    }

    @Override
    public List<Assignment> selectAllAssignmentsByExamination(long examinationId) {
        return selectEntities("SELECT * FROM assignment_full WHERE examination_id = ? " +
                        "ORDER BY start_date DESC", examinationId);
    }

    @Override
    protected Assignment getSingleResult(ResultSet resultSet) {
        try {
            Assignment assignment = new Assignment();
            assignment.setId(resultSet.getLong("id"));
            assignment.setAssignmentType(new AssignmentType(resultSet.getLong("assignment_type_id")));
            assignment.setPatient(new User(resultSet.getLong("patient_id")));
            assignment.setDoctor(new User(resultSet.getLong("doctor_id")));
            assignment.setExecutor(new User(resultSet.getLong("executor_id")));
            assignment.setDescription(resultSet.getString("description"));
            assignment.setStartDate(resultSet.getDate("start_date"));
            assignment.setEndDate(resultSet.getDate("end_date"));
            assignment.setExamination(new Examination(resultSet.getLong("examination_id")));
            return assignment;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Assignment assignment) {
        try {
            preparedStatement.setLong(1, assignment.getAssignmentType().getId());
            preparedStatement.setLong(2, assignment.getExamination().getId());
            preparedStatement.setLong(3, assignment.getExecutor().getId());
            preparedStatement.setString(4, assignment.getDescription());
            preparedStatement.setDate(5, assignment.getStartDate());
            preparedStatement.setDate(6, assignment.getEndDate());
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }
}
