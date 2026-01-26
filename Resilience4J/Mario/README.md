Requerimientos Resilience4J – Ejercicio Mario

JDK JAVA: 25

Springboot estable: 3.4.1

Resilience4J: 2.2.0

Maven: 3.9.11



1\.	Iniciar instancia Springboot con spingboot inicializer desde la pagina oficial.

2\.	Agregar a instancia Springboot dependencias web, circuitbreaker, aop, actuator.

3\.	Configurar versiones en POM, para no tener conflictos con versiones, actualizar versiones con plantilla POM de ejercicio básico.

4\.	Instalar chocolate con el siguiente comando:

Set-ExecutionPolicy Bypass -Scope Process -Force; \[System.Net.ServicePointManager]::SecurityProtocol = \[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

5\.	Instalar Maven con el siguiente comando

choco install maven

6\.	En powerShell, sobre la raíz del proyecto ejecutar “.\\mvnw clean install” para actualizar las versiones de las librerías e instalar las que no se encontrar en inicializer.



