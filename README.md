
### Escuela Colombiana de Ingeniería

### Arquitecturas de Software – ARSW
## Laboratorio Programación concurrente, condiciones de carrera, esquemas de sincronización, colecciones sincronizadas y concurrentes - Caso Dogs Race

### Descripción:
Este ejercicio tiene como fin que el estudiante conozca y aplique conceptos propios de la programación concurrente.
### Integrantes:
Brayan Jiménez
Mateo Quintero
### Parte I 
Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. 
   Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos 
   entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. 
   Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.
   
![](img/img_1.png)

2. Modifique el programa para que, en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.

![](img/img_2.png)

3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.

![](img/img_3.png)

### Parte II 


Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

RT: Se utilizó un for para hacer join() a los hilos antes de que se mostrara algún resultado 
para evitar resultados erróneos:

![](./img/img_4.png)

Parte III

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.

    RT: Se creo un ciclo que recorriera cada uno de los 'galgos' y utilizara el método join() antes de
    imprimir el galgo ganador, garantizando que todos terminen antes de dar un ganador.
    > 
    > Implementación:
      ![](./img/img_5.png)
    > 
    > Resultado:
      ![](./img/img_6.png)
    
    
2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.
    
    RT: Se encontraron 3 condiciones de carrera:
    * Al momento de consultar por la variable ultimaPosicionAlcanzada.
    * Al momento de modificar la variable ultimaPosicionAlcanzada.
    * El imprimir por consola el orden de llegada (1,2,3,4...17).

3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.
    
    > RT: Para el correcto funcionamiento dentro de estas zonas críticas
      se decidió crear un bloque que contenga el proceso de Lectura, Modificación y Escritura.
    >
    > Implementación: Se encuentra en la clase Galgo, en la linea 38.
    ![](./img/img_7.png)
    >
    > Resultado en consola:
    > 
    ![](./img/img_8.png)

4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos 
    de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).
    
    RT: Se agregó una ventana emergente que le avisa cuando la carrera fue pausada o reanudada.

    Implementación: Se agrega una variable que va a dictaminar cuando se debe o no estar ejecutando (boolean corriendo), 
    esta es modificada al inicio de la ejecución y se le asigna el valor true, para parar la ejecución se hace uso del 
    método setCorriendo(boolean estado), y se le da un valor false, mientras que si se desea reanudar la ejecución se 
    asigna un valor true que a su vez en el método realizara el llamado a notifyAll()
    >
    > ![](./img/img_9.png)
    > 
    > Resultado con Stop:
      ![](./img/img_10.png)
    > 
    > Resultado con Continue:
      ![](./img/img_11.png)
