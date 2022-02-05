<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="net.yoedtos.docman.model.Document"%>
<%@ page import="net.yoedtos.docman.search.Field"%>

<jsp:include page="header.jsp"/>

<c:set var="searchDocuments" scope="request" value="${pager.documents}" />
  
  <h2 class="w3-cursive w3-center">Search</h2>
  <hr>
  <form name="searchForm" method="get" action="search">
    <div class="w3-row-padding w3-margin-bottom">
      <div class="w3-col s8 w3-mobile">
        <input name="keyword" type="text" class="w3-input" value="${keyword}" placeholder="Search" required/>
      </div>
      <div class="w3-col s2 w3-mobile">
        <select class="w3-select" name="field" required>
         <c:choose>
          <c:when test="${empty field}">
             <option value="" selected>Field...</option>
          </c:when>
          <c:otherwise>
             <option value="${field.ordinal()}" selected>${field.name}</option>
          </c:otherwise>
         </c:choose>
         <c:forEach items="${Field.values()}" var="f">
          <option value="${f.ordinal()}">${f.name}</option>
         </c:forEach>
        </select>
      </div>
      <div class="w3-col s2 w3-mobile">
        <input class="w3-button w3-block w3-white w3-border" type="submit" value="Search" />
      </div>
    </div>
  </form>
  <c:if test="${pager.total != null}">
    <div class="w3-panel w3-light-grey">
      <p>Total search result: ${pager.total}</p>
    </div>
  </c:if>
  <c:forEach var="document" items="${searchDocuments}">
    <ul class="w3-ul w3-card-4">
      <li class="w3-bar">
        <div class="w3-bar-item ">
          <a href="download?id=${document.id}">
            <span class="w3-large">${document.title}</span>
          </a>
          <br>
          <p>${document.description}</p>
          <span class="w3-tiny">author: ${document.author}</span>
        </div>
      </li>
    </ul>
  </c:forEach>
  <c:if test="${pager.total != null}">
    <div class="w3-bar w3-center pager">
      <c:url value="search" var="previous">
        <c:param name="keyword" value="${keyword}"/>
        <c:param name="field" value="${field.ordinal()}"/>
        <c:param name="page" value="${pager.previous}"/>
      </c:url>
      <c:choose>
        <c:when test="${pager.previous != 0}">
          <a href="${previous}" class="w3-button w3-white w3-border">&laquo;</a>
        </c:when>
        <c:otherwise>
          <a href="#" class="w3-button w3-border w3-disabled">&laquo;</a>
        </c:otherwise>
      </c:choose>
      <a href="#" class="w3-button w3-white w3-border">${pager.page}</a>
      <c:url value="search" var="next">
        <c:param name="keyword" value="${keyword}"/>
        <c:param name="field" value="${field.ordinal()}"/>
        <c:param name="page" value="${pager.next}"/>
      </c:url>
      <c:choose>
        <c:when test="${pager.next > pager.pages}">
          <a href="#" class="w3-button w3-border w3-disabled">&raquo;</a>
        </c:when>
        <c:otherwise>
          <a href="${next}" class="w3-button w3-white w3-border">&raquo;</a>
        </c:otherwise>
      </c:choose>
    </div>
  </c:if>
  <hr> 
  <div class="w3-bar w3-padding">
    <a class="w3-button w3-white w3-border" href="list?page=1">List</a>
    <a class="w3-button w3-white w3-border w3-right" href="index.jsp">Home</a>
  </div>
 
<jsp:include page="footer.jsp"/>