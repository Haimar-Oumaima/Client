package fr.isty.architecture.app;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import fr.isty.architecture.app.classes.ReservationTable;
import fr.isty.architecture.data.Database;

import java.awt.*;


public class View extends JFrame{
    private JFrame frame;
    public JComboBox<String> choiceComboBox;


    public void frame() {
        initialize();
    }

    public JComboBox<String> GetCombobox()
    {
        return this.choiceComboBox;
    }

    private void initialize() {
        setTitle("Gestion des Ressources");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);  // Taille initiale de la fenêtre

        JPanel panel = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.2);
        splitPane.setDividerSize(0);
        splitPane.setOneTouchExpandable(true);
        
        JPanel leftPanel = UpdateLeftPanel();
        JPanel rightPanel = createRightPanel();
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        panel.add(splitPane, BorderLayout.CENTER);

        setContentPane(panel);
    }

    public JFrame getFrame() {
        return frame;
    }

    private JPanel UpdateLeftPanel() {
        // Créez votre permière grille (c1) ici
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        choiceComboBox = new JComboBox<>(new String[]{"Horaire", "Personne", "Salle"});
       
        

        // Ajoutez du padding autour du rightPanel en utilisant EmptyBorder
        int padding = 20; // Définissez la taille du padding en pixels
        leftPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        
        c.insets = new Insets(5, 5, 5, 5); // Padding de 5 pixels sur tous les côtés
        
        c.weightx=1;
        c.weighty=1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        leftPanel.add(new JLabel("Créer :"), c);

        c.gridx = 1;
        leftPanel.add(choiceComboBox, c);
        c.gridx = 2;
        
        leftPanel.add(createButton, c);
        c.gridy = 1;
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        
        leftPanel.add(reservationButton, c);

       

        return leftPanel;
    }


    public DefaultListModel<String> horaireListModel = new DefaultListModel<>();
    public DefaultListModel<String> personneListModel  = new DefaultListModel<>();
    public DefaultListModel<String> salleListModel  = new DefaultListModel<>();

    public JList<String> horaireList = new JList<>(horaireListModel);
    public JList<String> personneList = new JList<>(personneListModel);
    public JList<String> salleList = new JList<>(salleListModel);

    public ReservationTable reservationModel = new ReservationTable();
    public JTable reservationTable;
 
    public JButton createButton = new JButton("Créer");
    public JButton reservationButton = new JButton("Réserver");
    public JButton supprimerHoraireButton = new JButton("Supprimer Horaire");
    public JButton supprimerPersonneButton = new JButton("Supprimer Personne");
    public JButton supprimerSalleButton = new JButton("Supprimer Salle");
    public JButton modifierHoraireButton = new JButton("Modifier Horaire");
    public JButton supprimerReservationButton = new JButton("Supprimer Réservation");

    private JPanel createRightPanel() {
        
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

       
        
        reservationTable = new JTable(reservationModel);
        reservationTable.setAutoCreateRowSorter(true);
        
        c.weightx = 1.0;
        c.weighty = 1.0;
        // Ajoutez du padding autour du rightPanel en utilisant EmptyBorder
        int padding = 20; // Définissez la taille du padding en pixels
        rightPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        c.insets = new Insets(5, 5, 5, 5); // Padding de 5 pixels sur tous les côtés

        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 1;
        c.weighty = 0.1;
        JLabel horaireLabel = new JLabel("Horaire", SwingConstants.CENTER); // Centrer horizontalement
        rightPanel.add(horaireLabel, c);

        c.gridx = 1;
        JLabel personneLabel = new JLabel("Personne", SwingConstants.CENTER); // Centrer horizontalement
        rightPanel.add(personneLabel, c);

        c.gridx = 2;
        JLabel salleLabel = new JLabel("Salle", SwingConstants.CENTER); // Centrer horizontalement
        rightPanel.add(salleLabel, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.weighty = 0.4;
        rightPanel.add(new JScrollPane(horaireList), c);
        c.gridx = 1;
        rightPanel.add(new JScrollPane(personneList), c);
        c.gridx = 2;
        rightPanel.add(new JScrollPane(salleList), c);
        
        
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 1;
        c.weighty = 0.1;

        
        rightPanel.add(supprimerHoraireButton, c);

        c.gridx = 1;
        
        rightPanel.add(supprimerPersonneButton, c);

        c.gridx = 2;
        
        rightPanel.add(supprimerSalleButton, c);


        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 3;
        c.weighty = 0.4;
        rightPanel.add(new JScrollPane(reservationTable),c);


        // Ajoutez un bouton "Modifier Horaire" à votre interface utilisateur
        
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 4;
        rightPanel.add(modifierHoraireButton,c);

        // Ajoutez le bouton "Supprimer Réservation" à votre interface graphique
        
        c.gridy = 4;
        c.gridx = 2;
        c.gridwidth = 1;
        
        rightPanel.add(supprimerReservationButton, c);    

        return rightPanel;
    }

    @Override
	public void dispose() {
		Database.close();
	    super.dispose();
	}
    
}
