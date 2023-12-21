package fr.isty.architecture.app.classes;

public class Salle {
    private Long id;
    private String nom;
    private String localisation;
    private boolean occupee;

    public Salle() {
    }

    public Salle(String nom) {
        this.nom = nom;

    }
    public Salle(String nom, String localisation) {
        this.nom = nom;
        this.localisation = localisation;
        this.occupee = false; // Au départ, la salle est considérée comme non occupée
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public boolean isOccupee() {
        return occupee;
    }

    public void setOccupee(boolean occupee) {
        this.occupee = occupee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
