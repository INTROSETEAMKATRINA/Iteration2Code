/*******************************************************
	 *  Class name: ViewPersonnelView
 	 *  Inheritance: JFrame
	 *  Attributes: model
	 *  Methods:	ViewPersonnelView, getClient, setPickerListener
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ViewPersonnelView extends JPanel {

	@SuppressWarnings("unused")
	private PayrollSystemModel model;
	
	private JLabel selectPersLbl;
	private JLabel selectClientLbl;
	private JLabel statusLbl;
	private JComboBox viewCBox;
	private JComboBox clientCBox;
	private JTextPane personnelPanel;
	private JScrollPane personnelPane;
	
	private JPanel emptyPane;
	
	//Fixed
	private final static int HEIGHT = 40;
	private final static Color FOOTER_COLOR = new Color(0xFAFAFA);
	private final static Color BODY_COLOR = new Color(0xFFFFFF);
	
	public static Font helvetica_box = new Font("Helvetica", Font.PLAIN, 12);
	
	public ViewPersonnelView(PayrollSystemModel model) {
		this.model = model;
		
		setSize(new Dimension(851,670));
		setBackground(BODY_COLOR);
		
		emptyPane = new JPanel();
		
		statusLbl = new JLabel("Status: Generating File Information");
		
		selectPersLbl = new JLabel("Select Personnel: ");
		selectClientLbl = new JLabel("Select Client: ");

		String[] companyName = { "Gallant", "FedEx", "LBC", "Banco De Oro", "De La Salle University" };
		clientCBox = new JComboBox(companyName);
		viewCBox = new JComboBox(companyName);
		
		personnelPanel = new JTextPane();
		personnelPane = new JScrollPane(personnelPanel);
		
		modifyUI();
		initFont(helvetica_box);
	}
	
	private void modifyUI() {
		personnelPane.setPreferredSize(new Dimension(this.getWidth()-500,this.getHeight()-300));
		
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
		add(selectPersLbl,gbc);
		
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

	public void initFont(Font font)
	{
		clientCBox.setFont(font);
		selectClientLbl.setFont(font);
		selectPersLbl.setFont(font);
		viewCBox.setFont(font);
		statusLbl.setFont(font);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(FOOTER_COLOR);
		g2d.fillRect(0, this.getHeight()-HEIGHT, this.getWidth(), HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-HEIGHT, this.getWidth(), this.getHeight()-HEIGHT);
	}
	public String getClient(){ 
		return null; 
	}
	
	public void setPickerListener(){}
}
