package fr.isty.architecture.app.classes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import fr.isty.architecture.data.Database;

public class ReservationTable extends AbstractTableModel {
    private List<Reservation> reservations = new ArrayList<Reservation>();
 
    public List<Reservation> getReservations() {
		return reservations;
	}

	private final String[] entetes = {"Salle", "Personne", "Horaire"};
 
    public ReservationTable() {
        super();
    }
 
    public int getRowCount() {
        return reservations.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if(columnIndex == 2) {
    		return true; 
    	}
    	else
    	{
    		return false;
    	}
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            Reservation reservation = reservations.get(rowIndex);
            switch(columnIndex){
                case 2:
                    reservation.setHoraire((Horaire) aValue);
                    break;
            }
        }
    }
    @Override
    public Class getColumnClass(int columnIndex){
        switch(columnIndex){
            case 2 :
                return Horaire.class;
            default:
                return Object.class;
        }
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return reservations.get(rowIndex).getSalle().getNom();
            case 1:
                return reservations.get(rowIndex).getPersonne().getNom();
            case 2:
                return reservations.get(rowIndex).getHoraire().toString();
            default:
                return null;
        }
    }
    
    public void fetchReservation() {
    	reservations = Database.fetchReservation();
        fireTableRowsInserted(reservations.size() -1, reservations.size() -1);
    }
    public void addReservation(Reservation reservation) {
    	reservations.add(reservation);
    	Database.addReservation(reservation);
    	Database.commitAll();
        fireTableRowsInserted(reservations.size() -1, reservations.size() -1);
    }
    public Reservation getReservation(int rowIndex){
        return reservations.get(rowIndex);
    }
 
    public void removeReservation(int rowIndex) {
    	Database.deleteReservation(reservations.get(rowIndex));
    	Database.commitAll();
    	reservations.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public void modifyReservation(int rowIndex) {
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
