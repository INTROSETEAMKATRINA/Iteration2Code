import javax.swing.UIManager;

// For Testing purposes

public class Driver {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try{
      			  UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // For Mac Users. Temporary.
            	}
            	catch(Exception e){}
            	@SuppressWarnings("unused")
				PayrollSystemView pSystemView = new PayrollSystemView(new PayrollSystemModel());
            }
		});
	}

}