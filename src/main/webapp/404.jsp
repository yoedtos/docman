<jsp:include page="header.jsp"/>
  
  <div class="w3-center">
    <h2 >Error 404</h2>
    <h3>Page not found</h3>
    <p>${requestScope['javax.servlet.error.message']}</p>
  </div>
  <br/>
  <div class="w3-bar">
    <a class="w3-button w3-white w3-border" href="index.jsp">Home</a>
  </div>

<jsp:include page="footer.jsp"/>
