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
    private Date startDate;
    private Date endDate;
    private Examination initialExamination;
    private Examination dischargeExamination;
    private List<Examination> intermediateExaminations;

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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Examination getInitialExamination() {
        return initialExamination;
    }

    public Examination getDischargeExamination() {
        return dischargeExamination;
    }

    public List<Examination> getIntermediateExaminations() {
        return intermediateExaminations;
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setInitialExamination(Examination initialExamination) {
        this.initialExamination = initialExamination;
    }

    public void setDischargeExamination(Examination dischargeExamination) {
        this.dischargeExamination = dischargeExamination;
    }

    public void setIntermediateExaminations(List<Examination> intermediateExaminations) {
        this.intermediateExaminations = intermediateExaminations;
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
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(initialExamination, that.initialExamination) &&
                Objects.equals(dischargeExamination, that.dischargeExamination) &&
                Objects.equals(intermediateExaminations, that.intermediateExaminations);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, patient, acceptedDoctor, dischargedDoctor, startDate, endDate, initialExamination, dischargeExamination, intermediateExaminations);
    }
}
