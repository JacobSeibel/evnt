<html>
    <head>
        <style>

        </style>
    </head>
    <body>
        <h1>RSVP Notification</h1>
        <p>${email.sender.displayName} has responded ${rsvp.name} to your event ${email.event.name}. To view the event, click the link below.</p>
        http://localhost:8080/#!view-event/?eventPk=${email.event.pk}
    </body>
</html>