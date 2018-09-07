<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/ardenreminders/styles/codemirror.css"/>
<openmrs:htmlInclude file="/moduleResources/ardenreminders/styles/edit-mlm-source.css"/>

<h2><spring:message code="ardenreminders.edit_mlm_source.container_title" /></h2>

<div class="mlm-source-container">
    <div class="actions">
        <button id="save_button"><spring:message code="ardenreminders.edit_mlm_source.save" /></button>
        <button id="check_button"><spring:message code="ardenreminders.edit_mlm_source.check" /></button>
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
            jQuery.post("save_source.json", { source: editor.getValue() }, function (response) {
                processCompilerOutputResponse(response);
                editor.markClean();
            });
        };

        document.getElementById("close_without_saving_button").onclick = function () {
            if (editor.isClean() || confirm("Are you sure? You have unsaved changes that will be lost.")) {
                window.location.href = "../${mlm.uuid}.htm";
            }
        };

        function processCompilerOutputResponse(response) {
            compilerOutput.insertAdjacentHTML("afterbegin", "<p>[" + new Date().toLocaleString() + "]: " + response.output + "</p>");
        }
    </script>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
