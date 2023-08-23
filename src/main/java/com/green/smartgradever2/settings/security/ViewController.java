package com.green.smartgradever2.settings.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;

@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping
    public void getView(HttpServletRequest req, HttpServletResponse res) throws Exception {
        res.setContentType("text/html;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter pw = res.getWriter();
        pw.print("<html>");
        pw.print("<h1>안녕하세요</h1>");
        pw.print("<h1>Hello</h1>");
        pw.print("</html>");
    }
}
