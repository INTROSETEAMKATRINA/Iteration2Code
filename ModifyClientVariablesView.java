/*******************************************************
	 *  Class name: ModifyClientVariablesView
 	 *  Inheritance: JPanel
	 *  Attributes: model
	 *  Methods: 	ModifyClientVariablesView, getClient, getSHVariable, getLHVariable
	 *				setModifyListener, setCancelListener
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class ModifyClientVariablesView extends JPanel {
	
	private JLabel var1Lbl;
	private JLabel var2Lbl;
	private JLabel var3Lbl;
	private JLabel var4Lbl;
	private JLabel var5Lbl;
	private JLabel var6Lbl;
	private JLabel var7Lbl;
	private JLabel var8Lbl;
	private JLabel var9Lbl;
	private JLabel var10Lbl;
	private JLabel var11Lbl;
	private JLabel var12Lbl;
	private JLabel statusLbl;
	
	private JTextField var1TxtFld;
	private JTextField var2TxtFld;
	private JTextField var3TxtFld;
	private JTextField var4TxtFld;
	private JTextField var5TxtFld;
	private JTextField var6TxtFld;
	private JTextField var7TxtFld;
	private JTextField var8TxtFld;
	private JTextField var9TxtFld;
	private JTextField var10TxtFld;
	private JTextField var11TxtFld;
	private JTextField var12TxtFld;
	
	private JPanel panel;
	private JScrollPane scrollPane;

	private JButton applyBtn;
	
	private float rotVar = 1.25f;
	private float rnsdVar = .10f;
	private float lhRate = 30f;
	private float lhVar = 1.00f;
	private float lhOTVar = 1.30f;
	private float lhNSDVar = 1.00f*.10f;
	private float lhRDVar = 2.60f;
	private float shRate = 0f;
	private float shVar = .30f;
	private float shOTVar = .30f*.30f;
	private float shNSDVar = .30f*.10f;
	private float shRDVar = 1.5f;
	
	//FIXED
	private final static int FOOTER_HEIGHT = 70;

	private final static int TEXTBOX_WIDTH = 180;
	private final static int TEXTBOX_HEIGHT = 41;
	
	public ModifyClientVariablesView(){
		applyBtn = new JButton(new ImageIcon(getClass().getResource("/buttons/apply.png")));
		
		var1Lbl = new JLabel("Regular Overtime: ");
		var2Lbl = new JLabel("Regular NSD: ");
		var3Lbl = new JLabel("Legal Holiday Rate: ");
		var4Lbl = new JLabel("Legal Holiday Variable: ");
		var5Lbl = new JLabel("Legal Holiday Overtime: ");
		var6Lbl = new JLabel("Legal Holiday NSD: ");
		var7Lbl = new JLabel("Legal Holiday RD: ");
		var8Lbl = new JLabel("Special Holiday Rate: ");
		var9Lbl = new JLabel("Special Holiday Variable: ");
		var10Lbl = new JLabel("Special Holiday Overtime: ");
		var11Lbl = new JLabel("Special Holiday NSD: ");
		var12Lbl = new JLabel("Special Holiday RD: ");
		statusLbl = new JLabel("Status: Variables succesfully updated!");
		
		var1TxtFld = new CustomTextField(Float.toString(rotVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var2TxtFld = new CustomTextField(Float.toString(rnsdVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var3TxtFld = new CustomTextField(Float.toString(lhRate), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var4TxtFld = new CustomTextField(Float.toString(lhVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var5TxtFld = new CustomTextField(Float.toString(lhOTVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var6TxtFld = new CustomTextField(Float.toString(lhNSDVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var7TxtFld = new CustomTextField(Float.toString(lhRDVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var8TxtFld = new CustomTextField(Float.toString(shRate), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var9TxtFld = new CustomTextField(Float.toString(shVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var10TxtFld = new CustomTextField(Float.toString(shOTVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var11TxtFld = new CustomTextField(Float.toString(shNSDVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var12TxtFld = new CustomTextField(Float.toString(shRDVar), "/effects/in.png", "/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		
		panel = new JPanel();
		scrollPane = new JScrollPane(panel);
		
		modifyUI();
		initFont();
	}
	
	private void modifyUI(){
		setSize(new Dimension(486,450));
		setBackground(Utils.BODY_COLOR);
		
		panel.setBackground(Color.WHITE);
		
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBorder(null);
		
		statusLbl.setForeground(Utils.statusFGColor);
		
		var1TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var2TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var3TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var4TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var5TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var6TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var7TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var8TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var9TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var10TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var11TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		var12TxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		
		panel.setPreferredSize(new Dimension(400,500));
		scrollPane.setPreferredSize(new Dimension(470,290));
		
		applyBtn.setContentAreaFilled(false);
		applyBtn.setBorder(null);
		applyBtn.setOpaque(false);
		applyBtn.setForeground(null);
		applyBtn.setFocusPainted(false);
		applyBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/buttons/apply-r.png")));
		applyBtn.setPressedIcon(new ImageIcon(getClass().getResource("/buttons/apply-p.png")));
		applyBtn.setSize(new Dimension(applyBtn.getIcon().getIconWidth(), applyBtn.getIcon().getIconHeight()));
		
		addComponentsToPane();
	}
	
	private void addComponentsToPane(){
		setLayout(new GridBagLayout());
		ArrayList<JComponent> comp = new ArrayList<JComponent>();
		
		comp.add(var1Lbl);
		comp.add(var1TxtFld);
		comp.add(var2Lbl);
		comp.add(var2TxtFld);
		comp.add(var3Lbl);
		comp.add(var3TxtFld);
		comp.add(var4Lbl);
		comp.add(var4TxtFld);
		comp.add(var5Lbl);
		comp.add(var5TxtFld);
		comp.add(var6Lbl);
		comp.add(var6TxtFld);
		comp.add(var7Lbl);
		comp.add(var7TxtFld);
		comp.add(var8Lbl);
		comp.add(var8TxtFld);
		comp.add(var9Lbl);
		comp.add(var9TxtFld);
		comp.add(var10Lbl);
		comp.add(var10TxtFld);
		comp.add(var11Lbl);
		comp.add(var11TxtFld);
		comp.add(var12Lbl);
		comp.add(var12TxtFld);
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		
		for(int i = 0; i < 12; i++)
		{
			gbc.gridy = i;
			for(int j = 0; j < 2; j++)
			{
				if(j == 0)
					gbc.anchor = GridBagConstraints.EAST;
				else
					gbc.anchor = GridBagConstraints.WEST;
				
				gbc.gridx = j;
				
				panel.add(comp.get((i*2)+j),gbc);
			}
		}
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(60,0,0,0);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(scrollPane, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,10,0,0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(statusLbl, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(30,0,0,10);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(applyBtn, gbc);
	}
	
	public void initFont(){
		var1Lbl.setFont(Utils.labelFont);
		var2Lbl.setFont(Utils.labelFont);
		var3Lbl.setFont(Utils.labelFont);
		var4Lbl.setFont(Utils.labelFont);
		var5Lbl.setFont(Utils.labelFont);
		var6Lbl.setFont(Utils.labelFont);
		var7Lbl.setFont(Utils.labelFont);
		var8Lbl.setFont(Utils.labelFont);
		var9Lbl.setFont(Utils.labelFont);
		var10Lbl.setFont(Utils.labelFont);
		var11Lbl.setFont(Utils.labelFont);
		var12Lbl.setFont(Utils.labelFont);
		
		var1TxtFld.setFont(Utils.textFieldFont);
		var2TxtFld.setFont(Utils.textFieldFont);
		var3TxtFld.setFont(Utils.textFieldFont);
		var4TxtFld.setFont(Utils.textFieldFont);
		var5TxtFld.setFont(Utils.textFieldFont);
		var6TxtFld.setFont(Utils.textFieldFont);
		var7TxtFld.setFont(Utils.textFieldFont);
		var8TxtFld.setFont(Utils.textFieldFont);
		var9TxtFld.setFont(Utils.textFieldFont);
		var10TxtFld.setFont(Utils.textFieldFont);
		var11TxtFld.setFont(Utils.textFieldFont);
		var12TxtFld.setFont(Utils.textFieldFont);
		
		statusLbl.setFont(Utils.statusBarFont);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Utils.modifyVarHColor);
		g2d.fillRect(0, 0, this.getWidth(), 70);
		g2d.setColor(Color.WHITE);
		g2d.setFont(Utils.headerFont);
        g2d.drawString("Modify Variables", 20, 35);
		g2d.setFont(Utils.descFont);
        g2d.drawString("This section allows you to modify allowed variables.", 20, 55);
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-FOOTER_HEIGHT, this.getWidth(), this.getHeight()-FOOTER_HEIGHT);
	}
	
	public String getClient(){ 
		return null;
	}
	
	public float getSHVariable(){
		return 0f; 
	}
	
	public float getLHVariable(){ 
		return 0f; 
	}
	
	public void setModifyListener(){}
	public void setCancelListener(){}
	
}
