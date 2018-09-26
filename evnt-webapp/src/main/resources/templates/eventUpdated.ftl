<html>
    <head>
        <style>

        </style>
    </head>
    <body>
        <h1>Event Updated</h1>
        <p>${email.sender.displayName} has updated the details of the event ${email.event.name}. View the event by clicking the link below!</p>
        http://localhost:8080/#!view-event/?eventPk=${email.event.pk}
    </body>
</html>