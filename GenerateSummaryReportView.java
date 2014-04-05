import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GenerateSummaryReportView extends JPanel {

	private JLabel selectClientLbl;
	private JLabel selectReportLbl;
	private JLabel selectTimeLbl;
	private JLabel saveLbl;
	private JLabel locationLbl;
	private JLabel statusLbl;

	private JButton generateBtn;
	private JButton selSaveBtn;

	private JComboBox clientCBox;
	private JComboBox timePeriodCBox;
	private JComboBox reportCBox;
	
	public GenerateSummaryReportView(){
		generateBtn = new JButton(new ImageIcon(getClass().getResource("images/buttons/generate.png")));
		selSaveBtn = new JButton(new ImageIcon(getClass().getResource("images/buttons/select.png")));
		
		selectClientLbl = new JLabel("Select Client: ");
		selectTimeLbl = new JLabel("Select Time Period: ");
		selectReportLbl = new JLabel("Select Report: ");
		saveLbl = new JLabel("Save Location: ");
		locationLbl = new JLabel("C:// ");
		statusLbl = new JLabel("Status: Adjustment \"N?A\" successfully added!");
		
		clientCBox = new JComboBox();
		timePeriodCBox = new JComboBox();
		reportCBox = new JComboBox();
		
		modifyUI();
		initFont();
	}
	
	private void modifyUI(){
		setSize(new Dimension(851,650));
		setBackground(Utils.BODY_COLOR);
		
		statusLbl.setForeground(Utils.statusFGColor);
		
		clientCBox.setPreferredSize(new Dimension(300,30));
		clientCBox.setBackground(Utils.comboBoxBGColor);
		clientCBox.setForeground(Utils.comboBoxFGColor);
		
		timePeriodCBox.setPreferredSize(new Dimension(300,30));
		timePeriodCBox.setBackground(Utils.comboBoxBGColor);
		timePeriodCBox.setForeground(Utils.comboBoxFGColor);
		
		reportCBox.setPreferredSize(new Dimension(300,30));
		reportCBox.setBackground(Utils.comboBoxBGColor);
		reportCBox.setForeground(Utils.comboBoxFGColor);
		
		locationLbl.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5,5,5,5)));
		locationLbl.setPreferredSize(new Dimension(300,33));
		
		generateBtn.setContentAreaFilled(false);
		generateBtn.setBorder(null);
		generateBtn.setOpaque(false);
		generateBtn.setForeground(null);
		generateBtn.setFocusPainted(false);
		generateBtn.setRolloverIcon(new ImageIcon(getClass().getResource("images/buttons/generate-r.png")));
		generateBtn.setPressedIcon(new ImageIcon(getClass().getResource("images/buttons/generate-p.png")));
		generateBtn.setSize(new Dimension(generateBtn.getIcon().getIconWidth(), generateBtn.getIcon().getIconHeight()));
		
		selSaveBtn.setContentAreaFilled(false);
		selSaveBtn.setBorder(null);
		selSaveBtn.setOpaque(false);
		selSaveBtn.setForeground(null);
		selSaveBtn.setFocusPainted(false);
		selSaveBtn.setRolloverIcon(new ImageIcon(getClass().getResource("images/buttons/select-r.png")));
		selSaveBtn.setPressedIcon(new ImageIcon(getClass().getResource("images/buttons/select-p.png")));
		selSaveBtn.setPreferredSize(new Dimension(selSaveBtn.getIcon().getIconWidth(), selSaveBtn.getIcon().getIconHeight()));
		
		addComponentsToPane();
	}
	
	private void addComponentsToPane(){
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,40,0,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(selectClientLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(110,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(clientCBox,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,40,0,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(selectReportLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(reportCBox,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,40,0,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(selectTimeLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(timePeriodCBox,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,40,0,20);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(saveLbl,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(locationLbl,gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 3;
		add(selSaveBtn,gbc);
		
		gbc.fill = GridBagConstraints.WEST;
		gbc.insets = new Insets(80,0,0,0);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 4;
		add(generateBtn,gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(0,10,13,0);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(statusLbl,gbc);
	}
	
	public void initFont()
	{
		clientCBox.setFont(Utils.comboBoxFont);
		timePeriodCBox.setFont(Utils.comboBoxFont);
		selectClientLbl.setFont(Utils.labelFont);
		selectReportLbl.setFont(Utils.labelFont);
		saveLbl.setFont(Utils.labelFont);
		locationLbl.setFont(Utils.labelFont);
		selectTimeLbl.setFont(Utils.labelFont);
		statusLbl.setFont(Utils.statusBarFont);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Utils.genSummHColor);
		g2d.fillRect(0, 0, this.getWidth(), 70);
		g2d.setColor(Color.WHITE);
		g2d.setFont(Utils.headerFont);
        g2d.drawString("Generate Summary Report", 20, 35);
		g2d.setFont(Utils.descFont);
        g2d.drawString("This section allows you to generate summary reports of a client.", 20, 55);
		
		g2d.setColor(Utils.statusBGColor);
		g2d.fillRect(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), Utils.HEIGHT);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(0, this.getHeight()-Utils.HEIGHT, this.getWidth(), this.getHeight()-Utils.HEIGHT);
	}
}
