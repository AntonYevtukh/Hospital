package utils;

import javax.servlet.http.Part;
import java.io.IOException;

public class PartConverter {

    private PartConverter() {
    }

    public static byte[] convertPartToByteArray(Part part) {
        try {
            return (part != null) ? part.getInputStream().readAllBytes() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
