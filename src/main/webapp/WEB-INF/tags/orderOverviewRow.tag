<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="paramName" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>
<%@ attribute name="label" required="true"%>

<tr>
  <td>${label}</td>
  <td>
    ${order[paramName]}
  </td>
</tr>
