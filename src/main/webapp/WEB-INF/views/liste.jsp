<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ListeIMC</title>
<style>
  body { font-family: Arial, sans-serif; margin: 30px; }
  table { border-collapse: collapse; width: 100%; }
  th, td { border: 1px solid #999; padding: 8px; text-align: center; }
  th { background: #2c5282; color: #fff; }
  .btn { display: inline-block; padding: 8px 14px; background: #2f855a; color: #fff;
         text-decoration: none; border-radius: 4px; margin-top: 15px; }
  .sup { color: #c53030; }
</style>
</head>
<body>
  <jsp:include page="entete.jsp">
    <jsp:param name="titre" value="Liste des patients (IMC)" />
  </jsp:include>

  <table>
    <tr>
      <th>Nom</th><th>Prénom</th><th>Masse</th><th>Taille</th>
      <th>IMC</th><th>Interprétation</th><th colspan="2">Opérations</th>
    </tr>

    <c:forEach items="${liste}" var="p">
      <tr>
        <td>${p.nom}</td>
        <td>${p.prenom}</td>
        <td>${p.masse}</td>
        <td>${p.taille}</td>
        <td><fmt:formatNumber value="${p.imc}" minFractionDigits="2" maxFractionDigits="2"/></td>
        <td>${p.interpretation}</td>
        <td>
          <c:url value="/patients" var="urlSup">
            <c:param name="action" value="supprimer"/>
            <c:param name="id" value="${p.id}"/>
          </c:url>
          <a class="sup" href="${urlSup}" onclick="return confirm('Supprimer ce patient ?');">Supprimer</a>
        </td>
        <td>
          <c:url value="/patients" var="urlMod">
            <c:param name="action" value="modifier"/>
            <c:param name="id" value="${p.id}"/>
          </c:url>
          <a href="${urlMod}">Modifier</a>
        </td>
      </tr>
    </c:forEach>

    <c:if test="${empty liste}">
      <tr><td colspan="8">Aucun patient enregistré.</td></tr>
    </c:if>
  </table>

  <c:url value="/patients" var="urlAjout">
    <c:param name="action" value="ajouter"/>
  </c:url>
  <a class="btn" href="${urlAjout}">Ajouter un patient</a>
</body>
</html>