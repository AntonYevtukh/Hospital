package tags;

import model.entities.Diagnose;
import model.entities.Examination;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.StringJoiner;

public class ExaminationDiagnosesTag extends SimpleTagSupport {

    private Examination examination;

    public ExaminationDiagnosesTag() {
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    @Override
    public void doTag() throws JspException, IOException {
        StringJoiner joiner = new StringJoiner(", ");
        examination.getDiagnoses().forEach((Diagnose diagnose) -> joiner.add(diagnose.getCode() + " " + diagnose.getName()));
        getJspContext().getOut().write(joiner.toString());
    }
}
