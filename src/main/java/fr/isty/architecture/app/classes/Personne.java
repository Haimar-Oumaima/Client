package fr.isty.architecture.app.classes;

public class Personne {
    private Long id;
    private String nom;

    // Constructeur par défaut nécessaire pour la désérialisation JSON
    public Personne() {
    }

    public Personne(String nom) {
        this.nom = nom;
    }

    // Getters and setters pour 'id' et 'nom'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
