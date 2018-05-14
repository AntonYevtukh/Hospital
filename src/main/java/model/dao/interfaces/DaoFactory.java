package model.dao.interfaces;

public interface DaoFactory {

    AssignmentDao createAssignmentDao();
    AssignmentTypeDao createAssignmentTypeDao();
    ExaminationDao createExaminationDao();
    RoleDao createRoleDao();
    UserDao createUserDao();
    PhotoDao createPhotoDao();
    HospitalizationDao createHospitalizationDao();
    DiagnoseDao createDiagnoseDao();
}
