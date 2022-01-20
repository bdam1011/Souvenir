package gift_java;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class HelpTableModel extends DefaultTableModel{
	JTable jtable;
	String sqlupdate;
	public HelpTableModel(JTable jtable) {
		this.jtable = jtable;
	}
	public HelpTableModel(JTable jtable, String sqlupdate) {
		this.jtable = jtable;
		this.sqlupdate = sqlupdate;	
	}

	public boolean isCellEditable(int row, int col){
		return true; 
	}
	public void setValueAt(Object value, int row, int col) {
	
		value =jtable.getValueAt(row,col);
	    fireTableCellUpdated(row, col);
    }
	
	public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        Object data = model.getValueAt(row, column);
	}
}
