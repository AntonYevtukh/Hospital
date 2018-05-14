package model.entities;

import validation.HospitalizationValidator;
import validation.ValidatedEntity;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@ValidatedEntity(validatorClass = HospitalizationValidator.class)
public class Hospitalization implements Entity {

    private long id;
    private User patient;
    private User acceptedDoctor;
    private User dischargedDoctor;
    private String comment;
    private Date startDate;
    private Date endDate;
    private List<Examination> examinations;

    public Hospitalization() {
    }

    public Hospitalization(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public User getPatient() {
        return patient;
    }

    public User getAcceptedDoctor() {
        return acceptedDoctor;
    }

    public User getDischargedDoctor() {
        return dischargedDoctor;
    }

    public String getComment() {
        return comment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<Examination> getExaminations() {
        return examinations;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public void setAcceptedDoctor(User acceptedDoctor) {
        this.acceptedDoctor = acceptedDoctor;
    }

    public void setDischargedDoctor(User dischargedDoctor) {
        this.dischargedDoctor = dischargedDoctor;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setExaminations(List<Examination> examinations) {
        this.examinations = examinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hospitalization that = (Hospitalization) o;
        return id == that.id &&
                Objects.equals(patient, that.patient) &&
                Objects.equals(acceptedDoctor, that.acceptedDoctor) &&
                Objects.equals(dischargedDoctor, that.dischargedDoctor) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient, acceptedDoctor, dischargedDoctor, comment, startDate, endDate);
    }
}
