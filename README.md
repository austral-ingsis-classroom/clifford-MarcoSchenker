[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/eV4hUM6u)
# Ingeniería de Sistemas - Trabajo práctico "Cli.FF.Ord"

## Consigna
- Implementar un CLI que permita ejecutar ciertos comandos en un file system virtual. No hace falta que las operaciones se persistan sobre un file system real. Esto quiere decir que también deben implementar
un file system en memoria. 
- También deberán implementar los tests planteados.
- Deberán usar los patrones "Command" y "Composite" para implementar este TP.


## Comandos a implementar.

#### ls
**Descripción**: listar los elementos del directorio actual. Puede recibir el _flag_ --ord que puede recibir 2 parámetros _asc_ o _desc_ según se quiera un orden ascendente o descendente. En caso de
que no se agregue un parámetro, los elementos tendrán que ser listados en el orden en el que fueron creados. Si el directorio está vacio, devolverá una linea vacia.

**Parámetros**: ninguno

**Opciones**:
- "--ord": define el algoritmo que tiene que usar para ordenar el output de los elements. Puede recibir 2 valores: _asc_ o _desc_.

**Output**: Los elementos de primer nivel del directorio.

**Ejemplos**
```bash
$ ls --ord=asc
a b c d e
```

#### cd
**Descripción**: cambia el directorio actual por el directorio que recibe como parámetro. Falla si el directorio no existe o es un archivo. Tené en cuenta los siguientes casos:
  1. Si el parámetro recibido es "..", se debe mover al directorio padre.
  2. Si el parámetro es ".", se debe quedar en el directorio actual pero no debe fallar.
  3. El parámetro puede ser el nombre de un directorio o una ruta hacia un directorio. Los componentes de una ruta van separadas por el caracter "/".
  4. Si la ruta empieza con "/", tendrán que empezar desde el _root_ del filesystem.

**Parámetros**: el nombre del directorio al cual moverse.

**Opciones**: Ninguno

**Output**: tendrá que devolver un mensaje con el formato "Moved to directory: '%1'" donde %1 es el nombre del directorio al cual se movió

**Ejemplos**
```bash
$ cd musica
Moved to directory: 'musica'
```

#### touch
**Descripción**: crea un archivo vacio con el nombre que recibe como parámetro en el directorio actual. No se puede crear archivos que tengan "/" ni espacio en el nombre.

**Parámetros**: el nombre del archivo a crear.

**Opciones**: Ninguno

**Output**: tendrá que devolver un mensaje con el formato "'%1' file created" donde %1 es el nombre del archivo que creó.

**Ejemplos**
```bash
$ touch ciclon.txt
'ciclon.txt' file created
```

#### mkdir
**Descripción**: crea un directorio vacio con el nombre que recibe como parámetro en el directorio actual. No se puede crear directorios que tengan "/" ni espacio en el nombre.

**Parámetros**: el nombre del directorio a crear.

**Opciones**: Ninguno

**Output**: tendrá que devolver un mensaje con el formato "'%1' directory created" donde %1 es el nombre del directorio que creó.

**Ejemplos**
```bash
$ mkdir san-lorenzo-dir
'san-lorenzo-dir' file created
```

#### rm
**Descripción**: borra el archivo o directorio que recibe como parámetro. Deberá usar la opción --recursive para borrar directorios.
Si no se usa la opción --recursive y si quiere borrar un directorio entonces deberá devolver un mensaje de error.

**Parámetros**: el nombre del archivo o directorio a borrar.

**Opciones**:
- "--recursive": borra recursivamente todo el árbol de archivos. No recibe argumentos.

**Output**: tendrá que devolver un mensaje con el formato "'%1' removed" donde %1 es el nombre del archivo o directorio que se borró.

**Ejemplos**
```bash
$ rm san-lorenzo-dir
'san-lorenzo-dir' removed

$ rm ciclon.txt
'ciclon.txt' removed

$ rm --recursive san-lorenzo-dir
'san-lorenzo-dir' removed
```

#### pwd
**Descripción**: imprime la ruta que tiene el cursor en el file system.

**Parámetros**: ninguno

**Argumentos**: ninguno

**Output**: la ruta del cursos en el file system.

**Ejemplo**
```bash
$ pwd
/Users/clifford/files
```
