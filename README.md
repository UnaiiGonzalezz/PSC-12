[![Java CI with Maven](https://github.com/UnaiiGonzalezz/PSC-12/actions/workflows/maven.yml/badge.svg)](https://github.com/UnaiiGonzalezz/PSC-12/actions/workflows/maven.yml)

# SpringBoot + REST API + MySQL (Proyecto Farmacia)

Este proyecto está desarrollado como parte de la asignatura **Procesos del Software y Calidad**. Se trata de una API REST para la gestión de una farmacia, utilizando tecnologías como **Spring Boot**, **MySQL**, **JPA** y **Maven**.

La estructura básica del proyecto se ha generado con la herramienta [Spring Initializr](https://start.spring.io/).

---

## Cómo ejecutar la aplicación

### 1. Verificar dependencias
Asegúrate de que todas las dependencias estén correctamente definidas en el archivo `pom.xml`, y que la configuración de la base de datos esté en `src/main/resources/application.properties`.

### 2. Compilar el proyecto
Ejecuta el siguiente comando para descargar todas las dependencias y comprobar que la compilación se realiza correctamente:

```bash
mvn compile
```

### 3. Configurar la base de datos
Antes de iniciar la aplicación, asegúrate de tener un servidor **MySQL** en funcionamiento. Puedes usar el script `src/main/resources/dbsetup.sql` para:
- Crear la base de datos
- Crear el usuario `spq` con contraseña `spq`
- Asignarle los permisos necesarios

Ejemplo de ejecución desde terminal:

```bash
mysql -uroot -p < src/main/resources/dbsetup.sql
```

También puedes ejecutar el contenido del archivo desde cualquier cliente gráfico de MySQL.

### 4. Iniciar la aplicación
Para iniciar el servidor Spring Boot, ejecuta:

```bash
mvn spring-boot:run
```

Si no hay errores, la aplicación se ejecutará en:  
[http://localhost:8080](http://localhost:8080)  
Para detenerla, presiona `Ctrl+C`.

### 5. Ejecución de tests
Para ejecutar los tests unitarios y de integración definidos en el proyecto, utiliza el siguiente comando:

```bash
mvn test
```

Este comando compilará el proyecto (si es necesario) y ejecutará todos los tests definidos con frameworks como JUnit y Mockito.
Al finalizar, se mostrará un resumen de los resultados de los tests en la consola.

Si también ejecutas mvn site, los resultados detallados de los tests estarán disponibles en:

```bash
target/site/jacoco/index.html
```

Puedes abrir este archivo en tu navegador para consultar los resultados de cada test.

### 6. Generación de documentación
Maven permite generar documentación automática del proyecto mediante el siguiente comando:

```bash
mvn site
```

Este comando genera un sitio web estático con:

- Información general del proyecto
- Dependencias
- Resultados de los tests
- Informes de calidad del código

Los archivos generados se ubican en el directorio:

```bash
docs/html/index.html
```

Puedes abrir el archivo index.html con cualquier navegador para visualizar la documentación.
---

REST API
--------
La aplicación expone una REST API que es utilizada por la interfaz web y clientes externos. Los endpoints están implementados principalmente en las clases controladoras como ClienteController, MedicamentoController y CompraController.
Para probar estos métodos puedes usar herramientas como Postman o CURL.

A continuación se muestran algunos ejemplos representativos:

Obtener todos los clientes registrados

```bash
GET http://localhost:8080/api/cliente
```

Registrar un nuevo cliente

```bash
POST http://localhost:8080/api/cliente
```
Content-Type: application/json

{
  "nombre": "Ana",
  "apellido": "Pérez",
  "email": "ana.perez@demo.es",
  "metodoPago": "Tarjeta",
  "rol": "USER"
}

Obtener lista de medicamentos

```bash
GET http://localhost:8080/api/medicamento
```
Registrar una compra

```bash
POST http://localhost:8080/api/compra
```
Content-Type: application/json

{
  "clienteId": 1,
  "medicamentos": [
    { "id": 5, "cantidad": 2 },
    { "id": 3, "cantidad": 1 }
  ]
}

Eliminar un cliente

```bash
DELETE http://localhost:8080/api/cliente/1
```
Para ver la lista completa de endpoints expuestos por la API, puedes visitar la interfaz de Swagger en:

```bash
http://localhost:8080/swagger-ui.html
```

Autenticación y Seguridad
El proyecto incluye autenticación basada en JWT. Algunos endpoints requieren autorización. Para los accesos públicos y privados se usan filtros configurados mediante Spring Security.

Pruebas automatizadas
La suite de tests utiliza JUnit 5, Mockito y Spring Boot Test. Incluye pruebas funcionales, de integración y de rendimiento (con junitperf).

Cobertura con JaCoCo
JaCoCo está configurado para exigir una cobertura mínima del 95% tanto en instrucciones como en ramas. Para generar el informe de cobertura:

```bash
mvn clean test
```

El reporte estará disponible en:

```bash
target/site/jacoco/index.html
```

Generar documentación técnica (Doxygen)
Puedes generar documentación a partir de los comentarios del código fuente con:

```bash
mvn site
```
Esto ejecuta Doxygen usando el archivo doxyfile presente en la raíz del proyecto.

Empaquetar la aplicación
Para empaquetar el proyecto con todas sus dependencias:

```bash
mvn clean package
```
Esto generará un archivo .jar en target/, por ejemplo:

```bash
target/rest-api-0.0.1-SNAPSHOT.jar
```

Puedes ejecutarlo con:

```bash
java -jar target/rest-api-0.0.1-SNAPSHOT.jar
```
Referencias
Guía completa para construir REST APIs con Spring Boot
Documentación oficial de Spring REST
Documentar APIs con Swagger en Spring Boot
Ejemplo Spring Boot + MySQL + Docker