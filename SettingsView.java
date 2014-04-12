import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


public class SettingsView extends JPanel  {

	private JToggleButton changePassBtn;
	private JToggleButton restoreBtn;
	private JToggleButton modifyVarBtn;
	private JToggleButton changeMinWageBtn;
	
	private ButtonGroup sideGroup;
	
	private BufferedImage settings_img, side_img;
	
	private JPanel sidePane;
	private ChangePasswordView changePassPanel;
	private RestoreBackUpView restorePanel;
	private ModifyClientVariablesView modifyVarPanel;
	private ChangeMinWageView changeMinWagPanel;
	
	@SuppressWarnings("unused")
	private PayrollSystemModel model;
	
	public SettingsView(PayrollSystemModel model){
		this.model = model;
		
		sideGroup = new ButtonGroup();
		changePassBtn = new CustomToggleButton("Change Password");
		restoreBtn= new CustomToggleButton("      Restore From Back up");
		modifyVarBtn = new CustomToggleButton("Modify Variables   ");
		changeMinWageBtn = new CustomToggleButton("       Change Minimum Wage");
		
		sidePane = new JPanel();
		changePassPanel = new ChangePasswordView();
		restorePanel = new RestoreBackUpView();
		modifyVarPanel = new ModifyClientVariablesView(model);
		changeMinWagPanel = new ChangeMinWageView();
		try {
			settings_img = ImageIO.read(getClass().getResource("/images/settings.png"));
			side_img = ImageIO.read(getClass().getResource("/images/side_white.png"));
		} catch (IOException e) {}
		
		modifyUI();
	}
	
