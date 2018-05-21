package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import model.dao.interfaces.DiagnoseDao;
import model.dao.interfaces.GenericDaoSupport;
import model.entities.Diagnose;
import utils.LongLimit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlDiagnoseDao extends GenericDaoSupport<Diagnose> implements DiagnoseDao {

    private static MySqlDiagnoseDao instance;

    public static MySqlDiagnoseDao getInstance() {
        if (instance == null) {
            synchronized (MySqlDiagnoseDao.class) {
                if (instance == null)
                    instance = new MySqlDiagnoseDao();
            }
        }
        return instance;
    }

    @Override
    public long insert(Diagnose diagnose) {
        long diagnoseId = insertEntity(diagnose, "INSERT INTO diagnose (code, name, description) VALUES(?, ?, ?)");
        diagnose.setId(diagnoseId);
        return diagnoseId;
    }

    @Override
    public void update(Diagnose diagnose) {
        updateEntity(diagnose, "UPDATE diagnose SET code = ?, name = ?, description = ? WHERE id = " + diagnose.getId());
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM diagnose WHERE id = " + id);
    }

    @Override
    public List<Diagnose> selectAll() {
        return selectInRange(new LongLimit(0L, Long.MAX_VALUE));
    }

    @Override
    public long selectCountOfDiagnoses() {
        return selectCountOfEntities("SELECT count(*) FROM diagnose");
    }

    @Override
    public List<Diagnose> selectInRange(LongLimit longLimit) {
        return selectEntities("SELECT * FROM diagnose LIMIT ?, ?", longLimit.getOffset(), longLimit.getSize());
    }

    @Override
    public Diagnose selectById(long id) {
        return selectEntity("SELECT * FROM diagnose WHERE id = " + id);
    }

    @Override
    public List<Diagnose> selectByExaminationId(long examinationId) {
        return selectEntities("SELECT * FROM diagnose WHERE id IN " +
                "(SELECT diagnose_id FROM Examination_To_Diagnose WHERE examination_id = ?)", examinationId);
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Diagnose diagnose) {
        try {
            preparedStatement.setString(1, diagnose.getCode());
            preparedStatement.setString(2, diagnose.getName());
            preparedStatement.setString(3, diagnose.getDescription());
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }

    @Override
    protected Diagnose getSingleResult(ResultSet resultSet) {
        try {
            Diagnose diagnose = new Diagnose();
            diagnose.setId(resultSet.getLong("id"));
            diagnose.setCode(resultSet.getString("code"));
            diagnose.setName(resultSet.getString("name"));
            diagnose.setDescription(resultSet.getString("description"));
            return diagnose;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }
}
