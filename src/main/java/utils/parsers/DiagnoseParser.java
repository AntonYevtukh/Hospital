package utils.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import enums.Gender;
import model.entities.Diagnose;
import model.entities.Photo;
import model.entities.Role;
import model.entities.User;
import utils.DateParser;
import utils.PartConverter;
import utils.SessionRequestContent;
import utils.json.JsonSerializer;

import javax.servlet.http.Part;
import java.util.*;
import java.util.stream.Collectors;

public class DiagnoseParser {

    public static Diagnose parseDiagnose(SessionRequestContent sessionRequestContent) {
        Diagnose diagnose = new Diagnose();
        if ("application/json".equals(sessionRequestContent.getRequestContentType())) {
            diagnose = JsonSerializer.deserialize(Diagnose.class,
                    sessionRequestContent.getRequestBodyString());
        } else {
            try {
                diagnose.setId(Long.parseLong(sessionRequestContent.getSingleRequestParameter("id")));
            } catch (NullPointerException | NumberFormatException e) {

            }
            diagnose.setCode(sessionRequestContent.getSingleRequestParameter("code"));
            diagnose.setName(sessionRequestContent.getSingleRequestParameter("name"));
            diagnose.setDescription(sessionRequestContent.getSingleRequestParameter("description"));
        }
        return diagnose;
    }
}
