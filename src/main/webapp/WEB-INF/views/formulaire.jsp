<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Patient</title>
<style>
  body { font-family: Arial, sans-serif; margin: 30px; }
  label { display: block; margin-top: 12px; }
  input { padding: 6px; width: 250px; }
  .erreur { color: #c53030; }
  button { margin-top: 18px; padding: 8px 14px; }
</style>
</head>
<body>
  <c:choose>
    <c:when test="${not empty patient.id and patient.id != 0}">
      <jsp:include page="entete.jsp"><jsp:param name="titre" value="Modifier le patient" /></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="entete.jsp"><jsp:param name="titre" value="Ajouter un patient" /></jsp:include>
    </c:otherwise>
  </c:choose>

  <c:if test="${not empty erreur}">
    <p class="erreur">${erreur}</p>
  </c:if>

  <c:url value="/patients" var="urlAction"/>
  <form action="${urlAction}" method="post">
    <input type="hidden" name="id" value="${patient.id != 0 ? patient.id : ''}">

    <label>Nom
      <input type="text" name="nom" value="${patient.nom}" required>
    </label>
    <label>Prénom
      <input type="text" name="prenom" value="${patient.prenom}" required>
    </label>
    <label>Masse (kg)
      <input type="text" name="masse" value="${patient.masse != 0 ? patient.masse : ''}" required>
    </label>
    <label>Taille (m)
      <input type="text" name="taille" value="${patient.taille != 0 ? patient.taille : ''}" required>
    </label>

    <button type="submit">Enregistrer</button>
    <a href="${urlAction}">Annuler</a>
  </form>
</body>
</html>