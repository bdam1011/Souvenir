package gift_java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class HelperListener implements TableModelListener{
	JTable jtable;
	String sqlupdate;
	public HelperListener(JTable jtable){
		this.jtable = jtable;
		
	}
	public void tableChanged(TableModelEvent e) {
		 int row = e.getFirstRow();
	     int column = e.getColumn();
	     TableModel tableModel = (TableModel)e.getSource();
	     String columnName = tableModel.getColumnName(column);
	     Object data = tableModel.getValueAt(row, column); 
	     tableModel.isCellEditable(row, column);
        	
    }
       
}