import com.unicen.app.indicators.Response;
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

def getAverageDegreeOfProgress(){
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
            AND p.colegio_secundario = '${row.colegio}'
            GROUP BY c.legajo
        """) { subrow ->
            ret.add(new Response(row.colegio, row.nombre, subrow.totalapproved / row.nstudents))
        }

    }

    return ret
}

