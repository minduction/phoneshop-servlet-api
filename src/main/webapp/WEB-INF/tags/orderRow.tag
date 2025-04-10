<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="paramName" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>
<%@ attribute name="label" required="true"%>
<%@ attribute name="errors" required="true" type="java.util.Map"%>

<tr>
  <td>${label}<span style ="color:red">*</span></td>
  <td>
    <c:set var="error" value="${errors[paramName]}"/>
    <input name="${paramName}" value="${not empty error ? param[paramName] : order[paramName]}">
    <c:if test="${not empty error}">
      <div class="error">
        ${error}
      </div>
    </c:if>
  </td>
</tr>
