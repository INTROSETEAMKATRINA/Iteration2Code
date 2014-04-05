/*******************************************************
	 *  Class name: PayrollSystemController
 	 *  Inheritance:
	 *  Attributes: con, sdf, periodStartdate, model, view, removeAdjustments,
					addAdjustments, viewSummaryReport, changePassword,
					generatePayslips
	 *  Methods:	PayrollSystemController
	 *  Functionality: Controller
	 *  Visibility: public
	 *******************************************************/

import javax.swing.*;

import java.util.Date;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.io.PrintWriter;
import java.io.File;

import java.text.SimpleDateFormat;

public class PayrollSystemController{

	private Connection con;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date periodStartDate;
	private PayrollSystemModel model;
	private PayrollSystemView view;
	private String directory = "periodStartDate.txt";
	private LogInView loginPanel;
	public PayrollSystemController(PayrollSystemModel model, PayrollSystemView view, Connection con){
		this.model = model;
		this.view = view;
		this.con = con;
		try{
			Scanner in = new Scanner(this.getClass().getResourceAsStream(directory));
			String s = in.next();
			periodStartDate = sdf.parse(s);
			view.updateTimePeriod(sdf.format(periodStartDate));
			in.close();
		}catch(Exception ex){
			System.out.println("ERROR!");
			view.showPeriodStartDateNotFound();
			System.exit(1);
		}
		model.setPeriodStartDate(periodStartDate);
		
		loginPanel = view.getLogInView();
		loginPanel.setLoginListener(new loginListener());
		view.setAddPersonnelListener(new addPersonnelListener());
		view.setPersonnelFileLocationListener(new personnelFileLocationListener());
		view.setAddDTRListener(new addDTRListener());
		view.setDTRFileLocationListener(new dtrFileLocationListener());
		/*	
		
		view.setAddAdjustmentListener(new addAdjustmentListener());
		view.setRemoveAdjustmentListener(new removeAdjustmentListener());
		view.setGeneratePayslipsListener(new generatePayslipsListener());
		view.setChangePasswordListener(new changePasswordListener());
		view.setNextTimeListener(new nextTimePeriod());
		*/
	}

	//Login Button Listener
	class loginListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(!loginPanel.getPassword().equals("INTROSE")){
				loginPanel.fadeInBallon();
			}
			else{
				loginPanel.setVisible(false);
				PayrollSystemView.showBlackPane(false);
			}
		}
	}
	//Main Menu Buttons Listeners

	//Add Personnel Button in Main Menu
	class addPersonnelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			File f = new File(view.getPersonnelFileLocation());
			if(f.isFile()){
				int add = model.addPersonnel(f, periodStartDate);
				if(add == 0){
					view.showSuccess();
				}else{
					view.showErrorPersonnel(add);
				}
			}else{
				System.out.println("No file chosen");
			}
		}
	}
	
	class personnelFileLocationListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			File f = view.fileChooser();
			if(f.isFile()){
				view.setPersonnelFileLocation(f.getPath());
			}else{
				System.out.println("No file chosen");
			}
		}
	}
	//Add DTR button in main menu
	class addDTRListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			File f = new File(view.getDTRFileLocation());
			if(f!=null){
				int add = model.addDTR(f, periodStartDate);
				if(add == 0){
					view.showSuccess();
				}else{
					view.showErrorDTR(add);
				}
			}else{
				System.out.println("No file chosen");
			}
		}
	}
	class dtrFileLocationListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			File f = view.fileChooser();
			if(f!=null){
				view.setDTRFileLocation(f.getPath());
			}else{
				System.out.println("No file chosen");
			}
		}
	}
	
