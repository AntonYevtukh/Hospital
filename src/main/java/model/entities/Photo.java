package model.entities;

import validation.PhotoValidator;
import validation.ValidatedEntity;

import java.util.Objects;

@ValidatedEntity(validatorClass = PhotoValidator.class)
public class Photo implements Entity {

    private long id;
    private String name;
    private byte[] content;

    public Photo() {
    }

    public Photo(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return id == photo.id &&
                Objects.equals(name, photo.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
