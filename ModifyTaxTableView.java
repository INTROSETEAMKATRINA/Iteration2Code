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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ModifyTaxTableView extends JPanel {

	private JButton applyBtn;
	
	private JLabel bracketLbl;
	private JLabel taxLbl;
	private JLabel taxOverLbl;
	private JLabel zLbl;
	private JLabel sMeLbl;
	private JLabel me1Lbl;
	private JLabel me2Lbl;
	private JLabel me3Lbl;
	private JLabel me4Lbl;
	
	private JComboBox<Object> bracketCBox;
	
	private JTextField taxTxtFld;
	private JTextField taxOverTxtFld;
	private JTextField zTxtFld;
	private JTextField sMeTxtFld;
	private JTextField me1TxtFld;
	private JTextField me2TxtFld;
	private JTextField me3TxtFld;
	private JTextField me4TxtFld;
	
	private JLabel statusLbl;

	private final static int TEXTBOX_WIDTH = 180;
	private final static int TEXTBOX_HEIGHT = 41;
	
	public ModifyTaxTableView() {
		applyBtn = new JButton(loadScaledImage("/images/buttons/apply.png",1f));
		
		bracketCBox = new JComboBox<Object>();
		
		taxTxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		taxOverTxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		zTxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		sMeTxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		me1TxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		me2TxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		me3TxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		me4TxtFld = new CustomTextField("0.00", "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		
		bracketLbl = new JLabel("Bracket: ");
		taxLbl = new JLabel("Tax:");
		taxOverLbl = new JLabel("Tax Percent Over +: ");
		zLbl = new JLabel("Z :");
		sMeLbl = new JLabel("S/ME: ");
		me1Lbl = new JLabel("ME1/S1: ");
		me2Lbl = new JLabel("ME2/S2: ");
		me3Lbl = new JLabel("ME3/S3: ");
		me4Lbl = new JLabel("ME4/S4: ");
		statusLbl = new JLabel("Status: No Tax Table found!");
		statusLbl.setIcon(loadScaledImage("/images/notifs/wrong.png",.08f));
		
		modifyUI();
	}
	
	private void modifyUI() {
		setSize(new Dimension(851,650));
		setBackground(Utils.BODY_COLOR);
		
		statusLbl.setForeground(Utils.statusFGColor);
		
		bracketCBox.setPreferredSize(new Dimension(350,25));
		bracketCBox.setBackground(Utils.comboBoxBGColor);
		bracketCBox.setForeground(Utils.comboBoxFGColor);
		
		taxTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		taxOverTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		zTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		sMeTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		me1TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		me2TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		me3TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		me4TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		
		initButtons();
		initFont();
		addComponentsToPane();
	}
	
	private void initButtons() {
		applyBtn.setContentAreaFilled(false);
		applyBtn.setBorder(null);
		applyBtn.setOpaque(false);
		applyBtn.setForeground(null);
		applyBtn.setFocusPainted(false);
		applyBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/images/buttons/apply-r.png")));
		applyBtn.setPressedIcon(new ImageIcon(getClass().getResource("/images/buttons/apply-p.png")));
		applyBtn.setSize(new Dimension(applyBtn.getIcon().getIconWidth(), applyBtn.getIcon().getIconHeight()));
	}

	private void addComponentsToPane() {
		this.setLayout(new GridBagLayout());	
		
		GridBagConstraints gbc = new GridBagConstraints();	
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(bracketLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(taxLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(taxOverLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(zLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(sMeLbl,gbc);	
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(me1Lbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 6;
		add(me2Lbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 7;
		add(me3Lbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 8;
		add(me4Lbl,gbc);
		
		gbc.anchor = GridBagConstraints.WEST;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,0,0,0);
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(bracketCBox,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(taxTxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(taxOverTxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(zTxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(sMeTxtFld,gbc);	
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(me1TxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 6;
		add(me2TxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 7;
		add(me3TxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 8;
		add(me4TxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10,15,25,15);
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 9;
		add(applyBtn,gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,10,13,0);
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 10;
		add(statusLbl,gbc);
	}

	public void initFont()
	{
		bracketCBox.setFont(Utils.comboBoxFont);
		bracketLbl.setFont(Utils.labelFont);
		taxLbl.setFont(Utils.labelFont);
		taxOverLbl.setFont(Utils.labelFont);
		zLbl.setFont(Utils.labelFont);
		sMeLbl.setFont(Utils.labelFont);
		me1Lbl.setFont(Utils.labelFont);
		me2Lbl.setFont(Utils.labelFont);
		me3Lbl.setFont(Utils.labelFont);
		me4Lbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.statusBarFont);
	}
	
	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
	
		g2d.setColor(Utils.modifyTaxHColor);
		g2d.fillRect(0, 0, this.getWidth(), 70);
		g2d.setColor(Color.WHITE);
		g2d.setFont(Utils.headerFont);
		g2d.drawString("Modify Tax Table", 20, 35);
		g2d.setFont(Utils.descFont);
		g2d.drawString("This section allows you to modify the values related to tax.", 20, 55);
		
		g2d.setColor(Utils.statusBGColor);
		g2d.fillRect(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), Utils.HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), this.getHeight()-Utils.HEIGHT);
	}
	public String getClient(){ 
		return null; 
	}
	
	public void setPickerListener(){}
	
	private ImageIcon loadScaledImage(String img_url, float percent)
	{	
		ImageIcon img_icon = new ImageIcon(this.getClass().getResource(img_url));
		int new_width = (int) (img_icon.getIconWidth()*percent);
		int new_height = (int) (img_icon.getIconHeight()*percent);
		Image img = img_icon.getImage().getScaledInstance(new_width,new_height,java.awt.Image.SCALE_SMOOTH);  
		img_icon = new ImageIcon(img);
		return img_icon;
	}
}
