import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import java.io.ObjectInputStream;

public class SocketDef {
	OperationsDB db = new OperationsDB();

	public void Initialize() {
		ServerSocket servidor = null;
		Socket sc = null;
		final int PUERTO = 9000;
		List<Saldo> resultados = new ArrayList<>();

		try {
			servidor = new ServerSocket(PUERTO);
			System.out.println("Servidor en ejecución");

			while (true) {
				sc = servidor.accept();

				System.out.println("Petición cliente iniciada");

				ObjectInputStream objectIS = new ObjectInputStream(sc.getInputStream());
				Saldo saldo = (Saldo) objectIS.readObject();

				if (saldo.getTypeOperation() == 1) {
					resultados = db.Buscar();
					String listaObjetosJson = convertirListaAJson(resultados);
					DataOutputStream out = new DataOutputStream(sc.getOutputStream());
					out.writeInt(listaObjetosJson.length());
					out.writeBytes(listaObjetosJson);
					out.flush();

				} else {
					validateOperation(saldo);
					DataOutputStream out = new DataOutputStream(sc.getOutputStream());
					out.writeUTF("Operación realizada correctamente");
				}

				sc.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(SocketDef.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static String convertirListaAJson(List<Saldo> listaObjetos) {
		Gson gson = new Gson();
		return gson.toJson(listaObjetos);
	}

	public void validateOperation(Saldo saldo) {
		switch (saldo.getTypeOperation()) {
		case 1:
			// Obtener datos.
			break;
		case 2:
			// Guardar.
			db.Insertar(saldo.getCuenta(), saldo.getNombrePersona(), saldo.getSaldo());
			break;
		case 3:
			// Actualizar.
			db.Editar(saldo.getId(), saldo.getCuenta(), saldo.getNombrePersona(), saldo.getSaldo());
			break;
		case 4:
			// Eliminar.
			db.Eliminar(saldo.getId());
			break;

		default:
			// Default secuencia de sentencias.
			break;
		}
	}

}
