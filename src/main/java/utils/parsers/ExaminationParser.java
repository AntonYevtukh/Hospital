package utils.parsers;

import model.entities.Diagnose;
import model.entities.Examination;
import model.entities.User;
import utils.SessionRequestContent;
import utils.json.JsonSerializer;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExaminationParser {

    public static Examination parseExamination(SessionRequestContent sessionRequestContent) {
        Examination examination = new Examination();
        if ("application/json".equals(sessionRequestContent.getRequestContentType())) {
            examination = JsonSerializer.deserialize(Examination.class,
                    sessionRequestContent.getRequestBodyString());
        } else {
            try {
                examination.setId(Long.parseLong(sessionRequestContent.getSingleRequestParameter("id")));
            } catch (NullPointerException | NumberFormatException e) {

            }
            examination.setComment(sessionRequestContent.getSingleRequestParameter("comment"));
            long patientId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("patient"));
            examination.setPatient(new User(patientId));
            long doctorId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("doctor"));
            examination.setDoctor(new User(doctorId));
            examination.setDate(new Date(new java.util.Date().getTime()));
            List<Diagnose> diagnoses = Arrays.stream(sessionRequestContent.getRequestParameter("diagnose")).
                    map((String idsString) -> new Diagnose(Long.parseLong(idsString))).collect(Collectors.toList());
            examination.setDiagnoses(diagnoses);
            System.out.println(examination);
        }
        return examination;
    }
}
