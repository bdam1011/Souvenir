package gift_java;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class AutoChangeLine extends JTextArea implements TableCellRenderer {
	AutoChangeLine(){
		setLineWrap(true);
        setWrapStyleWord(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setText(value.toString());
    	setSize(table.getColumnModel().getColumn(column).getWidth(), 80);
    	//table.setFont(new Font("18",1,18));
    	if (table.getRowHeight(row) != 80) {
    		table.setRowHeight(row,80);
    	}
    	return this;
	}

}
