/*
Desarrolla una aplicación Java que utilice JDBC para interactuar con una base de datos MariaDB que contiene dos tablas relacionadas en una relación de muchos a muchos. Las tablas se denominan "Estudiantes" y "Cursos". Cada tabla debe contener los siguientes atributos:

Tabla "Estudiantes"
    ID (Clave primaria)
    Nombre
    Edad
    Dirección
    Correo electrónico
Tabla "Cursos"
    ID (Clave primaria)
    Nombre del curso
    Descripción
    Créditos

Además, crea una tercera tabla llamada "Inscripciones" que represente la relación muchos a muchos entre estudiantes y cursos. 
Esta tabla debe contener al menos los siguientes atributos:

Tabla "Inscripciones"
//ID (Clave primaria)
//ID del estudiante (clave foránea)
//ID del curso (clave foránea)
//Fecha de inscripción

La aplicación debe ser capaz de realizar las siguientes operaciones:

Establecer una conexión a la base de datos MariaDB. En caso de que no exista alguna de las tablas, la cree.
Insertar nuevos estudiantes y cursos en las tablas respectivas.
Registrar inscripciones de estudiantes en cursos.
Consultar la lista de estudiantes inscritos en un curso específico.
Consultar los cursos en los que un estudiante particular está inscrito.
Actualizar información de estudiantes o cursos.
Eliminar registros de estudiantes, cursos o inscripciones.

Asegúrate de manejar excepciones adecuadamente, crear las relaciones entre las tablas y aislar cada una de las consultas en un método.
 */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.E3_ConexionJDBCconMariaDB;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Luis Quintano
 */
public class InsertarDatosYConsultas_MariaDB {
    public static final String DRIVER = Configuration.DRIVER;
    public static final String URLCONNECTION = Configuration.URLCONNECTION;
    public static final String USER = Configuration.USER;
    public static final String PASSW = Configuration.PASSW;

    static {  //los bloques static se llaman solo la primera vez que alguien ejecuta o hace un new de esta clase.
        try {
            Class.forName(DRIVER);  //cargamos el driver mediante la ruta que está en la variable tipo final.
        } catch (Exception e) {
            System.err.println("ERROR al cargar el Driver");
            System.exit(1);  //termina el programa.
        }
        
        //para crear las tablas si no se han creado aún:
        String queryTabla1 = "CREATE TABLE if not exists estudiantes("
                           + " id int AUTO_INCREMENT PRIMARY KEY,"
                           + " nombre VARCHAR(20),"
                           + " edad int,"
                           + " direccion VARCHAR(70),"
                           + " correo_electronico VARCHAR(80)"
                           + ");";

        String queryTabla2 = "CREATE TABLE if not exists cursos("
                           + " id int AUTO_INCREMENT PRIMARY KEY,"
                           + " nombre VARCHAR(20),"
                           + " descripcion VARCHAR(150),"
                           + " creditos int"
                           + ");";

        String queryTabla3 = "CREATE TABLE if not exists inscripciones("
                           + " id int AUTO_INCREMENT PRIMARY KEY,"
                           + " id_estudiante int,"
                           + " id_curso int,"
                           + " fecha_inscripcion DATE,"
                           + " FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                           + " FOREIGN KEY (id_curso) REFERENCES cursos(id) ON DELETE CASCADE ON UPDATE CASCADE"
                           + ");";

        PreparedStatement sentencia = null;
        try (Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);) {
            //para crear la tabla de estudiantes si no existe:
            sentencia = con.prepareStatement(queryTabla1); //sentencia SQL
            int r1 = sentencia.executeUpdate();  //el .executeUpdate() se utiliza para introducir o actualizar datos.

            //para crear la tabla de cursos si no existe:
            sentencia = con.prepareStatement(queryTabla2); //sentencia SQL
            int r2 = sentencia.executeUpdate();  //el .executeUpdate() se utiliza para introducir o actualizar datos.

            //para crear la tabla de inscripciones si no existe:
            sentencia = con.prepareStatement(queryTabla3); //sentencia SQL
            int r3 = sentencia.executeUpdate();  //el .executeUpdate() se utiliza para introducir o actualizar datos.
            
            System.out.println();
            System.out.println();
            System.out.println();
        } catch (SQLException e) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar la primera sentencia");
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opcion;
        
