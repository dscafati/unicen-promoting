import com.unicen.app.indicators.Response
/**
 * Available vars:
 *
 * mysqlDB
 * pgsqlDB
 * informixDB
 *
 * Expected methods:
 *
 * getAllAverageIndicator()
 */

/**
 *
 */
def initialization( internalDB ){
    // A way to identify if the database already exists
    //internalDB.execute('''
//           CREATE TABLE INSTALLED
//    ''')

    // Create a better table for schools
   internalDB.execute('''
           CREATE TABLE `escuela` (
            `escuela_id` INT(11) NOT NULL,
             `nombre` VARCHAR(255) NOT NULL
             );
    ''')
    // Bring only schools from Argentina
    mysqlDB.eachRow('''
            SELECT c.colegio AS escuela_id, c.nombre AS nombre
            FROM sga_coleg_sec c
            JOIN mug_localidades l ON l.localidad = c.localidad
            JOIN mug_dptos_partidos d ON d.dpto_partido = l.dpto_partido
            JOIN mug_provincias p ON p.provincia = d.dpto_partido
            JOIN mug_paises pp ON pp.pais = p.pais
            WHERE pp.pais = 54;
    ''') { row ->
        internalDB.execute("INSERT INTO escuela VALUES (:escuela_id, :nombre)", [escuela_id: row.escuela_id, nombre: row.nombre] )
    }
    // Remove duplicates
    internalDB.executeUpdate('''
        UPDATE escuela SET escuela_id = 56002138 WHERE escuela_id = 59800162;
        UPDATE escuela SET escuela_id = 5001099 WHERE escuela_id = 2600389;
        UPDATE escuela SET escuela_id = 5001139 WHERE escuela_id = 200724;
        UPDATE escuela SET escuela_id = 5000216 WHERE escuela_id = 201783;
        UPDATE escuela SET escuela_id = 7000623 WHERE escuela_id = 278;
        UPDATE escuela SET escuela_id = 54000283 WHERE escuela_id = 2202069;
        UPDATE escuela SET escuela_id = 56001207 WHERE escuela_id = 56002140;
        UPDATE escuela SET escuela_id = 602808 WHERE escuela_id = 614624;
        UPDATE escuela SET escuela_id = 540003734 WHERE escuela_id = 622658 AND escuela_id = 540003735;
        UPDATE escuela SET escuela_id = 2200798 WHERE escuela_id = 4200291;
        UPDATE escuela SET escuela_id = 688 WHERE escuela_id = 7000621;
    ''')

    // Create a better table for students, there may be duplicated if studying more than one major
    internalDB.execute('''
           CREATE TABLE `alumno` (
            `alumno_id` VARCHAR(255) NOT NULL,
             `escuela_id` VARCHAR(255) NOT NULL
             )
    ''')
    mysqlDB.eachRow('''
            SELECT a.legajo AS alumno_id, p.colegio_secundario AS escuela_id
            FROM sga_alumnos a
            JOIN sga_personas p ON p.nro_inscripcion = a.nro_inscripcion
    '''){
        row ->
            internalDB.execute("INSERT INTO alumno VALUES (:alumno_id, :escuela_id)", [alumno_id: row.alumno_id, escuela_id: row.escuela_id] )
    }
/*
    // Create a better DB for courses
    internalDB.execute('''
        CREATE TABLE `cursada` (
            `alumno_id` VARCHAR(255) NOT NULL,
            `resultado` VARCHAR(255) NOT NULL
        )
    ''')
    mysqlDB.eachRow('''
            SELECT c.legajo AS alumno_id, c.resultado AS resultado
            FROM sga_cursadas c
    '''){
        row ->
            internalDB.execute("INSERT INTO cursada VALUES (:alumno_id, :resultado)", [alumno_id: row.alumno_id, resultado: row.resultado] )
    }*/
/*
    // Create a better DB for academical history
    internalDB.execute('''
        CREATE TABLE `historia_academica` (
          `carrera` varchar(5) NOT NULL,
          `alumno_id` varchar(255) NOT NULL,
          `fecha` date DEFAULT NULL,
          `nota` float DEFAULT NULL,
        )
    ''')
    mysqlDB.eachRow('''
            SELECT
                h.carrera AS carrera,
                h.legajo AS alumno_id,
                h.fecha AS fecha,
                h.nota AS nota
            FROM vw_hist_academica
    '''){
        row ->
            internalDB.execute("INSERT INTO historia_academica VALUES (:c, :a, :f, :n)",
                    [c: row.carrera, a: row.alumno_id, f: row.fecha, n: row.nota] )
    }*/

    // Create a better table for degrees
    internalDB.execute('''
        CREATE TABLE `titulos` (
          `alumno_id` varchar(15) NOT NULL,
          `prom_general` float DEFAULT NULL,
          `prom_sin_aplazos` float DEFAULT NULL,
          `duracion_carrera` int(11) DEFAULT NULL
        )
    ''')
    mysqlDB.eachRow('''
            SELECT
                t.legajo AS alumno_id,
                t.prom_general AS prom_general,
                t.prom_sin_aplazos AS prom_sin_aplazos,
                t.duracion_carrera AS duracion_carrera
            FROM sga_titulos_otorg t;
    '''){
        row ->
            internalDB.execute("INSERT INTO titulos VALUES (:a, :p, :pa, :d)",
                    [a: row.alumno_id,
                     p: row.prom_general,
                     pa: row.prom_sin_aplazos,
                     d: row.duracion_carrera]
            )
    }

}



