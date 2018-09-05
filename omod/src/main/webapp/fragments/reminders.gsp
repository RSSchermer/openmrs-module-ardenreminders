<div class="info-section allergies">
    <div class="info-header">
        <i class="icon-lightbulb"></i>
        <h3>${ ui.message("ardenreminders.widget.title").toUpperCase() }</h3>
    </div>
    <div class="info-body">

        <% if (!results.errors.isEmpty()) { %>
            <div class="note-container">
                <div class="note warning">
                    <div class="text">
                        <i class="icon-warning-sign medium"></i>

                        <p>
                            <strong>${ ui.message("ardenreminders.widget.errors_notification") }</strong>
                        </p>
                    </div>
                </div>
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
</div>
