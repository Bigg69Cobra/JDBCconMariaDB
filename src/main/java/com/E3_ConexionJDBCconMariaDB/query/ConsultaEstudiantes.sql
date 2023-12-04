USE Instituto;

drop table if EXISTS inscripciones;
drop table if EXISTS estudiantes;
drop table if EXISTS cursos;


CREATE TABLE if not exists estudiantes(
	id int AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(20),
	edad int,
	direccion VARCHAR(150),
	correo_electronico VARCHAR(80)
);

CREATE TABLE if not exists cursos(
	id int AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(20),
	descripcion VARCHAR(50),
	creditos int
);

CREATE TABLE if not exists inscripciones(
	id int AUTO_INCREMENT PRIMARY KEY,
	id_estudiante int,
	id_curso int,
	fecha_inscripcion DATE,
	FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (id_curso) REFERENCES cursos(id) ON DELETE CASCADE ON UPDATE CASCADE
);

/* consultas: */
select e.id, e.nombre, e.edad, e.direccion, e.correo_electronico, c.nombre 
from estudiantes as e 
inner join inscripciones as i
on e.id = i.id_estudiante
inner join cursos as c
on c.id = i.id_curso
where c.nombre = "matematicas";


SELECT e.id, e.nombre, e.edad, e.direccion, e.correo_electronico
FROM estudiantes AS e;

SELECT c.id, c.nombre, c.descripcion, c.creditos
FROM cursos AS c;

/*Para eliminar las tablas:*/
DROP TABLE inscripciones;
DROP TABLE estudiantes;
DROP TABLE cursos;


CREATE DATABASE examen;
USE examen;


SELECT * FROM libreria;

DROP TABLE libreria;

CREATE DATABASE superheroes;
USE superheroes;

SELECT * FROM Departamentos;
SELECT * FROM Empleados;

DROP TABLE departamentos;
DROP TABLE Empleados;
