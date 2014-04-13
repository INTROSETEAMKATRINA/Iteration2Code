/*******************************************************
	 *  Class name: ViewSummaryReportView

 	 *  Inheritance: Jpanel
	 *  Attributes: model
	 *  Methods:	ViewSummaryReportView, getClient, getPeriodStartDate,
	 * 		getReport, updateTableColumn, setPeriodStartDateListener,
	 * 		setViewListener, updateTable, updateClientList,
	 * 		updateDateList, updateViewList, showError,
	 * 		empty
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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.*;


public class ViewSummaryReportView extends JPanel {

	private PayrollSystemModel model;
	
	private JTextField searchTxtFld;
	private JButton searchBtn;
	private JLabel categoryLbl;
	private JComboBox<Object> categoryCBox;
	
	private JLabel selectSummLbl;
	private JLabel selectClientLbl;
	private JLabel selectTimeLbl;
	private JLabel statusLbl;
	private JComboBox<Object> viewCBox;
	private JComboBox<Object> timePeriodCBox;
	private JComboBox<Object> clientCBox;
	
	private DefaultTableModel summaryModel;
	private JTable summaryTable;
	private JScrollPane summaryPane;
	
    private JTableHeader header;
	
	
	public ViewSummaryReportView(PayrollSystemModel model) {
		this.model = model;
		
		//
		searchTxtFld = new CustomTextField("Enter search keyword here.", 
				"/images/effects/in.png", "/images/effects/out.png", 180, 41);
		categoryLbl = new JLabel("Select Category to search: ");
		categoryCBox = new JComboBox<Object>();
		searchBtn = new JButton(new ImageIcon(
				getClass().getResource("/images/buttons/search.png")));
		
		statusLbl = new JLabel("Status: No Data Found!");
		statusLbl.setIcon(loadScaledImage("/images/notifs/warning.png",.08f));
		
		selectSummLbl = new JLabel("Select Summary Report: ");
		selectClientLbl = new JLabel("Select Client: ");
		selectTimeLbl = new JLabel("Select Time Period: ");

		clientCBox = new JComboBox<Object>();
		viewCBox = new JComboBox<Object>();
		timePeriodCBox = new JComboBox<Object>();
		
		updateViewList();
		
		summaryModel = new DefaultTableModel(32, 12) {
		    public boolean isCellEditable(int rowIndex, int columnIndex) {
	            return false;
	        }
		};
		
		summaryTable = new JTable(summaryModel);
		summaryTable.setRowHeight(32);
		summaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		summaryTable.setColumnSelectionAllowed(true);
		summaryTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		
		header = summaryTable.getTableHeader();
		header.setBackground(new Color(0xFAFAFA));
		header.setPreferredSize(new Dimension(header.getPreferredSize().width * 999, 25));
		header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		header.setReorderingAllowed(false);
		
		for(int i = 0; i < summaryTable.getColumnCount(); i++){
			if(i == 0) {
				summaryTable.getColumnModel().getColumn(i).setCellRenderer(
						new ColorfulCellRenderer(new Color(0xFAFAFA),Color.BLACK,
								Utils.colorfulSRColumn));
				summaryTable.getColumnModel().getColumn(i).setPreferredWidth(40);
			}else if(Utils.colorfulSRColumn.contains(i)){
				switch(i){
					case 2:
						summaryTable.getColumnModel().getColumn(i).setCellRenderer(
								new ColorfulCellRenderer(new Color(0xbee1fe),Color.BLACK,
										Utils.colorfulSRColumn));
						break;
					default:
						summaryTable.getColumnModel().getColumn(i).setCellRenderer(
								new ColorfulCellRenderer(Color.ORANGE,Color.BLACK,
										Utils.colorfulSRColumn));
						break;
				};
			} else{
				summaryTable.getColumnModel().getColumn(i).setCellRenderer(
						new ColorfulCellRenderer(Color.WHITE,Color.BLACK,
								Utils.colorfulSRColumn));
			}
				
		}
		
		summaryTable.addMouseListener(new TableMouseListener());
		summaryPane = new JScrollPane(summaryTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		modifyUI();
		initFont();
	}
	
	private void modifyUI() {
		setSize(new Dimension(851,670));
		setBackground(Utils.BODY_COLOR);
		
		statusLbl.setForeground(Utils.statusFGColor);
		
		summaryTable.setFillsViewportHeight(true);
		summaryPane.setBorder(null);
		summaryPane.setPreferredSize(new Dimension(this.getWidth()-500,this.getHeight()-300));
	
		viewCBox.setPreferredSize(new Dimension(350,25));
		viewCBox.setBackground(Utils.comboBoxBGColor);
		viewCBox.setForeground(Utils.comboBoxFGColor);
		clientCBox.setPreferredSize(new Dimension(350,25));
		clientCBox.setBackground(Utils.comboBoxBGColor);
		clientCBox.setForeground(Utils.comboBoxFGColor);
		timePeriodCBox.setPreferredSize(new Dimension(200,25));
		timePeriodCBox.setBackground(Utils.comboBoxBGColor);
		timePeriodCBox.setForeground(Utils.comboBoxFGColor);
		categoryCBox.setPreferredSize(new Dimension(150,25));
		categoryCBox.setBackground(Utils.comboBoxBGColor);
		categoryCBox.setForeground(Utils.comboBoxFGColor);
		
		searchBtn.setContentAreaFilled(false);
		searchBtn.setBorder(null);
		searchBtn.setOpaque(false);
		searchBtn.setForeground(null);
		searchBtn.setFocusPainted(false);
		searchBtn.setRolloverIcon(new ImageIcon(
				getClass().getResource("/images/buttons/search-r.png")));
		searchBtn.setPressedIcon(new ImageIcon(
				getClass().getResource("/images/buttons/search-p.png")));
		searchBtn.setSize(new Dimension(searchBtn.getIcon().getIconWidth(),
				searchBtn.getIcon().getIconHeight()));
		
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
		gbc.insets = new Insets(0,15,5,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(selectTimeLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(timePeriodCBox,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0,15,5,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(selectSummLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(viewCBox,gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,15,25,15);
		gbc.weighty = 1;
		gbc.gridwidth = 5;
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(summaryPane,gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,10,13,0);
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(statusLbl,gbc);
	}

	public void initFont(){
		categoryLbl.setFont(Utils.labelFont);
		categoryCBox.setFont(Utils.comboBoxFont);
		timePeriodCBox.setFont(Utils.comboBoxFont);
		summaryTable.setFont(Utils.tableFont);
		header.setFont(Utils.tableFont);
		clientCBox.setFont(Utils.comboBoxFont);
		viewCBox.setFont(Utils.comboBoxFont);
		selectClientLbl.setFont(Utils.labelFont);
		selectSummLbl.setFont(Utils.labelFont);
		selectTimeLbl.setFont(Utils.labelFont);
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
			
				int rowIndex = table.getSelectedRow();
				int colIndex = table.getSelectedColumn();
						
				if(rowIndex == rowNum && Utils.colorfulSRColumn.contains(colIndex) &&
						e.isPopupTrigger() && e.getComponent() instanceof JTable){
					JMenuItem menuItem = new JMenuItem("View Formula");
					menuItem.setFont(Utils.descFont);
					menuItem.setBackground(Color.WHITE);
					menuItem.setOpaque(false);
					JPopupMenu popup = new JPopupMenu();
					popup.setBackground(Color.WHITE);
						
					popup.add(menuItem);
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Utils.statusBGColor);
		g2d.fillRect(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), 
				Utils.HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), 
				this.getHeight()-Utils.HEIGHT);
	}
	
	public void updateTableColumn(){
		ArrayList<String> column = model.getColumnName(getReport());
		summaryModel.setColumnCount(0);
		for(String s : column){
			summaryModel.addColumn(s);
		}
	}
	
	public String getClient(){ 
		return (String)clientCBox.getSelectedItem();
	}
	
	public String getPeriodStartDate(){ 
		return (String)timePeriodCBox.getSelectedItem(); 
	}
	
	public String getReport(){ 
		return (String)viewCBox.getSelectedItem(); 
	}
	
	public void setPeriodStartDateListener(ActionListener list){
		clientCBox.addActionListener(list);
	}
	
	public void setViewListener(ActionListener list){
		viewCBox.addActionListener(list);
	}
	
	public void updateTable(){
		summaryModel.setRowCount(0);
		statusLbl.setText("Status: You are now viewing " + 
		getReport().toLowerCase() + ".");
		statusLbl.setIcon(loadScaledImage("/images/notifs/right.png",.08f));
		ArrayList<Object[]> row = model.getTableRow(getClient(),getPeriodStartDate(),
				getReport());
		for(Object[] t : row){
			summaryModel.addRow(t);
		}
	}
	public void updateClientList(){
		clientCBox.removeAllItems();
		ArrayList<String> clients = model.getClientList();
		
		for(String t : clients){
			clientCBox.addItem(t);
		}
	}
	
	public void updateDateList(){
		timePeriodCBox.removeAllItems();
		ArrayList<String> dates = model.getDateListPayslips(getClient());
		if(dates.size() == 0){
			showError(1);
		}else{
			statusLbl.setText("Payslips found!");
			statusLbl.setIcon(loadScaledImage("/images/notifs/right.png",.08f));
		}
		for(String t : dates){
			timePeriodCBox.addItem(t);
		}
	}
	
	public void updateViewList(){
		viewCBox.removeAllItems();
		String[] summaryReports = model.getSummaryReports();
		for(String t : summaryReports)
			viewCBox.addItem(t);
	}
	
	public void showError(int i){
		String error = "";
		if(i == 0){
			error = "Lacking Input!";
		}else if(i==1){
			error = "No payslips for client!";
		}
		statusLbl.setText(error);
		statusLbl.setIcon(loadScaledImage("/images/notifs/wrong.png",.08f));
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
	
	public boolean empty(){
		if(clientCBox.getItemCount() == 0 || timePeriodCBox.getItemCount() == 0)
			return true;
		return false;
	}
}

