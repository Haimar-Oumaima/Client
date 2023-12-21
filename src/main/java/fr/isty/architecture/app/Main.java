package fr.isty.architecture.app;

import fr.isty.architecture.data.Database;

public class Main {
    public static void main(String[] args) {
        // Créez des instances du modèle, de la vue et du contrôleur
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(view,model);

        // Démarrez l'application à partir du contrôleur
        controller.startApplication();
    }
}
