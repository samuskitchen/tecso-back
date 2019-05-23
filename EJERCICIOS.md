
#### CONSIDERACIONES GENERALES ####

- Ambos ejercicios se resuelven en el mismo proyecto, no es necesario separarlos. De hecho hay cierta relación lógica entre ambos a nivel de entidades (titular/cuenta).

- El archivo de "RESPUESTAS.md" (en donde se indicarían las URL para el ejercicio 2) es el lugar donde debería incluirse cualquier información relacionada a la solución de los ejercicios que se considere necesaria.

- Las funcionalidades "Bonus" no reemplazan a las requeridas. Por favor no invertir tiempo en los puntos "Bonus" en detrimento de lo requerido.

- Teniendo en cuenta que los ejercicios requeridos son relativamente simples, se espera que los candidatos postulados a búsquedas de seniority mas alto resuelvan algunos de los puntos "Bonus".



=====================================================================================================================================================================================
#### Ejercicio 1 ####

Desarollar todos los artefactos necesarios en el backend (controller, servicio, repositorio, modelo, etc) y proyecto de frontend para implementar un CRUD + listado de **titulares de cuentas corrientes**, según las siguientes consideraciones:

- Un titular puede ser tanto una persona física como jurídica.
	
- Si el titular es una persona física, los atributos requeridos serán los siguientes: nombre (máximo 80 caracteres), apellido (máximo 250 caracteres) y CC.
	
- Si el titular es una persona jurídica, los atributos requeridos serán los siguientes: razón social (máximo 100 caracteres) y año de fundación.
	
- Tanto para las personas físicas como jurídicas, se requiere RUT.
	
- No pueden existir 2 titulares con el mismo RUT.



=====================================================================================================================================================================================
#### Ejercicio 2 ####

Implementar funcionalidades de alta, consulta, baja y listado de cuentas corrientes y movimientos. Tener en cuenta las siguientes consideraciones:

- Una cuenta corriente puede tener n movimientos.

- Los movimientos no pueden eliminarse ni modificarse.

- Las cuentas solo pueden eliminarse si no tienen movimientos asociados.

- Las cuentas poseen un número de cuenta (valor requerido y único), una moneda (peso, dolar, euro) y un saldo (valor numérico de 2 decimales).

- Los movimientos poseen fecha (tomar horario UTC), tipo de movimiento (débito o crédito), descripción (200 caracteres) e importe (numérico de 2 decimales).

- Cada vez que se incorpora un nuevo movimiento, y dependiendo de su tipo, se debe actualizar el saldo de la cuenta asociada.

- Si un movimiento genera un descubierto mayor a 1000 (para cuentas en pesos), 300 (para cuentas en dólares) o 150 (para cuentas en euros)... se deberá rechazar.



En la capa REST, los endpoints requeridos son los siguientes:
   1. *crear cuenta*
   2. *eliminar cuenta*
   3. *listar cuentas*
   4. *agregar movimiento*
   5. *listar movimientos x cuenta* (ordenados de forma decreciente, por fecha)

Se deberán aplicar todas las validaciones funcionales que se consideren oportunas, informando los errores de manera conveniente.

Al no ser requerida la implementación del frontend para el ejercicio 2, se requiere que se provean las url para realizar las pruebas de la funcionalidad 
implementada en el backend (en archivo "RESPUESTAS.md").



=====================================================================================================================================================================================
#### Bonus (funcionalidad no requerida) ####

- Crear un proyecto front end (Angular o React) con las pantallas necesarias para invocar las funcionalidades del backend. No es necesario que la interfaz sea muy sofisticada.

- Escribir pruebas unitarias (Junit) de los distintos componentes.

- Desplegar la aplicación en algún servidor público y especificar la forma de acceder (incluir info en el archivo "RESPUESTAS.md").

- Reemplazar la base de datos configurada en memoria (H2) por una a elección de entre las siguientes: PostgreSQL o MySQL.
	De hacerlo, la solución deberá incluir:
		- Información sobre la BD utilizada.
		- Script de creación e inicialización de datos.
		- Modificación correspondiente en la configuración del datasource.
		- Cualquier otra información que considere necesaria.

- Incluir funcionalidad de registración y autenticación de usuario.

- Incluir funcionalidad de autorización de acceso a las funcionalidades desarrolladas.
		
- Implementar funcionalidad básica de navegación entre las funcionalidades y la landing page ("Volver", "Inicio" o similar), para evitar la necesidad de navegar manualmente por url.

- Implementar la funcionalidad del ejercicio 2 en el frontend (aplican las mismas consideraciones que para el ejercicio 1).