/**
 * Returns the overall average student rate of each school
 *
 * @return List<Response>
 */
def getAllAverageIndicator( internalDB ){
    def ret = new ArrayList<Response>()

    mysqlDB.eachRow ('''
      SELECT AVG(vw_hist_academica.nota) as prom, colegio, sga_coleg_sec.nombre FROM `vw_hist_academica`
      JOIN sga_alumnos ON vw_hist_academica.legajo = sga_alumnos.legajo
      JOIN sga_personas ON sga_personas.nro_inscripcion = sga_alumnos.nro_inscripcion
      RIGHT JOIN sga_coleg_sec ON sga_coleg_sec.colegio = sga_personas.colegio_secundario
      GROUP BY sga_personas.colegio_secundario;
    ''') { row ->
            ret.add(new Response(row.colegio, row.nombre, row.prom))
     }

     return ret
}

def getAverageDegreeOfProgressIndicator( internalDB ){
    def ret = new ArrayList<Response>()

    // Select schools with students
    mysqlDB.eachRow('''
        SELECT
        c.nombre as nombre,
        c.colegio as colegio,
        count(*) as nstudents
        FROM sga_coleg_sec c
        JOIN sga_personas p on p.colegio_secundario = c.colegio
        JOIN sga_alumnos a on a.nro_inscripcion = p.nro_inscripcion
        GROUP BY c.colegio;
    ''') { row ->
        // For each school
        mysqlDB.eachRow("""
            SELECT
            count( c.resultado ) as totalapproved
            FROM sga_cursadas c
            JOIN sga_alumnos a ON a.legajo = c.legajo
            JOIN sga_personas p ON p.nro_inscripcion = a.nro_inscripcion
            WHERE c.resultado = 'A'
            AND p.colegio_secundario = :colegio
            GROUP BY c.legajo
        """, [colegio: row.colegio]) { subrow ->
            ret.add(new Response(row.colegio, row.nombre, subrow.totalapproved / row.nstudents))
        }

    }

    return ret
}

/**
 *
 * @todo get estimated duration of each major
 */
def getAverageDegreeDurationIndicator( internalDB ){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow('''
        SELECT
            c.colegio as colegio,
            c.nombre as nombre,
            AVG(t.duracion_carrera) AS duracion
        FROM sga_titulos_otorg t
        LEFT JOIN sga_alumnos a USING(legajo)
        LEFT JOIN sga_personas p ON (a.nro_inscripcion = p.nro_inscripcion)
        JOIN sga_coleg_sec c ON( p.colegio_secundario = c.colegio )
        GROUP BY c.colegio
    ''') { row ->
        ret.add(new Response(row.colegio, row.nombre, row.duracion))
    }

    return ret
}

def getGraduatedAverageIndicator( internalDB ){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow('''
        SELECT
            c.colegio as colegio,
            c.nombre as nombre,
            AVG(t.prom_general) AS prom
        FROM sga_titulos_otorg t
        LEFT JOIN sga_alumnos a USING(legajo)
        LEFT JOIN sga_personas p ON (a.nro_inscripcion = p.nro_inscripcion)
        JOIN sga_coleg_sec c ON( p.colegio_secundario = c.colegio )
        GROUP BY c.colegio
    ''') { row ->
        ret.add(new Response(row.colegio, row.nombre, row.prom))
    }

    return ret
}
def getGraduatedAverageAltIndicator( internalDB ){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow('''
        SELECT
            c.colegio as colegio,
            c.nombre as nombre,
            AVG(t.prom_general) AS prom
        FROM sga_titulos_otorg t
        LEFT JOIN sga_alumnos a USING(legajo)
        LEFT JOIN sga_personas p ON (a.nro_inscripcion = p.nro_inscripcion)
        JOIN sga_coleg_sec c ON( p.colegio_secundario = c.colegio )
        GROUP BY c.colegio
    ''') { row ->
        ret.add(new Response(row.colegio, row.nombre, row.prom))
    }

    return ret
}
def getDesertionDegreeIndicator( internalDB ){

    def ret = new ArrayList<Response>()
    mysqlDB.execute('''
        CREATE TEMPORARY TABLE tmp_desertion
        SELECT carrera, legajo, datediff(now(),MAX(fecha))/360 as fecha
        FROM vw_hist_academica
        GROUP BY legajo
        HAVING fecha >= 5
     ''')

    mysqlDB.eachRow('''
        SELECT
            c.colegio, c.nombre, COUNT(a.legajo) as desertors
        FROM tmp_desertion d
        RIGHT JOIN sga_alumnos a ON (a.legajo = d.legajo)
        JOIN sga_personas p ON (p.nro_inscripcion = a.nro_inscripcion)
        JOIN sga_coleg_sec c ON (c.colegio = p.colegio_secundario)
        GROUP BY c.colegio
    ''') { row ->
        ret.add(new Response(row.colegio, row.nombre, row.desertors))
    }

    return ret
}