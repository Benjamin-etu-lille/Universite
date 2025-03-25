package controleur.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.FilDAO;
import model.dao.UtilisateurDAO;
import model.dto.Fil;

@WebServlet("/fils/*")
public class FilRestAPI extends HttpServlet {
    FilDAO dao = new FilDAO();
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
        try {
            if (info == null || info.equals("/")) {
                for (Fil f : dao.findAll()) {
                    out.println(objectMapper.writeValueAsString(f));
                }
            } else {
                String[] splitURI = info.split("/");
                if (splitURI.length != 2) {
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                int fno = Integer.parseInt(splitURI[1]);
                Fil fil = dao.findById(fno);
                if (fil == null) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND); 
                    return;
                }
                out.println(objectMapper.writeValueAsString(fil));
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
        Fil fil = new Fil();
        try {
            fil = objectMapper.readValue(req.getReader(), Fil.class);
            dao.save(fil);
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
            Integer fno = Integer.parseInt(splitURI[1]);
            if (!dao.delete(fno)) {
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
        Fil fil = new Fil();
        try {
            fil = objectMapper.readValue(req.getReader(), Fil.class);
            dao.update(fil);
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
        Fil fil = new Fil();
        Fil filToPatch;
        try {
            fil = objectMapper.readValue(req.getReader(), Fil.class);
            filToPatch = dao.findById(fil.getFno());
            dao.update(new Fil(fil.getFno(),
                fil.getTitre() == null ? filToPatch.getTitre() : fil.getTitre(),
                fil.getDescription() == null ? filToPatch.getDescription() : fil.getDescription(),
                fil.getDateCreation() == null ? filToPatch.getDateCreation() : fil.getDateCreation()));
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("patched");
        out.close();
    }
}
