<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
    Product details page!
  </p>
  <c:if test="${not empty param.message and empty error}">
    <p class="success">
      ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty error}">
    <div class="error">
        There was an error while adding to cart
    </div>
  </c:if>
  <p>
    ${product.description}
  </p>
  <form method="post">
    <table>
      <tr>
        <td>Image</td>
        <td><img src="${product.imageUrl}"></td>
      </tr>
      <tr>
        <td>Price</td>
        <td class="price">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
      <tr>
        <td>code</td>
        <td>${product.code}</td>
      </tr>
      <tr>
        <td>Stock</td>
        <td class="stock">${product.stock}</td>
      </tr>
      <tr>
        <td>
          Quantity
        </td>
        <td>
          <input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity">
          <c:if test="${not empty error}">
            <div class="error">
              ${error}
            </div>
          </c:if>
        </td>
      </tr>
    </table>
    <button>Add to cart</button>
  </form>
</tags:master>
