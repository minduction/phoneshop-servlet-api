<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message and empty error}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
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
        <td class="quantity">
          Quantity
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc"/>
          <tags:sortLink sort="price" order="desc"/>
        </td>
        <td>
          Action
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <form method="post">
        <tr>
          <td>
            <img class="product-tile" src="${product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                ${product.description}
            </a>
          </td>
          <td>
            <input name="quantity" value="${not empty error and errorProductId eq product.id ? param.quantity : 1}" class="quantity">
            <input type="hidden" name="productId" value="${product.id}">
            <c:if test="${not empty error and errorProductId eq product.id}">
              <div class="error">
                  ${error}
              </div>
            </c:if>
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
          <td>
            <button formaction="${pageContext.servletContext.contextPath}/products">
              Add to cart
            </button>
          </td>
        </tr>
      </form>
    </c:forEach>
  </table>


</tags:master>
