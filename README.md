# Redes de Ordenadores - Servidor #

Este es el repositorio de la parte de servidor del proyecto de redes de ordenadores. Será una aplicación para Android que controlará los sensores (por ahora simulados) que tendrá el paciente, para poder posteriormente enviar los datos al médico que lo requiera.

## Lógica del servidor ##

Habrá un hilo de ejecución por cada cliente, dispuesto en todo momento para enviar y recibir datos. Además, dispondremos de otro hilo que será el encargado de comprobar los clientes periódicamente para comprobar si hay nuevas comunicaciones.

En un futuro, de implementarse un prototipo con Arduino, el sistema estará comunicado por medio de Bluetooth o Wifi, y se accederá a los sensores según sea requerido. Todo esto se gestionará asíncronamente en otro hilo de ejecución.