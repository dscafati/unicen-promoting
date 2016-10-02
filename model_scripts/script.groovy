import com.unicen.app.indicators.Response
/**
 * Available vars:
 *
 * mysqlDB
 * pgsqlDB
 * informixDB
 *
 *
 * Expected methods:
 *
 * beforeIndicator()
 * getAllAverageIndicator()
 * getAverageDegreeOfProgressIndicator()
 * getAverageDegreeDurationIndicator()
 * getGraduatedAverageIndicator()
 * getDesertionDegreeIndicator()
 *
 */

/**
 * This method is called only once, before calling any other metric method
 * It's pretended to be used for preparing the database, or any other initialization
 *
 * @return void
 */
def beforeIndicator(){
    // Create a better table for schools
    mysqlDB.execute('''
           CREATE TEMPORARY TABLE `tmp_escuela` (
            `escuela_id` INT(11) NOT NULL,
             `nombre` VARCHAR(255) NOT NULL
             );
    ''')
    // Populate only with schools from Argentina
    mysqlDB.execute('''
            INSERT INTO tmp_escuela
                SELECT c.colegio AS escuela_id, c.nombre AS nombre
                FROM sga_coleg_sec c
                JOIN mug_localidades l ON l.localidad = c.localidad
                JOIN mug_dptos_partidos d ON d.dpto_partido = l.dpto_partido
                JOIN mug_provincias p ON p.provincia = d.provincia
                JOIN mug_paises pp ON pp.pais = p.pais
                WHERE pp.pais = 54
                GROUP BY escuela_id;
    ''')

    // Create a better table for students, there may be duplicated if studying more than one major
    mysqlDB.execute('''
       CREATE TEMPORARY TABLE `tmp_alumno` (
        `legajo` VARCHAR(255) NOT NULL,
         `escuela_id` VARCHAR(255) NOT NULL
         )
    ''')
    mysqlDB.execute('''
        INSERT INTO tmp_alumno
            SELECT a.legajo, p.colegio_secundario
            FROM sga_alumnos a
            JOIN sga_personas p ON p.nro_inscripcion = a.nro_inscripcion
    ''')


    // Remove duplicates
    mysqlDB.executeUpdate('''
        DELETE FROM tmp_escuela WHERE escuela_id = 59800162;
        DELETE FROM tmp_escuela WHERE escuela_id = 2600389;
        DELETE FROM tmp_escuela WHERE escuela_id = 200724;
        DELETE FROM tmp_escuela WHERE escuela_id = 201783;
        DELETE FROM tmp_escuela WHERE escuela_id = 278;
        DELETE FROM tmp_escuela WHERE escuela_id = 2202069;
        DELETE FROM tmp_escuela WHERE escuela_id = 56002140;
        DELETE FROM tmp_escuela WHERE escuela_id = 614624;
        DELETE FROM tmp_escuela WHERE escuela_id = 622658 OR escuela_id = 540003735;
        DELETE FROM tmp_escuela WHERE escuela_id = 4200291;
        DELETE FROM tmp_escuela WHERE escuela_id = 7000621;
        
        UPDATE tmp_alumno SET escuela_id = 56002138 WHERE escuela_id = 59800162;
        UPDATE tmp_alumno SET escuela_id = 5001099 WHERE escuela_id = 2600389;
        UPDATE tmp_alumno SET escuela_id = 5001139 WHERE escuela_id = 200724;
        UPDATE tmp_alumno SET escuela_id = 5000216 WHERE escuela_id = 201783;
        UPDATE tmp_alumno SET escuela_id = 7000623 WHERE escuela_id = 278;
        UPDATE tmp_alumno SET escuela_id = 54000283 WHERE escuela_id = 2202069;
        UPDATE tmp_alumno SET escuela_id = 56001207 WHERE escuela_id = 56002140;
        UPDATE tmp_alumno SET escuela_id = 602808 WHERE escuela_id = 614624;
        UPDATE tmp_alumno SET escuela_id = 540003734 WHERE escuela_id = 622658 OR escuela_id = 540003735;
        UPDATE tmp_alumno SET escuela_id = 2200798 WHERE escuela_id = 4200291;
        UPDATE tmp_alumno SET escuela_id = 688 WHERE escuela_id = 7000621;
    ''')
}


/**
 * Returns the overall student rate average for every school
 *
 * @return List<Response>
 */
def getAllAverageIndicator(){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow ('''
      SELECT
        AVG(h.nota) as prom,
        e.escuela_id as colegio,
        e.nombre
      FROM tmp_escuela e
      LEFT JOIN tmp_alumno a USING(escuela_id)
      LEFT JOIN vw_hist_academica h USING(legajo)
      GROUP BY e.escuela_id
      ORDER BY prom DESC;
    ''') { row ->
            def prom = ( row.prom == null ) ?  0 : row.prom ;
            ret.add(new Response(row.colegio, row.nombre, row.prom))
     }

     return ret
}

