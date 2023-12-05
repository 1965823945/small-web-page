package lab7.contr;

import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import lab7.dao.SubjectsDAO;
import lab7.entity.Subject;

public class SubjectController implements IController {
	@Override
	public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {
		WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
		ctx.setVariable("today", Calendar.getInstance());

		SubjectsDAO subjectsDAO = SubjectsDAO.getInstance();
		List<Subject> subjects = subjectsDAO.loadAllSubjects(0);

		ctx.setVariable("subjects", subjects);

		templateEngine.process("subject/list", ctx, writer);

	}
}
