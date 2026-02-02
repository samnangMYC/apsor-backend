window.onload = function () {
    window.ui = SwaggerUIBundle({
        url: "openapi.yaml",   // relative path
        dom_id: "#swagger-ui",
        deepLinking: true,
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        layout: "StandaloneLayout"
    });
};
