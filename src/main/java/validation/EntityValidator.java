package validation;

import model.entities.Entity;

import java.util.List;
import java.util.Map;

public interface EntityValidator<E extends Entity> {

    List<String> validate(E entity);
}
