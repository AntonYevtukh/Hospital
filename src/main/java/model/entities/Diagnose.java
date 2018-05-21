package model.entities;


import validation.DiagnoseValidator;
import validation.ValidatedEntity;

import java.util.Objects;

/**
 * Entity, that represents a diagnose accordingly to the ICD-10
 */
@ValidatedEntity(validatorClass = DiagnoseValidator.class)
public class Diagnose implements Entity {

    private long id;
    private String code;
    private String name;
    private String description;

    public Diagnose() {
    }

    public Diagnose(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diagnose diagnose = (Diagnose) o;
        return id == diagnose.id &&
                Objects.equals(code, diagnose.code) &&
                Objects.equals(name, diagnose.name) &&
                Objects.equals(description, diagnose.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, code, name, description);
    }

    @Override
    public String toString() {
        return "Diagnose{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
