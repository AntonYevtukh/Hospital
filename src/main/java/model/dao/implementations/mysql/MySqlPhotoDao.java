package model.dao.implementations.mysql;

import exceptions.EntitySQLParseException;
import exceptions.QueryPreparationException;
import model.dao.interfaces.GenericDaoSupport;
import model.dao.interfaces.PhotoDao;
import model.entities.Photo;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlPhotoDao extends GenericDaoSupport<Photo> implements PhotoDao {

    private static MySqlPhotoDao instance;

    public static MySqlPhotoDao getInstance() {
        if (instance == null) {
            synchronized (MySqlPhotoDao.class) {
                if (instance == null)
                    instance = new MySqlPhotoDao();
            }
        }
        return instance;
    }

    private MySqlPhotoDao() {

    }

    @Override
    public long insert(Photo photo) {
        long photoId = insertEntity(photo, String.format("INSERT INTO photo (name, content) VALUES(?, ?)"));
        photo.setId(photoId);
        return photoId;
    }

    @Override
    public void update(Photo photo) {
        updateEntity(photo, String.format("UPDATE photo SET name = ?, content = ? WHERE id = " + photo.getId()));
    }

    @Override
    public void delete(long id) {
        deleteEntity("DELETE FROM photo WHERE id = " + id);
    }

    @Deprecated
    @Override
    public List<Photo> selectAll() {
        return selectEntities("SELECT * FROM photo");
    }

    @Override
    public Photo selectById(long id) {
        return selectEntity("SELECT * FROM photo WHERE id = ?", id);
    }

    @Override
    protected PreparedStatement setQueryParameters(PreparedStatement preparedStatement, Photo photo) {
        try {
            preparedStatement.setString(1, photo.getName());
            preparedStatement.setBlob(2, new SerialBlob(photo.getContent()));
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QueryPreparationException(e.getMessage());
        }
    }

    @Override
    protected Photo getSingleResult(ResultSet resultSet) {
        try {
            Photo photo = new Photo();
            photo.setId(resultSet.getLong("id"));
            photo.setName(resultSet.getString("name"));
            Blob blob = resultSet.getBlob("content");
            photo.setContent(blob.getBytes(1L, (int)blob.length()));
            return photo;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntitySQLParseException(e.getMessage());
        }
    }
}
