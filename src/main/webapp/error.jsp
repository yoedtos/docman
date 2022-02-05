<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@ page isErrorPage="true" %>

<jsp:include page="header.jsp"/>

  <h3>Error <%= exception.getMessage() %></h3>
  <br/>
  <%
    StringWriter writer = new StringWriter();
    exception.printStackTrace(new PrintWriter(writer));
  %>
  <div class="w3-small">
    <%= writer %>
  </div>
  <br/>
  <div class="w3-bar">
    <a class="w3-button w3-white w3-border w3-right" href="index.jsp">Home</a>
  </div>

<jsp:include page="footer.jsp"/>