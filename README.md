## Herramientas necesarias:

	- GIT     ( Version homologada: 2.11.0 )
	- Maven   ( Version homologada: 3.3.9 )
	- Java    ( Version homologada: 1.8.0_181, vendor: Oracle Corporation )
	- Eclipse ( Version homologada: Photon Release (4.8.0) )
	- curl    ( Version homologada: 7.52.1 )


## Comando para ejecutar la aplicacion base utilizando Maven:

	mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=dev"


## Setup del proyecto en Eclipse:

	- Importar como proyecto maven existente.
	- Abrir vista "Boot Dashboard".
	- Click derecho sobre el proyecto + "(Re)start" / "(Re)debug".
	
	
## Probando controllers:

	Obteniendo version de build:
	curl -X GET "http://localhost:8080/api/version/number"
	
	Realizando POST echo test:
	curl -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d "{\"message\":\"mensaje recibido\"}" http://localhost:8080/api/echo
		
	Obtener suma de numeros:
	curl -X GET "http://localhost:8080/api/math/sum?valores=1.1&valores=2.22&valores=3.33"
	
	Obtener todos los paises:
	curl -X GET "http://localhost:8080/api/country/findAll"
	
	
## Workflow:
	
	- Validar funcionamiento del proyecto básico provisto ejecutando la aplicacion via Maven y utilizando los requests de prueba (sección "Probando Controllers")
	mediante la utilizacion de "curl". Puede utilizarse cualquier otra herramienta en lugar de ejecutar "curl" por consola.
	
	- Desde Eclipse, importar el proyecto (importar como proyecto Maven existente).
	
	- Resolver los ejercios indicados. Ver enuciados en archivo "EJERCICIOS.md".
	
	- Agregar un archivo llamado "RESPUESTAS.md" que contenga todas las "curl requests" que permitan probar las funcionalidades creadas. 
	
	- Crear nuevo proyecto git (en GitHub, BitBucket o similar). Pushear en este nuevo espacio el proyecto con todos los ejercicios resueltos y enviar la url del repositorio 
	para revisión de las soluciones implementadas.  
	
	- Asegurarse de que los permisos de acceso al repositorio sean adecuados para que quien reciba la url pueda realizar el clonado.
