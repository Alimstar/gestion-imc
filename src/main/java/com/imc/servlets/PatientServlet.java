package com.imc.servlets;

import java.io.IOException;
import java.sql.SQLException;

import com.imc.beans.MonIMC;
import com.imc.dao.PatientDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PatientDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new PatientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "liste";

        try {
            switch (action) {
                case "ajouter":
                    request.getRequestDispatcher("/WEB-INF/views/formulaire.jsp").forward(request, response);
                    break;

                case "modifier":
                    MonIMC p = dao.trouver(parseId(request.getParameter("id")));
                    if (p == null) {
                        response.sendRedirect(request.getContextPath() + "/patients");
                        return;
                    }
                    request.setAttribute("patient", p);
                    request.getRequestDispatcher("/WEB-INF/views/formulaire.jsp").forward(request, response);
                    break;

                case "supprimer":
                    dao.supprimer(parseId(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/patients");
                    break;

                default:
                    request.setAttribute("liste", dao.lister());
                    request.getRequestDispatcher("/WEB-INF/views/liste.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");

        try {
            double masse = Double.parseDouble(request.getParameter("masse").replace(',', '.'));
            double taille = Double.parseDouble(request.getParameter("taille").replace(',', '.'));
            if (masse <= 0 || taille <= 0) throw new NumberFormatException();

            if (idParam == null || idParam.isEmpty()) {
                dao.ajouter(new MonIMC(0, nom, prenom, masse, taille));
            } else {
                dao.modifier(new MonIMC(parseId(idParam), nom, prenom, masse, taille));
            }
            response.sendRedirect(request.getContextPath() + "/patients");

        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("erreur", "Masse et taille doivent être des nombres positifs (taille en mètres).");
            MonIMC saisie = new MonIMC();
            saisie.setNom(nom);
            saisie.setPrenom(prenom);
            saisie.setId(parseId(idParam));
            request.setAttribute("patient", saisie);
            request.getRequestDispatcher("/WEB-INF/views/formulaire.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }
    }

    /** Convertit un identifiant en entier ; retourne 0 si invalide. */
    private int parseId(String idParam) {
        try {
            return Integer.parseInt(idParam);
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }
}