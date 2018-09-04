<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="ardenreminders.title" /></h2>

<h3><spring:message code="ardenreminders.new_mlm_form_title" /></h3>

<form:form method="post" modelAttribute="mlm" action="../mlms.list">
    <div class="form-group">
        <form:label path="name"><spring:message code="ardenreminders.mlm_form_name_label" /></form:label>
        <form:input path="name" />

        <div class="ui-state-error-text">
            <form:errors path="name" />
        </div>
    </div>

    <div class="form-group">
        <form:label path="name"><spring:message code="ardenreminders.mlm_form_evoke_label" /></form:label>
        <form:checkbox path="evoke" />

        <div class="ui-state-error-text">
            <form:errors path="evoke" />
        </div>
    </div>

    <div class="form-group">
        <input type="submit" value="Submit"/>
    </div>
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
