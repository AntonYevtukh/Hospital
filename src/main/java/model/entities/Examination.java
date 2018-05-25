package model.entities;

import enums.HospitalizationRelation;
import validation.ExaminationValidator;
import validation.ValidatedEntity;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@ValidatedEntity(validatorClass = ExaminationValidator.class)
public class Examination implements Entity {
    private long id;
    private User patient;
    private User doctor;
    private String comment;
    private Date date;
    private Hospitalization hospitalization;
    private List<Assignment> assignments;
    private List<Diagnose> diagnoses;
    private HospitalizationRelation hospitalizationRelation;

    public Examination() {
    }

    public Examination(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public User getDoctor() {
        return doctor;
    }

    public User getPatient() {
        return patient;
    }

    public String getComment() {
        return comment;
    }

    public Hospitalization getHospitalization() {
        return hospitalization;
    }

    public List<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public HospitalizationRelation getHospitalizationRelation() {
        return hospitalizationRelation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }

    public void setDiagnoses(List<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setHospitalizationRelation(HospitalizationRelation hospitalizationRelation) {
        this.hospitalizationRelation = hospitalizationRelation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Examination that = (Examination) o;
        return id == that.id &&
                Objects.equals(date, that.date) &&
                Objects.equals(diagnoses, that.diagnoses) &&
                Objects.equals(assignments, that.assignments) &&
                Objects.equals(doctor, that.doctor) &&
                Objects.equals(patient, that.patient);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, date, diagnoses, assignments, doctor, patient);
    }

    @Override
    public String toString() {
        return "Examination{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", hospitalization=" + hospitalization +
                ", assignments=" + assignments +
                ", diagnoses=" + diagnoses +
                '}';
    }
}
