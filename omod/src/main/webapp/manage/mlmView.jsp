<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="ardenreminders.viewing_mlm" /></h2>

<div class="mlm-attributes-container">
    <h3><spring:message code="ardenreminders.mlm_attributes" /></h3>

    <div class="attribute">
        Name: ${mlm.name}
    </div>

    <div class="attribute">
        Evoke: ${mlm.evoke}
    </div>
</div>

<div class="mlm-attributes-container">
    <h3><spring:message code="ardenreminders.mlm_source" /></h3>

    <pre>${mlm.source}</pre>
</div>

<div class="actions">
    <a href="../mlms.list"><spring:message code="ardenreminders.back_to_mlm_list" /></a>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
