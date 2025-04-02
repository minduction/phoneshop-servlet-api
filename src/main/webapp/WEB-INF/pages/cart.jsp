<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.Cart" scope="request"/>
<tags:master pageTitle="Cart">

  <div class="success">
      ${param.message}
  </div>
  <c:if test="${not empty errors}">
    <div class="error">
      There were errors while updating cart
    </div>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <table>
      <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
        </td>
        <td class="quantity">
          Quantity
        </td>
        <td class="price">
          Price
        </td>
        <td>
          Action
        </td>
      </tr>
      </thead>
      <c:forEach var="item" items="${cart.items}" varStatus="status">
        <tr>
          <td>
            <img class="product-tile" src="${item.product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                ${item.product.description}
            </a>
          </td>
          <td class="quantity">
            <input name="quantity" value="${not empty errors[item.product.id] ? paramValues['quantity'][status.index] : item.quantity}" class="quantity">
            <c:if test="${not empty errors[item.product.id]}">
              <div class="error">
                ${errors[item.product.id]}
              </div>
            </c:if>
            <input type="hidden" name="productId" value="${item.product.id}">
          </td>
          <td class="price">
            <a href="#popup-${item.product.id}">
              <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
            <div id="popup-${item.product.id}" class="price-history-popup">
              <div class="popup-content">
                <a href="#" class="close-btn">&times;</a>
                <h2>Price History</h2>
                <h3>${item.product.description}</h3>
                <c:forEach var="historyElement" items="${item.product.productPriceHistory}">
                  <p>
                      ${historyElement.date}
                    <fmt:formatNumber value="${historyElement.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                  </p>

                </c:forEach>
              </div>
            </div>
          </td>
          <td>
            <button form="deleteCartItemForm" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
              Delete
            </button>
          </td>
        </tr>
      </c:forEach>
      <tr>
        <td></td>
        <td></td>
        <td>Total Cost:</td>
        <td><fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="$"/></td>
      </tr>
    </table>
    <p>
      <button>
        Update
      </button>
    </p>
  </form>
  <form id="deleteCartItemForm" method="post"></form>
</tags:master>