        do {
            opcion = menu(sc);
            if (opcion.equals("1.1")) {
                insertarEstudiante(sc);
            } else if (opcion.equals("1.2")) {
                insertarCurso(sc);
            } else if (opcion.equals("1.3")) {
                registrarInscripcionesDeEstudiantesEnCursos(sc);
            } else if (opcion.equals("2.0")) {
                mostrarTodosLosEstudiantes();
                mostrarTodosLosCursos();
                mostrarTodasLasInscripciones();
            } else if (opcion.equals("2.1")) {
                consultaListaEstudiantesDeUnCurso(sc);
            } else if (opcion.equals("2.2")) {
                consultaCursosDeUnEstudiante(sc);
            } else if (opcion.equals("3.1")) {
                actualizarInfoEstudianteOCurso(sc);
            } else if (opcion.equals("4.1")) {
                eliminarDatosEstudianteCursoOInscripcion(sc);
            }
        } while (!opcion.equalsIgnoreCase("S"));
    }

    public static String menu(Scanner sc) {
        System.out.println("---  ---  ---  --- Bienvenido-al-Menú-de-la-BBDD-Instituto ---  ---  ---  ---");
        String opcion = "";
        System.out.println("1. Insertar datos");
        System.out.println("2. Consultar datos");
        System.out.println("3. Actualizar datos");
        System.out.println("4. Eliminar datos");
        System.out.println("S. Salir");

        do {
            System.out.print("Selecciona una opción: ");
            opcion = sc.nextLine().toUpperCase();
            System.out.println();
        } while (!(opcion.equals("1") || opcion.equals("2") || opcion.equals("3") || opcion.equals("4") || opcion.equals("S")));

        switch (opcion) {
            case "1":
                System.out.println("--- 1. OPCIONES PARA INSERTAR DATOS ---");
                System.out.println("1.1. Insertar Estudiante");
                System.out.println("1.2. Insertar Curso");
                System.out.println("1.3. Registrar una inscripción de un estudiante en un cursos");
                System.out.println("V. Volver hacia atrás");
                System.out.println("S. Salir");
                do {
                    System.out.print("Selecciona una opción para insertar datos: ");
                    opcion = sc.nextLine().toUpperCase();
                } while (!(opcion.equals("1.1") || opcion.equals("1.2") || opcion.equals("1.3")
                        || opcion.equals("V") || opcion.equals("S")));
                break;
            case "2":
                System.out.println("--- 2. OPCIONES PARA CONSULTAR DATOS ---");
                System.out.println("2.0. Mostrar todos los estudiantes, todos los cursos y todas las inscripciones");
                System.out.println("2.1. Consultar la lista de estudiantes inscritos en un curso específico");
                System.out.println("2.2. Consultar los cursos en los que un estudiante particular está inscrito");
                System.out.println("V. Volver hacia atrás");
                System.out.println("S. Salir");
                do {
                    System.out.print("Selecciona una opción de consulta: ");
                    opcion = sc.nextLine().toUpperCase();
                } while (!(opcion.equals("2.0") || opcion.equals("2.1") || opcion.equals("2.2")
                        || opcion.equals("V") || opcion.equals("S")));
                break;
            case "3":
                System.out.println("--- 3. OPCIONES PARA ACTUALIZAR DATOS ---");
                System.out.println("3.1. Actualizar información de estudiantes o cursos");
                System.out.println("V. Volver hacia atrás");
                System.out.println("S. Salir");
                do {
                    System.out.print("Selecciona una opción para actualizar datos: ");
                    opcion = sc.nextLine().toUpperCase();
                } while (!(opcion.equals("3.1") || opcion.equals("V") || opcion.equals("S")));
                break;
            case "4":
                System.out.println("--- 4. OPCIONES PARA ELIMIAR DATOS ---");
                System.out.println("4.1. Eliminar registros de estudiantes, cursos o inscripciones.");
                System.out.println("V. Volver hacia atrás");
                System.out.println("S. Salir");
                do {
                    System.out.print("Selecciona una opción para eliminar datos: ");
                    opcion = sc.nextLine().toUpperCase();
                } while (!(opcion.equals("4.1") || opcion.equals("V") || opcion.equals("S")));
                break;
            default:
                break;
        }
        return opcion;
    }
    
    public static void insertarEstudiante(Scanner sc) {
        System.out.println("---------------------------");
        System.out.println("1.1. Insertar Estudiante");

        String query = "insert into estudiantes(nombre, edad, direccion, correo_electronico) values(?,?,?,?)";
        String nombre;
        int edad;
        String direccion;
        String correo_electronico;

        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL
                ) {
            System.out.print("Introduce el NOMBRE del estudiante: ");
            nombre = sc.nextLine();
            do {
                System.out.print("Introduce la EDAD del estudiante: ");
                edad = Integer.parseInt(sc.nextLine());
                
                if (edad <= 0) {
                    System.err.println("ERROR al introducir la edad, tiene que ser mayor a 0 años");
                }
            } while (edad <= 0);
            System.out.print("Introduce la DIRECCION del estudiante: ");
            direccion = sc.nextLine();
            System.out.print("Introduce el CORREO_ELECTRONICO del estudiante: ");
            correo_electronico = sc.nextLine();
            
            sentencia.setString(1, nombre);
            sentencia.setInt(2, edad);
            sentencia.setString(3, direccion);
            sentencia.setString(4, correo_electronico);

            int r = sentencia.executeUpdate();  //el .executeUpdate() se utiliza para introducir o actualizar datos.
            if (r == 1) {
                System.out.println("Estudiante Creado");
            } else {
                System.err.println("ERROR al crear el estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR, introduce una edad válida.");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL: " + ex);
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void insertarCurso(Scanner sc) {
        System.out.println("---------------------------");
        System.out.println("1.2. Insertar Curso");

        String query = "insert into cursos(nombre, descripcion, creditos) values(?,?,?)";
        String nombre;
        String descripcion;
        int creditos;

        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL
                ) {
            System.out.print("Introduce el NOMBRE del curso: ");
            nombre = sc.nextLine();
            System.out.print("Introduce la DESCRIPCIÓN del curso: ");
            descripcion = sc.nextLine();
            System.out.print("Introduce los CREDITOS del curso: ");
            creditos = Integer.parseInt(sc.nextLine());

            sentencia.setString(1, nombre);
            sentencia.setString(2, descripcion);
            sentencia.setInt(3, creditos);
            int r = sentencia.executeUpdate();  //el .executeUpdate() se utiliza para introducir o actualizar datos.
            if (r == 1) {
                System.out.println("Curso Creado");
            } else {
                System.err.println("ERROR al crear el curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void registrarInscripcionesDeEstudiantesEnCursos(Scanner sc) {
        boolean b = false, b2 = false;
        
        System.out.println("---------------------------");
        System.out.println("1.3. Registrar una inscripción de un estudiante en un cursos");

        String query = "";
        int idEstudiante;
        int idCurso;
        String fechaInscripcion;
        //LocalDate fechaInscripcion = LocalDate.of(year, mes, diaMes);

        PreparedStatement sentencia = null;
        try (Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);) {
            b = mostrarEstudiantesPorFiltroNombre(sc, query);
            b2 = mostrarCursosPorFiltroNombre(sc, query);
            
            if (b || b2) {
                //se empieza a insertar las inscripciones:
                query = "insert into inscripciones(id_estudiante, id_curso, fecha_inscripcion) values(?,?,?)";
                sentencia = con.prepareStatement(query); //sentencia SQL

                System.out.print("Introduce el ID del estudiante: ");
                idEstudiante = Integer.parseInt(sc.nextLine());
                System.out.print("Introduce el ID del curso: ");
                idCurso = Integer.parseInt(sc.nextLine());
                System.out.print("Introduce la FECHA de la inscripción (YYYY-MM-DD): ");
                fechaInscripcion = sc.nextLine();
                Date fecha = Date.valueOf(fechaInscripcion);  //introducimos la variable "fechaInscripcion" de tipo String a una variable tipo Date.
    //            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM/DD");
    //            LocalDate fi = LocalDate.parse(fechaInscripcion, formatter);

                sentencia.setInt(1, idEstudiante);
                sentencia.setInt(2, idCurso);
                sentencia.setDate(3, fecha);
                int r = sentencia.executeUpdate();
                if (r == 1) {
                    System.out.println("Inscripción Creada");
                } else {
                    System.err.println("ERROR al crear la inscripción");
                }
            } else {
                System.err.println("No existen estudiantes o cursos");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el PreparedStatement");
                }
            }
        }
    }
    
    public static void consultaListaEstudiantesDeUnCurso(Scanner sc) {
        boolean b = false;
        
        System.out.println("---------------------------");
        System.out.println("2.1. Consultar la lista de estudiantes inscritos en un curso específico");

        String query = "select e.id, e.nombre, e.edad, e.direccion, e.correo_electronico, c.nombre "
                + " from estudiantes as e "
                + " inner join inscripciones as i "
                + " on e.id = i.id_estudiante "
                + " inner join cursos as c "
                + " on c.id = i.id_curso "
                + " where c.id = ? ";

        ResultSet resultado = null;
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            b = mostrarCursosPorFiltroNombre(sc, query);
            
            if (b) {
                System.out.print("Introduce el id del curso el cual quieres ver que estudiantes están inscritos a él: ");
                int id = Integer.parseInt(sc.nextLine());

                sentencia.setInt(1, id);  //en la posición 1 de interrogaciones ponemos el nombre del curso.
                resultado = sentencia.executeQuery(); //ejecuta la consulta y lo guarda en la variable "resultado".

                if (resultado.next()) {
                    System.out.println("\nEstudiantes inscritos al curso con id " + id + ":");
                    System.out.println("ID_ESTUDIANTE" + "\t" + "\t" + "NOMBRE" + "\t" + "\t" + "EDAD" + "\t" + "\t" + "DIRECCION" + "\t" + "\t" + "CORREO_ELECTRONICO");
                    while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                        System.out.println(resultado.getInt("e.id") + "\t" + "\t" + "\t"
                                + resultado.getString("e.nombre") + "\t" + "\t"
                                + resultado.getInt("e.edad") + "\t" + "\t"
                                + resultado.getString("e.direccion") + "\t" + "\t" + "\t"
                                + resultado.getString("e.correo_electronico")
                        );
                    }
                    System.out.println();
                } else {
                    System.out.println("No hay estudiantes en este curso");
                }
            } else {
                System.err.println("No existe ningún curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex){ 
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
    }
    
    public static void consultaCursosDeUnEstudiante(Scanner sc) {
        boolean r = false;
        System.out.println("---------------------------");
        System.out.println("2.2. Consultar los cursos en los que un estudiante particular está inscrito");

        String query = "select c.id, c.nombre, c.descripcion, c.creditos"
                     + " from cursos as c "
                     + " inner join inscripciones as i "
                     + " on c.id = i.id_curso "
                     + " inner join estudiantes as e "
                     + " on e.id = i.id_estudiante "
                     + " where e.id = ? ";

        ResultSet resultado = null;
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            r = mostrarEstudiantesPorFiltroNombre(sc, query);
            
            if (r) {
                System.out.print("Introduce el ID del Estudiante: ");
                int id = Integer.parseInt(sc.nextLine());

                sentencia.setInt(1, id);
                resultado = sentencia.executeQuery(); //ejecuta la consulta y lo guarda en la variable "resultado".

                if (resultado.next()) {
                    System.out.println("\nCursos a los que está inscrito el estudiante con id " + id + ":");
                    System.out.println("ID_CURSO" + "\t" + "\t" + "NOMBRE" + "\t" + "\t" + "\t" + "\t" + "DESCRIPCION" + "\t" + "\t" + "\t" + "CREDITOS");
                    while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                        System.out.println(resultado.getInt("c.id") + "\t" + "\t" + "\t"
                                + resultado.getString("c.nombre") + "\t" + "\t" + "\t"
                                + resultado.getString("c.descripcion") + "\t" + "\t" + "\t"
                                + resultado.getInt("c.creditos")
                        );
                    }
                    System.out.println();
                } else {
                    System.out.println("Este estudiante no está inscrito a ningún curso");
                }
            } else {
                System.err.println("No existe ningún estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex){ 
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
    }
    
    public static void actualizarInfoEstudianteOCurso(Scanner sc) {
        String query = "", opcion = "";
        int id = 0;

        System.out.println("---------------------------");
        System.out.println("3.1. Actualizar información de estudiantes o cursos");

        System.out.println("Actualizar datos de un Estudiante (E) o de un Curso (C).");
        System.out.print("Introducir: E o C: ");
        opcion = sc.nextLine().toUpperCase();

        if (opcion.equals("E")) {
            actualizarEstudiante(sc, query, id);
        } else if (opcion.equals("C")) {
            actualizarCurso(sc, query, id);
        } else {
            System.err.println("Opción no valida");
        }
    }
    
    public static void actualizarEstudiante(Scanner sc, String query, int id) {
        boolean b = false;
        String respuesta = "";
        
        try {
            b = mostrarEstudiantesPorFiltroNombre(sc, query);
            
            if (b) {
                System.out.print("Introduce el ID del estudiante al que quieres actualizar la información: ");
                id = Integer.parseInt(sc.nextLine());

                do {
                    System.out.println("\nTienes estos campos que puedes cambiar de un estudiante: nombre, edad, direccion, correo_electronico");
                    System.out.print("Que dato queres actualizar: ");
                    String dato = sc.nextLine().toLowerCase();

                    switch (dato) {
                        case "nombre" -> actualizarNombreEstudiante(sc, query, id);
                        case "edad" -> actualizarEdadEstudiante(sc, query, id);
                        case "direccion" -> actualizarDireccionEstudiante(sc, query, id);
                        case "correo_electronico" -> actualizarCorreoEstudiante(sc, query, id);
                        default -> System.err.println("ERROR, introduzca un dato valido para cambiarlo");
                    }
                    System.out.print("Quieres actualizar más datos de ese estudiante?(S/N): ");
                    respuesta = sc.nextLine().toUpperCase();
                } while (respuesta.equals("S"));
            } else {
                System.err.println("No existe ningún estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarNombreEstudiante(Scanner sc, String query, int id) {
        query = "update estudiantes "
              + " set nombre = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce el nuevo NOMBRE que quieres ponerle al estudiante: ");
            String nombre = sc.nextLine();
            
            sentencia.setString(1, nombre);  //metemos en la posición 1 de interrogaciones de la consulta el nombre que nos han metido en la variable.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Nombre del Estudiante Actualizado con Exito");
            } else {
                System.err.println("ERROR al actiualizar el nombre del estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarEdadEstudiante(Scanner sc, String query, int id) {
        query = "update estudiantes "
              + " set edad = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce la nueva EDAD que quieres ponerle al estudiante: ");
            int edad = Integer.parseInt(sc.nextLine());
            
            sentencia.setInt(1, edad);  //metemos en la posición 1 de interrogaciones de la consulta la edad que nos han metido en la variable.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Edad del Estudiante Actualizada con Exito");
            } else {
                System.err.println("ERROR al actualizar la edad del estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarDireccionEstudiante(Scanner sc, String query, int id) {
        query = "update estudiantes "
              + " set direccion = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce la nueva DIRECCION que quieres ponerle al estudiante: ");
            String direccion = sc.nextLine();
            
            sentencia.setString(1, direccion);  //metemos en la posición 1 de interrogaciones de la consulta la direccion que nos han metido en la variable.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Direccion del Estudiante Actualizada con Exito");
            } else {
                System.err.println("ERROR al actualizar la direccion del estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarCorreoEstudiante(Scanner sc, String query, int id) {
        query = "update estudiantes "
              + " set correo_electronico = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce el nuevo CORREO que quieres ponerle al estudiante: ");
            String correo = sc.nextLine();
            
            sentencia.setString(1, correo);  //metemos en la posición 1 de interrogaciones de la consulta el correo que nos han metido en la variable.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Correo Electronico del Estudiante Actualizado con Exito");
            } else {
                System.err.println("ERROR al actualizar el correo electronico del estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarCurso(Scanner sc, String query, int id) {
        boolean b = false;
        String respuesta = "";
        
        try {
            b = mostrarCursosPorFiltroNombre(sc, query);
            
            if (b) {
                System.out.print("Introduce el ID del curso al que quieres actualizar la información: ");
                id = Integer.parseInt(sc.nextLine());

                do {
                    System.out.println("\nTienes estos campos que puedes cambiar de un curso: nombre, descripcion, creditos");
                    System.out.print("Que dato queres actualizar: ");
                    String dato = sc.nextLine().toLowerCase();

                    switch (dato) {
                        case "nombre" -> actualizarNombreCurso(sc, query, id);
                        case "descripcion" -> actualizarDescripcionCurso(sc, query, id);
                        case "creditos" -> actualizarCreditosCurso(sc, query, id);
                        default -> System.err.println("ERROR, introduzca un dato valido para cambiarlo");
                    }
                    System.out.print("Quieres actualizar más datos de ese curso?(S/N): ");
                    respuesta = sc.nextLine().toUpperCase();
                    if (!respuesta.equals("S")) {
                        break;
                    }
                } while (!respuesta.equals("N"));
            } else {
                System.err.println("No existe ningún curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarNombreCurso(Scanner sc, String query, int id) {
        query = "update cursos "
              + " set nombre = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce el nuevo NOMBRE que quieres ponerle al curso: ");
            String nombre = sc.nextLine();
            
            sentencia.setString(1, nombre);  //metemos en la posición 1 de interrogaciones de la consulta el nombre que nos han metido en la variable.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Nombre del Curso Actualizado con Exito");
            } else {
                System.err.println("ERROR al actiualizar el nombre del curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarDescripcionCurso(Scanner sc, String query, int id) {
        query = "update cursos "
              + " set descripcion = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce la nueva DESCRIPCION que quieres ponerle al curso: ");
            String descripcion = sc.nextLine();
            
            sentencia.setString(1, descripcion);  //metemos en la posición 1 de interrogaciones de la consulta la descripcion que nos han metido en la variable.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Descripcion del Curso Actualizada con Exito");
            } else {
                System.err.println("ERROR al actiualizar la descripcion del curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void actualizarCreditosCurso(Scanner sc, String query, int id) {
        query = "update cursos "
              + " set creditos = ? "
              + " WHERE id = ? ";
        
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);
                PreparedStatement sentencia = con.prepareStatement(query); //sentencia SQL.
                ) {
            System.out.print("Introduce los nuevos CREDITOS que quieres ponerle al curso: ");
            int creditos = Integer.parseInt(sc.nextLine());
            
            sentencia.setInt(1, creditos);  //metemos en la posición 1 de interrogaciones de la consulta los creditos que nos han dicho.
            sentencia.setInt(2, id);  //metemos en la posición 2 de interrogaciones de la consulta el id.
            
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Creditos del Curso Actualizados con Exito");
            } else {
                System.err.println("ERROR al actiualizar los creditos del curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void eliminarDatosEstudianteCursoOInscripcion(Scanner sc) {
        String query = "", opcion = "";
        int id = 0;

        System.out.println("---------------------------");
        System.out.println("4.1. Eliminar registros de estudiantes, cursos o inscripciones.");

        System.out.println("Eliminar datos de un Estudiante (E), de un Curso (C) o de una Inscripción (I).");
        System.out.print("(Introducir: E, C o I): ");
        opcion = sc.nextLine().toUpperCase();

        switch (opcion) {
            case "E":
                eliminarEstudiante(sc, query, id);
                break;
            case "C":
                eliminarCurso(sc, query, id);
                break;
            case "I":
                eliminarInscripcion(sc, query, id);
                break;
            default:
                System.err.println("ERROR, opción no valida");
                break;
        }
    }
    
    public static void eliminarEstudiante(Scanner sc, String query, int id) {
        boolean b = false;
        query = "delete from estudiantes "
                + " where id = ? ";

        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query);
                ) {
            b = mostrarEstudiantesPorFiltroNombre(sc, query);
            
            if (b) {
                System.out.print("Introduce el ID del estudiante que quieres eliminar: ");
                id = Integer.parseInt(sc.nextLine());

                sentencia.setInt(1, id);
                int r = sentencia.executeUpdate();
                if (r == 1) {
                    System.out.println("Estudiante Eliminado");
                } else {
                    System.err.println("ERROR al eliminar el estudiante");
                }
            } else {
                System.err.println("No existe ningún estudiante");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void eliminarCurso(Scanner sc, String query, int id) {
        boolean b = false;
        query = "delete from cursos "
                + " where id = ? ";

        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query);
                ) {
            b = mostrarCursosPorFiltroNombre(sc, query);
            if (b) {
                System.out.print("Introduce el ID del curso que quieres eliminar: ");
                id = Integer.parseInt(sc.nextLine());

                sentencia.setInt(1, id);
                int r = sentencia.executeUpdate();
                if (r == 1) {
                    System.out.println("Curso Eliminado");
                } else {
                    System.err.println("ERROR al eliminar el curso");
                }
            } else {
                System.err.println("No existe ningún curso");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void eliminarInscripcion(Scanner sc, String query, int id) {
        query = "delete from inscripciones "
                + " where id = ? ";

        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query);
                ) {
            mostrarTodasLasInscripciones();  //llamamos al método donde muestras todas las inscripciones que hay.

            System.out.print("Introduce el ID de la inscripción que quieres eliminar: ");
            id = Integer.parseInt(sc.nextLine());

            sentencia.setInt(1, id);
            int r = sentencia.executeUpdate();
            if (r == 1) {
                System.out.println("Inscripción Eliminada");
            } else {
                System.err.println("ERROR al eliminar la inscripción");
            }
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch (SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }
    
    public static void mostrarTodosLosEstudiantes() {
        System.out.println("------------------------ ESTUDIANTES ------------------------");
        String query = "select e.id, e.nombre, e.edad, e.direccion, e.correo_electronico "
                     + " from estudiantes as e "
                     + " order by e.id ";
        
        ResultSet resultado = null;
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //abrimos una sentencia sql.
                ) {
            resultado = sentencia.executeQuery(); //ejecuta la consulta y lo guarda en la variable "resultado".

            System.out.println("ID_ESTUDIANTE" + "\t" + "\t" + "NOMBRE" + "\t" + "\t" + "\t" + "\t" + "EDAD" + "\t" + "\t" + "\t" + "DIRECCION" + "\t" + "\t" + "\t" + "CORREO_ELECTRONICO");
            while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                System.out.println(resultado.getInt("e.id") + "\t" + "\t" + "\t"
                        + resultado.getString("e.nombre") + "\t" + "\t" + "\t" + "\t"
                        + resultado.getInt("e.edad") + "\t" + "\t" + "\t"
                        + resultado.getString("e.direccion") + "\t" + "\t" + "\t" + "\t"
                        + resultado.getString("e.correo_electronico")
                );
            }
            System.out.println();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
    }
    
    public static boolean mostrarEstudiantesPorFiltroNombre(Scanner sc, String query){
        int contador = 0;
        query = "select e.id, e.nombre, e.edad, e.direccion, e.correo_electronico "
              + " from estudiantes as e "
              + " where 1 = 1 ";
        
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try (Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);){
            System.out.println("\n*************** MENU PARA ACTUALIZAR LOS DATOS DE UN ESTUDIANTE CON FILTRO NOMBRE ***************");
            System.out.print("Introduce el nombre del estudiante que quieres mostrar: ");
            String nombre = sc.nextLine();
            
            if (nombre != null && !nombre.equals("")) {
                query = query + " and e.nombre like ? ";
            }
            
            sentencia = con.prepareStatement(query); //sentencia SQL.
            
            if (nombre != null && !nombre.equals("")) {
                sentencia.setString(1, nombre + "%");
            }
            
            resultado = sentencia.executeQuery();  //con esto ejecutamos la consulta y luego lo cerramos en un finally.
            
            System.out.println("ID_ESTUDIANTE" + "\t" + "\t" + "NOMBRE" + "\t" + "\t" + "\t" + "EDAD" + "\t" + "\t" + "DIRECCION" + "\t" + "\t" + "\t" + "CORREO_ELECTRONICO");
            while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                System.out.println(resultado.getInt("e.id") + "\t" + "\t" + "\t"
                        + resultado.getString("e.nombre") + "\t" + "\t"
                        + resultado.getInt("e.edad") + "\t" + "\t"
                        + resultado.getString("e.direccion") + "\t" + "\t" + "\t"
                        + resultado.getString("e.correo_electronico")
                );
                contador++;
            }
            System.out.println();
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch(SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el PreparedStatement");
                }
            }
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
        
        if (contador > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void mostrarTodosLosCursos() {
        System.out.println("------------------------ CURSOS ------------------------");
        String query = "select c.id, c.nombre, c.descripcion, c.creditos "
                + " from cursos as c "
                + " order by c.id ";

        ResultSet resultado = null;
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //abrimos una sentencia sql.
                ) {
            resultado = sentencia.executeQuery(); //ejecuta la consulta y lo guarda en la variable "resultado".

            System.out.println("ID_CURSO" + "\t" + "\t" + "NOMBRE" + "\t" + "\t" + "\t" + "DESCRIPCION" + "\t" + "\t" + "\t" + "\t" + "CREDITOS");
            while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                System.out.println(resultado.getInt("c.id") + "\t" + "\t" + "\t"
                        + resultado.getString("c.nombre") + "\t" + "\t" + "\t" + "\t"
                        + resultado.getString("c.descripcion") + "\t" + "\t" + "\t" + "\t"
                        + resultado.getInt("c.creditos")
                );
            }
            System.out.println();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
    }
    
    public static boolean mostrarCursosPorFiltroNombre(Scanner sc, String query) {
        int contador = 0;
        query = "select c.id, c.nombre, c.descripcion, c.creditos "
              + " from cursos as c "
              + " where 1 = 1 ";
        
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        try (Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW);){
            System.out.println("\n*************** MENU PARA ACTUALIZAR LOS DATOS DE UN CURSO CON FILTRO NOMBRE ***************");
            System.out.print("Introduce el nombre del curso que quieres mostrar: ");
            String nombre = sc.nextLine();
            
            if (nombre != null && !nombre.equals("")) {
                query = query + " and c.nombre like ? ";
            }
            
            sentencia = con.prepareStatement(query); //sentencia SQL.
            
            if (nombre != null && !nombre.equals("")) {
                sentencia.setString(1, nombre + "%");
            }
            
            resultado = sentencia.executeQuery();  //con esto ejecutamos la consulta y luego lo cerramos en un finally.
            
            System.out.println("ID_CURSO" + "\t" + "\t" + "NOMBRE" + "\t" + "\t" + "\t" + "DESCRIPCION" + "\t" + "\t" + "\t" + "\t" + "CREDITOS");
            while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                System.out.println(resultado.getInt("c.id") + "\t" + "\t" + "\t"
                        + resultado.getString("c.nombre") + "\t" + "\t" + "\t"
                        + resultado.getString("c.descripcion") + "\t" + "\t" + "\t" + "\t"
                        + resultado.getInt("c.creditos")
                );
                contador++;
            }
            System.out.println();
        } catch(NumberFormatException e) {
            System.err.println("ERROR al introducir un String cuando hay que meter un Integer");
        } catch(SQLException ex) {
            System.err.println("ERROR de SQL");
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el PreparedStatement");
                }
            }
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
        
        if (contador > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void mostrarTodasLasInscripciones() {
        System.out.println("------------------------ INSCRIPCIONES ------------------------");
        String query = "select i.id, i.id_estudiante, i.id_curso, i.fecha_inscripcion "
                + " from inscripciones as i "
                + " order by i.id ";

        ResultSet resultado = null;
        try (
                Connection con = DriverManager.getConnection(URLCONNECTION, USER, PASSW); 
                PreparedStatement sentencia = con.prepareStatement(query); //abrimos una sentencia sql.
                ) {
            resultado = sentencia.executeQuery(); //ejecuta la consulta y lo guarda en la variable "resultado".

            System.out.println("ID_INSCRIPCION" + "\t" + "\t" + "ID_ESTUDIANTE" + "\t" + "\t" + "ID_CURSO" + "\t" + "\t" + "FECHA_INSCRIPCION");
            while (resultado.next()) {  //nos recorremos la consulta si tiene algo a continuación.
                System.out.println(resultado.getInt("i.id") + "\t" + "\t" + "\t"
                        + resultado.getString("i.id_estudiante") + "\t" + "\t" + "\t"
                        + resultado.getString("i.id_curso") + "\t" + "\t" + "\t"
                        + resultado.getDate("i.fecha_inscripcion")
                );
            }
            System.out.println();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    System.err.println("ERROR al cerrar el ResultSet");
                }
            }
        }
    }
    
    
    
    //este método no lo utilizo pero me lo guardo por si acaso lo necesito.
    public static String menu2NoUsar(Scanner sc) {  //Es el mismo menú que el otro pero con else ifs y por si acaso lo guardo.
        System.out.println("---  ---  ---  --- Bienvenido-al-Menú-de-la-BBDD-Instituto ---  ---  ---  ---");
        String opcion = "";
        System.out.println("1. Insertar datos");
        System.out.println("2. Consultar datos");
        System.out.println("3. Actualizar datos");
        System.out.println("4. Eliminar datos");
        System.out.println("S. Salir");
        
        do {
            System.out.print("Selecciona una opción: ");
            opcion = sc.nextLine().toUpperCase();
            System.out.println();
        } while (!(opcion.equals("1") || opcion.equals("2") || opcion.equals("3") || opcion.equals("4") || opcion.equals("S")));

        if (opcion.equals("1")) {
            System.out.println("--- 1. OPCIONES PARA INSERTAR DATOS ---");
            System.out.println("1.1. Insertar Estudiante");
            System.out.println("1.2. Insertar Curso");
            System.out.println("1.3. Registrar una inscripción de un estudiante en un cursos");
            System.out.println("V. Volver hacia atrás");
            System.out.println("S. Salir");

            do {
                System.out.print("Selecciona una opción para insertar datos: ");
                opcion = sc.nextLine().toUpperCase();
            } while (!(opcion.equals("1.1") || opcion.equals("1.2") || opcion.equals("1.3")
                    || opcion.equals("V") || opcion.equals("S")));
        } else if (opcion.equals("2")) {
            System.out.println("--- 2. OPCIONES PARA CONSULTAR DATOS ---");
            System.out.println("2.0. Mostrar todos los estudiantes y todos los cursos");
            System.out.println("2.1. Consultar la lista de estudiantes inscritos en un curso específico");
            System.out.println("2.2. Consultar los cursos en los que un estudiante particular está inscrito");
            System.out.println("V. Volver hacia atrás");
            System.out.println("S. Salir");

            do {
                System.out.print("Selecciona una opción de consulta: ");
                opcion = sc.nextLine().toUpperCase();
            } while (!(opcion.equals("2.0") || opcion.equals("2.1") || opcion.equals("2.2")
                    || opcion.equals("V") || opcion.equals("S")));
        } else if (opcion.equals("3")) {
            System.out.println("--- 3. OPCIONES PARA ACTUALIZAR DATOS ---");
            System.out.println("3.1. Actualizar información de estudiantes o cursos");
            System.out.println("V. Volver hacia atrás");
            System.out.println("S. Salir");
            
            do {
                System.out.print("Selecciona una opción para actualizar datos: ");
                opcion = sc.nextLine().toUpperCase();
            } while (!(opcion.equals("3.1") || opcion.equals("V") || opcion.equals("S")));
        } else if (opcion.equals("4")) {
            System.out.println("--- 4. OPCIONES PARA ELIMIAR DATOS ---");
            System.out.println("4.1. Eliminar registros de estudiantes, cursos o inscripciones.");
            System.out.println("V. Volver hacia atrás");
            System.out.println("S. Salir");

            do {
                System.out.print("Selecciona una opción para eliminar datos: ");
                opcion = sc.nextLine().toUpperCase();
            } while (!(opcion.equals("4.1") || opcion.equals("V") || opcion.equals("S")));
        }
        return opcion;
    }
    
}
