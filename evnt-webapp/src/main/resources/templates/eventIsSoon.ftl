<html>
    <head>
        <style>

        </style>
    </head>
    <body>
        <h1>Reminder</h1>
        <p>${email.event.name} is on {email.event.startDate}! To view the event, click the link below.</p>
        http://localhost:8080/#!view-event/?eventPk=${email.event.pk}
    </body>
</html>