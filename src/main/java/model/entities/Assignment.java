package model.entities;

import validation.AssignmentValidator;
import validation.ValidatedEntity;

import java.sql.Date;
import java.util.Objects;

@ValidatedEntity(validatorClass = AssignmentValidator.class)
public class Assignment implements Entity {
    private long id;
    private AssignmentType assignmentType;
    private String description;
    private User patient;
    private User doctor;
    private User executor;
    private Date startDate;
    private Date endDate;
    private Examination examination;

    public Assignment() {
    }

    public Assignment(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public String getDescription() {
        return description;
    }

    public User getPatient() {
        return patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public User getExecutor() {
        return executor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return id == that.id &&
                Objects.equals(description, that.description) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(patient, that.patient) &&
                Objects.equals(doctor, that.doctor) &&
                Objects.equals(executor, that.executor) &&
                Objects.equals(assignmentType, that.assignmentType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, description, startDate, endDate, patient, doctor, executor, assignmentType);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", assignmentType=" + assignmentType +
                ", description='" + description + '\'' +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", executor=" + executor +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", examination=" + examination +
                '}';
    }
}
