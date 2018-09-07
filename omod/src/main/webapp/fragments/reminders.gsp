<%
    ui.includeCss("ardenreminders", "reminders.css")
%>

<div class="info-section ardenreminders">
    <div class="info-header">
        <i class="icon-lightbulb"></i>
        <h3>${ ui.message("ardenreminders.widget.title").toUpperCase() }</h3>
    </div>
    <div class="info-body">

        <% if (!results.errors.isEmpty()) { %>
            <div id="errors_notification" class="errors-note-container">
                <i class="icon-warning-sign medium icon"></i>

                <p>
                    <strong>${ ui.message("ardenreminders.widget.errors_notification") }</strong>
                </p>
            </div>
        <% } %>

        <% if (!results.messages.isEmpty()) { %>
            <ul>
                <% results.messages.each { message -> %>
                    <li>
                        ${message}
                    </li>
                <% } %>
            </ul>
        <% } else { %>
            None
        <% } %>
    </div>

    <div id="ardenreminders_error_dialog" class="dialog">
        <div class="dialog-header">
            <i class="icon-folder-open"></i>
            <h3>${ ui.message("ardenreminders.widget.error_dialog_title") }</h3>
        </div>
        <div class="dialog-content">
            <p>
                <em>${ ui.message("ardenreminders.widget.the_following_errors_occurred") }</em>
            </p>

            <ul>
                <% results.errors.each { message -> %>
                <li>
                    ${message}
                </li>
                <% } %>
            </ul>

            <span id="ardenreminders_error_dialog_close_button" class="button">Close</span>
        </div>
    </div>

    <script type="text/javascript">
        var dialog = document.getElementById("ardenreminders_error_dialog");

        document.body.appendChild(dialog);

        var errorsNofitication = document.getElementById("errors_notification");

        if (errorsNofitication) {
            errorsNofitication.onclick = function () {
                dialog.style.display = "block";
            };
        }

        document.getElementById("ardenreminders_error_dialog_close_button").onclick = function () {
            dialog.style.display = "none";
        };
    </script>
</div>
