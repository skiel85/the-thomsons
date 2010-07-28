package conexion;
import java.sql.*;

public class conexion {
	private Connection conexion=null;
    private String servidor="";
    private String database="";
    private String usuario="";
    private String password="";
    private String url="";
    
    
    public conexion(String servidor, String database, String usuario, String password){
        try {

            this.servidor = servidor;
            this.database = database;

            Class.forName("com.mysql.jdbc.Driver");
            //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            url="jdbc:mysql://"+servidor+"/"+database;
            conexion=DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexion a Base de Datos "+url+" . . . . .Ok");

        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public Connection getConexion(){
        return conexion;
    }

    public Connection cerrarConexion(){
        try {
            conexion.close();
             System.out.println("Cerrando conexion a "+url+" . . . . . Ok");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        conexion=null;
        return conexion;
    }
    
    public ResultSet conectarlogeo(String query) {
        Statement sentenciaConsulta = null;
        ResultSet resultado = null;
        boolean rs=false;
        try
        {
          sentenciaConsulta = this.conexion.createStatement();
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
        try {
          rs = sentenciaConsulta.execute(query);
        }
        catch (SQLException e) {
          e.printStackTrace();
        }

        return resultado;
      }
    
}
