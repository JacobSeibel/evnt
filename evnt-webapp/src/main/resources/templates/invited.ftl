<html>
    <head>
        <style>

        </style>
    </head>
    <body>
        <p>${email.sender.displayName} has invited you to the event ${email.event.name}! Click the link below to view and RSVP.</p>
        http://localhost:8080/#!view-event/?eventPk=${email.event.pk}
    </body>
</html>