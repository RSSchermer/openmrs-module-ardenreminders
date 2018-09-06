<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/ardenreminders/stylesheets/codemirror.css"/>
<openmrs:htmlInclude file="/moduleResources/ardenreminders/stylesheets/edit-mlm-source.css"/>

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

<div class="mlm-source-container">
    <h3><spring:message code="ardenreminders.edit_mlm_source.container_title" /></h3>

    <div class="actions">
        <button id="check_button"><spring:message code="ardenreminders.edit_mlm_source.check" /></button>
        <button id="save_button"><spring:message code="ardenreminders.edit_mlm_source.save" /></button>
        <button id="save_and_close_button"><spring:message code="ardenreminders.edit_mlm_source.save_and_close" /></button>
        <button id="close_without_saving_button"><spring:message code="ardenreminders.edit_mlm_source.close_without_saving" /></button>
    </div>

    <div class="editor-container">
        <div class="source-container">
            <h4>Source</h4>

            <textarea id="source">${mlm.source}</textarea>
        </div>

        <div class="compiler-output-container">
            <h4>Compiler Output</h4>

            <div id="compiler_output" class="compiler-output">

            </div>
        </div>
    </div>

    <openmrs:htmlInclude file="/moduleResources/ardenreminders/scripts/codemirror.js"/>

    <script>
        var editor = CodeMirror.fromTextArea(document.getElementById("source"), {
            lineNumbers: true,
            indentUnit: 4,
            autofocus: true
        });

        var compilerOutput = document.getElementById("compiler_output");

        document.getElementById("check_button").onclick = function () {
            jQuery.post("check_source.json", { source: editor.getValue() }, processCompilerOutputResponse);
        };

        document.getElementById("save_button").onclick = function () {
            jQuery.post("save_source.json", { source: editor.getValue() }, processCompilerOutputResponse);
        };

        document.getElementById("save_and_close_button").onclick = function () {
            jQuery.post("save_source.json", { source: editor.getValue() }, function () {
                window.location.href = "../${mlm.uuid}.htm";
            });
        };

        document.getElementById("close_without_saving_button").onclick = function () {
            if (confirm("Are you sure? Any unsaved changes will be lost.")) {
                window.location.href = "../${mlm.uuid}.htm";
            }
        };

        function processCompilerOutputResponse(response) {
            compilerOutput.insertAdjacentHTML("afterbegin", "<p>[" + new Date().toLocaleString() + "]: " + response.output + "</p>");
        }
    </script>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
