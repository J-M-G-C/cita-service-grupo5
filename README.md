Cita Service - Grupo 5
Microservicio encargado de la gestión de citas médicas.

Para Ejecutar
En Terminal
.\mvnw.cmd spring-boot:run

Requisitos:
*Java 25
*Puerto: `8081`
Accesos para pruebas
- Eureka Server: `http://localhost:8761`
- Config Server: `http://localhost:8888`
- Cita Service: `http://localhost:8081`
- Paciente Service: `http://localhost:8082`
- API Gateway: `http://localhost:8070`
Pruebas principales
- `GET http://localhost:8081/api/v1/citas/1`
- `GET http://localhost:8081/api/v1/citas/evaluar-y-completar/1`
- `GET http://localhost:8081/api/v1/citas/ficha-medica/1`
- `GET http://localhost:8081/api/v1/citas/configuracion`
