<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.yoedtos.docman.model.Type"%>

<jsp:include page="header.jsp"/>

<c:set var="d" scope="request" value="${document}" />

    <h2 class="w3-cursive w3-center">Document</h2>
    <hr>
    <form name="uploadForm" onsubmit="return validateFields();" method="post" enctype="multipart/form-data" action="upload">
        <input type="hidden" name="id" value="${d.id}"/>
        <div class="w3-row-padding w3-margin">
          <div class="w3-half">
             <label>File Select</label>
             <c:choose>
                <c:when test="${empty d.id}">
                   <input class="w3-input w3-border" name="file" type="file" required />
                </c:when>
                <c:otherwise>
                    <input class="w3-input w3-border" name="file" type="file" disabled />
                </c:otherwise>
             </c:choose>
          </div>
          <div class="w3-half">
            <label for="type">Type</label>
            <select id="type" class="w3-select" style="margin-top: 5px" name="type" required>
             <c:choose>
                <c:when test="${empty d.id}">
                   <option value="" selected>Choose...</option>
                   </c:when>
                <c:otherwise>
                   <option value="${d.type}" selected>${d.type.name}</option>
                </c:otherwise>
             </c:choose>
             <c:forEach items="${Type.values()}" var="t">
              <option value="${t}">${t.name}</option>
             </c:forEach>
            </select>
          </div>
        </div>
        <div class="w3-row-padding w3-margin">
          <div class="w3-half">
             <label for="title">Title:</label><span id="titleError" class="error"></span>
             <input id="title" class="w3-input w3-border" type="text" name="title" value="${d.title}" required/>
          </div>
          <div class="w3-half">
             <label for="author">Author:</label><span id="authorError" class="error"></span>
             <input id="author" class="w3-input w3-border" type="text" name="author" value="${d.author}" required/>
          </div>
        </div>  
        <div class="w3-row-padding w3-margin">
          <div class="w3-half">
            <label for="description">Description:</label><span id="descriptionError" class="error"></span>
            <textarea id="description" class="w3-input w3-border" name="description" required><c:out value="${d.description}"></c:out></textarea>
          </div>
          <div class="w3-half">
            <label for="tags">Tags:</label><span id="tagsError" class="error"></span>
            <textarea id="tags" class="w3-input w3-border" name="tags" required><c:out value="${d.tags}"></c:out></textarea>
          </div>
        </div>
        <hr>
        <div class="w3-bar w3-padding">
          <a class="w3-button w3-white w3-border" href="search.jsp">Search</a>
          <a class="w3-button w3-white w3-border" href="list?page=1">List</a>
          <input class="w3-button w3-white w3-border w3-right" type="submit" value="${empty d.id ? 'Upload' : 'Update'}"/>
        </div>
    </form>
  
<jsp:include page="footer.jsp"/>