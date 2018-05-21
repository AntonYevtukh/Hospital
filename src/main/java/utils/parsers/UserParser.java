package utils.parsers;

import enums.Gender;
import model.entities.Photo;
import model.entities.Role;
import model.entities.User;
import utils.DateParser;
import utils.PartConverter;
import utils.SessionRequestContent;

import javax.servlet.http.Part;
import java.util.*;
import java.util.stream.Collectors;

public class UserParser {

    public static User parseUser(SessionRequestContent sessionRequestContent) {
        User user = new User();
        try {
            user.setId(Long.parseLong(sessionRequestContent.getSingleRequestParameter("id")));
        } catch (NullPointerException | NumberFormatException e) {

        }
        user.setLogin(sessionRequestContent.getSingleRequestParameter("login"));
        user.setPassword(sessionRequestContent.getSingleRequestParameter("password"));
        user.setFirstName(sessionRequestContent.getSingleRequestParameter("first_name"));
        user.setLastName(sessionRequestContent.getSingleRequestParameter("last_name"));
        user.setMiddleName(sessionRequestContent.getSingleRequestParameter("middle_name"));
        user.setGender(Gender.getGenderByCode(Integer.parseInt(sessionRequestContent.getSingleRequestParameter("gender"))));
        user.setPassportNumber(sessionRequestContent.getSingleRequestParameter("passport_number"));
        user.setPhone(sessionRequestContent.getSingleRequestParameter("phone"));
        user.setAddress(sessionRequestContent.getSingleRequestParameter("address").trim());
        user.setEmail(sessionRequestContent.getSingleRequestParameter("email"));
        user.setLanguage(sessionRequestContent.getSingleRequestParameter("language"));
        Locale userLocale = new Locale(user.getLanguage());
        String dateString = sessionRequestContent.getSingleRequestParameter("date_of_birth");
        user.setDateOfBirth(DateParser.parseDate(dateString, userLocale, "date.regular"));

        try {
            user.setItemsPerPage(Integer.parseInt(sessionRequestContent.getSingleRequestParameter("items")));
        } catch (NumberFormatException e) {
            user.setItemsPerPage(0);
        }
        List<Long> roleIds;
        String[] roleIdsString = sessionRequestContent.getRequestParameter("role");
        if (roleIdsString != null) {
            roleIds = Arrays.stream(sessionRequestContent.getRequestParameter("role")).
                    map((String idString) -> Long.parseLong(idString)).collect(Collectors.toList());
        } else {
            roleIds = new ArrayList<>(0);
        }
        Map<Long, Role> roleMap = new HashMap<>();
        for (long roleId : roleIds) {
            roleMap.put(roleId, null);
        }
        user.setRoleMap(roleMap);
        long photoId;
        try {
            photoId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("photo_id"));
        } catch (NullPointerException | NumberFormatException e) {
            photoId = 0;
        }
        Part part = sessionRequestContent.getRequestPart("photo");
        byte[] photoContent = PartConverter.convertPartToByteArray(part);
        Photo photo = new Photo(photoId);
        if (photoContent != null && photoContent.length != 0) {
            photo.setContent(photoContent);
            //photo.setName(part.getSubmittedFileName());
        }
        user.setPhoto(photo);
        return user;
    }
}
