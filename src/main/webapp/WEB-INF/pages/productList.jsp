<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}"/>
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sort="description" order="asc"/>
          <tags:sortLink sort="description" order="desc"/>
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc"/>
          <tags:sortLink sort="price" order="desc"/>
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                ${product.description}
            </a>
        </td>
        <td class="price">
          <a href="#popup-${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
          <div id="popup-${product.id}" class="price-history-popup">
            <div class="popup-content">
              <a href="#" class="close-btn">&times;</a>
              <h2>Price History</h2>
              <h3>${product.description}</h3>
              <c:forEach var="historyElement" items="${product.productPriceHistory}">
                <p>
                    ${historyElement.date}
                  <fmt:formatNumber value="${historyElement.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </p>
              </c:forEach>
            </div>
          </div>
        </td>
      </tr>
    </c:forEach>
  </table>


</tags:master>
