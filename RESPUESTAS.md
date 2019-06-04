# Solución prueba técnica Tecso

##Notas

* No se agregan URLS porque todas se encuentran procesadas 
 y alojadas en el front para su funcionalidades.
 
* Cuando se cree un usuario por favor colocar un correo valido(Real)
ya que la aplicacion envia un correo de confirmacion de la cuenta.
 
* Pero para realizar pruebas de endpoint se anexa 'Tecso.postman_collection.json'
para facilitar las pruebas a traves de postman.

* La aplicación se corre manualmente en MainApplication.

### Conexión a la base de datos PostgreSQL

Reemplazar datasource.username y datasource.password por los datos
de la base de datos local.

Debe existir una base de datos llamada postgres, ya que las tablas 
son creadas automáticamente por Flywaydb. Porque ejecuta automaticamente los scripts 
alojados /resources/db.migration

