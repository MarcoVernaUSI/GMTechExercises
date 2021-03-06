package com.app.seminar.controller;

import static com.app.seminar.model.Student.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.Context;
import com.app.controller.Controller;
import com.app.seminar.model.EntityModel;
import com.app.seminar.model.Student;
import com.app.seminar.view.FeedBack;
import com.app.seminar.view.Form;
import com.app.seminar.view.Html;
import com.app.seminar.view.Layout;
import com.app.seminar.view.ViewUtil;

public class StudentCreationController implements Controller {
    
    private final String _path = "/student/create";

    @Override
    public boolean handles(String route) {
        return _path.equals(route);
    }

    @Override
    public void execute(Context context) throws Exception {
        context.response().setContentType("text/html");
        context.response().setCharacterEncoding("UTF-8");
        
        FeedBack feedBack = new FeedBack();
        if (context.post()) {
            EntityModel entityModel = new EntityModel(Student.class, Student.rules(), context.requestMap());
            if (new ControllerUtil().saveStudent(context, entityModel, "/student")) {
                return;
            } else {
                feedBack = new ViewUtil().feedback(entityModel, context);
            }
        }
        viewForm(context, feedBack);
    }

    private void viewForm(Context context, FeedBack feedBack) throws IOException {
        List<String> components = Arrays.asList(FIRST_NAME, LAST_NAME, ID);
        Html form = new Form(feedBack,"create", components, "id");
        context.response().getWriter().write(new Layout("create student", form).build().render());
    }
}
