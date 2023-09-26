import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class UConnection {
	private static String jdbcUrl = "jdbc:postgresql://localhost:5432/poli_dist";
	private static String usuario = "postgres";
	private static String clave = "holamundo";
	private static Connection con = null;

	public static Connection getConnection() {
		try {
			if (con == null) {
				Runtime.getRuntime().addShutdownHook(new MiShDwnHook());
				con = DriverManager.getConnection(jdbcUrl, usuario, clave);
			}

			return con;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error al crear la conexion", ex);
		}
	}

	static class MiShDwnHook extends Thread {
		public void run() {
			try {
				Connection con = UConnection.getConnection();
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}
}
