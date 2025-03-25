<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Page d'affichage d'erreur</title>
</head>
<body>
    <h3>
        <% 
            if (exception != null) {
                out.print("Une erreur est survenue : " + exception.getMessage());
            } else {
                out.print("Erreur inconnue.");
            }
        %>
    </h3>
    <p><strong>Stack Trace:</strong></p>
    <pre>
        <%= exception != null ? exception.toString() : "Pas d'exception disponible." %>
    </pre>
</body>
</html>
