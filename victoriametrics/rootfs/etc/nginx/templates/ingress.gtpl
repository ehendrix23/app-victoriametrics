server {
    listen {{ .interface }}:{{ .port }} default_server;

    include /etc/nginx/includes/server_params.conf;
    include /etc/nginx/includes/proxy_params.conf;

    # Open the web interface instead of the plain list of endpoints
    # VictoriaMetrics serves on its root. The redirect is relative, so the
    # browser resolves it against the Ingress path it is already on.
    location = / {
        allow   172.30.32.2;
        deny    all;

        return 302 vmui/;
    }

    location / {
        allow   172.30.32.2;
        deny    all;

        proxy_pass http://backend;
    }
}
