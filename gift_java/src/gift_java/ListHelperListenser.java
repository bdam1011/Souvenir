package gift_java;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListHelperListenser implements ListSelectionListener{
	JList<String> jList;JTextArea jTextA;
	public ListHelperListenser(JList<String> jList,JTextArea jTextA){
		this.jList = jList;
		this.jTextA =jTextA;
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		ArrayList<String> values = new ArrayList<>(jList.getSelectedValuesList()); 
		int[] indices = jList.getSelectedIndices();
		String show = "";
		int i = 0;
		for(String s : values) {
			show += s+"\n";
			i++;
		}
		jTextA.setText(show);
		
	}

}
