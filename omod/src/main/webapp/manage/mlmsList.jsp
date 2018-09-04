<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="ardenreminders.title" /></h2>

<div class="mlm-list-container">
    <c:choose>
        <c:when test="${!mlms.isEmpty()}">
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Evoke</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="mlm" items="${mlms}">
                        <tr>
                            <td><a href="mlms/${mlm.uuid}.htm">${mlm.name}</a></td>
                            <td>${mlm.evoke}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>
                <spring:message code="ardenreminders.no_mlms_placeholder" />
            </p>
        </c:otherwise>
    </c:choose>
</div>


<div class="actions">
    <a href="mlms/new.form"><spring:message code="ardenreminders.add_mlm" /></a>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
