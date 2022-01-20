package gift_java;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;


public class MainFrame extends JFrame{
	JTable jtable;
	JPanel left,right,down;
	JButton select,pageUp,pageDown,pageFirst,pageLast,add,delete,update,databasein;
	JLabel pageinfo,chooserinfo,whereinfo;
	JTextArea columnChooser1,columnChooser2;
	JList<String >columNames1,columNames2;
	String[] columnName = {"*","id","name","feature","salePlace","produceOrg","specAndPrice","contactTel","picurl","orderUrl"};
	int page=1,start = 0;

	

	public MainFrame() {
		super("農委會伴手禮Open Data");
		
		jtable = new JTable(new DefaultTableModel());
		left = new JPanel(); right = new JPanel(); down = new JPanel();
		select = new JButton("Select"); pageUp = new JButton("Page UP"); pageDown = new JButton("Page Down");
		add = new JButton("Add"); delete = new JButton("Delete"); update = new JButton("Update");databasein = new JButton("Data in");
		pageFirst= new JButton("First Page");pageLast =new JButton("Last Page");
		pageinfo = new JLabel("Page");chooserinfo = new JLabel("Choose");whereinfo= new JLabel("Where");
		columNames1 =new JList<>(columnName);columNames2 =new JList<>(columnName);
		columnChooser1 = new JTextArea();columnChooser2 = new JTextArea();
		start = (page-1)*10;
		
		layoutView();
		
		setEvent();
		
		
		setVisible(true);
		setSize(900,680);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	
	private void layoutView() {
		Font Font18 =(new Font("JLabel",1,18));
		Font Font16 =(new Font("JLabel",1,16));
		Font Font14 =(new Font("JLabel",1,14));
		
		pageUp.setFont(Font18);pageinfo.setFont(Font18);pageDown.setFont(Font18);pageFirst.setFont(Font18);pageLast.setFont(Font18);
		select.setFont(Font16);chooserinfo.setFont(Font18);whereinfo.setFont(Font18);
		columnChooser1.setFont(Font14);columnChooser2.setFont(Font14);
		
		//add.setFont(Font16);delete.setFont(Font16);update.setFont(Font16);edit.setFont(Font16);
		
		setLayout(new BorderLayout());
		
		add(down,BorderLayout.SOUTH);
		down.setLayout(new FlowLayout());
		down.add(pageFirst);down.add(pageUp);down.add(pageinfo);down.add(pageDown);down.add(pageLast);
		
		
		
        add(left,BorderLayout.WEST);
		left.setLayout(new GridLayout(6,1));
		left.add(chooserinfo);
		JScrollPane jscrList1 = new JScrollPane(columNames1);
		left.add(jscrList1);
		columNames1.setVisibleRowCount(5);
		listenJList(columNames1,columnChooser1);
		//columNames1.addListSelectionListener(this);
		JScrollPane jscrArea1 = new JScrollPane(columnChooser1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		left.add(jscrArea1);
		
		left.add(whereinfo);
		JScrollPane jscrList2 = new JScrollPane(columNames2);
		left.add(jscrList2);
		columNames2.setVisibleRowCount(5);
		listenJList(columNames2,columnChooser2);
		//columNames2.addListSelectionListener(this);
		JScrollPane jscrArea2 = new JScrollPane(columnChooser2,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		left.add(jscrArea2);
		
		
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER ;
        gbc.weighty = 1;
        gbc.gridheight = 3;
		add(right,BorderLayout.EAST);
		right.setLayout(gbl);
		right.add(databasein,gbc);right.add(add,gbc);right.add(delete,gbc);right.add(update,gbc); right.add(select,gbc);
		
		
		
		
		JScrollPane jscrtable = new JScrollPane(jtable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jscrtable,BorderLayout.CENTER);
		jtable.setLayout(new BorderLayout());
        JTableHeader header = jtable.getTableHeader();
        header.setFont(Font18);
        jtable.add(header,BorderLayout.NORTH);

	}


	
	private void setEvent() {
		//select,pageUP,pageDown,add,delete,update;
	
		select.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String presql1 = columnChooser1.getText().replace("\n",",");
				presql1 = presql1.substring(0,presql1.length()-1);
				String presql2 = columnChooser2.getText();
				System.out.println();
				String sqlselect;
				if(presql2.equals("")) {
					sqlselect = "SELECT "+presql1+" FROM giftinfo";
				}else {
					sqlselect = "SELECT "+presql1+" FROM giftinfo where "+presql2 ;
				}
				System.out.println(sqlselect);
				Properties prop = new Properties();
				prop.put("user", "root");
				//prop.put("password", "root");
				
				try(Connection connection = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/gift", prop)) {
				
					PreparedStatement pstmt = connection.prepareStatement(sqlselect);
					ResultSet rs =pstmt.executeQuery();
					resultSetToTableModel(rs,jtable);

					
					
				} catch (Exception e1) {
					System.out.println(e1.toString());;
				}
				
			}});
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = new DefaultTableModel();
		        model.addColumn("id");
				model.addColumn("name");
		        model.addColumn("feature");
		        model.addColumn("salePlace");
		        model.addColumn("produceOrg");
		        model.addColumn("specAndPrice");
		        model.addColumn("contactTel");
		        model.addColumn("picurl");
		        model.addColumn("orderUrl");
				
		        Object[] row = new Object[model.getColumnCount()];
		        model.addRow(row);
		        
		        jtable.setModel(model);
		        jtable.setRowHeight(80);
		        jtable.getModel().addTableModelListener(new TableModelListener() {
					@Override
					public void tableChanged(TableModelEvent e) {
						 int row = e.getFirstRow();
					     int column = e.getColumn();
					     TableModel tableModel = (TableModel)e.getSource();
					     String columnName = tableModel.getColumnName(column);
					     Object data = tableModel.getValueAt(row, column); 
					     tableModel.isCellEditable(row, column);     
					}} );
		           
			}});
		update.addActionListener(new ActionListener() {
			String sqlupdate ="";

			@Override
			public void actionPerformed(ActionEvent e) {
				jtable.getModel().addTableModelListener(new TableModelListener() {

					@Override
					public void tableChanged(TableModelEvent e) {
						 int row = e.getFirstRow();
					     int column = e.getColumn();
					     TableModel tableModel = (TableModel)e.getSource();
					     String columnName = tableModel.getColumnName(column);
					     Object data = tableModel.getValueAt(row, column); 
					     tableModel.isCellEditable(row, column);
					     
					     if(tableModel.getValueAt(row,0)==null) {
					     String name = tableModel.getValueAt(row,1).toString();
					     String feature = tableModel.getValueAt(row,2).toString();
					     String salePlace = tableModel.getValueAt(row,3).toString();
					     String produceOrg = tableModel.getValueAt(row,4).toString();
					     String specAndPrice = tableModel.getValueAt(row,5).toString();
					     String contactTel = tableModel.getValueAt(row,6).toString();
					     String picurl = tableModel.getValueAt(row,7).toString();
					     String orderUrl = tableModel.getValueAt(row,8).toString();    
//					         	sqlupdate = "INSERT INTO giftinfo (id, name, feature, salePlace, produceOrg, specAndPrice, contactTel, picurl, orderUrl) "
//					         			+ "VALUES (NULL,?,?,?,?,?,?,?,?)";					     
					     sqlupdate = "INSERT INTO giftinfo (id, name, feature, salePlace, produceOrg, specAndPrice, contactTel, picurl, orderUrl) "
					        		+ "VALUES (NULL,+ '"+name+"', '"+feature+"', '"+salePlace+"','"+produceOrg+"','"+
					        			specAndPrice+"','"+contactTel+"','"+ picurl+"','"+ orderUrl+"')";
					     }else {
					    	 String id = tableModel.getValueAt(row,0).toString();
					    	 sqlupdate = "UPDATE giftinfo SET "+ columnName + " = '"+ data +"' WHERE id = "+ id;
					    	 System.out.println(sqlupdate);
					     }	
					     Properties prop = new Properties();
					     prop.put("user", "root");
					     try (Connection connection = DriverManager.getConnection(
					 		"jdbc:mysql://localhost:3306/gift", prop)) { 
					    	 PreparedStatement pstmt = connection.prepareStatement(sqlupdate);
					    	 pstmt.executeUpdate();
					     }catch(Exception e2) {
					    	 System.out.println(e.toString());
					     }
					     JOptionPane.showMessageDialog(null,"Update Successfully!");
						
					}});
		
			}});
		delete.addActionListener(new ActionListener() {
			String sqldelete = "";
			@Override
			public void actionPerformed(ActionEvent e) {
				jtable.getModel().addTableModelListener(new TableModelListener() {
					
					@Override
					public void tableChanged(TableModelEvent e) {
						int row = e.getFirstRow();
						int column = e.getColumn();
						TableModel tableModel = (TableModel)e.getSource();
						String columnName = tableModel.getColumnName(column);
						Object data = tableModel.getValueAt(row, column); 
						tableModel.isCellEditable(row, column);
					    
						//warning
						Object[] options = { "OK", "CANCEL" };
						JOptionPane.showOptionDialog(null, "Click OK to continue", "Warning",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
						
					    sqldelete ="DELETE FROM giftinfo WHERE " + columnName+" = "+ data;
					    
					    Properties prop = new Properties();
					    prop.put("user", "root");
					    try (Connection connection = DriverManager.getConnection(
					    		"jdbc:mysql://localhost:3306/gift", prop)) {
					    	PreparedStatement pstmt = connection.prepareStatement(sqldelete);
					    	pstmt.executeUpdate();
					    	 	
					    }catch(Exception e2) {
					    	System.out.println(e.toString());
					    }
					    JOptionPane.showMessageDialog(null,"Delete Complete!");
					}
				});
				
			}});
		
		
		
		pageUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(page>1) { page--;}
				start = (page-1)*10;	  
				String sql= String.format("SELECT * FROM giftinfo ORDER BY id LIMIT %d,10",start);
				pageinfo.setText("page:"+page);
				pageinfo.repaint();
				dataQuery(sql);	
				
			}});
		pageDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {	
				if(page<=22) {page++;}
				start = (page-1)*10;
				String sql= String.format("SELECT * FROM giftinfo ORDER BY id LIMIT %d,10",start);
				pageinfo.setText("page:"+page);
				pageinfo.repaint();
				dataQuery(sql);
				
			}});
		pageFirst.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String sql= String.format("SELECT * FROM giftinfo ORDER BY id LIMIT %d,10",0);
				page=1;
				pageinfo.setText("page:"+page);
				pageinfo.repaint();
				dataQuery(sql);
				
			}});
		pageLast.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				page=22;
				start = (page-1)*10;
				String sql= String.format("SELECT * FROM giftinfo ORDER BY id LIMIT %d,10",start);
				pageinfo.setText("page:"+page);
				pageinfo.repaint();
				dataQuery(sql);
				
			}});
		
		
		
		databasein.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String jsonData = GiftDataBase.fetchOpendata();
				//System.out.println(jsonData);
				if (jsonData != null) {
					GiftDataBase.parseOpendata(jsonData);	
				}
				JOptionPane.showMessageDialog(null,"Input Sccess!");
				
			}});
		
	}
	private void dataQuery(String sql) {
		Properties prop = new Properties();
		prop.put("user", "root");
		//prop.put("password", "root");
		
		try(Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/gift", prop)) {
		
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs =pstmt.executeQuery();
			resultSetToTableModel(rs,jtable);
		} catch (Exception e1) {
			System.out.println(e1.toString());;
		}
	}
	
	private void resultSetToTableModel(ResultSet rs, JTable jtable) throws SQLException{
		DefaultTableModel tableModel = new DefaultTableModel();
        ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
            tableModel.addColumn(metaData.getColumnLabel(columnIndex));
        }
        Object[] row = new Object[columnCount];
        
        while (rs.next()){
            for (int i = 0; i < columnCount; i++){
                row[i] = rs.getObject(i+1);
            }
            tableModel.addRow(row);
        }
        jtable.setModel(tableModel);
        jtable.setFont(new Font("table",1,18));
        
        //自動換行
        for(int i =0;i<jtable.getColumnCount();i++) {
        	jtable.getColumnModel().getColumn(i).setCellRenderer(new AutoChangeLine());
        }
        jtable.getColumnModel().getColumn(0).setPreferredWidth(40);
        jtable.getColumnModel().getColumn(jtable.getColumnCount()-1).setPreferredWidth(40);
        
    }
	
	private void listenJList(JList<String> jList,JTextArea jTextA) {
		ListSelectionModel listSelectionModel = jList.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ListHelperListenser(jList,jTextA));
	}

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				new MainFrame();
				
			}
		});

	}

}

