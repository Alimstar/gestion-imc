package com.imc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.imc.beans.MonIMC;

public class PatientDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/ListeIMC"
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";   // à adapter si votre MySQL a un mot de passe

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL introuvable dans WEB-INF/lib", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private MonIMC map(ResultSet rs) throws SQLException {
        MonIMC p = new MonIMC();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setMasse(rs.getDouble("masse"));
        p.setTaille(rs.getDouble("taille"));
        p.setImc(rs.getDouble("imc"));
        p.setInterpretation(rs.getString("interpretation"));
        return p;
    }

    public List<MonIMC> lister() throws SQLException {
        List<MonIMC> liste = new ArrayList<>();
        String sql = "SELECT * FROM patient ORDER BY id";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        }
        return liste;
    }

    public MonIMC trouver(int id) throws SQLException {
        String sql = "SELECT * FROM patient WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void ajouter(MonIMC p) throws SQLException {
        p.calculer();
        String sql = "INSERT INTO patient (nom, prenom, masse, taille, imc, interpretation) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setDouble(3, p.getMasse());
            ps.setDouble(4, p.getTaille());
            ps.setDouble(5, p.getImc());
            ps.setString(6, p.getInterpretation());
            ps.executeUpdate();
        }
    }

    public void modifier(MonIMC p) throws SQLException {
        p.calculer();
        String sql = "UPDATE patient SET nom = ?, prenom = ?, masse = ?, taille = ?, "
                   + "imc = ?, interpretation = ? WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setDouble(3, p.getMasse());
            ps.setDouble(4, p.getTaille());
            ps.setDouble(5, p.getImc());
            ps.setString(6, p.getInterpretation());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM patient WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}