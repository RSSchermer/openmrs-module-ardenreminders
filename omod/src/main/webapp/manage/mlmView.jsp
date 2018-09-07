<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="ardenreminders.viewing_mlm" /></h2>

<div class="actions">
    <a href="${mlm.uuid}/edit_source.htm"><spring:message code="ardenreminders.edit_source" /></a> |
    <a href="../mlms.list"><spring:message code="ardenreminders.back_to_mlm_list" /></a>
</div>

<pre>${mlm.source}</pre>

<%@ include file="/WEB-INF/template/footer.jsp"%>
