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
 * getAllAverageIndicator()
 */

/**
 * Returns the overall average student rate of each school
 *
 * @return List<Response>
 */
def getAllAverageIndicator(){
    def ret = new ArrayList<Response>()

    mysqlDB.eachRow ('''
      SELECT AVG(vw_hist_academica.nota) as prom, colegio, sga_coleg_sec.nombre FROM `vw_hist_academica`
      JOIN sga_alumnos ON vw_hist_academica.legajo = sga_alumnos.legajo
      JOIN sga_personas ON sga_personas.nro_inscripcion = sga_alumnos.nro_inscripcion
      JOIN sga_coleg_sec ON sga_coleg_sec.colegio = sga_personas.colegio_secundario
      GROUP BY sga_personas.colegio_secundario;
    ''') { row ->
            ret.add(new Response(row.colegio, row.nombre, row.prom))
     }

     return ret
}

def getAverageDegreeOfProgressIndicator(){
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
def getAverageDegreeDurationIndicator(){

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

def getGraduatedAverageIndicator(){

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
def getGraduatedAverageAltIndicator(){

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
def getDesertionDegreeIndicator(){

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