	private void modifyUI() {
		setSize(new Dimension(700,500));
		setBackground(Utils.BODY_COLOR);
		setLayout(null);

		sidePane.setOpaque(false);
		sidePane.setSize(new Dimension(214,this.getHeight()));
		sidePane.setBounds(0, 50, sidePane.getWidth(), sidePane.getHeight());
		sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.PAGE_AXIS));
		
		changePassPanel.setOpaque(false);
		changePassPanel.setSize(new Dimension(this.getWidth()-214,changePassPanel.getHeight()));
		changePassPanel.setBounds(214, 50, changePassPanel.getWidth(), changePassPanel.getHeight());
		
		restorePanel.setOpaque(false);
		restorePanel.setSize(new Dimension(this.getWidth()-214,restorePanel.getHeight()));
		restorePanel.setBounds(214, 50, restorePanel.getWidth(), restorePanel.getHeight());
		
		modifyVarPanel.setOpaque(false);
		modifyVarPanel.setSize(new Dimension(this.getWidth()-214,modifyVarPanel.getHeight()));
		modifyVarPanel.setBounds(214, 50, modifyVarPanel.getWidth(), modifyVarPanel.getHeight());
		
		changeMinWagPanel.setOpaque(false);
		changeMinWagPanel.setSize(new Dimension(this.getWidth()-214,changeMinWagPanel.getHeight()));
		changeMinWagPanel.setBounds(214, 50, changeMinWagPanel.getWidth(), changeMinWagPanel.getHeight());
		
		initButtons();
		addComponentsToPane();
	}
	
	private void addComponentsToPane() {
		sideGroup.add(changePassBtn);
		sideGroup.add(restoreBtn);
		sideGroup.add(modifyVarBtn);
		sideGroup.add(changeMinWageBtn);
		
		sidePane.add(Box.createRigidArea(new Dimension(0,55)));
		sidePane.add(changePassBtn);
		sidePane.add(Box.createRigidArea(new Dimension(0,10)));
		sidePane.add(restoreBtn);
		sidePane.add(Box.createRigidArea(new Dimension(0,10)));
		sidePane.add(modifyVarBtn);
		sidePane.add(Box.createRigidArea(new Dimension(0,10)));
		sidePane.add(changeMinWageBtn);
		
		sidePane.setVisible(true);
		changePassPanel.setVisible(true);
		restorePanel.setVisible(false);
		modifyVarPanel.setVisible(false);
		changeMinWagPanel.setVisible(false);
		
		add(sidePane);
		add(changePassPanel);
		add(restorePanel);
		add(modifyVarPanel);
		add(changeMinWagPanel);
	}

	private void initButtons()
	{
		changePassBtn.setContentAreaFilled(false);
		changePassBtn.setBorder(null);
		changePassBtn.setHorizontalTextPosition(JButton.CENTER);
		changePassBtn.setVerticalTextPosition(JButton.CENTER);
		changePassBtn.setIcon(loadScaledImage("/images/effects/password.png", .5f));
		changePassBtn.setSelectedIcon(loadScaledImage("/images/effects/password-p.png", .5f));
		changePassBtn.setRolloverIcon(loadScaledImage("/images/effects/password-r.png", .5f));
		changePassBtn.setForeground(null);
		changePassBtn.setFocusPainted(false);
		changePassBtn.setFont(Utils.menuFont);
		changePassBtn.setForeground(Utils.menuFGColor);
		changePassBtn.setSelected(true);
		
		restoreBtn.setContentAreaFilled(false);
		restoreBtn.setBorder(null);
		restoreBtn.setHorizontalTextPosition(JButton.CENTER);
		restoreBtn.setVerticalTextPosition(JButton.CENTER);
		restoreBtn.setIcon(loadScaledImage("/images/effects/tile.png", .5f));
		restoreBtn.setSelectedIcon(loadScaledImage("/images/effects/tile-p.png", .5f));
		restoreBtn.setRolloverIcon(loadScaledImage("/images/effects/tile-r.png", .5f));
		restoreBtn.setForeground(null);
		restoreBtn.setFocusPainted(false);
		restoreBtn.setFont(Utils.menuFont);
		restoreBtn.setForeground(Utils.menuFGColor);
		
		modifyVarBtn.setContentAreaFilled(false);
		modifyVarBtn.setBorder(null);
		modifyVarBtn.setHorizontalTextPosition(JButton.CENTER);
		modifyVarBtn.setVerticalTextPosition(JButton.CENTER);
		modifyVarBtn.setIcon(loadScaledImage("/images/effects/var.png", .5f));
		modifyVarBtn.setSelectedIcon(loadScaledImage("/images/effects/var-p.png", .5f));
		modifyVarBtn.setRolloverIcon(loadScaledImage("/images/effects/var-r.png", .5f));
		modifyVarBtn.setForeground(null);
		modifyVarBtn.setFocusPainted(false);
		modifyVarBtn.setFont(Utils.menuFont);
		modifyVarBtn.setForeground(Utils.menuFGColor);
		
		changeMinWageBtn.setContentAreaFilled(false);
		changeMinWageBtn.setBorder(null);
		changeMinWageBtn.setHorizontalTextPosition(JButton.CENTER);
		changeMinWageBtn.setVerticalTextPosition(JButton.CENTER);
		changeMinWageBtn.setIcon(loadScaledImage("/images/effects/mw.png", .5f));
		changeMinWageBtn.setSelectedIcon(loadScaledImage("/images/effects/mw-p.png", .5f));
		changeMinWageBtn.setRolloverIcon(loadScaledImage("/images/effects/mw-r.png", .5f));
		changeMinWageBtn.setForeground(null);
		changeMinWageBtn.setFocusPainted(false);
		changeMinWageBtn.setFont(Utils.menuFont);
		changeMinWageBtn.setForeground(Utils.menuFGColor);
		
		
		changePassBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(true);
				restorePanel.setVisible(false);
				modifyVarPanel.setVisible(false);
				changeMinWagPanel.setVisible(false);
			}
		});
		
		restoreBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(false);
				restorePanel.setVisible(true);
				modifyVarPanel.setVisible(false);
				changeMinWagPanel.setVisible(false);
			}
		});
		
		modifyVarBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				modifyVarPanel.updateClientList();
				changePassPanel.setVisible(false);
				restorePanel.setVisible(false);
				modifyVarPanel.setVisible(true);
				changeMinWagPanel.setVisible(false);
			}
		});
		
		changeMinWageBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(false);
				restorePanel.setVisible(false);
				modifyVarPanel.setVisible(false);
				changeMinWagPanel.setVisible(true);
			}
		});
		
	}
	
	private ImageIcon loadScaledImage(String img_url, float percent)
	{
		ImageIcon img_icon = new ImageIcon(getClass().getResource(img_url));
		int new_width = (int) (img_icon.getIconWidth()*percent);
		int new_height = (int) (img_icon.getIconHeight()*percent);
		Image img = img_icon.getImage().getScaledInstance(new_width,new_height,java.awt.Image.SCALE_SMOOTH);  
		img_icon = new ImageIcon(img);
		return img_icon;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.drawImage(side_img, 0, 50, 213, this.getHeight(), null);
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(214, 0, 214, this.getHeight());
        
        g2d.setFont(Utils.labelFont);
        g2d.setColor(Utils.settingsHColor);
        g2d.drawString("SYSTEM", 30, 85);
        
        //Header
		g2d.setColor(Utils.settingsHColor);
		g2d.fillRect(0, 0, this.getWidth(), 50);
		g2d.setColor(Color.WHITE);
		g2d.drawImage(settings_img, 20, 16, settings_img.getWidth()/2, settings_img.getHeight()/2, null);
	}
	
	public ChangePasswordView getChangePasswordPanel(){
		return changePassPanel;
	}
	
	public ModifyClientVariablesView getModifyVarsPanel(){
		return modifyVarPanel;
	}
        
	public RestoreBackUpView getRestorePanel(){
		return restorePanel;
	}
	
	public ChangeMinWageView getMinWagePanel(){
		return changeMinWagPanel;
	}
}
