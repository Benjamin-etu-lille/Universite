package controleur.endpoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AbonnementDAO;
import model.dao.UtilisateurDAO;
import model.dto.Abonnement;

@WebServlet("/abonnements/*")
public class AbonnementRestAPI extends HttpServlet {
    AbonnementDAO dao = new AbonnementDAO();
    UtilisateurDAO udao = new UtilisateurDAO();
    ObjectMapper objectMapper = new ObjectMapper();

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
                if (splitURI.length != 2 && splitURI.length != 3) {
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                List<Abonnement> abonnements = new ArrayList<>();
                switch (idType) {
                    case "userId":  abonnements.addAll(dao.findAllByUtilisateur(splitURI[1])); break;
                    case "userFilId": abonnements.add(dao.findByUserAndFil(splitURI[1], Integer.parseInt(splitURI[2]))); break;
                    case "filId": abonnements.addAll(dao.findAllByFil(Integer.parseInt(splitURI[1]))); break;
                }
                if (abonnements.isEmpty()) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND); 
                    return;
                }
                out.println("{");
                for (int i = 0; i < abonnements.size(); i++) {
                    out.println(objectMapper.writeValueAsString(abonnements.get(i)) + ", ");
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
        Abonnement abonnement = new Abonnement();
        try {
            abonnement = objectMapper.readValue(req.getReader(), Abonnement.class);
            dao.save(abonnement);
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
            if (splitURI.length != 3) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String uno = splitURI[1];
            Integer fno = Integer.parseInt(splitURI[2]);
            if (!dao.delete(uno, fno)) {
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
        Abonnement abonnement = new Abonnement();
        try {
            abonnement = objectMapper.readValue(req.getReader(), Abonnement.class);
            dao.update(abonnement);
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
        Abonnement abonnement = new Abonnement();
        Abonnement abonnementToPatch;
        try {
            abonnement = objectMapper.readValue(req.getReader(), Abonnement.class);
            abonnementToPatch = dao.findByUserAndFil(abonnement.getUno(), abonnement.getFno());
            dao.update(new Abonnement(abonnement.getUno(), abonnement.getFno(),
                abonnement.getRole() == null ? abonnementToPatch.getRole() : abonnement.getRole()));
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("patched");
        out.close();
    }
}
