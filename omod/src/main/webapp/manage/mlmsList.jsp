<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/ardenreminders/stylesheets/mlms-list.css"/>

<h2><spring:message code="ardenreminders.title" /></h2>

<div class="mlm-list-container">
    <h3>Medical Logic Modules</h3>

    <div class="actions">
        <a href="mlms/new.form"><spring:message code="ardenreminders.add_mlm" /></a>
    </div>

    <c:choose>
        <c:when test="${!mlms.isEmpty()}">
            <table class="mlms-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Evoke</th>
                        <th>Created</th>
                        <th>Last Updated</th>
                        <th></th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="mlm" items="${mlms}">
                        <tr>
                            <td><a href="mlms/${mlm.uuid}.htm">${mlm.name}</a></td>
                            <td>${mlm.evoke}</td>
                            <td>${mlm.dateCreated}</td>
                            <td>${mlm.dateChanged}</td>
                            <td><button class="delete-button" id="${mlm.uuid}">Delete</button></td>
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

<script type="text/javascript">
    Array.from(document.getElementsByClassName("delete-button")).forEach(function (deleteButton) {
        deleteButton.onclick = function () {
            if (confirm("Are you sure you want to delete this module?")) {
                var uuid = deleteButton.getAttribute("id");

                jQuery.ajax({
                    url: "mlms/" + uuid + ".htm",
                    type: "DELETE",
                    success: function () {
                        window.location.href = "mlms.list"
                    }
                });
            }
        };
    });
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
