package fr.isty.architecture.app.classes;

public class Reservation {


    private Long id;
    private Personne personne;
    private Salle salle;
    private Horaire horaire;

    public Reservation() {
    }

    public Reservation(Personne personne, Salle salle, Horaire horaire) {
        this.personne = personne;
        this.salle = salle;
        this.horaire = horaire;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Horaire getHoraire() {
        return horaire;
    }

    public void setHoraire(Horaire horaire) {
        this.horaire = horaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
