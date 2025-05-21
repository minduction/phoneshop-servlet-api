<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="AdvancedSearch">

  <h1>
    Advanced Search
  </h1>
  <form>

    <table style="border: none;">
      <tr>
        <td>Description</td>
        <td>
          <input name="description" value="${param.description}">
        </td>
        <td>
          <select name="queryIncludeType">
            <option>All words</option>
            <option>Any word</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>
          Min price
        </td>
        <td>
          <input name="minPrice" class="price" value="${param.minPrice}">
          <c:if test="${not empty errors['minPrice']}">
            <div class="error">
                ${errors['minPrice']}
            </div>
          </c:if>
        </td>
      </tr>
      <tr>
        <td>
          Max price
        </td>
        <td>
          <input name="maxPrice" class="price" value="${param.maxPrice}">
          <c:if test="${not empty errors['maxPrice']}">
            <div class="error">
                ${errors['maxPrice']}
            </div>
          </c:if>
        </td>
      </tr>
    </table>
    <p>
      <button>Search</button>
    </p>
  </form>

  <c:if test="${not empty param}">
    <table>
      <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
        </td>
        <td class="price">
          Price
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
        </form>
      </c:forEach>
    </table>
  </c:if>
</tags:master>
