<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
  <header>
    <a href="${pageContext.servletContext.contextPath}">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
    <a href="${pageContext.servletContext.contextPath}/cart">
      <jsp:include page="/cart/miniCart"/>
    </a>

  </header>
  <p>
    Cart: ${cart}
  </p>
  <main>
    <jsp:doBody/>
  </main>
  <c:if test="${not empty recentlyVisitedProducts}">
    <h2>
      Recently viewed:
    </h2>
    <table class="recently-visited">
      <tr>
        <c:forEach var="product" items="${recentlyVisitedProducts.items}">
          <td class="recently-product">
            <c:if test="${not empty product}">
              <img class="product-tile" src="${product.imageUrl}">
              <p>
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                    ${product.description}
                </a>
              </p>
              <p>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
              </p>
            </c:if>
          </td>
        </c:forEach>
      </tr>
    </table>
  </c:if>
  <footer>
    (C) ExpertSoft 2025
  </footer>
</body>
</html>
