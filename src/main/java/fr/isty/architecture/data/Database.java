package fr.isty.architecture.data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.isty.architecture.app.classes.Horaire;
import fr.isty.architecture.app.classes.Personne;
import fr.isty.architecture.app.classes.Reservation;
import fr.isty.architecture.app.classes.Salle;

public class Database {
	final static String DBname = "jdbc:sqlite:ressources.db";
	static Connection connection = null;

	public static void init() {
		try {
			// Chargement du pilote JDBC SQLite
			Class.forName("org.sqlite.JDBC");
			// Connexion à la base de données (crée un fichier de base de données s'il n'existe pas)
			connection = DriverManager.getConnection(DBname);
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			if (!tableExist("horaires")) {
				String createTableSQL = "CREATE TABLE horaires (id INTEGER PRIMARY KEY, debut TEXT, fin TEXT)";
				statement.execute(createTableSQL);
			}
			if (!tableExist("salles")) {
				String createTableSQL = "CREATE TABLE salles (id INTEGER PRIMARY KEY, name TEXT, localization TEXT, occupee NUMBER(1))";
				statement.execute(createTableSQL);
			}
			if (!tableExist("personnes")) {
				String createTableSQL = "CREATE TABLE personnes (id INTEGER PRIMARY KEY, name TEXT)";
				statement.execute(createTableSQL);
			}
			if (!tableExist("reservations")) {
				String createTableSQL = "CREATE TABLE reservations (id INTEGER PRIMARY KEY, salle TEXT, personne TEXT, horaire TEXT)";
				statement.execute(createTableSQL);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void commitAll() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println("Transaction annulée.");
		}
	}

	public static void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static boolean tableExist(String tableName) {
		try {
			DatabaseMetaData metadata;
			metadata = connection.getMetaData();
			ResultSet tables = metadata.getTables(null, null, tableName, null);
			if (tables.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	/*public static void addSalle(Salle salle) {
		try {
			Statement statement = connection.createStatement();
			String insertDataSQL = "INSERT INTO salles (name, localization, occupee) VALUES ";
			insertDataSQL += " ( '" + salle.getNom() + "' , '" + salle.getLocalisation() + "' ," + salle.isOccupee()
					+ ")";
			statement.execute(insertDataSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

/*	public static void addPersonne(Personne personne) {
		try {
			Statement statement = connection.createStatement();
			String insertDataSQL = "INSERT INTO personnes (name) VALUES ";
			insertDataSQL += " ( '" + personne.getNom() + "')";
			statement.execute(insertDataSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

	public static void addHoraire(Horaire horaire) {
		try {
			List<String> time = Arrays.asList(horaire.toString().split("\\s-\\s"));
			Statement statement = connection.createStatement();
			String insertDataSQL = "INSERT INTO horaires (debut, fin) VALUES ";
			insertDataSQL += " ( '" + time.get(0) + "'"
					 	  + ",'" + time.get(1)+ "')";
			statement.execute(insertDataSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addReservation(Reservation reservation) {
		try {
			Statement statement = connection.createStatement();
			String insertDataSQL = "INSERT INTO reservations (salle, personne, horaire) VALUES ";
			insertDataSQL += " ( '" + reservation.getSalle().getNom()
						  + "' , '" + reservation.getPersonne().getNom()
						  + "' ,'" + reservation.getHoraire().toString() + "')";
			statement.execute(insertDataSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteSalle(Salle salle) {
		try {
			String deleteQuery = "DELETE FROM salles WHERE name = ? AND localization = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setString(1, salle.getNom());
			preparedStatement.setString(2, salle.getLocalisation());

			int rowsDeleted = preparedStatement.executeUpdate();
			if (rowsDeleted == 0) {
				System.out.println("salles not found or deletion failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deletePersonne(Personne personne) {
		try {
			String deleteQuery = "DELETE FROM personnes WHERE name = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setString(1, personne.getNom());

			int rowsDeleted = preparedStatement.executeUpdate();
			if (rowsDeleted == 0) {
				System.out.println("personne not found or deletion failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteHoraire(Horaire horaire) {
		try {
			List<String> time = Arrays.asList(horaire.toString().split("\\s-\\s"));
			String deleteQuery = "DELETE FROM horaires WHERE debut = ? AND fin = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setString(1, time.get(0));
			preparedStatement.setString(2, time.get(1));

			int rowsDeleted = preparedStatement.executeUpdate();
			if (rowsDeleted == 0) {
				System.out.println("horaire not found or deletion failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteReservation(Reservation reservation) {
		try {
			String deleteQuery = "DELETE FROM reservations WHERE personne = ? AND salle =  ? AND horaire = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setString(1, reservation.getPersonne().getNom());
			preparedStatement.setString(2, reservation.getSalle().getNom());
			preparedStatement.setString(3, reservation.getHoraire().toString());

			int rowsDeleted = preparedStatement.executeUpdate();
			if (rowsDeleted == 0) {
				System.out.println("Reservation not found or deletion failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Salle> fetchSalle() {
		List<Salle> salles = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8081/api/salles"))
				.header("Accept", "application/json")
				.GET()
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			Salle[] sallesArray = new ObjectMapper().readValue(response.body(), Salle[].class);
			return Arrays.asList(sallesArray);
		} catch (Exception e) {
			e.printStackTrace();
			return List.of(); // Retourne une liste vide en cas d'erreur
		}
	}

    public static List<Personne> fetchPersonne() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/personnes")) // Remplacez par votre URL
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Supposons que votre API renvoie un tableau JSON de personnes
            Personne[] personnesArray = new ObjectMapper().readValue(response.body(), Personne[].class);
            return Arrays.asList(personnesArray);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Retourne une liste vide en cas d'erreur
        }
    }

	public static List<Horaire> fetchHoraires() {
		/*List<Horaire> horaires = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			String query = "SELECT debut, fin FROM horaires";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			// connection.commit(); maybe ?
			while (resultSet.next()) {
				String Sdebut = resultSet.getString("debut");
				String Sfin = resultSet.getString("fin");
                LocalTime Ldebut = LocalTime.parse(Sdebut);
                LocalTime Lfin = LocalTime.parse(Sfin);
                Horaire horaire = new Horaire(Ldebut,Lfin);
				horaires.add(horaire);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return horaires;*/
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8081/api/horaires")) // Remplacez par votre URL
				.header("Accept", "application/json")
				.GET()
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// Supposons que votre API renvoie un tableau JSON de personnes
			Horaire[] horairesArray = new ObjectMapper().readValue(response.body(), Horaire[].class);
			return Arrays.asList(horairesArray);
		} catch (Exception e) {
			e.printStackTrace();
			return List.of(); // Retourne une liste vide en cas d'erreur
		}
	}

	public static List<Reservation> fetchReservation() {
		List<Reservation> reservations = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8081/api/reservations"))
				.header("Accept", "application/json")
				.GET()
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// Supposons que votre API renvoie un tableau JSON de personnes
			Reservation[] reservationsArray = new ObjectMapper().readValue(response.body(), Reservation[].class);
			return Arrays.asList(reservationsArray);
		} catch (Exception e) {
			e.printStackTrace();
			return List.of(); // Retourne une liste vide en cas d'erreur
		}
	}

	public static boolean isConnected()
	{
		try {
			return connection.isValid(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}