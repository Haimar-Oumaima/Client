package fr.isty.architecture.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import fr.isty.architecture.app.classes.Horaire;
import fr.isty.architecture.app.classes.Personne;
import fr.isty.architecture.app.classes.Reservation;
import fr.isty.architecture.app.classes.ReservationTable;
import fr.isty.architecture.app.classes.Salle;
import fr.isty.architecture.data.Database;
import com.google.gson.Gson;

public class Controller {

    private Model model;
    private View view;
    private List<Personne> personnes = new ArrayList<>();
    private List<Salle> salles = new ArrayList<>();
    private List<Horaire> horaires = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
//        Database.init();
        fetchData();
    }

    public void startApplication() {
        AddActionButton();
        view.frame();
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true); // Rend la fenêtre Swing visible
        });
    }

    private void fetchData() {
        view.reservationModel.fetchReservation();
        view.salleListModel.clear();
        view.personneListModel.clear();
        view.horaireListModel.clear();
        personnes.clear();
        salles.clear();
        reservations.clear();
        horaires.clear();
        for (Salle s : Database.fetchSalle()) {
            salles.add(s);
            view.salleListModel.addElement(s.getNom() + " " + s.getLocalisation());
        }
        for (Personne p : Database.fetchPersonne()) {
            personnes.add(p);
            view.personneListModel.addElement(p.getNom());
        }
        for (Horaire h : Database.fetchHoraires()) {
            horaires.add(h);
            view.horaireListModel.addElement(h.toString());
        }
        for (Reservation r : Database.fetchReservation()) {
            reservations.add(r);
        }
    }

    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean isValidTimeFormat(String timeString) {
        if (timeString != null && !timeString.isEmpty()) {
            try {
                LocalTime.parse(timeString, TIME_FORMATTER);
                return true;
            } catch (DateTimeParseException e) {
                showErrorDialog("Le format de l'heure doit être hh:mm");
                return false;
            }
        }
        showErrorDialog("Le format de l'heure doit être hh:mm et comprise entre 00:00 et 23:59");
        return false;

    }

    private void AddActionButton() {
        addActionButtonCreation();
        AddActionButtonSupprHoraire();
        AddActionButtonSupprPersonne();
        AddActionButtonSupprSalle();
        AddActionButtonSupprReserv();
        AddActionButtonAddReserv();
        AddActionButtonModifHoraire();
    }

    private void addActionButtonCreation() {
        view.createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedChoice = (String) view.choiceComboBox.getSelectedItem();
                if ("Horaire".equals(selectedChoice)) {
                    String debutStr = "";
                    String finStr = "";
                    do {
                        debutStr = JOptionPane.showInputDialog("Heure de début (hh:mm) :");
                    } while (!isValidTimeFormat(debutStr));

                    do {
                        finStr = JOptionPane.showInputDialog("Heure de fin (hh:mm) :");
                    } while (!isValidTimeFormat(finStr));

                    if (debutStr != null && finStr != null && !debutStr.isEmpty() && !finStr.isEmpty()) {
                        LocalTime debut = LocalTime.parse(debutStr);
                        LocalTime fin = LocalTime.parse(finStr);
                        String debutAsString = debut.toString();  // Convertit `debut` en String
                        String finAsString = fin.toString();
                        // Créez une nouvelle instance de Horaire avec les heures de début et de fin spécifiées
                        Horaire nouvelHoraire = new Horaire(debutAsString, finAsString);
                        model.add(nouvelHoraire); // Ajoutez l'horaire à la liste
                        view.horaireListModel.addElement(nouvelHoraire.getDebut() + " - " + nouvelHoraire.getFin()); // Ajoutez l'heure de début et de fin au modèle du tableau
                        Database.addHoraire(nouvelHoraire);
                        Database.commitAll();
                    }
                } else if ("Personne".equals(selectedChoice)) {
                    String resourceName = JOptionPane.showInputDialog("Nom de la personne :");
                    Personne nouvellePersonne = new Personne(resourceName);
                    model.add(nouvellePersonne);
                    view.personneListModel.addElement(nouvellePersonne.getNom()); // Ajoutez le nom de la personne au modèle du tableau
                } else if ("Salle".equals(selectedChoice)) {
                    String resourceName = JOptionPane.showInputDialog("Nom de la Salle:");
                    String localisation = JOptionPane.showInputDialog("Localisation de la salle :");
                    if (localisation != null && !localisation.isEmpty()) {
                        // Créez une nouvelle instance de Salle avec le nom et la localisation spécifiés
                        Salle nouvelleSalle = new Salle(resourceName, localisation);
                        model.add(nouvelleSalle); // Ajoutez la salle à la liste
                        fetchData();
                    }
                }
                fetchData();

            }
        });

    }

    private void AddActionButtonSupprHoraire() {
        // Gestion de la suppression d'un horaire
        view.supprimerHoraireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = view.horaireList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Horaire horaireASupprimer = horaires.get(selectedIndex);
                    long idASupprimer = horaireASupprimer.getId();
                    System.out.println("id a supprimer " + idASupprimer);

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8081/api/horaires/" + idASupprimer))
                            .header("Content-Type", "application/json")
                            .DELETE()
                            .build();
                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        fetchData();
                    }
                    /*List<Integer> toDelete = new ArrayList<Integer>();
                    for (int i = 0; i < view.reservationTable.getRowCount(); i++) {
                        if (view.reservationTable.getValueAt(i, 2).equals(model.getHoraires().get(selectedIndex).getDebut() + " - " + model.getHoraires().get(selectedIndex).getFin())) {
                            toDelete.add(i);
                        }
                    }
                    for (int i = toDelete.size(); i > 0; i--) {
                        ReservationTable reservationTableModel = (ReservationTable) view.reservationTable.getModel();
                        reservationTableModel.removeReservation(i - 1);
                    }
                    model.remove(selectedIndex, "horaires"); // Supprimez l'horaire de la liste
                    view.horaireListModel.remove(selectedIndex); // Supprimez l'heure de début et de fin du modèle du tableau*/

                } else {
                    showErrorDialog("Il faut selectionnez l'Horaire pour la supprimer");
                }
            }
        });
    }

    private void AddActionButtonSupprPersonne() {
        // Gestion de la suppression d'une personne
        view.supprimerPersonneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = view.personneList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Personne personneASupprimer = personnes.get(selectedIndex);
                    long idASupprimer = personneASupprimer.getId();
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8081/api/personnes/" + idASupprimer))
                            .header("Content-Type", "application/json")
                            .DELETE()
                            .build();
                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        fetchData();
                    }
                } else {
                    showErrorDialog("Il faut selectionnez la Personne pour la supprimer");
                }
            }
        });
    }

    private void AddActionButtonSupprSalle() {
        // Gestion de la suppression d'une salle
        view.supprimerSalleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = view.salleList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Salle salleASupprimer = salles.get(selectedIndex);
                    long idASupprimer = salleASupprimer.getId();
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8081/api/salles/" + idASupprimer))
                            .header("Content-Type", "application/json")
                            .DELETE()
                            .build();
                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        fetchData();
                    }
                } else {
                    showErrorDialog("Il faut selectionnez la salle pour la supprimer");
                }
            }
        });
    }

    private void AddActionButtonSupprReserv() {
        // Gestion de la suppression d'une réservation
        view.supprimerReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int[] selection = view.reservationTable.getSelectedRows();
                if (selection.length != 0) {
                    for (int i = selection.length - 1; i >= 0; i--) {
                        Reservation reservation = reservations.get(selection[i]);
                        long idASupprimer = reservation.getId();
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8081/api/reservations/" + idASupprimer))
                                .header("Content-Type", "application/json")
                                .DELETE()
                                .build();
                        try {
                            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    fetchData();
                } else {
                    showErrorDialog("Il faut selectionnez la Réservation pour la supprimer");
                }
            }
        });
    }

    private void AddActionButtonAddReserv() {
        view.reservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int idS = view.salleList.getSelectedIndex();
                int idP = view.personneList.getSelectedIndex();
                int idH = view.horaireList.getSelectedIndex();
                Personne selectedPersonne = personnes.get(idP);
                Salle selectedSalle = salles.get(idS);
                Horaire selectedHoraire = horaires.get(idH);
                var reservationInfo = Map.of(
                        "personneId", selectedPersonne.getId(),
                        "salleId", selectedSalle.getId(),
                        "horaireId", selectedHoraire.getId()
                );


                Gson gson = new Gson();

                String jsonBody = null;
                try {
                    jsonBody = gson.toJson(reservationInfo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8081/api/reservations"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("response " + response.statusCode());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    fetchData();
                }
//                if (idS != -1 && idP != -1) {
//                    int[] selectedIndices = view.horaireList.getSelectedIndices();
//
//                    if (selectedIndices.length > 0) {
//                        Salle s = model.getSalles().get(idS);
//                        Personne p = model.getPersonnes().get(idP);
//
//                        for (int selectedIdx : selectedIndices) {
//                            Horaire h = model.getHoraires().get(selectedIdx);
//                            Reservation reservation = new Reservation(p, s, h);
//                            view.reservationModel.addReservation(reservation);
//                        }
//
//                        view.salleList.clearSelection();
//                        view.personneList.clearSelection();
//                        view.horaireList.clearSelection();
//                    } else {
//                        showErrorDialog("Veuillez sélectionner au moins un horaire.");
//                    }
//                } else {
//                    showErrorDialog("Il faut cliquer sur une Personne ET une Salle");
//                }
            }
        });
    }

    private void AddActionButtonModifHoraire() {
        view.modifierHoraireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = view.reservationTable.getSelectedRow();
                if (selectedIndex != -1) {
                    Reservation reservationAModifier = view.reservationModel.getReservation(selectedIndex);
                    String debutStr;
                    String finStr;
                    // Affichez une boîte de dialogue pour obtenir le nouvel horaire
                    do {
                        debutStr = JOptionPane.showInputDialog("Nouvelle heure de début (hh:mm) :");
                    } while (!isValidTimeFormat(debutStr));

                    do {
                        finStr = JOptionPane.showInputDialog("Nouvelle heure de début (hh:mm) :");
                    } while (!isValidTimeFormat(finStr));

                    // Vérifiez si les formats des heures sont valides
                    if (isValidTimeFormat(debutStr) && isValidTimeFormat(finStr)) {
                        LocalTime nouvelleHeureDebut = LocalTime.parse(debutStr, TIME_FORMATTER);
                        LocalTime nouvelleHeureFin = LocalTime.parse(finStr, TIME_FORMATTER);
                        String debutAsString = nouvelleHeureDebut.toString();
                        String finAsString = nouvelleHeureFin.toString();
                        Horaire nouvelHoraire = new Horaire(debutAsString, finAsString);
                        // Modifiez l'horaire de la réservation
                        reservationAModifier.setHoraire(nouvelHoraire);
                        // Ajoutez le nouvel horaire à la liste des horaires si nécessaire
                        if (!model.getHoraires().contains(reservationAModifier.getHoraire())) {
                            model.add(reservationAModifier.getHoraire());
                            view.horaireListModel.addElement(reservationAModifier.getHoraire().getDebut() + " - " + reservationAModifier.getHoraire().getFin());
                        }

                        // Mettez à jour le modèle de la table
                        view.reservationModel.fireTableRowsUpdated(selectedIndex, selectedIndex);
                    } else {
                        showErrorDialog("Format d'heure invalide. Utilisez le format hh:mm.");
                    }
                } else {
                    showErrorDialog("Sélectionnez une réservation à modifier.");
                }
            }
        });
    }
}
