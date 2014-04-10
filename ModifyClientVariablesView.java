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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ModifyClientVariablesView extends JPanel {
	
	private JLabel selectClientLbl;
	private JComboBox<Object> clientCBox;
	
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
	
	private CustomTextField var1TxtFld;
	private CustomTextField var2TxtFld;
	private CustomTextField var3TxtFld;
	private CustomTextField var4TxtFld;
	private CustomTextField var5TxtFld;
	private CustomTextField var6TxtFld;
	private CustomTextField var7TxtFld;
	private CustomTextField var8TxtFld;
	private CustomTextField var9TxtFld;
	private CustomTextField var10TxtFld;
	private CustomTextField var11TxtFld;
	private CustomTextField var12TxtFld;
	
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
	
	private PayrollSystemModel model;
	public ModifyClientVariablesView(PayrollSystemModel model){
		this.model = model;
		selectClientLbl = new JLabel("Select Client: ");
		clientCBox = new JComboBox<Object>();
		
		applyBtn = new JButton(new ImageIcon(getClass().getResource("/images/buttons/apply.png")));
		
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
		statusLbl = new JLabel();
		
		var1TxtFld = new CustomTextField(Float.toString(rotVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var2TxtFld = new CustomTextField(Float.toString(rnsdVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var3TxtFld = new CustomTextField(Float.toString(lhRate), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var4TxtFld = new CustomTextField(Float.toString(lhVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var5TxtFld = new CustomTextField(Float.toString(lhOTVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var6TxtFld = new CustomTextField(Float.toString(lhNSDVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var7TxtFld = new CustomTextField(Float.toString(lhRDVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var8TxtFld = new CustomTextField(Float.toString(shRate), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var9TxtFld = new CustomTextField(Float.toString(shVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var10TxtFld = new CustomTextField(Float.toString(shOTVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var11TxtFld = new CustomTextField(Float.toString(shNSDVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		var12TxtFld = new CustomTextField(Float.toString(shRDVar), "/images/effects/in.png", "/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		panel = new JPanel();
		scrollPane = new JScrollPane(panel);
		
		modifyUI();
		initFont();
	}
	
	private void modifyUI(){
		setSize(new Dimension(486,450));
		setBackground(Utils.BODY_COLOR);
		
		panel.setBackground(Color.WHITE);
		
		clientCBox.setPreferredSize(new Dimension(350,25));
		clientCBox.setBackground(Utils.comboBoxBGColor);
		clientCBox.setForeground(Utils.comboBoxFGColor);
		
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
		scrollPane.setPreferredSize(new Dimension(470,250));
		
		applyBtn.setContentAreaFilled(false);
		applyBtn.setBorder(null);
		applyBtn.setOpaque(false);
		applyBtn.setForeground(null);
		applyBtn.setFocusPainted(false);
		applyBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/images/buttons/apply-r.png")));
		applyBtn.setPressedIcon(new ImageIcon(getClass().getResource("/images/buttons/apply-p.png")));
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
		gbc.insets = new Insets(60,10,0,10);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(selectClientLbl, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(60,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(clientCBox, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(scrollPane, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(40,10,0,0);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(statusLbl, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(40,0,0,10);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(applyBtn, gbc);
	}
	
	public void initFont(){
		selectClientLbl.setFont(Utils.labelFont);
		clientCBox.setFont(Utils.comboBoxFont);
		
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
		return (String)clientCBox.getSelectedItem();
	}
	
	public void setVariablesToDefault(){
		var1TxtFld.setText(Float.toString(rotVar));
		var2TxtFld.setText(Float.toString(rnsdVar));
		var3TxtFld.setText(Float.toString(lhRate));
		var4TxtFld.setText(Float.toString(lhVar));
		var5TxtFld.setText(Float.toString(lhOTVar));
		var6TxtFld.setText(Float.toString(lhNSDVar));
		var7TxtFld.setText(Float.toString(lhRDVar));
		var8TxtFld.setText(Float.toString(shRate));
		var9TxtFld.setText(Float.toString(shVar));
		var10TxtFld.setText(Float.toString(shOTVar));
		var11TxtFld.setText(Float.toString(shNSDVar));
		var12TxtFld.setText(Float.toString(shRDVar));
	}
	
	public void setVariables(float[] vars){
		var1TxtFld.setText(Float.toString(vars[0]));
		var2TxtFld.setText(Float.toString(vars[1]));
		var3TxtFld.setText(Float.toString(vars[2]));
		var4TxtFld.setText(Float.toString(vars[3]));
		var5TxtFld.setText(Float.toString(vars[4]));
		var6TxtFld.setText(Float.toString(vars[5]));
		var7TxtFld.setText(Float.toString(vars[6]));
		var8TxtFld.setText(Float.toString(vars[7]));
		var9TxtFld.setText(Float.toString(vars[8]));
		var10TxtFld.setText(Float.toString(vars[9]));
		var11TxtFld.setText(Float.toString(vars[10]));
		var12TxtFld.setText(Float.toString(vars[11]));
		var1TxtFld.setHint(Float.toString(vars[0]));
		var2TxtFld.setHint(Float.toString(vars[1]));
		var3TxtFld.setHint(Float.toString(vars[2]));
		var4TxtFld.setHint(Float.toString(vars[3]));
		var5TxtFld.setHint(Float.toString(vars[4]));
		var6TxtFld.setHint(Float.toString(vars[5]));
		var7TxtFld.setHint(Float.toString(vars[6]));
		var8TxtFld.setHint(Float.toString(vars[7]));
		var9TxtFld.setHint(Float.toString(vars[8]));
		var10TxtFld.setHint(Float.toString(vars[9]));
		var11TxtFld.setHint(Float.toString(vars[10]));
		var12TxtFld.setHint(Float.toString(vars[11]));
		repaint();
	}
	
	public float[] getVariables(){
		float variables[] = new float[12];
		variables[0] = Float.parseFloat(var1TxtFld.getText());
		variables[1] = Float.parseFloat(var2TxtFld.getText());
		variables[2] = Float.parseFloat(var3TxtFld.getText());
		variables[3] = Float.parseFloat(var4TxtFld.getText());
		variables[4] = Float.parseFloat(var5TxtFld.getText());
		variables[5] = Float.parseFloat(var6TxtFld.getText());
		variables[6] = Float.parseFloat(var7TxtFld.getText());
		variables[7] = Float.parseFloat(var8TxtFld.getText());
		variables[8] = Float.parseFloat(var9TxtFld.getText());
		variables[9] = Float.parseFloat(var10TxtFld.getText());
		variables[10] = Float.parseFloat(var11TxtFld.getText());
		variables[11] = Float.parseFloat(var12TxtFld.getText());
		return variables;
	}
	
	public void updateClientList(){
		clientCBox.removeAllItems();
		ArrayList<String> clients = model.getClientList();
		
		for(String t : clients){
			clientCBox.addItem(t);
		}
	}
	
	public void setClientListener(ActionListener list){
		clientCBox.addActionListener(list);
	}
	
	public void setModifyListener(ActionListener list){
		applyBtn.addActionListener(list);
	}
	
	private ImageIcon loadScaledImage(String img_url, float percent){	
		ImageIcon img_icon = new ImageIcon(this.getClass().getResource(img_url));
		int new_width = (int) (img_icon.getIconWidth()*percent);
		int new_height = (int) (img_icon.getIconHeight()*percent);
		Image img = img_icon.getImage().getScaledInstance(new_width,new_height,java.awt.Image.SCALE_SMOOTH);  
		img_icon = new ImageIcon(img);
		return img_icon;
	}
	
	public void showSuccess(){
		statusLbl.setText("Status: Variables succesfully updated!");
		statusLbl.setIcon(loadScaledImage("/images/notifs/right.png",.08f));
	}
	
	public void showError(String s){
		statusLbl.setText(s);
		statusLbl.setIcon(loadScaledImage("/images/notifs/wrong.png",.08f));
	}
	
}