/*
	//Add adjustment button in main menu
	class addAdjustmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
		//	addAdjustments.updateClientList();
		//	addAdjustments.setVisible(true);
		}
	}

	//Remove adjustment button in main menu
	class removeAdjustmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
		//	removeAdjustments.updateClientList();
		//	removeAdjustments.setVisible(true);
		}
	}

	//Change password button in main menu
	class changePasswordListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
		//	changePassword.setVisible(true);
		}
	}

	//View summary report button in main menu
	class viewSummaryReportListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
		//	viewSummaryReport.updateClientList();
		//	viewSummaryReport.updateViewList();
		//	viewSummaryReport.setVisible(true);
		}
	}

	//Generate payslips button in main menu
	class generatePayslipsListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
		//	generatePayslips.updateClientList();
		//	generatePayslips.setVisible(true);
		}
	}

	//Listeners in Adjustments view
	//Add adjustments button in add adjustments view
	class addAdjustmentButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String type = addAdjustments.getTypeAdjustment();
			float adjustment = addAdjustments.getAdjustment();
			String tin = addAdjustments.getTIN();
			if(type.length()!=0 && adjustment !=0 && tin.length() != 0){
				if(addAdjustments.askConfirmation()){
					model.addAdjustment(type, adjustment, tin, periodStartDate);
					addAdjustments.showSuccess();
					addAdjustments.clear();
				}
			}else
				addAdjustments.showWrongInput();
		}
	}

	//Cancel add adjustments in add adjustments view
	class cancelAddAdjustmentButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			addAdjustments.clear();
			addAdjustments.setVisible(false);
		}
	}

	//client list combo box listener in add adjustments view
	class clientListAddAdjustmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			addAdjustments.updatePersonnelList();
		}
	}

	//listeners in remove adjustments view
	//remove adjustments in remove adjustments view
	class removeAdjustmentButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(removeAdjustments.getNumAdjustments()>0){
				String type = removeAdjustments.getTypeAdjustment();
				float adjustment = removeAdjustments.getAdjustment();
				String tin = removeAdjustments.getTIN();
				if(removeAdjustments.askConfirmation()){
					model.removeAdjustment(type, adjustment, tin, periodStartDate);
					removeAdjustments.updateAdjustmentsList();
					removeAdjustments.showSuccess();
				}
			}else
				removeAdjustments.showNoAdjustments();
		}
	}

	//cancel remove adjustments in remove adjustments view
	class cancelRemoveAdjustmentButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			removeAdjustments.setVisible(false);
		}
	}
	
	class cancelViewSummaryReportListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			viewSummaryReport.setVisible(false);
		}
	}
	//client list combo box in remove adjustments view
	class clientListRemoveAdjustmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			removeAdjustments.updatePersonnelList();
		}
	}

	//personnel list combo box in remove adjustments view
	class personnelListRemoveAdjustmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			removeAdjustments.updateAdjustmentsList();
		}
	}

	class addPeriodStartDateListener implements ActionListener{ //This is going to be updated
		public void actionPerformed(ActionEvent e){
			viewSummaryReport.updateDateList();
		}
	}
	class viewReportListener implements ActionListener{ //This is going to be updated
		public void actionPerformed(ActionEvent e){
			if(viewSummaryReport.getClient() == null || viewSummaryReport.getPeriodStartDate() == null){
				viewSummaryReport.showError(0);
			}else{
				String client = viewSummaryReport.getClient();
				String psd = viewSummaryReport.getPeriodStartDate();
				if(model.checkPeriodForPayslips(client, psd)){
					viewSummaryReport.updateTableColumn();
					viewSummaryReport.updateTable();
				}else{
					viewSummaryReport.showError(1);
				}
			}
		}
	}
	
	
	//listeners in change password view
	//change password button in change password view
	class changePasswordButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				if(changePassword.askConfirmation()){
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("select password from password");
					rs.next();
					String oldPass = changePassword.getOldPass();
					if(oldPass.equals(rs.getString("password"))){//GetPasswordFromDatabase
						String newPass = changePassword.getNewPass();
						String confirmNewPass = changePassword.getConfirmNewPass();
						if(newPass.equals(confirmNewPass)){
							if(model.changePassword(oldPass, newPass)==1){
								changePassword.showSuccess();
								changePassword.clear();
								changePassword.setVisible(false);
							}else{
								changePassword.showError(0);
							}
						}else{
							changePassword.showError(1);
						}
					}else{
						changePassword.showError(2);
					}
				}
			}catch(Exception ex){
				System.out.println(ex);
			}
		}
	}

	//cancel change password button in change password view
	class cancelChangePasswordButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			changePassword.clear();
			changePassword.setVisible(false);
		}
	}
	
	//show password checkbox in change password view
	class showPasswordListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() != ItemEvent.SELECTED) {
				changePassword.showPassword(true);
			}else{
				 changePassword.showPassword(false);
			}
		}
	}

	//listeners in generate payslips view
	//generate payslips in generate payslips view
	class generatePayslipsButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			File f = generatePayslips.getFileDirectory();
			String client = generatePayslips.getClient();
			String psd = generatePayslips.getPeriodStartDate();
			if(model.checkPeriodForDTR(client,psd)){
				if(f!=null){
					boolean go = true;
					if(f.exists()){
						go = generatePayslips.askConfirmation();
					}
					if(go){
						if(model.generatePayslips(f, client, psd)==0){
							generatePayslips.showSuccess();
							generatePayslips.setFileDirectory(null);
						}else{
							generatePayslips.showError(2);
						}
					}
				}else{
					generatePayslips.showError(1);
				}
			}else{
				generatePayslips.showError(0);
			}
		}
	}

	//cancel generate payslips in generate payslips view
	class cancelGeneratePayslipsButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			generatePayslips.setFileDirectory(null);
			generatePayslips.setVisible(false);
		}
	}

	//choose where to save listener in generate payslips view
	class fileSaverGeneratePayslipsButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			generatePayslips.setFileDirectory(generatePayslips.fileSaver());
		}
	}
	
	//client list combo box listener to automatically change avaiable dates
	class clientListGeneratePayslipsListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			generatePayslips.updateDateList();
		}
	}
	
	//next time period listener
	class nextTimePeriod implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(view.askConfirmation()){
				periodStartDate = model.nextTimePeriod();
				System.out.println(sdf.format(periodStartDate));
				PrintWriter writer = null;
				try{
					writer = new PrintWriter(directory);
				}catch(Exception ex){
					System.out.println(ex);
					return ;
				}
				writer.println(sdf.format(periodStartDate));
				writer.close();
				view.updateTimePeriod(sdf.format(periodStartDate));
			}
		}
	}*/
}
