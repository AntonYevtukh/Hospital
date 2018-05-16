package model.entities;

import validation.AssignmentTypeValidator;
import validation.ValidatedEntity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@ValidatedEntity(validatorClass = AssignmentTypeValidator.class)
public class AssignmentType implements Entity {
    private long id;
    private String name;
    private boolean hospitalizationRequired;

    public AssignmentType() {
    }

    public AssignmentType(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHospitalizationRequired() {
        return hospitalizationRequired;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHospitalizationRequired(boolean hospitalizationRequired) {
        this.hospitalizationRequired = hospitalizationRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentType that = (AssignmentType) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "AssignmentType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hospitalizationRequired=" + hospitalizationRequired +
                '}';
    }
}
