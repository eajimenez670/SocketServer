import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OperationsDB {
	Connection con = null;

	public List<Saldo> Buscar() {
		try {
			con = UConnection.getConnection();

			String consultaSQL = "SELECT * FROM saldo";
			PreparedStatement statement = con.prepareStatement(consultaSQL);
			ResultSet resultado = statement.executeQuery();
			List<Saldo> listaObjetos = new ArrayList<>();
			while (resultado.next()) {
				Saldo saldo = new Saldo();
				saldo.setId(resultado.getInt("id"));
				saldo.setCuenta(resultado.getBigDecimal("cuenta"));
				saldo.setNombrePersona(resultado.getString("nombre_persona"));
				saldo.setSaldo(resultado.getInt("saldo"));

				listaObjetos.add(saldo);
			}

			resultado.close();
			statement.close();

			return listaObjetos;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void Insertar(BigDecimal cuenta, String nombre_persona, double saldo) {
		try {
			con = UConnection.getConnection();

			String insertSQL = "INSERT INTO saldo (cuenta, nombre_persona, saldo) VALUES (?, ?, ?)";
			PreparedStatement statement = con.prepareStatement(insertSQL);

			statement.setBigDecimal(1, cuenta);
			statement.setString(2, nombre_persona);
			statement.setDouble(3, saldo);

			statement.executeUpdate();

			// Cierra recursos
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Eliminar(int id) {
		try {
			con = UConnection.getConnection();

			String deleteSQL = "DELETE FROM saldo WHERE id = ?";
			PreparedStatement statement = con.prepareStatement(deleteSQL);

			// Setea el ID del registro a eliminar
			statement.setInt(1, id);

			// Ejecuta la eliminación
			statement.executeUpdate();

			// Cierra recursos
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Editar(int id, BigDecimal cuenta, String nombre_persona, double saldo) {
		try {
			con = UConnection.getConnection();

			String updateSQL = "UPDATE saldo SET cuenta = ?, nombre_persona = ?, saldo = ? WHERE id = ?";
			PreparedStatement statement = con.prepareStatement(updateSQL);

			// Setea los nuevos valores para los campos
			statement.setBigDecimal(1, cuenta);
			statement.setString(2, nombre_persona);
			statement.setDouble(3, saldo);
			statement.setInt(4, id);

			// Ejecuta la actualización
			statement.executeUpdate();

			// Cierra recursos
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
