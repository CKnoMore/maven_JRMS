package com.cxy.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;


public class HomeController extends Controller {

	@ActionKey("/")
	public void index() {
		Pattern regex = Pattern
				.compile(
						"(iemobile|iphone|ipod|android|nokia|sonyericsson|blackberry|samsung|sec\\-|windows ce|motorola|mot\\-|up.b|midp\\-)",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher regexMatcher = regex.matcher(getRequest().getHeader(
				"user-agent"));
		if (regexMatcher.find())
			redirect("/mob");
		else
			render("/index.html");
	}

	@ActionKey("/image")
	public void image() {
		render(new CaptchaRender("key"));
	}

	@ActionKey("/mob")
	public void mob() {
		render("/mob/index.html");
	}
}
