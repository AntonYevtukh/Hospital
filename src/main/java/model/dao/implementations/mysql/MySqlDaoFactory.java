package model.dao.implementations.mysql;

import model.dao.interfaces.*;

public class MySqlDaoFactory implements DaoFactory {

    private static MySqlDaoFactory instance;

    public static MySqlDaoFactory getInstance() {
        if (instance == null) {
            synchronized (MySqlDaoFactory.class) {
                if (instance == null)
                    instance = new MySqlDaoFactory();
            }
        }
        return instance;
    }

    private MySqlDaoFactory() {

    }

    @Override
    public AssignmentDao createAssignmentDao() {
        return MySqlAssignmentDao.getInstance();
    }

    @Override
    public AssignmentTypeDao createAssignmentTypeDao() {
        return MySqlAssignmentTypeDao.getInstance();
    }

    @Override
    public ExaminationDao createExaminationDao() {
        return MySqlExaminationDao.getInstance();
    }

    @Override
    public RoleDao createRoleDao() {
        return MySqlRoleDao.getInstance();
    }

    @Override
    public UserDao createUserDao() {
        return MySqlUserDao.getInstance();
    }

    @Override
    public PhotoDao createPhotoDao() {
        return MySqlPhotoDao.getInstance();
    }

    @Override
    public HospitalizationDao createHospitalizationDao() {
        return MySqlHospitalizationDao.getInstance();
    }

    @Override
    public DiagnoseDao createDiagnoseDao() {
        return MySqlDiagnoseDao.getInstance();
    }
}
