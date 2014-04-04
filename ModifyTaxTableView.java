/*******************************************************
	 *  Class name: ModifyTaxTableView
 	 *  Inheritance: JPanel
	 *  Attributes: model
	 *  Methods: 	ModifyTaxTableView, getFileDirectory, setModifyListener,
	 *				setCancelListener
	 *  Functionality: View
	 *  Visibility: public
	 *******************************************************/

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class ModifyTaxTableView extends JPanel {
	
	private JTable taxTable;
	private JScrollPane taxPane;
	
	private JLabel titleLbl;
	private JLabel descLbl;
	private JLabel statusLbl;
	
	private JButton modifyBtn;
	private JButton applyBtn;
	
	public ModifyTaxTableView()
	{
		taxTable = new JTable();
		taxPane = new JScrollPane(taxTable);
		
		titleLbl = new JLabel("Modify Tax Table");
		descLbl = new JLabel("Description: Tax Table as of.....");
		statusLbl = new JLabel("Status:");
		
		modifyBtn = new JButton("Modify");
		applyBtn = new JButton("Apply");
		
		modifyUI();
	}
	
	private void modifyUI()
	{
		titleLbl.setFont(new Font("Helvetica", Font.PLAIN, 32));
		
		taxTable.setPreferredScrollableViewportSize(new Dimension(700, 400));
		taxTable.setFillsViewportHeight(true);
		taxPane.setPreferredSize(new Dimension(500,200));
		
		applyBtn.setPreferredSize(new Dimension(140,30));
		modifyBtn.setPreferredSize(new Dimension(140,30));
		
		addComponentsToPane();
	}

	private void addComponentsToPane() {
		setLayout(new GridBagLayout());	
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0,0,30,0);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(titleLbl,gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(descLbl,gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0,0,5,0);
		gbc.gridwidth = 5;
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(taxPane,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,0,5,10);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(modifyBtn,gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30,0,5,10);
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		gbc.gridy = 3;
		add(applyBtn,gbc);
	}
	public String getFileDirectory(){ 
		return null; 
	}
	
	public void setModifyListener(){}
	public void setCancelListener(){}
}
