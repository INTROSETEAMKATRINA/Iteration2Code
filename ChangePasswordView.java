/*******************************************************
	 *  Class name: ChangePasswordView
 	 *  Inheritance: JFrame
	 *  Attributes: 
	 *  Methods:	ChangePasswordView, getOldPass, getNewPass,
	 *				getConfirmNewPass, clear, setChangeListener,
	 *				setCancelListener, setShowListener, askConfirmation,
	 *				showPassword, showError, showSuccess
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class ChangePasswordView extends JPanel {
	
	private JLabel oldPassLbl;
	private JLabel newPassLbl;
	private JLabel conPassLbl;
	private JLabel statusLbl;

	private JTextField oldPassTxtFld;
	private JTextField newPassTxtFld;
	private JTextField conPassTxtFld;

	private JButton applyBtn;
	
	private final static int TEXTBOX_WIDTH = 180;
	private final static int TEXTBOX_HEIGHT = 41;
	
	public ChangePasswordView() {
		applyBtn = new JButton(new ImageIcon(getClass().getResource("images/buttons/apply.png")));
		
		oldPassLbl = new JLabel("Old Password: ");
		newPassLbl = new JLabel("New Password: ");
		conPassLbl = new JLabel("Confirm Password: ");
		statusLbl = new JLabel("Status: Adjustment \"N?A\" successfully added!");
		
		oldPassTxtFld = new CustomPTextField("*****", "images/effects/in.png", "images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		newPassTxtFld = new CustomPTextField("*****", "images/effects/in.png", "images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		conPassTxtFld = new CustomPTextField("*****", "images/effects/in.png", "images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		
		modifyUI();
		initFont();
	}
	
	private void modifyUI() {
		setSize(new Dimension(486,450));
		setBackground(Utils.BODY_COLOR);
		
		statusLbl.setForeground(Utils.statusFGColor);
		
		oldPassTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		newPassTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		conPassTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		
		applyBtn.setContentAreaFilled(false);
		applyBtn.setBorder(null);
		applyBtn.setOpaque(false);
		applyBtn.setForeground(null);
		applyBtn.setFocusPainted(false);
		applyBtn.setRolloverIcon(new ImageIcon(getClass().getResource("images/buttons/apply-r.png")));
		applyBtn.setPressedIcon(new ImageIcon(getClass().getResource("images/buttons/apply-p.png")));
		applyBtn.setSize(new Dimension(applyBtn.getIcon().getIconWidth(), applyBtn.getIcon().getIconHeight()));
		
		addComponentsToPane();
	}
	
	private void addComponentsToPane(){
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(oldPassLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(oldPassTxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(newPassLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(newPassTxtFld,gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,40,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(conPassLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(conPassTxtFld,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(40,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(applyBtn,gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,10,13,0);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(statusLbl,gbc);
	}
	
	public void initFont(){
		oldPassLbl.setFont(Utils.labelFont);
		newPassLbl.setFont(Utils.labelFont);
		conPassLbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.labelFont);
	}
	public String getOldPass(){ 
		return new String(oldPassTxtFld.getText()); 
	}
	
	public String getNewPass(){ 
		return new String(newPassTxtFld.getText()); 
	}
	
	public String getConfirmNewPass(){
		return new String(conPassTxtFld.getText());
	}
	
	public void clear(){
		oldPassTxtFld.setText("");
		newPassTxtFld.setText("");
		conPassTxtFld.setText("");
	}
	
	/*public void setChangeListener(ActionListener list){
		changeBtn.addActionListener(list);
	}
	
	public void setCancelListener(ActionListener list){
		cancelBtn.addActionListener(list);
	}
	
	public void setShowListener(ItemListener list){
		showPassBox.addItemListener(list);
	}
	*/
	public boolean askConfirmation(){ 
		int confirmation = JOptionPane.showConfirmDialog(null, "Please confirm!", "Please confirm!",
		
		JOptionPane.YES_NO_OPTION);
		if(confirmation ==JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	
	public void showPassword(boolean b){
		/*if(b){
			oldPassTxtFld.setEchoChar(defaultEchoChar);
			newPassTxtFld.setEchoChar(defaultEchoChar);
			conPassTxtFld.setEchoChar(defaultEchoChar);
		}else{
			oldPassTxtFld.setEchoChar((char) 0);
			newPassTxtFld.setEchoChar((char) 0);
			conPassTxtFld.setEchoChar((char) 0);
		}*/
	}
	
	public void showError(int i){
		String error = "";
		
		if(i == 0){
			error = "Change password failed!";
		}else if(i == 1){
			error = "New and confirm password not the same.";
		}else if(i == 2){
			error = "Wrong old password.";
		}
		JOptionPane.showMessageDialog(null, error, error, JOptionPane.ERROR_MESSAGE);
	}

	public void showSuccess(){
		JOptionPane.showMessageDialog(null, "Change password is successful.", "Change password is successful.", JOptionPane.PLAIN_MESSAGE);
	}

}
