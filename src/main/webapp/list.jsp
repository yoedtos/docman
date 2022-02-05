<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.yoedtos.docman.model.Document"%>

<jsp:include page="header.jsp"/>
  
  <div id="dropModal" class="w3-modal">
    <div class="w3-modal-content w3-card-4 w3-animate-zoom w3-center" style="max-width:500px">
      <div class="w3-container">
        <span onclick="closeDropModal();" class="w3-button w3-display-topright">&times;</span>
        <h2>Are you sure to remove it!</h2>
      </div>
      <div class="w3-container w3-border-top w3-light-grey" style="padding:20px 0px">
        <form action="controller">
          <button type="button" onclick="closeDropModal();" class="w3-button w3-white w3-border">Cancel</button>
          <button type="submit" class="w3-button w3-white w3-border">OK</button>
          <input name="action" type="hidden" value="drop">
          <input id="id" name="id" type="hidden">
        </form>
      </div>
    </div>
  </div>
  <h2 class="w3-cursive w3-center">List</h2>
  <hr>
  <div class="w3-panel w3-light-grey">
    <p>Total list result: ${pager.total}</p>
  </div>
  <div class="w3-responsive">
    <c:if test="${pager.total > 0}">
      <div>
        <table id="listTable" class="w3-table-all">
          <thead>
            <tr class="w3-light-grey">
              <th class="w3-center">#</th>
              <th>Title</th>
              <th>Size</th>
              <th>Author</th>
              <th class="w3-center">Action</th>
            </tr>
          </thead>
          <c:forEach var="document" items="${pager.documents}" varStatus="doc">
            <tbody>
              <tr>
                <td><c:out value="${doc.index +1}"/></td>
                <td>
                  <a href="download?id=${document.id}"><c:out value="${document.title}"/></a>
                </td>
                <td><c:out value="${document.formatedSize}"/></td>
                <td><c:out value="${document.author}"/></td>         
                <td class="w3-center">
                  <a class="w3-button" href="controller?action=edit&id=${document.id}">
                    <img class="icons" alt="edit" src="icons/edit.svg">
                  </a>
                  <a class="w3-button" onclick="openDropModal(${document.id});">
                    <img class="icons" alt="trash" src="icons/trash.svg">
                  </a>
                </td>
              </tr>
            </tbody>
          </c:forEach>
        </table>
        <div class="w3-bar w3-center pager">
          <c:choose>
            <c:when test="${pager.previous != 0}">
              <a href="list?page=${pager.previous}" class="w3-button w3-white w3-border">&laquo;</a>
            </c:when>
            <c:otherwise>
              <a href="#" class="w3-button w3-border w3-disabled">&laquo;</a>
            </c:otherwise>
          </c:choose>
          <a href="#" class="w3-button w3-white w3-border">${pager.page}</a>
          <c:choose>
            <c:when test="${pager.next > pager.pages}">
             <a href="#" class="w3-button w3-border w3-disabled">&raquo;</a>
            </c:when>
            <c:otherwise>
             <a href="list?page=${pager.next}" class="w3-button w3-white w3-border">&raquo;</a>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </c:if>
    <hr>
    <div class="w3-bar w3-padding">
      <a class="w3-button w3-white w3-border" href="search.jsp">Search</a>
      <a class="w3-button w3-white w3-border w3-right" href="index.jsp">Home</a>
    </div>
  </div>

<jsp:include page="footer.jsp"/>