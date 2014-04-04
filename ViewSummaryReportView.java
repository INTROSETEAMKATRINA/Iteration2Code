/*******************************************************
	 *  Class name: ViewSummaryReportView
 	 *  Inheritance: JFrame
	 *  Attributes: model
	 *  Methods:	ViewSummaryReportView, getClient, setPickerListener
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/
	 
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
public class ViewSummaryReportView extends JPanel {

	@SuppressWarnings("unused")
	private PayrollSystemModel model;
	
	private JLabel selectSummLbl;
	private JLabel selectClientLbl;
	private JLabel statusLbl;
	private JComboBox viewCBox;
	private JComboBox clientCBox;
	
	private JTable summaryTable;
	private JScrollPane summaryPane;
	
	private JPanel emptyPane;
    private int rowPoint, colPoint;
    private JTableHeader header;
	
	public ViewSummaryReportView(PayrollSystemModel model) {
		this.model = model;
		
		rowPoint = 0;
		colPoint = 0;
		
		emptyPane = new JPanel();
		
		statusLbl = new JLabel("Status: Generating File Information");
		
		selectSummLbl = new JLabel("Select Summary Report: ");
		selectClientLbl = new JLabel("Select Client: ");

		String[] companyName = { "Gallant", "FedEx", "LBC", "Banco De Oro", "De La Salle University" };
		clientCBox = new JComboBox(companyName);
		viewCBox = new JComboBox(companyName);
		
		summaryTable = new JTable(30,12);
		summaryTable.setFont(Utils.tableFont);
		summaryTable.setRowHeight(32);
		summaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		summaryTable.setColumnSelectionAllowed(true);
		summaryTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		header = summaryTable.getTableHeader();
		header.setBackground(new Color(0xFAFAFA));
		header.setFont(Utils.tableFont);
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 20));
		header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		header.setReorderingAllowed(false);
		
		for(int i = 0; i < summaryTable.getColumnCount(); i++)
		{
			summaryTable.getColumnModel().getColumn(i).setCellRenderer(new ColorfulCellRenderer(Color.WHITE,Color.BLACK));
			
			if(i == 2)
			summaryTable.getColumnModel().getColumn(2).setCellRenderer(new ColorfulCellRenderer(new Color(0xbee1fe),Color.BLACK));
		}
		summaryTable.addMouseMotionListener(new TableMouseAdapter());
		
		summaryTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e)
			{
				if(SwingUtilities.isRightMouseButton(e))
				{
					int rowNum = summaryTable.rowAtPoint(e.getPoint());
					if(rowNum >=0 && rowNum < summaryTable.getRowCount()) {
						summaryTable.setRowSelectionInterval(rowNum, rowNum);
					} else {
						summaryTable.clearSelection();
					}
					
					int rowIndex = summaryTable.getSelectedRow();
					
					if(rowIndex > 0 && e.isPopupTrigger() && e.getComponent() instanceof JTable)
					{
						JMenuItem menuItem = new JMenuItem("View Formula");
						JPopupMenu popup = new JPopupMenu();
						popup.add(menuItem);
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
		
		summaryPane = new JScrollPane(summaryTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
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
		
		emptyPane.setPreferredSize(new Dimension(this.getWidth()-700,20));
		emptyPane.setOpaque(false);
	
		viewCBox.setPreferredSize(new Dimension(300,20));
		clientCBox.setPreferredSize(new Dimension(300,20));
		
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
		add(selectSummLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0,10,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		add(emptyPane,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(viewCBox,gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,15,25,15);
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(summaryPane,gbc);
		
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

	public void initFont()
	{
		clientCBox.setFont(Utils.comboBoxFont);
		viewCBox.setFont(Utils.comboBoxFont);
		selectClientLbl.setFont(Utils.labelFont);
		selectSummLbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.statusBarFont);
	}
	
	public class ColorfulCellRenderer extends DefaultTableCellRenderer {

	    private Color bgColor;
	    private Color fgColor;
	    
	    public ColorfulCellRenderer(Color bg, Color fg) {
	        bgColor = bg;
	        fgColor = fg;
	        this.setHorizontalAlignment(JLabel.CENTER);
	    }
	    
	    public boolean isCellEditable(int row, int column) {
	        return false;
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
	        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	        
	        if(row == rowPoint && col == colPoint)
	        {
		    	if(bgColor != Color.WHITE)
		    	{
		    		label.setBackground(bgColor.darker());
		    	}
	        	label.setBackground(new Color(0,0,0,10));
	        	label.setForeground(Color.WHITE);
	        }
	        else if((row == rowPoint && col == colPoint) && isSelected) 
	        {
	        	label.setBackground(Color.ORANGE);
	        }
	        else if((row == rowPoint && col == colPoint) || isSelected) {
	        	
	        	if(col == 2)
	        		label.setBackground(new Color(206,238,255,100));
	        	else
	        		label.setBackground(new Color(154,217,255,100));
	        	
	        	label.setForeground(Color.WHITE);
		    } else {
		    	label.setBackground(bgColor);
		    	label.setForeground(fgColor);
		    }
        	
	        return label;
	    }
	}
	
	public class TableMouseAdapter extends MouseMotionAdapter {
        public void mouseMoved(MouseEvent e) {
            JTable table =  (JTable)e.getSource();
            rowPoint = table.rowAtPoint(e.getPoint());
            colPoint = table.columnAtPoint(e.getPoint());
            table.repaint();
        }
    }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Utils.statusBGColor);
		g2d.fillRect(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), Utils.HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), this.getHeight()-Utils.HEIGHT);
	}
	
	public void updateTableColumn(){
	/*	ArrayList<String> column = model.getColumnName(getReport());
		tableModel.setColumnCount(column.size());
		for(int i = 0; i < column.size();i++){
			summaryTable.getColumnModel().getColumn(i).setHeaderValue(column.get(i));
		}*/
	}
	
	public String getClient(){ 
		return (String)clientCBox.getSelectedItem();
	}
	
	public String getPeriodStartDate(){ 
		return null;//(String)dateCBox.getSelectedItem(); 
	}
	
	public String getReport(){ 
		return (String)viewCBox.getSelectedItem(); 
	}
	
/*	public void setPeriodStartDateListener(ActionListener list){
		clientCBox.addActionListener(list);
	}
	
	public void setViewListener(ActionListener list){
		viewBtn.addActionListener(list);
	}
	
	public void backListener(ActionListener list){
		backBtn.addActionListener(list);
	}
	*/
	public void updateTable(){
	/*	tableModel.setRowCount(0);
		statusLbl.setText("Status: You are now viewing " + getReport().toLowerCase() + ".");
		ArrayList<Object[]> row = model.getTableRow(getClient(),getPeriodStartDate(),getReport());
		
		for(Object[] t : row){
			tableModel.addRow(t);
		}*/
	}
	
	public void updateClientList(){
		clientCBox.removeAllItems();
		ArrayList<String> clients = model.getClientList();
		
		for(String t : clients){
			clientCBox.addItem(t);
		}
	}
	
	public void updateDateList(){
	/*	dateCBox.removeAllItems();
		ArrayList<String> dates = model.getDateListPayslips(getClient());
		for(String t : dates)
			dateCBox.addItem(t);	*/	
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
			error = "No payslips for client in current period!";
		}
		JOptionPane.showMessageDialog(null, error, error, JOptionPane.ERROR_MESSAGE);
	}
	
}
