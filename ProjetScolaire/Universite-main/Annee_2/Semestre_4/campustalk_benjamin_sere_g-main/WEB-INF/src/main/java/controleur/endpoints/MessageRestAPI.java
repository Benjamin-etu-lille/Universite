package controleur.endpoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.MessageDAO;
import model.dao.UtilisateurDAO;
import model.dto.Message;

@WebServlet("/messages/*")
public class MessageRestAPI extends HttpServlet {
    MessageDAO dao = new MessageDAO();
    UtilisateurDAO udao = new UtilisateurDAO();
    ObjectMapper objectMapper = new ObjectMapper();
    
    public void init() throws ServletException {
        super.init();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, res);
        } else {
            super.service(req, res);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, udao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String info = req.getPathInfo();
        String idType = req.getParameter("reqType") == null ? "" : req.getParameter("reqType");
        try {
            if (info == null || info.equals("/")) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND); 
                return;
            } else {
                String[] splitURI = info.split("/");
                if (splitURI.length != 2) {
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Integer id = Integer.parseInt(splitURI[1]);
                List<Message> messages = new ArrayList<>();
                switch (idType) {
                    case "messageId":  messages.add(dao.findById(id)); break;
                    // case "userId": messages.add(dao.findMessageWithUser(id)); break;
                    case "filId": messages.addAll(dao.findAllByFil(id)); break;
                }
                if (messages.isEmpty()) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND); 
                    return;
                }
                out.println("{");
                for (int i = 0; i < messages.size(); i++) {
                    out.println(objectMapper.writeValueAsString(messages.get(i)) + ", ");
                }
                out.println("}");
            }
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }
        out.close();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, udao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        Message message = new Message();
        try {
            message = objectMapper.readValue(req.getReader(), Message.class);
            dao.save(message);
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }
        out.println("saved");
        out.close();
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, udao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String info = req.getPathInfo();
        try {
            String[] splitURI = info.split("/");
            if (splitURI.length != 2) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Integer mno = Integer.parseInt(splitURI[1]);
            if (!dao.delete(mno)) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND); 
                return;
            }
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("deleted");
        out.close();
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, udao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        Message message = new Message();
        try {
            message = objectMapper.readValue(req.getReader(), Message.class);
            dao.update(message);
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("put");
        out.close();
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, udao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        Message message = new Message();
        Message messageToPatch;
        try {
            message = objectMapper.readValue(req.getReader(), Message.class);
            messageToPatch = dao.findById(message.getMno());
            dao.update(new Message(message.getMno(),
                message.getContenu() == null ? messageToPatch.getContenu() : message.getContenu(),
                message.getImagePath() == null ? messageToPatch.getImagePath() : message.getImagePath(),
                message.getDateEnvoi() == null ? messageToPatch.getDateEnvoi() : message.getDateEnvoi(),
                message.getUno() == null ? messageToPatch.getUno() : message.getUno(),
                message.getFno() == 0 ? messageToPatch.getFno() : message.getFno()));
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("patched");
        out.close();
    }
}
