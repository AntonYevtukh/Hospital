package validation;

import model.entities.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoValidator implements EntityValidator<Photo> {

    private static PhotoValidator instance;

    public static PhotoValidator getInstance() {
        if (instance == null) {
            synchronized (PhotoValidator.class) {
                if (instance == null)
                    instance = new PhotoValidator();
            }
        }
        return instance;
    }

    private PhotoValidator() {

    }

    @Override
    public List<String> validate(Photo photo) {
        List<String> errorMessageKeys = new ArrayList<>(1);
        if (photo.getName() == null || photo.getName().length() > 50) {
            errorMessageKeys.add("photo.name");
        }
        return errorMessageKeys;
    }
}
