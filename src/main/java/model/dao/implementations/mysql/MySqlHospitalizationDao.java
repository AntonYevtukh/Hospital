package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import model.dao.interfaces.GenericDaoSupport;
import model.dao.interfaces.HospitalizationDao;
import model.entities.Hospitalization;
import model.entities.User;
import utils.LongLimit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlHospitalizationDao extends GenericDaoSupport<Hospitalization> implements HospitalizationDao {

    private static MySqlHospitalizationDao instance;
    private static final String INSERT_TEMPLATE =
            "INSERT INTO hospitalization(patient_id, accepted_doctor_id, discharged_doctor_id, comment, start_date, end_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TEMPLATE =
            "UPDATE hospitalization SET patient_id = ?, accepted_doctor_id = ?, discharged_doctor_id = ?, comment = ?, " +
                    "start_date = ?, end_date = ? WHERE id = ";

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
        updateEntity(hospitalization, UPDATE_TEMPLATE + hospitalization.getId());
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
        return selectEntities("SELECT * FROM hospitalization LIMIT ?, ? ORDER BY start_date DESC",
                longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizations() {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization");
    }

    @Override
    public List<Hospitalization> selectHospitalizationsByPatientIdInRange(long patientId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization WHERE patient_id = ? " +
                        "LIMIT ?, ? ORDER BY start_date DESC",
                patientId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizationsWithPatientId(long patientId) {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization WHERE patient_id = ? ", patientId);
    }

    @Override
    public List<Hospitalization> selectHospitalizationsByAcceptedDoctorIdInRange(long doctorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization WHERE accepted_doctor_id = ? " +
                        "LIMIT ?, ? ORDER BY start_date DESC",
                doctorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizationsWithAcceptedDoctorId(long doctorId) {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization WHERE accepted_doctor_id = ? ", doctorId);
    }

    @Override
    public List<Hospitalization> selectHospitalizationsByDischargedDoctorIdInRange(long doctorId, LongLimit longLimit) {
        return selectEntities("SELECT * FROM hospitalization WHERE discharged_doctor_id = ? " +
                        "LIMIT ?, ? ORDER BY start_date DESC",
                doctorId, longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public long selectCountOfHospitalizationsWithDischargedDoctorId(long doctorId) {
        return selectCountOfEntities("SELECT count(*) FROM hospitalization WHERE discharged_doctor_id = ? ", doctorId);
    }

    @Override
    public Hospitalization selectById(long id) {
        return selectEntity("SELECT * FROM hospitalization WHERE id = ?", id);
    }

    @Override
    protected Hospitalization getSingleResult(ResultSet resultSet) {
        try {
            Hospitalization hospitalization = new Hospitalization();
            hospitalization.setId(resultSet.getLong("id"));
            hospitalization.setPatient(new User(resultSet.getLong("patient_id")));
            hospitalization.setAcceptedDoctor(new User(resultSet.getLong("accepted_doctor_id")));
            hospitalization.setDischargedDoctor(new User(resultSet.getLong("discharged_doctor_id")));
            hospitalization.setComment(resultSet.getString("comment"));
            hospitalization.setStartDate(resultSet.getDate("start_date"));
            hospitalization.setEndDate(resultSet.getDate("end_date"));
            return hospitalization;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Hospitalization hospitalization) {
        try {
            preparedStatement.setLong(1, hospitalization.getPatient().getId());
            preparedStatement.setLong(2, hospitalization.getAcceptedDoctor().getId());
            preparedStatement.setLong(3, hospitalization.getDischargedDoctor().getId());
            preparedStatement.setString(4, hospitalization.getComment());
            preparedStatement.setDate(5, hospitalization.getStartDate());
            preparedStatement.setDate(6, hospitalization.getEndDate());
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }
}
