/*******************************************************
	 *  Class name: ChangeMinWageView
 	 *  Inheritance: JPanel
	 *  Attributes: 
	 *  Methods:	getMinWageTxtFld, clear, setChangeListener,
					askConfirmation, showSuccess, showError,
					setText
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class ChangeMinWageView extends JPanel {
	
	private JLabel minWageLbl;
	private JLabel statusLbl;

	private CustomTextField minWageTxtFldTxtFld;

	private JButton applyBtn;
	
	private final static int TEXTBOX_WIDTH = 180;
	private final static int TEXTBOX_HEIGHT = 41;
	
	public ChangeMinWageView() {
		applyBtn = new JButton(new ImageIcon(
				getClass().getResource("/images/buttons/apply.png")));
		minWageLbl = new JLabel("Minimum Wage: ");
		statusLbl = new JLabel();
		minWageTxtFldTxtFld = new CustomTextField("", "/images/effects/in.png",
				"/images/effects/out.png", TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		
		modifyUI();
		initFont();
	}
	
	private void modifyUI(){
		setSize(new Dimension(486,450));
		setBackground(Utils.BODY_COLOR);
		
		statusLbl.setForeground(Utils.statusFGColor);
		minWageTxtFldTxtFld.setPreferredSize(new Dimension(TEXTBOX_WIDTH,TEXTBOX_HEIGHT));
		
		applyBtn.setContentAreaFilled(false);
		applyBtn.setBorder(null);
		applyBtn.setOpaque(false);
		applyBtn.setForeground(null);
		applyBtn.setFocusPainted(false);
		applyBtn.setRolloverIcon(new ImageIcon(
				getClass().getResource("/images/buttons/apply-r.png")));
		applyBtn.setPressedIcon(new ImageIcon(
				getClass().getResource("/images/buttons/apply-p.png")));
		applyBtn.setSize(new Dimension(applyBtn.getIcon().getIconWidth(),
				applyBtn.getIcon().getIconHeight()));
		
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
		add(minWageLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(minWageTxtFldTxtFld,gbc);

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
		minWageLbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.labelFont);
	}
	
	public String getMinWageTxtFld(){ 
		return new String(minWageTxtFldTxtFld.getText()); 
	}
	
	public void clear(){
		minWageTxtFldTxtFld.setText(null);
	}
	
	public void setChangeListener(ActionListener list){
		applyBtn.addActionListener(list);
	}
	
	public boolean askConfirmation(){ 
		int confirmation = JOptionPane.showConfirmDialog(null, 
				"Are you sure you want to change minimum wage?", "Change Minimum Wage!",
				JOptionPane.YES_NO_OPTION);
		
		if(confirmation ==JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	
	public void showSuccess(){
		statusLbl.setText("Change of minimum wage is successful.");
		statusLbl.setIcon(loadScaledImage("/images/notifs/right.png",.08f));
	}
	
	public void showError(String s){
		statusLbl.setText(s);
		statusLbl.setIcon(loadScaledImage("/images/notifs/wrong.png",.08f));
	}
	
	public void setText(String s){
		minWageTxtFldTxtFld.setHint(s);
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
}
