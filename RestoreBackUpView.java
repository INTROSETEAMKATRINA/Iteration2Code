/*******************************************************
	 *  Class name: RestoreBackUpView
 	 *  Inheritance: JPanel
	 *  Attributes: 
	 *  Methods: setRestoreListener, setSelectFileListener, setStatus,
	 *			askConfirmation, setFileDirectory,
	 *			fileChooser, getFileDirectory, loadScaledImage 
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
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


public class RestoreBackUpView extends JPanel {
	
	private JLabel saveLbl;
	private JLabel locationLbl;
	private JLabel statusLbl;

	private JButton restoreBtn;
	private JButton selSaveBtn;
	
	private File file;

	
	public RestoreBackUpView() {
		
		restoreBtn = new JButton(new ImageIcon(
				getClass().getResource("/images/buttons/restore.png")));
		selSaveBtn = new JButton(new ImageIcon(
				getClass().getResource("/images/buttons/select.png")));
		
		saveLbl = new JLabel("Back Up Location: ");
		locationLbl = new JLabel();
		statusLbl = new JLabel("Do you wanna restore from back up?");
		
		modifyUI();
	}
	
	private void modifyUI() {
		setSize(new Dimension(486,450));
		setBackground(Utils.BODY_COLOR);
		
		statusLbl.setForeground(Utils.statusFGColor);
		
		locationLbl.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), 
				new EmptyBorder(5,5,5,5)));
		locationLbl.setPreferredSize(new Dimension(200,33));
		
		restoreBtn.setContentAreaFilled(false);
		restoreBtn.setBorder(null);
		restoreBtn.setOpaque(false);
		restoreBtn.setForeground(null);
		restoreBtn.setFocusPainted(false);
		restoreBtn.setRolloverIcon(new ImageIcon(
				getClass().getResource("/images/buttons/restore-r.png")));
		restoreBtn.setPressedIcon(new ImageIcon(
				getClass().getResource("/images/buttons/restore-p.png")));
		restoreBtn.setSize(new Dimension(restoreBtn.getIcon().getIconWidth(),
				restoreBtn.getIcon().getIconHeight()));
		
		selSaveBtn.setContentAreaFilled(false);
		selSaveBtn.setBorder(null);
		selSaveBtn.setOpaque(false);
		selSaveBtn.setForeground(null);
		selSaveBtn.setFocusPainted(false);
		selSaveBtn.setRolloverIcon(new ImageIcon(
				getClass().getResource("/images/buttons/select-r.png")));
		selSaveBtn.setPressedIcon(new ImageIcon(
				getClass().getResource("/images/buttons/select-p.png")));
		selSaveBtn.setPreferredSize(new Dimension(selSaveBtn.getIcon().getIconWidth(), 
				selSaveBtn.getIcon().getIconHeight()));
		
		initFont();
		addComponentsToPane();
	}
	
	private void addComponentsToPane(){
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,40,0,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(saveLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(locationLbl,gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		add(selSaveBtn,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 1;
		add(restoreBtn,gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,10,13,0);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(statusLbl,gbc);
	}
	
	public void initFont(){
		saveLbl.setFont(Utils.labelFont);
		locationLbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.statusBarFont);
	}
	
	public void paintComponent(Graphics g){		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
	
		g2d.setColor(Utils.backUpHColor);
		g2d.fillRect(0, 0, this.getWidth(), 70);
		g2d.setColor(Color.WHITE);
		g2d.setFont(Utils.headerFont);
		g2d.drawString("Restore From Back Up", 20, 35);
		g2d.setFont(Utils.descFont);
		g2d.drawString("This section allows you to recover your system from a back up file.",
				20, 55);
		
		g2d.setColor(Utils.statusBGColor);
		g2d.fillRect(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), 
				Utils.HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), 
				this.getHeight()-Utils.HEIGHT);
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
        
    public File getFileDirectory(){
		return file;
    }

    public File fileChooser(){
		JFileChooser fc = null;
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("sql files", "sql");
		fc.setFileFilter(filter);
		
		//In response to a button click:
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}else{
			return null;
		}
	}
            
    public void setFileDirectory(File f){
		file = f;
		
		if(f!=null){
			locationLbl.setText(file.getPath());
		}else{
			locationLbl.setText("");
		}
    }
        
    public boolean askConfirmation(){
		int confirmation = JOptionPane.showConfirmDialog(null, 
				"Are you sure you want to restore from back up?", 
				"Restore from back up",
		JOptionPane.YES_NO_OPTION);
		if(confirmation ==JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
        
    public void setStatus(String e, boolean b){
		statusLbl.setText("Status: "+e);	
		if(b){
			statusLbl.setIcon(loadScaledImage("/images/notifs/right.png",.08f));
		}else{
			statusLbl.setIcon(loadScaledImage("/images/notifs/wrong.png",.08f));
		}
    }
        
	public void setSelectFileListener(ActionListener list){
		selSaveBtn.addActionListener(list);
    }
	
    public void setRestoreListener(ActionListener list){
		restoreBtn.addActionListener(list);
    }

}
