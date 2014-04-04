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
import javax.swing.border.LineBorder;


public class SettingsView extends JPanel  {

	private JToggleButton changePassBtn;
	private JToggleButton accessBtn;
	private JToggleButton appearanceBtn;
	private JToggleButton modifyVarBtn;
	
	private ButtonGroup sideGroup;
	
	private BufferedImage settings_img, side_img;
	
	private JPanel sidePane;
	private JPanel changePassPanel;
	private JPanel accessPanel;
	private JPanel modifyVarPanel;
	
	public SettingsView()
	{
		sideGroup = new ButtonGroup();
		changePassBtn = new CustomToggleButton("Change Password");
		accessBtn= new CustomToggleButton("Accessibility");
		appearanceBtn = new CustomToggleButton("Appearance");
		modifyVarBtn = new CustomToggleButton("Modify Variables");
		
		sidePane = new JPanel();
		changePassPanel = new ChangePasswordView();
		accessPanel = new JPanel();
		modifyVarPanel = new ModifyClientVariablesView();
		
		try {
			settings_img = ImageIO.read(getClass().getResource("/settings.png"));
			side_img = ImageIO.read(getClass().getResource("/side_white.png"));
		} catch (IOException e) {}
		
		modifyUI();
	}
	
	private void modifyUI() {
		setSize(new Dimension(700,500));
		setBackground(Utils.BODY_COLOR);
		setBorder(new LineBorder(Color.LIGHT_GRAY));
		setLayout(null);

		sidePane.setOpaque(false);
		sidePane.setSize(new Dimension(214,this.getHeight()));
		sidePane.setBounds(0, 50, sidePane.getWidth(), sidePane.getHeight());
		sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.PAGE_AXIS));
		
		changePassPanel.setOpaque(false);
		changePassPanel.setSize(new Dimension(this.getWidth()-214,changePassPanel.getHeight()));
		changePassPanel.setBounds(214, 50, changePassPanel.getWidth(), changePassPanel.getHeight());
		
		accessPanel.setOpaque(false);
		accessPanel.setSize(new Dimension(this.getWidth()-214,accessPanel.getHeight()));
		accessPanel.setBounds(214, 50, accessPanel.getWidth(), accessPanel.getHeight());
		
		modifyVarPanel.setOpaque(false);
		modifyVarPanel.setSize(new Dimension(this.getWidth()-214,modifyVarPanel.getHeight()));
		modifyVarPanel.setBounds(214, 50, modifyVarPanel.getWidth(), modifyVarPanel.getHeight());
		
		initButtons();
		addComponentsToPane();
	}
	
	private void addComponentsToPane() {
		sideGroup.add(changePassBtn);
		sideGroup.add(accessBtn);
		sideGroup.add(appearanceBtn);
		sideGroup.add(modifyVarBtn);
		
		sidePane.add(Box.createRigidArea(new Dimension(0,55)));
		sidePane.add(changePassBtn);
		sidePane.add(Box.createRigidArea(new Dimension(0,10)));
		sidePane.add(accessBtn);
		sidePane.add(Box.createRigidArea(new Dimension(0,10)));
		sidePane.add(appearanceBtn);
		sidePane.add(Box.createRigidArea(new Dimension(0,60)));
		sidePane.add(modifyVarBtn);
		
		sidePane.setVisible(true);
		changePassPanel.setVisible(true);
		accessPanel.setVisible(false);
		modifyVarPanel.setVisible(false);
		
		add(sidePane);
		add(changePassPanel);
		add(accessPanel);
		add(modifyVarPanel);
	}

	private void initButtons()
	{
		ImageIcon normal = loadScaledImage("/effects/normal.png", .5f);
		ImageIcon hover = loadScaledImage("/effects/hover.png", .5f);
		ImageIcon pressed = loadScaledImage("/effects/pressed.png", .5f);
		
		changePassBtn.setContentAreaFilled(false);
		changePassBtn.setBorder(null);
		changePassBtn.setHorizontalTextPosition(JButton.CENTER);
		changePassBtn.setVerticalTextPosition(JButton.CENTER);
		changePassBtn.setIcon(normal);
		changePassBtn.setSelectedIcon(pressed);
		changePassBtn.setRolloverIcon(hover);
		changePassBtn.setForeground(null);
		changePassBtn.setFocusPainted(false);
		changePassBtn.setFont(Utils.menuFont);
		changePassBtn.setForeground(Utils.menuFGColor);
		changePassBtn.setSelected(true);
		
		accessBtn.setContentAreaFilled(false);
		accessBtn.setBorder(null);
		accessBtn.setHorizontalTextPosition(JButton.CENTER);
		accessBtn.setVerticalTextPosition(JButton.CENTER);
		accessBtn.setIcon(normal);
		accessBtn.setSelectedIcon(pressed);
		accessBtn.setRolloverIcon(hover);
		accessBtn.setForeground(null);
		accessBtn.setFocusPainted(false);
		accessBtn.setFont(Utils.menuFont);
		accessBtn.setForeground(Utils.menuFGColor);
		
		appearanceBtn.setContentAreaFilled(false);
		appearanceBtn.setBorder(null);
		appearanceBtn.setHorizontalTextPosition(JButton.CENTER);
		appearanceBtn.setVerticalTextPosition(JButton.CENTER);
		appearanceBtn.setIcon(normal);
		appearanceBtn.setSelectedIcon(pressed);
		appearanceBtn.setRolloverIcon(hover);
		appearanceBtn.setForeground(null);
		appearanceBtn.setFocusPainted(false);
		appearanceBtn.setFont(Utils.menuFont);
		appearanceBtn.setForeground(Utils.menuFGColor);
		
		modifyVarBtn.setContentAreaFilled(false);
		modifyVarBtn.setBorder(null);
		modifyVarBtn.setHorizontalTextPosition(JButton.CENTER);
		modifyVarBtn.setVerticalTextPosition(JButton.CENTER);
		modifyVarBtn.setIcon(normal);
		modifyVarBtn.setSelectedIcon(pressed);
		modifyVarBtn.setRolloverIcon(hover);
		modifyVarBtn.setForeground(null);
		modifyVarBtn.setFocusPainted(false);
		modifyVarBtn.setFont(Utils.menuFont);
		modifyVarBtn.setForeground(Utils.menuFGColor);
		
		changePassBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(true);
				accessPanel.setVisible(false);
				modifyVarPanel.setVisible(false);
			}
		});
		
		appearanceBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(false);
				accessPanel.setVisible(false);
				modifyVarPanel.setVisible(false);
			}
		});
		
		accessBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(false);
				accessPanel.setVisible(true);
				modifyVarPanel.setVisible(false);
			}
		});
		
		modifyVarBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePassPanel.setVisible(false);
				accessPanel.setVisible(false);
				modifyVarPanel.setVisible(true);
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
        g2d.drawLine(213, 0, 213, this.getHeight());
        
        g2d.setFont(Utils.labelFont);
        g2d.setColor(Utils.settingsHColor);
        g2d.drawString("SYSTEM", 30, 85);
        
        g2d.setFont(Utils.labelFont);
        g2d.setColor(Utils.settingsHColor);
        g2d.drawString("OTHERS", 30, 240);
        
        //Header
		g2d.setColor(Utils.settingsHColor);
		g2d.fillRect(0, 0, this.getWidth(), 50);
		g2d.setColor(Color.WHITE);
		g2d.drawImage(settings_img, 20, 16, settings_img.getWidth()/2, settings_img.getHeight()/2, null);
	}
}
