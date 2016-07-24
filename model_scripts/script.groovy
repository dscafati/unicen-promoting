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


