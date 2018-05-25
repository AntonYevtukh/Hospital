package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.UnknownSqlException;
import model.dao.interfaces.GenericDaoSupport;
import model.dao.interfaces.HospitalizationDao;
import model.database.ConnectionProvider;
import model.entities.Examination;
import model.entities.Hospitalization;
import model.entities.User;
import utils.LongLimit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlHospitalizationDao extends GenericDaoSupport<Hospitalization> implements HospitalizationDao {

    private static MySqlHospitalizationDao instance;
    private static final String INSERT_TEMPLATE =
            "INSERT INTO hospitalization (id) VALUES (DEFAULT)";

    public static MySqlHospitalizationDao getInstance() {
        if (instance == null) {
            synchronized (MySqlHospitalizationDao.class) {
                if (instance == null)
                    instance = new MySqlHospitalizationDao();
            }
        }
        return instance;
    }

    private MySqlHospitalizationDao() {

    }

    @Override
    public long insert(Hospitalization hospitalization) {
        return insertEntity(hospitalization, INSERT_TEMPLATE);
    }

    @Override
    public void update(Hospitalization hospitalization) {
        throw new UnsupportedOperationException("There is nothing to update in hospitalization");
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM hospitalization WHERE id = " + id);
    }

    @Override
    public List<Hospitalization> selectAll() {
        return selectAllInRange(new LongLimit(0L, Long.MAX_VALUE));
    }

    @Override
    public List<Hospitalization> selectAllInRange(LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization_full ORDER BY start_date DESC LIMIT ?, ?",
                longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizations() {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization_full");
    }

    @Override
    public List<Hospitalization> selectHospitalizationsByPatientIdInRange(long patientId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization_full WHERE patient_id = ? " +
                        "ORDER BY start_date DESC LIMIT ?, ?",
                patientId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizationsWithPatientId(long patientId) {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization_full WHERE patient_id = ? ", patientId);
    }

    @Override
    public List<Hospitalization> selectHospitalizationsByAcceptedDoctorIdInRange(long doctorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization_full WHERE accepted_doctor_id = ? " +
                        "ORDER BY start_date DESC LIMIT ?, ?",
                doctorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizationsWithAcceptedDoctorId(long doctorId) {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization_full WHERE accepted_doctor_id = ? ", doctorId);
    }

    @Override
    public List<Hospitalization> selectHospitalizationsByDischargedDoctorIdInRange(long doctorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization_full WHERE discharged_doctor_id = ? " +
                        "ORDER BY start_date DESC LIMIT ?, ?",
                doctorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizationsWithDischargedDoctorId(long doctorId) {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization_full WHERE discharged_doctor_id = ? ", doctorId);
    }

    @Override
    public Hospitalization selectById(long id) {
        return selectEntity("SELECT * FROM hospitalization_full WHERE id = ?", id);
    }

    @Override
    public long getIdOfCurrentHospitalizationByPatientId(long patientId) {
        Connection connection = ConnectionProvider.getConnection();
        long currentHospitalizationId = 0;
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT id FROM hospitalization_full WHERE end_date IS NULL AND patient_id = ?")) {
            preparedStatement.setLong(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                currentHospitalizationId = resultSet.getLong("id");
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        }
        return currentHospitalizationId;
    }

    @Override
    protected Hospitalization getSingleResult(ResultSet resultSet) {
        try {
            Hospitalization hospitalization = new Hospitalization();
            hospitalization.setId(resultSet.getLong("id"));
            hospitalization.setPatient(new User(resultSet.getLong("patient_id")));
            hospitalization.setAcceptedDoctor(new User(resultSet.getLong("accepted_doctor_id")));
            hospitalization.setDischargedDoctor(new User(resultSet.getLong("discharged_doctor_id")));
            hospitalization.setStartDate(resultSet.getDate("start_date"));
            hospitalization.setEndDate(resultSet.getDate("end_date"));
            hospitalization.setInitialExamination(new Examination(resultSet.getLong("initial_examination_id")));
            hospitalization.setDischargeExamination(new Examination(resultSet.getLong("discharge_examination_id")));
            return hospitalization;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Hospitalization hospitalization) {
        return preparedStatement;
    }
}
