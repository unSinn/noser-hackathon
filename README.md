
# Start the Game Server (Docker)
docker run -p 8080:8080 suterchristoph1/connect-four:latest



# Start Prometheus/Grafana
Temporary:
update \prometheus-grafana\prometheus\prometheus.yml (add your ip address)

start docker container
cd \prometheus-grafana\docker-compose.yml
docker-compose up -d

# Access prometheus
- http://localhost:9090/targets
- admin/admin

# Access grafana
- http://localhost:3000
- admin/admin