/**
 * Get the average degree of progress of the students for every school
 * The degree of progress is measured as the sum of approved subjects
 *
 * @return List<Response>
 */
def getAverageDegreeOfProgressIndicator(){
    def ret = new ArrayList<Response>()
    mysqlDB.eachRow('''
        SELECT
            e.escuela_id,
            e.nombre,
            COUNT(*) as nstudents,
            SUM(sub.approved) as napproved
        FROM tmp_escuela e
        LEFT JOIN (
            SELECT
                count( c.resultado ) as approved,
                a.escuela_id as escuela_id
            FROM sga_cursadas c
            JOIN tmp_alumno a USING(legajo)
            WHERE c.resultado = 'A'
            GROUP BY c.legajo
        ) sub USING(escuela_id)
        GROUP BY e.escuela_id
    '''){
        row ->
            def num = (row.napproved == null) ? 0 : row.napproved
            def div = (row.nstudents > 0) ? (num / row.nstudents) : 0

            ret.add(new Response(row.escuela_id, row.nombre, div))
    }

    return ret
}

/**
 * Return the average degree duration of the students of every school
 *
 * @return List<Response>
 */
def getAverageDegreeDurationIndicator(){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow('''
        SELECT
            e.escuela_id AS colegio,
            e.nombre AS nombre,
            sub.duration AS duration
        FROM tmp_escuela e
        LEFT JOIN (
            SELECT
                AVG(t.duracion_carrera) AS duration,
                a.escuela_id AS escuela_id
            FROM sga_titulos_otorg t
            JOIN tmp_alumno a USING(legajo)
            GROUP BY a.escuela_id
        ) sub ON sub.escuela_id = e.escuela_id
        ORDER BY duration DESC;
    ''') { row ->
        def duration = (row.duration == null ) ? 0 : row.duration
        ret.add(new Response(row.colegio, row.nombre, duration))
    }

    return ret
}

/**
 * Get the average student rate, of the graduated students, for every school
 * This method doen't take into account the exam fails (exams with a result < 4)
 *
 * @return List<Response>
 */
def getGraduatedAverageIndicator(){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow('''
        SELECT
            e.escuela_id AS colegio,
            e.nombre AS nombre,
            sub.prom AS prom
        FROM tmp_escuela e
        LEFT JOIN (
            SELECT
                AVG(t.prom_general) AS prom,
                a.escuela_id AS escuela_id
            FROM sga_titulos_otorg t
            JOIN tmp_alumno a USING(legajo)
            GROUP BY a.escuela_id
        ) sub ON sub.escuela_id = e.escuela_id
        ORDER BY prom DESC;
    ''') { row ->
        def prom = (row.prom == null ) ? 0 : row.prom;
        ret.add(new Response(row.colegio, row.nombre, prom))
    }

    return ret
}

/**
 * Get the average student rate, of the graduated students, for every school
 * This method takes into account the exam fails (exams with a result < 4)
 *
 * @return List<Response>
 */
def getGraduatedAverageAltIndicator(){

    def ret = new ArrayList<Response>()

    mysqlDB.eachRow('''
        SELECT
            e.escuela_id AS colegio,
            e.nombre AS nombre,
            sub.prom AS prom
        FROM tmp_escuela e
        LEFT JOIN (
            SELECT
                AVG(t.prom_sin_aplazos) AS prom,
                a.escuela_id AS escuela_id
            FROM sga_titulos_otorg t
            JOIN tmp_alumno a USING(legajo)
            GROUP BY a.escuela_id
        ) sub ON sub.escuela_id = e.escuela_id
        ORDER BY prom DESC;
    ''') { row ->
        def prom = (row.prom == null ) ? 0 : row.prom;
        ret.add(new Response(row.colegio, row.nombre, prom))
    }

    return ret
}

/**
 * Get the amount of student desertors for every school
 *
 * @return List<Response>
 */
def getDesertionDegreeIndicator(){

    def ret = new ArrayList<Response>()
    mysqlDB.eachRow('''
        SELECT
            e.escuela_id AS colegio,
            e.nombre AS nombre,
            COUNT(*) AS desertors
        FROM tmp_escuela e
        LEFT JOIN (
            SELECT
                h.carrera,
                h.legajo,
                a.escuela_id
            FROM vw_hist_academica h
            JOIN tmp_alumno a USING(legajo)
            GROUP BY h.legajo
            HAVING (datediff(now(),MAX(fecha))/360) >= 5
        ) sub ON sub.escuela_id = e.escuela_id
        GROUP BY e.escuela_id
        ORDER BY desertors DESC;
    ''') { row ->
        def desertors = (row.desertors == null ) ? 0 : row.desertors;
        ret.add(new Response(row.colegio, row.nombre, desertors))
    }

    return ret
}