package com.imc.servlets;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.imc.beans.MonIMC;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private List<MonIMC> liste;
    private final AtomicInteger compteur = new AtomicInteger(0);

    // QUESTION 2 : redéfinition de init() avec une liste de MonIMC
    @Override
    public void init() throws ServletException {
        liste = new CopyOnWriteArrayList<>();
        liste.add(new MonIMC(compteur.incrementAndGet(), "DIOP", "Daouda", 70, 1.70));
        liste.add(new MonIMC(compteur.incrementAndGet(), "SOW", "Aminata", 52, 1.65));
        liste.add(new MonIMC(compteur.incrementAndGet(), "FALL", "Moussa", 95, 1.75));
        // La liste est partagée avec les JSP (portée application)
        getServletContext().setAttribute("liste", liste);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "ajouter":
                request.getRequestDispatcher("/WEB-INF/views/formulaire.jsp").forward(request, response);
                break;

            case "modifier":
                MonIMC p = trouver(request.getParameter("id"));
                if (p == null) {
                    response.sendRedirect(request.getContextPath() + "/patients");
                    return;
                }
                request.setAttribute("patient", p);
                request.getRequestDispatcher("/WEB-INF/views/formulaire.jsp").forward(request, response);
                break;

            case "supprimer":
                MonIMC aSupprimer = trouver(request.getParameter("id"));
                if (aSupprimer != null) liste.remove(aSupprimer);
                response.sendRedirect(request.getContextPath() + "/patients");
                break;

            default:
                request.getRequestDispatcher("/WEB-INF/views/liste.jsp").forward(request, response);
        }
    }

    // QUESTION 3 : création (ou modification) d'un patient
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
                // Création : nouveau Bean ajouté à la liste
                liste.add(new MonIMC(compteur.incrementAndGet(), nom, prenom, masse, taille));
            } else {
                // Modification
                MonIMC p = trouver(idParam);
                if (p != null) {
                    p.setNom(nom);
                    p.setPrenom(prenom);
                    p.setMasse(masse);
                    p.setTaille(taille);
                    p.calculer();
                }
            }
            response.sendRedirect(request.getContextPath() + "/patients");

        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("erreur", "Masse et taille doivent être des nombres positifs (taille en mètres).");
            MonIMC saisie = new MonIMC();
            saisie.setNom(nom);
            saisie.setPrenom(prenom);
            if (idParam != null && !idParam.isEmpty()) saisie.setId(Integer.parseInt(idParam));
            request.setAttribute("patient", saisie);
            request.getRequestDispatcher("/WEB-INF/views/formulaire.jsp").forward(request, response);
        }
    }

    private MonIMC trouver(String idParam) {
        try {
            int id = Integer.parseInt(idParam);
            for (MonIMC p : liste) {
                if (p.getId() == id) return p;
            }
        } catch (NumberFormatException e) { /* id invalide */ }
        return null;
    }
}