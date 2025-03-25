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
import model.dao.UtilisateurDAO;
import model.dto.Utilisateur;

@WebServlet("/utilisateurs/*")
public class UtilisateurRestAPI extends HttpServlet {
    UtilisateurDAO dao = new UtilisateurDAO();
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
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
        if (!BasicAuth.auth(req, dao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String info = req.getPathInfo();
        try {
            if (info == null || info.equals("/")) {
                for (Utilisateur u : dao.findAll()) {
                    out.println(objectMapper.writeValueAsString(u));
                }
            } else {
                String[] splitURI = info.split("/");
                if (splitURI.length != 2) {
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                String uno = splitURI[1];
                Utilisateur utilisateur = dao.findById(uno);
                if (utilisateur == null) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND); 
                    return;
                }
                out.println(objectMapper.writeValueAsString(utilisateur));
            }
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }
        out.close();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, dao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = objectMapper.readValue(req.getReader(), Utilisateur.class);
            dao.save(utilisateur);
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }
        out.println("saved");
        out.close();
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, dao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String info = req.getPathInfo();
        try {
            String[] splitURI = info.split("/");
            if (splitURI.length != 2) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String uno = splitURI[1];
            if (!dao.delete(uno)) {
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
        if (!BasicAuth.auth(req, dao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = objectMapper.readValue(req.getReader(), Utilisateur.class);
            dao.update(utilisateur);
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("put");
        out.close();
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (!BasicAuth.auth(req, dao)) {res.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;}
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        Utilisateur utilisateur = new Utilisateur();
        Utilisateur utilisateurToPatch;
        try {
            utilisateur = objectMapper.readValue(req.getReader(), Utilisateur.class);
            utilisateurToPatch = dao.findById(utilisateur.getUno());
            dao.update(new Utilisateur(utilisateur.getUno(),
                utilisateur.getNom() == null ? utilisateurToPatch.getNom() : utilisateur.getNom(),
                utilisateur.getPrenom() == null ? utilisateurToPatch.getPrenom() : utilisateur.getPrenom(),
                utilisateur.getEmail() == null ? utilisateurToPatch.getEmail() : utilisateur.getEmail(),
                utilisateur.getMdp() == null ? utilisateurToPatch.getMdp() : utilisateur.getMdp(),
                utilisateur.getDateCreation() == null ? utilisateurToPatch.getDateCreation() : utilisateur.getDateCreation()));
        } catch (DAOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println("patched");
        out.close();
    }
}
