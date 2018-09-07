<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/ardenreminders/styles/mlms-list.css"/>

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
                        <th>Compiles</th>
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
                            <td>
                                <c:choose>
                                    <c:when test="${mlm.compiles}">
                                        <span class="does-compile">YES</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="does-not-compile">NO</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${mlm.evoke}">
                                        <span class="evoke-enabled">YES</span>
                                        <button class="disable-evoke-button" id="${mlm.uuid}">Disable</button>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="evoke-disabled">NO</span>
                                        <button class="enable-evoke-button" id="${mlm.uuid}">Enable</button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
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

                jQuery.post("mlms/" + uuid + "/delete.json", null, function () {
                    window.location.href = "mlms.list"
                });
            }
        };
    });

    Array.from(document.getElementsByClassName("enable-evoke-button")).forEach(function (deleteButton) {
        deleteButton.onclick = function () {
            if (confirm("Are you sure you want to enable evocation for this module?")) {
                var uuid = deleteButton.getAttribute("id");

                jQuery.post("mlms/" + uuid + "/enable_evoke.json", null, function () {
                    window.location.href = "mlms.list"
                });
            }
        };
    });

    Array.from(document.getElementsByClassName("disable-evoke-button")).forEach(function (deleteButton) {
        deleteButton.onclick = function () {
            if (confirm("Are you sure you want to disable evocation for this module?")) {
                var uuid = deleteButton.getAttribute("id");

                jQuery.post("mlms/" + uuid + "/disable_evoke.json", null, function () {
                    window.location.href = "mlms.list"
                });
            }
        };
    });
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
