/*******************************************************
	 *  Class name: ViewPersonnelView
 	 *  Inheritance: JPanel
	 *  Attributes: model
	 *  Methods:	ViewPersonnelView, setClientListener, setRemoveListener,
	 * 		updateClientList, updateTable, setStatus, askConfirmation
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class ViewPersonnelView extends JPanel {

	private JButton deleteBtn;
	private JComboBox<Object> clientCBox;
	private DefaultTableModel tableModel;
	private JLabel selectClientLbl;
	private JLabel statusLbl;
	private JScrollPane personnelPane;
	private JTable personnelTable;
	private JTableHeader header;
	private PayrollSystemModel model;
	
	public ViewPersonnelView(PayrollSystemModel model) {
		this.model = model;
		
		deleteBtn = new JButton(new ImageIcon(
				getClass().getResource("/images/buttons/delete.png")));
		
		statusLbl = new JLabel("Status: No Data Found!");
		statusLbl.setIcon(loadScaledImage("/images/notifs/warning.png",.08f));
		
		selectClientLbl = new JLabel("Select Client: ");

		clientCBox = new JComboBox<Object>();
		
		personnelTable = new JTable(30,12);
		personnelTable.setRowHeight(32);
		personnelTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		personnelTable.setColumnSelectionAllowed(true);
		personnelTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		personnelTable.setEnabled(false);
		
		ArrayList<String> columnName = model.getTableColumn("personnel");
		columnName.add(0, "No.");
		tableModel = new DefaultTableModel(columnName.toArray(),0);
		personnelTable.setModel(tableModel);
		
		header = personnelTable.getTableHeader();
		header.setBackground(new Color(0xFAFAFA));
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 25));
		header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		header.setReorderingAllowed(false);
		
		for(int i = 0; i < personnelTable.getColumnCount(); i++){
			if(i == 0) {
				personnelTable.getColumnModel().getColumn(i).setCellRenderer(
						new ColorfulCellRenderer(new Color(0xFAFAFA),
								Color.BLACK,Utils.colorfulPColumn));
				personnelTable.getColumnModel().getColumn(i).setPreferredWidth(40);
			}
			else if(Utils.colorfulPColumn.contains(i))
			{
				switch(i){
				case 2:
					personnelTable.getColumnModel().getColumn(i).setCellRenderer(
							new ColorfulCellRenderer(new Color(0xbee1fe),
									Color.BLACK,Utils.colorfulPColumn));
					break;
				default:
					personnelTable.getColumnModel().getColumn(i).setCellRenderer(
							new ColorfulCellRenderer(Color.ORANGE,Color.BLACK,
									Utils.colorfulPColumn));
					break;
				};
			} else
				personnelTable.getColumnModel().getColumn(i).setCellRenderer(
						new ColorfulCellRenderer(Color.WHITE,Color.BLACK,
								Utils.colorfulPColumn));
		}
		
		personnelTable.addMouseListener(new TableMouseListener());
		
		personnelPane = new JScrollPane(personnelTable, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		modifyUI();
	}
	
	private void modifyUI() {
		setSize(new Dimension(851,670));
		setBackground(Utils.BODY_COLOR);
		
		personnelPane.setPreferredSize(
				new Dimension(this.getWidth()-500,this.getHeight()-300));
	
		deleteBtn.setContentAreaFilled(false);
		deleteBtn.setBorder(null);
		deleteBtn.setOpaque(false);
		deleteBtn.setForeground(null);
		deleteBtn.setFocusPainted(false);
		deleteBtn.setRolloverIcon(
				new ImageIcon(getClass().getResource("/images/buttons/delete-r.png")));
		deleteBtn.setPressedIcon(
				new ImageIcon(getClass().getResource("/images/buttons/delete-p.png")));
		deleteBtn.setSize(new Dimension(deleteBtn.getIcon().getIconWidth(),
				deleteBtn.getIcon().getIconHeight()));
		
		clientCBox.setPreferredSize(new Dimension(350,25));
		clientCBox.setBackground(Utils.comboBoxBGColor);
		clientCBox.setForeground(Utils.comboBoxFGColor);
		
		initFont();
		addComponentsToPane();
	}
	
	private void addComponentsToPane() {
		this.setLayout(new GridBagLayout());	
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,15,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(selectClientLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(clientCBox,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,15,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		add(deleteBtn,gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,15,25,15);
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(personnelPane,gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,10,13,0);
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(statusLbl,gbc);
	}

	public void initFont() {
		personnelTable.setFont(Utils.tableFont);
		header.setFont(Utils.tableFont);
		clientCBox.setFont(Utils.comboBoxFont);
		selectClientLbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.statusBarFont);
	}
	
	public class TableMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			JTable table = (JTable) e.getSource();
			int rowNum = table.rowAtPoint(e.getPoint());
			int colNum = table.columnAtPoint(e.getPoint());
					
			if(colNum == 0) {
				table.setColumnSelectionInterval(0, table.getColumnCount()-1);
			}
					
			if(SwingUtilities.isRightMouseButton(e)){
				table.clearSelection();
				table.changeSelection(rowNum, colNum, false, false);
				
				if(colNum == 0) {
					table.setColumnSelectionInterval(0, table.getColumnCount()-1);
				}
			
				int rowIndex = table.getSelectedRow();
				int colIndex = table.getSelectedColumn();
						
				if(rowIndex == rowNum && colIndex == 0 &&
						e.getModifiers() == InputEvent.META_MASK && e.getComponent() instanceof JTable){
					JMenuItem menuItem = new JMenuItem("Delete Personnel");
					
					menuItem.setFont(Utils.descFont);
					menuItem.setBackground(Color.WHITE);
					menuItem.setOpaque(false);
					menuItem.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent actionEvent) {
						    String client,personnel="",TIN="";
						    int row = 0;
						    client = getSelectedClient();
							   for(int i = 0;i < personnelTable.getColumnCount(); i++){
								   if(personnelTable.getColumnName(i).compareToIgnoreCase("name") == 0){
							    		row = personnelTable.getSelectedRow();
							    		personnel = (String)tableModel.getValueAt(row,i);
							    	}else if(personnelTable.getColumnName(i).compareToIgnoreCase("tin") == 0){
							    		row = personnelTable.getSelectedRow();
							    		TIN = (String)tableModel.getValueAt(row,i);
									}
							   }
							   try{
								   model.removePersonnel(client, personnel, TIN);
								   tableModel.removeRow(row);
								   setStatus("Successfully deleted.",true);
							   }catch(Exception ex){
							    	setStatus(ex.getMessage(),false);
							   }
						}
					});
						
					JPopupMenu popup = new JPopupMenu();
					popup.setBackground(Color.WHITE);
						
					popup.add(menuItem);
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Utils.statusBGColor);
		g2d.fillRect(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), Utils.HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-Utils.HEIGHT, 
				this.getWidth(),this.getHeight()-Utils.HEIGHT);
	}
	
	private ImageIcon loadScaledImage(String img_url, float percent){	
		ImageIcon img_icon = new ImageIcon(this.getClass().getResource(img_url));
		int new_width = (int) (img_icon.getIconWidth()*percent);
		int new_height = (int) (img_icon.getIconHeight()*percent);
		
		Image img = img_icon.getImage().getScaledInstance(new_width,new_height,
				java.awt.Image.SCALE_SMOOTH);  
		img_icon = new ImageIcon(img);
		
		return img_icon;
	}
	
	public void setClientListener(ActionListener list){
		clientCBox.addActionListener(list);
	}
	
	public void setRemoveListener(ActionListener list){
		deleteBtn.addActionListener(list);
	}
	
	public String getSelectedClient(){
		String client = "";
		client = (String)clientCBox.getSelectedItem();
		
		return client;
	}
	
	public void updateClientList(){
		clientCBox.removeAllItems();
		ArrayList<String> clients = model.getClientList();
		
		for(String t : clients){
			clientCBox.addItem(t);
		}
	}
	
	public void updateTable(){
		tableModel.setRowCount(0);
		try{
			ArrayList<Object[]> rowData = model.getPesonnelData((String)clientCBox.getSelectedItem());
			for(Object[] data:rowData){
				tableModel.addRow(data);
			}
		}catch(Exception ex){
			setStatus(ex.getMessage(),false);
		}
	}
	
	public void setStatus(String e, boolean b){
		statusLbl.setText("Status: "+e);
		if(b){
			statusLbl.setIcon(loadScaledImage("/images/notifs/right.png",.08f));
		}else{
			statusLbl.setIcon(loadScaledImage("/images/notifs/wrong.png",.08f));
		}
	}
	
	public void setStatus(String e){
		statusLbl.setText("Status: "+e);
		statusLbl.setIcon(null);
	}
	
	public boolean askConfirmation(){
		int confirmation = JOptionPane.showConfirmDialog(null, "Please confirm!", "Please confirm!", JOptionPane.YES_NO_OPTION);
		
		if(confirmation ==JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
}
