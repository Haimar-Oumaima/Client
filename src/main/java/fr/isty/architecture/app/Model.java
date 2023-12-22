package fr.isty.architecture.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.isty.architecture.app.classes.Horaire;
import fr.isty.architecture.app.classes.Personne;
import fr.isty.architecture.app.classes.Reservation;
import fr.isty.architecture.app.classes.Salle;
import fr.isty.architecture.data.Database;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;


public class Model {
    private List<Personne> personnes = new ArrayList<>();
    private List<Salle> salles = new ArrayList<>();
    private List<Horaire> horaires = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    
    public void add(Object obj) {
        HttpClient client = HttpClient.newHttpClient();

        if (obj instanceof Personne) {
            String json = convertObjectToJson(obj);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/personnes")) // Remplacez par votre URL
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (obj instanceof Salle) {
            String json = convertObjectToJson(obj);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/salles")) // Remplacez par votre URL
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (obj instanceof Horaire) {
            //horaires.add((Horaire) obj);
            String json = convertObjectToJson(obj);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/horaires")) // Remplacez par votre URL
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (obj instanceof Reservation) {
            reservations.add((Reservation) obj);
            
        } else {
            // Gestion d'une classe inconnue
            throw new IllegalArgumentException("Type d'objet non pris en charge : " + obj.getClass());
        }
    }

    public void remove(int index, String liste) {
        switch (liste) {
            case "personnes":

                    System.out.println("personnes.get(index) "+index);
//                if (index >= 0 && index < personnes.size()) {
////                    Database.deletePersonne(personnes.get(index));
////                    Database.commitAll();
////                	personnes.remove(index);
//                }
                break;
            case "salles":
//                if (index >= 0 && index < salles.size()) {
//                    Database.deleteSalle(salles.get(index));
//                    Database.commitAll();
//                    salles.remove(index);
//                }
                break;
            case "horaires":
               /* if (index >= 0 && index < horaires.size()) {
                    Database.deleteHoraire(horaires.get(index));
                    Database.commitAll();
                	horaires.remove(index);
                }*/
                break;
            case "reservations":
                if (index >= 0 && index < reservations.size()) {
                    reservations.remove(index);
                }
                break;
            default:
                // Gestion d'une liste inconnue
                throw new IllegalArgumentException("Liste non reconnue : " + liste);
        }
    }
    public List<Personne> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(List<Personne> personnes) {
        this.personnes = personnes;
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

    public List<Horaire> getHoraires() {
        return horaires;
    }

    public void setHoraires(List<Horaire> horaires) {
        this.horaires = horaires;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    private String convertObjectToJson(Object obj) {
        // Création de l'instance Gson
        GsonBuilder builder = new GsonBuilder();

        // Personnalisez le format de la date si nécessaire. Exemple : ISO 8601
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        // Créez l'objet Gson
        Gson gson = builder.create();

        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

