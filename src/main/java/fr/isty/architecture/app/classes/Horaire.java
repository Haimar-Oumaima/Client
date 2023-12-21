package fr.isty.architecture.app.classes;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Horaire {
    private Long id;
    private String debut;
    private String fin;

    public Horaire() {
    }

    public Horaire(String debut, String fin) {
        this.debut = debut;
        this.fin = fin;
    }
    
    @Override
    public String toString() {
    	return getDebut() + " - " + getFin();
    }
    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }



    public Long getId() {
        return id;
    }
}
