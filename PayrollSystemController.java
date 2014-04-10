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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class PayrollSystemController{

	private Connection con;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date periodStartDate;
	private PayrollSystemModel model;
	private PayrollSystemView view;
	private String directory = "periodStartDate.txt";
	private LogInView loginPanel;
	private GeneratePayslipsView generatePayslips;
	private AddAdjustmentsView addAdjustments;
	private RemoveAdjustmentsView removeAdjustments;
	private ChangePasswordView changePassword;
	private ModifyClientVariablesView modifyClientsVar;
	
	public PayrollSystemController(PayrollSystemModel model, PayrollSystemView view, SettingsView sView, Connection con){
		this.model = model;
		this.view = view;
		this.con = con;
		try{
			Scanner in = new Scanner(new File(directory));
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
		
		generatePayslips = view.getGenPayslips();
		generatePayslips.setSelectFileListener(new fileSaverGeneratePayslipsButtonListener());
		generatePayslips.setGenerateListener(new generatePayslipsButtonListener());
		
		addAdjustments = view.getAdjPanel();
		addAdjustments.setClientListener(new clientListAddAdjustmentListener());
		addAdjustments.setAddListener(new addAdjustmentButtonListener());
		
		removeAdjustments = view.getRemAdjPanel();
		removeAdjustments.setClientListener(new clientListRemoveAdjustmentListener());
		removeAdjustments.setPersonnelListener(new personnelListRemoveAdjustmentListener());
		removeAdjustments.setRemoveListener(new removeAdjustmentButtonListener());
		
		changePassword = sView.getChangePasswordPanel();
		changePassword.setChangeListener(new changePasswordButtonListener());
		
		modifyClientsVar = sView.getModifyVarsPanel();
		modifyClientsVar.setClientListener(new updateVariables());
		modifyClientsVar.setModifyListener(new modifyVariables());
		view.setAddPersonnelListener(new addPersonnelListener());
		view.setPersonnelFileLocationListener(new personnelFileLocationListener());
		view.setAddDTRListener(new addDTRListener());
		view.setDTRFileLocationListener(new dtrFileLocationListener());
		view.setNextTimeListener(new nextTimePeriod());
		
		/*
		view.setAddAdjustmentListener(new addAdjustmentListener());
		view.setRemoveAdjustmentListener(new removeAdjustmentListener());
		view.setGeneratePayslipsListener(new generatePayslipsListener());
		view.setChangePasswordListener(new changePasswordListener());
		*/
	}

	//Login Button Listener
	class loginListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(!model.checkPassword(loginPanel.getPassword())){
				loginPanel.fadeInBalloon();
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
			view.setStatusPersonnel("Importing...");
			File f = new File(view.getPersonnelFileLocation());
			if(f.isFile()){
				try{
					model.addPersonnel(f, periodStartDate);
					view.setStatusPersonnel("Excel successfully added!", true);
					view.setCount();
				}catch(Exception ex){
					view.setStatusPersonnel(ex.getMessage(), false);
				}
			}else{
				view.setStatusPersonnel("No file chosen", false);
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
			view.setStatusDTR("Importing...");
			File f = new File(view.getDTRFileLocation());
			if(f.isFile()){
				try{
					model.addDTR(f, periodStartDate);
					view.setStatusDTR("Excel successfully added!", true);
				}catch(Exception ex){
					view.setStatusDTR(ex.getMessage(),false);
				}
			}else{
				view.setStatusDTR("No file chosen", false);
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
	
	//listeners in generate payslips view
	//generate payslips in generate payslips view
	class generatePayslipsButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			File f = generatePayslips.getFileDirectory();
			String client = generatePayslips.getClient();
			String psd = sdf.format(periodStartDate);
			generatePayslips.setStatus("Generating...");
			if(model.checkPeriodForDTR(client,psd)){
				if(f!=null){
					boolean go = true;
					if(f.exists()){
						go = generatePayslips.askConfirmation();
					}
					if(go){
						if(model.generatePayslips(f, client, psd)==0){
							generatePayslips.setStatus("Success!", true);
							generatePayslips.setFileDirectory(null);
						}else{
							generatePayslips.setStatus("File is in use!", false);
						}
					}
				}else{
					generatePayslips.setStatus("No file chosen!", false);
				}
			}else{
				generatePayslips.setStatus("No personnel DTR in client!", false);
			}
		}
	}

	//choose where to save listener in generate payslips view
	class fileSaverGeneratePayslipsButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			generatePayslips.setFileDirectory(generatePayslips.fileSaver());
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
	}
	
	class updateVariables implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				float var[] = model.getVariables(modifyClientsVar.getClient());
				modifyClientsVar.setVariables(var);
			}catch(Exception ex){
				modifyClientsVar.setVariablesToDefault();
				System.out.println(ex);
			}
		}
	}
	
	class modifyVariables implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				float[] variables = modifyClientsVar.getVariables();
				String client = modifyClientsVar.getClient();
				model.modifyVariables(variables, client);
				modifyClientsVar.showSuccess();
			}catch(Exception ex){
				modifyClientsVar.showError("Input must be numbers.");
				System.out.println(ex);
			}
		}
	}
}
