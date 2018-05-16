package model.entities;

import validation.RoleValidator;
import validation.ValidatedEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.List;

@ValidatedEntity(validatorClass = RoleValidator.class)
public class Role implements Entity {
    private long id;
    private String name;
    private List<AssignmentType> allowedAssignmentTypes;

    public Role() {
    }

    public Role(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<AssignmentType> getAllowedAssignmentTypes() {
        return allowedAssignmentTypes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllowedAssignmentTypes(List<AssignmentType> allowedAssignmentTypes) {
        this.allowedAssignmentTypes = allowedAssignmentTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, allowedAssignmentTypes);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", allowedAssignmentTypes=" + allowedAssignmentTypes +
                '}';
    }
}
