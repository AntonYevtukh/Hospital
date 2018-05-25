package validation;

import model.entities.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public class EntityValidatorFactory {

    @SuppressWarnings("unchecked")
    public static <E extends Entity> EntityValidator<E> getValidatorFor(Class<E> entityClass) {
        if (!entityClass.isAnnotationPresent(ValidatedEntity.class))
            throw new RuntimeException("Validator not found");
        ValidatedEntity validatedEntityAnnotation = entityClass.getAnnotation(ValidatedEntity.class);
        Class<? extends EntityValidator> entityValidatorClass;
        EntityValidator<E> entityValidator;
        entityValidatorClass = validatedEntityAnnotation.validatorClass();
        Class<E> parameterType = (Class<E>) ((ParameterizedType) (entityValidatorClass.getGenericInterfaces()[0])).
                getActualTypeArguments()[0];
        if (!parameterType.equals(entityClass))
            throw new RuntimeException("Wrong validator");
        try {
            try {
                entityValidator =
                        (EntityValidator<E>)(entityValidatorClass.getMethod("getInstance").invoke(null));

            } catch (NoSuchMethodException | InvocationTargetException | ClassCastException e) {
                entityValidator = entityValidatorClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Validator not found");
        }
        return entityValidator;
    }
}
