import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Principal {

	public static void main(String[] args) {

		Cliente clienteAinsertar = new Cliente("Tinda ropa", "Calle sol 34",
				"Salamanca", "37006", "671865478");

		// objectivo: registrar el cliente en la tabla_clientes usando jdbc

		// 1er paso: configurar jdbc para decirle si voy a usar:
		// mysql, sql server, oracle, db2

		// para ello simplemente tenemos que cargar el driver de la libreria
		// de conexion correspondente.

		// para cargar una clase en la aplicacion
		// segun un String

		// la clase que hemos cargado tiene un codigo
		// especifico que configura jdbc para trabajar con mysql.

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("no encuentro el driver");
		}
		String url = "jdbc:mysql://localhost:3306/practica_escritorio";

		// en url digo donde esta mi base de datos
		// donde esta mi base de datos,
		// habra que cargar del fabricante con el que trabajamos
		// *forName(--------)

		// ahora el resto de codigo es 100% compatible
		// con cualquier otra base de datos

		// establecemos connexion:

		Connection miConnexion = null;
		try {
			miConnexion = DriverManager.getConnection(url, "root", "jeveris");
		} catch (SQLException e) {

			System.out
					.println("no pude obtener connexion, mirar usuario y pass");
		}
		// seguridad: jamas deberiamos conesctarnos comoroot desde la aplicacion
		// final, la aplicacion deberia onectarse con la base de datos
		// usando un usuario y contraseña especifico que tenga limitadas
		// todo las operaciones que no se vayan a usar

		// voy a preparar un statement -> es algo que me permite lanzar una sql

		try {
			Statement statementInsercionCliente = miConnexion.createStatement();

			// String sqlInsercionCliente =
			// "insert into tabla_clientes(nombre, domicilio, poblacion, codigo_postal, telefono) values ('"+clienteAinsertar.getNombre()+"', 'W', 'X','Y','Z');";
			// la sql anterior es mala porque es vulnerable a ataques de
			// inyecciom
			// sql, si el nombre del cliente es una sql dicha sql altera lo que
			// realmente
			// queremos hacer, que no es mas que un insertar

			// asi es como se deberia hacer:

			String sqlInsercionCliente = "insert into tabla_clientes(nombre, domicilio, poblacion, codigo_postal, telefono) "
					+ "values ( ?, ?, ?,?,?);";

			// de esta forma le decimos a la base de datos que esta es la sql
			// que queremos lanzar con,
			// en este caso 5 variables.
			// cuando le digamos a la base de datos que es cada variable
			// no se podra inyectar sql,porque la base de datos espera
			// variables del tipo indicado: por ejemplo la primera ?
			// tiene que ser un nombre valido correspondiente al tipo de dato
			// de nombre en la base de datos

			PreparedStatement ps = miConnexion
					.prepareStatement(sqlInsercionCliente);

			// lo siguiente no vale por ser vulnerable a inyecciones sql

			// statementInsercionCliente.executeUpdate(sqlInsercionCliente);
			//
			// System.out.println("insercion realizada corectamente");
			// statementInsercionCliente.close();

			ps.setString(1, clienteAinsertar.getNombre());
			ps.setString(2, clienteAinsertar.getDomicilio());
			ps.setString(3, clienteAinsertar.getPoblacion());
			ps.setString(4, clienteAinsertar.getCodigoPostal());
			ps.setString(5, clienteAinsertar.getTelefono());

			ps.execute();
			ps.close();
			System.out.println("cliente insertado correctamente");

		} catch (SQLException e) {
			System.out
					.println("error obteniendo el statement o error en la sql");
			e.printStackTrace();
		}// end catch
			// no olvidar liberar conexion
		try {
			miConnexion.close();
		} catch (SQLException e) {
			System.out.println("no pude hacer el close");
			e.printStackTrace();
		}

	}// end main
}
