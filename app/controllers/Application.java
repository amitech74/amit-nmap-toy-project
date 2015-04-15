package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return play.mvc.Controller.ok(index.render("Welcome to Nmap Scan"));
    }

}
