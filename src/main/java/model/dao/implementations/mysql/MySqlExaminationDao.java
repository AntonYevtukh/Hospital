package model.dao.implementations.mysql;

import enums.HospitalizationRelation;
import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import exceptions.UnknownSqlException;
import model.dao.interfaces.ExaminationDao;
import model.dao.interfaces.GenericDaoSupport;
import model.database.ConnectionProvider;
import model.entities.Diagnose;
import model.entities.Examination;
import model.entities.Hospitalization;
import model.entities.User;
import utils.LongLimit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

public class MySqlExaminationDao extends GenericDaoSupport<Examination>
        implements ExaminationDao {

    private static MySqlExaminationDao instance;
    private static final String INSERT_TEMPLATE =
            "INSERT INTO examination(patient_id, doctor_id, comment, date, hospitalization_id, hospitalization_relation) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TEMPLATE =
            "UPDATE examination SET patient_id = ?, doctor_id = ?, comment = ?, date = ?, hospitalization_id = ?,  " +
                    "hospitalization_relation = ? WHERE id = ";

    public static MySqlExaminationDao getInstance() {
        if (instance == null) {
            synchronized (MySqlExaminationDao.class) {
                if (instance == null)
                    instance = new MySqlExaminationDao();
            }
        }
        return instance;
    }

    private MySqlExaminationDao() {

    }

    @Override
    public long insert(Examination examination) {
        long examinationId = insertEntity(examination, INSERT_TEMPLATE);
        examination.setId(examinationId);
        updateExaminationDiagnoses(examination);
        return examinationId;
    }

    @Override
    public void update(Examination examination) {
        updateEntity(examination, UPDATE_TEMPLATE + examination.getId());
        updateExaminationDiagnoses(examination);
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM examination WHERE id = " + id);
    }

    @Override
    public List<Examination> selectAll() {
        return selectAllInRange(new LongLimit(0L, Long.MAX_VALUE));
    }

    @Override
    public List<Examination> selectAllInRange(LongLimit longLimit) {
        return selectEntities("SELECT * FROM examination ORDER BY date DESC LIMIT ?, ?",
                longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfExaminations() {
        return selectCountOfEntities("SELECT count(*) FROM examination");
    }

    @Override
    public List<Examination> selectExaminationsByPatientIdInRange(long patientId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM examination WHERE patient_id = ? " +
                        "ORDER BY date DESC LIMIT ?, ?",
                patientId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfExaminationsWithPatientId(long patientId) {
        return selectCountOfEntities("SELECT count(*) FROM examination WHERE patient_id = ? ", patientId);
    }

    @Override
    public List<Examination> selectExaminationsByDoctorIdInRange(long doctorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM examination WHERE doctor_id = ? " +
                        "ORDER BY date DESC LIMIT ?, ?",
                doctorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfExaminationsWithDoctorId(long doctorId) {
        return selectCountOfEntities("SELECT count(*) FROM examination WHERE doctor_id = ? ", doctorId);
    }

    @Override
    public List<Examination> selectIntermediateExaminationsByHospitalizationIdInRange(long hospitalizationId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM examination WHERE hospitalization_id = ? " +
                        "AND hospitalization_relation = 2 ORDER BY date DESC LIMIT ?, ?",
                hospitalizationId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfIntermediateExaminationsWithHospitalizationId(long hospitalizationId) {
        return selectCountOfEntities("SELECT count(*) FROM examination WHERE hospitalization_id = ? " +
                "AND hospitalization_relation = 2", hospitalizationId);
    }

    @Override
    public Examination selectById(long id) {
        return selectEntity("SELECT * FROM examination WHERE id = ?", id);
    }

    protected Examination getSingleResult(ResultSet resultSet) {
        try {
            Examination examination = new Examination();
            examination.setId(resultSet.getLong("id"));
            examination.setPatient(new User(resultSet.getLong("patient_id")));
            examination.setDoctor(new User(resultSet.getLong("doctor_id")));
            examination.setComment(resultSet.getString("comment"));
            examination.setDate(resultSet.getDate("date"));
            long hospitalizationId = resultSet.getLong("hospitalization_id");
            examination.setHospitalization(!resultSet.wasNull() ? new Hospitalization(hospitalizationId) : null);
            examination.setHospitalizationRelation(
                    HospitalizationRelation.getValueByOrdinal(resultSet.getInt("hospitalization_relation")));
            return examination;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Examination examination) {
        try {
            preparedStatement.setLong(1, examination.getPatient().getId());
            preparedStatement.setLong(2, examination.getDoctor().getId());
            preparedStatement.setString(3, examination.getComment());
            preparedStatement.setDate(4, examination.getDate());
            if (examination.getHospitalization().getId() != 0)
                preparedStatement.setLong(5, examination.getHospitalization().getId());
            else
                preparedStatement.setObject(5, null);
            preparedStatement.setInt(6, examination.getHospitalizationRelation().ordinal());
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }

    private void updateExaminationDiagnoses(Examination examination) {
        Connection connection = ConnectionProvider.getConnection();
        StringJoiner queryBuilder = new StringJoiner(", ",
                "DELETE FROM Examination_To_Diagnose WHERE examination_id = ?; " +
                        "INSERT INTO Examination_To_Diagnose (examination_id, diagnose_id) VALUES ",
                ""
        );
        examination.getDiagnoses().forEach((Diagnose diagnose) -> queryBuilder.add("(?,?)"));

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryBuilder.toString()
        )) {
            int i = 2;
            preparedStatement.setLong(1, examination.getId());
            for (Diagnose diagnose: examination.getDiagnoses()) {
                preparedStatement.setLong(i++, examination.getId());
                preparedStatement.setLong(i++, diagnose.getId());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnknownSqlException(e.getMessage());
        }
    }
}
