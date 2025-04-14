<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">

  <div class="success">
      ${param.message}
  </div>
  <c:if test="${not empty errors}">
    <div class="error">
      There were errors while placing the order
    </div>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
      </tr>
      </thead>
      <c:forEach var="item" items="${order.items}" varStatus="status">
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
            <p class="quantity">
              ${item.quantity}
            </p>
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
        </tr>
      </c:forEach>
      <tr>
        <td></td>
        <td></td>
        <td>Subtotal cost:</td>
        <td class="price"><fmt:formatNumber value="${order.subTotalCost}" type="currency" currencySymbol="$"/></td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td>Delivery cost:</td>
        <td class="price"><fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="$"/></td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td>Total cost:</td>
        <td class="price"><fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="$"/></td>
      </tr>
    </table>
    <h1>Your details</h1>
    <table>
      <tags:orderRow paramName="firstName" order="${order}" label="First name" errors="${errors}"></tags:orderRow>
      <tags:orderRow paramName="lastName" order="${order}" label="Last name" errors="${errors}"></tags:orderRow>
      <tags:orderRow paramName="phone" order="${order}" label="Phone" errors="${errors}"></tags:orderRow>
      <tags:orderRow paramName="deliveryDate" order="${order}" label="Delivery date" errors="${errors}"></tags:orderRow>
      <tags:orderRow paramName="deliveryAddress" order="${order}" label="Delivery address" errors="${errors}"></tags:orderRow>
      <tr>
        <td>Payment method<span style ="color:red">*</span></td>
        <td>
          <select name="paymentMethod">
            <option></option>
            <c:forEach var="paymentMethod" items="${paymentMethods}">
              <option ${param['paymentMethod'] == paymentMethod ? 'selected' : ''}>${paymentMethod}</option>
            </c:forEach>
          </select>
          <c:set var="error" value="${errors['paymentMethod']}"/>
          <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
          </c:if>
        </td>
      </tr>
    </table>
    <button>
      Place order
    </button>
  </form>
</tags:master>
