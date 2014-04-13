/*******************************************************
	 *  Class name: PayrollSystem
 	 *  Inheritance: JPanel
	 *  Attributes: model
	 *  Methods: 	main
	 *  Functionality: Main
	 *  Visibility: public
	 *******************************************************/

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class PayrollSystem{
	
	public static void main(String[] args){
		Connection con;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			String url = "jdbc:mysql://localhost:3306/payroll system";
			String uname = "root";
			String pass = "p@ssword";
			con = DriverManager.getConnection (url, uname, pass);
			System.out.println("Connected!");
			PayrollSystemModel model = new PayrollSystemModel(con);
			SettingsView sView = new SettingsView(model);
			PayrollSystemView view = new PayrollSystemView(sView, model);
			PayrollSystemController controller = new PayrollSystemController(model, view, sView, con);
			view.setVisible(true);
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Database not found! Program will now exit.", "Database not found! Program will now exit.", JOptionPane.ERROR_MESSAGE); 
			System.out.println("Error: " + e);
		}	
	}	
}