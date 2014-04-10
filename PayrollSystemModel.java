/*******************************************************
	 *  Class name: PayrollSystemModel
 	 *  Inheritance:
	 *  Attributes: con, sdf, periodStartdate, model, view, removeAdjustments,
	 *				addAdjustments, viewSummaryReport, changePassword,
	 *				generatePayslips
	 *  Methods:	PayrollSystemModel, setPeriodStartDate, addPersonnel
	 *				addDTR, removePersonnel, getPersonnel,
	 *				addAdjustment, removeAdjustment, changePassword,
	 *				modifyTaxTable, modfyClientVariables, generatePayslips,
	 *				generateSummaryReport, backupDate, getSummaryReport,
	 *				getClientList, getPersonnelList, getAdjustmentsList, 
	 *				getDateListDTR, checkPeriodForDTR, checkPeriodForPayslips,
	 *				getDateListPayslips, getColumnName, getTableRow, 
	 *				getSummaryReport, tryGetFloat
	 *  Functionality: Model
	 *  Visibility: public
	 *******************************************************/
	 
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

public class PayrollSystemModel {

	private SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	private Connection con;
	private Date periodStartDate;
	private String client;
	private String taxStatus[] = {"z", "sme", "s1me1", "s2me2", "s3me3", "s4me4"};
	private String[] summaryReports = {"Daily time record summary",
									   "Billing summary",
									   "Atm/cash payroll summary",
									   "Payroll with total deduction"};
									   
	public PayrollSystemModel(Connection con){
		this.con = con;
	}

	public void setPeriodStartDate(Date psd){
		periodStartDate = psd;
	}

	public boolean addPersonnel(File fileDirectory, Date periodStartDate) throws Exception{
    	ArrayList<Personnel> personnels = new ArrayList<Personnel>();
		String assignment = "";
		
        try{
			File file = fileDirectory;
			
			String ext = getExtension(fileDirectory.toString());
			if(!ext.equals("xls")){
				throw new Exception("File is not an excel file.");
			}
			
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);

			String name,position,employeeStatus,tin,taxStatus;
			float sss, sssLoan, phic, hdmf, hdmfLoan, payrollAdvance, houseRental, uniformAndOthers;
			float dailyRate, colaRate, monthlyRate;
			Date psd;
			int row,column;

			row = 0;
			column = 1;

			assignment = sheet.getCell(column,row).getContents();
			if(assignment.length() <= 0){
				throw new Exception("No client name in excel file.");
			}
			psd = null;
			row++;

			try{
				Cell cell = sheet.getCell(column,row);
				if(cell.getType() == CellType.DATE){
					DateCell date = (DateCell)cell;
					psd = sdf.parse(sdf.format(date.getDate()));
				}else{
					throw new Exception("B2 is not formtted to Date!");
				}
			}catch(Exception e){
				throw new Exception("B2 is not formtted to Date!");
			}

			if(!sdf.format(psd).equals(sdf.format(periodStartDate))){
				throw new Exception("Date not equal to system date!");
			}

			row += 2;

			while(row < sheet.getRows()){

				column = 0;
				name = sheet.getCell(column,row).getContents();

				if(name.length() > 0){

					column++;
					position = sheet.getCell(column,row).getContents();
					column++;
					employeeStatus = sheet.getCell(column,row).getContents();
					
					float rates[] = new float[3];
					for(int i = 0; i < 3;i++){
						column++;
						rates[i] = tryGetFloat(sheet.getCell(column,row).getContents());
						if(rates[i] < 0){
							throw new Exception("Negative deduction or rate.");
						}
					}
					dailyRate = rates[0];
					colaRate = rates[1];
					monthlyRate = rates[2];

					column++;
					tin = sheet.getCell(column,row).getContents();
					if(tin.length() == 0){
						throw new Exception("Lacking tin!");
					}
					column++;
					taxStatus = sheet.getCell(column,row).getContents();
					
					float deductions[] = new float[8];
					for(int i = 0; i < 8;i++){
						column++;
						deductions[i] = tryGetFloat(sheet.getCell(column,row).getContents());
						if(deductions[i]<0){
							throw new Exception("Negative deduction or rate.");
						}
					}
					
					sss = deductions[0];
					sssLoan = deductions[1];
					phic = deductions[2];
					hdmf = deductions[3];
					hdmfLoan = deductions[4];
					payrollAdvance = deductions[5];
					houseRental = deductions[6];
					uniformAndOthers = deductions[7];

					personnels.add(new Personnel(name, position, assignment,employeeStatus, tin, taxStatus,
												 sss, sssLoan, phic, hdmf,hdmfLoan, payrollAdvance, houseRental,
												 uniformAndOthers, dailyRate, colaRate, monthlyRate));
				}else{
					throw new Exception("Lacking name!");
				}
				row++;
			}
		}catch(Exception e){
			System.out.println(e);
			throw e;
		}

        //ADD TO DATABASE
        Statement stmt = null;
        String sql;

        try{
            sql="INSERT INTO `Payroll System`.`Client`\n" +
            "(`Name`)\n" +
            "VALUES\n" +
            "(\""+assignment+"\");";
            stmt=con.prepareStatement(sql);
            stmt.execute(sql);

        } catch (SQLException ex) {
			if(ex.getErrorCode()!=1062){
				System.out.println(ex);
			}
        }

        for( Personnel personnel: personnels ){
            try{
                sql="REPLACE INTO Personnel" +
                "(Name, Assignment, Position, " +
                "EmployeeStatus, DailyRate, ColaRate, " +
                "MonthlyRate, TIN, TaxStatus)" +
                "VALUES ('"+ personnel.getName() +"',"
                        + " '"+personnel.getAssignment()+"',"
                        + " '"+personnel.getPosition()+"',"
                        + " '"+personnel.getEmployeeStatus()+"',"
                        + " '"+personnel.getDailyRate()+"',"
                        + " '"+personnel.getColaRate()+"',"
                        + " '"+personnel.getMonthlyRate()+"',"
                        + " '"+personnel.getTIN()+"',"
                        + " '"+personnel.getTaxStatus()+"');";
                stmt=con.prepareStatement(sql);
                stmt.execute(sql);
				String pTIN = personnel.getTIN();
                if(personnel.getSSS()!=0){
                    this.addAdjustment("SSS", personnel.getSSS(), pTIN, periodStartDate);
                }
                if(personnel.getSSSLoan()!=0){                
                    this.addAdjustment("SSS Loan", personnel.getSSSLoan(), pTIN, periodStartDate);
                }
                if(personnel.getPHIC()!=0){
                    this.addAdjustment("PHIC", personnel.getPHIC(), pTIN, periodStartDate);
                }
                if(personnel.getHDMF()!=0){
                    this.addAdjustment("HDMF", personnel.getHDMF(), pTIN, periodStartDate);
                }
                if(personnel.getHDMFLoan()!=0){
                    this.addAdjustment("HDMF Loan", personnel.getHDMFLoan(), pTIN, periodStartDate);
                }
                if(personnel.getPayrollAdvance()!=0){
                    this.addAdjustment("Payroll Advance", personnel.getPayrollAdvance(), pTIN, periodStartDate);
                }
                if(personnel.getHouseRental()!=0){
                    this.addAdjustment("House Rental", personnel.getHouseRental(), pTIN, periodStartDate);
                }
                if(personnel.getUniformAndOthers()!=0){
                    this.addAdjustment("Uniform and Others", personnel.getUniformAndOthers(), pTIN, periodStartDate);
                }
            } catch (SQLException ex){
				System.out.println(ex);
            }
        }
		this.client = assignment;
		return true;
	}

	public boolean addDTR(File fileDirectory, Date periodStartDate) throws Exception{
    	ArrayList<DTR> dtrs = new ArrayList<DTR>();

        try{
			File file = fileDirectory;
			
			String ext = getExtension(fileDirectory.toString());
			if(!ext.equals("xls")){
				throw new Exception("File is not an excel file.");
			}
			
			
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);

			String name,tin;
			float regularDaysWorks, regularOvertime, regularNightShiftDifferential,
				  specialHoliday, specialHolidayOvertime, specialHolidayNightShiftDifferential,
				  legalHoliday, legalHolidayOvertime, legalHolidayNightShiftDifferential,
				  specialHolidayOnRestDay, legalHolidayOnRestDay, late;
			Date psd;
			int row,column;

			row = 0;
			column = 1;
			psd= null;
			try{
				Cell cell = sheet.getCell(column,row);
				if(cell.getType() == CellType.DATE){
					DateCell date = (DateCell)cell;
					psd = sdf.parse(sdf.format(date.getDate()));
				}else{
					throw new Exception("B2 is not formtted to Date!");
				}
			}catch(Exception e){
				throw new Exception("B2 is not formtted to Date!");
			}

			if(!sdf.format(psd).equals(sdf.format(periodStartDate))){
				throw new Exception("Date not equal to system date!");
			}

			row += 2;
			
			while(row < sheet.getRows()){
			
				column = 0;
				name = sheet.getCell(column,row).getContents();

				if(name.length() > 0){

					column++;
					tin = sheet.getCell(column,row).getContents();
					if(tin.length() == 0){
						throw new Exception("Lacking tin!");
					}
					
					float timeWorked[] = new float[12];
					
					for(int i = 0; i < 12;i++){
						column++;
						timeWorked[i] = tryGetFloat(sheet.getCell(column,row).getContents());
						if(timeWorked[i] < 0){
							throw new Exception("Negative days worked or hours.");
						}
					}
					
					regularDaysWorks = timeWorked[0];
					regularOvertime = timeWorked[1];
					regularNightShiftDifferential = timeWorked[2];
					specialHoliday = timeWorked[3];
					specialHolidayOvertime = timeWorked[4];
					specialHolidayNightShiftDifferential = timeWorked[5];
					legalHoliday = timeWorked[6];
					legalHolidayOvertime = timeWorked[7];
					legalHolidayNightShiftDifferential = timeWorked[8];
					specialHolidayOnRestDay = timeWorked[9];
					legalHolidayOnRestDay = timeWorked[10];
					late = timeWorked[11];
									
				    dtrs.add(new DTR(name, tin, regularDaysWorks, regularOvertime, regularNightShiftDifferential,
			   								 specialHoliday, specialHolidayOvertime,specialHolidayNightShiftDifferential,
			   								 legalHoliday, legalHolidayOvertime, legalHolidayNightShiftDifferential,
			   								 legalHolidayOnRestDay, specialHolidayOnRestDay, late, periodStartDate));
				}else{
					throw new Exception("Lacking name!");
				}
				row++;
			}

		}catch(Exception e){
			System.out.println(e);
			throw e;
		}
		
        //ADD TO DATABASE
		Statement stmt = null;
		String sql = "";
		try{
			sql = "START TRANSACTION;";
			stmt=con.prepareStatement(sql);
			stmt.execute(sql);
        }catch(SQLException ex){
			System.out.println(ex);
		}
			for(DTR dtr: dtrs){
                try{
                    sql= "REPLACE INTO `Payroll System`.`DTR` " +
                    "(`RDW`, `ROT`, `RNSD`, `SH`, `SHOT`, " +
                    "`SHNSD`, `LH`, `LHOT`, `LHNSD`, " +
                    "`PeriodStartDate`, `LHRD`, `SHRD`, " +
					"`late`, `TIN`) VALUES " +
                    "('"+ dtr.getRegularDaysWorks() +"', " +
                    "'"+ dtr.getRegularOvertime() +"', " +
                    "'"+ dtr.getRegularNightShiftDifferential() +"', " +
                    "'"+ dtr.getSpecialHoliday() +"', " +
                    "'"+ dtr.getSpecialHolidayOvertime() +"', " +
                    "'"+ dtr.getSpecialHolidayNightShiftDifferential() +"', " +
                    "'"+ dtr.getLegalHoliday() +"', " +
                    "'"+ dtr.getLegalHolidayOvertime() +"', " +
                    "'"+ dtr.getLegalHolidayNightShiftDifferential() +"', " +
                    "'"+ sdf.format(dtr.getPeriodStartDate()) +"', " +
					"'"+ dtr.getLegalHolidayOnRestDay() +"', " +
					"'"+ dtr.getSpecialHolidayOnRestDay() +"', " +
					"'"+ dtr.getLate() +"', " +
                    "'"+ dtr.getTIN() +"');";
                    stmt=con.prepareStatement(sql);
                    stmt.execute(sql);

                }catch(SQLException ex){
					if(ex.getErrorCode() == 1452){
						try{
							sql = "ROLLBACK;";
							stmt=con.prepareStatement(sql);
							stmt.execute(sql);	
							throw new Exception("Adding dtr to a personnel not in the database.");
						}catch(SQLException ex2){
							System.out.println(ex2);
						}
					}
					throw new Exception("Unknown error.");
                }
            }
		try{
			sql = "COMMIT;";
			stmt = con.prepareStatement(sql);
			stmt.execute(sql);
        }catch(SQLException ex){
			System.out.println(ex);
		}
		return true;
	}

	public void removePersonnel(String client){
	}

	public void getPersonnel(String client){ //returns ResultSet

	}

	public void addAdjustment(String reason, float adjustment, String tin, Date periodStartDate) {
        Statement stmt = null;
        
	    try {
			String sql = "REPLACE INTO `Payroll System`.`AdjustmentsAndDeductions` " +
			"(`amount`, `type`, `PeriodStartDate`, `TIN`) VALUES " +
			"('"+ adjustment +"', " +
			"'"+ reason +"', " +
			"'"+ sdf.format(periodStartDate) +"', " +
			"'"+ tin +"');";
			stmt=con.prepareStatement(sql);
			stmt.execute(sql);
        } catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public void removeAdjustment(String reason, float adjustment, String tin, Date periodStartDate) {
        Statement stmt = null;
        
        try{
			String sql="DELETE FROM `Payroll System`.`AdjustmentsAndDeductions`\n" +
			"WHERE `TIN` = \""+ tin+"\" AND `PeriodStartDate` =\""+ sdf.format(periodStartDate)+"\" AND `amount` = \""+adjustment+"\" AND `TYPE` = \""+reason+"\";";
			stmt=con.prepareStatement(sql);
			stmt.execute(sql);
        } catch (SQLException ex) {
			System.out.println(ex);
        }
	}
	
	public boolean checkPassword(String pass){
		try{
			Statement st = con.createStatement();	
			ResultSet rs = st.executeQuery("select password from password where password = '"+pass+"'");
			int rowCount = 0;  
			if(rs.next()){
				return true;
			}
			return false;
		}catch (SQLException ex){
			System.out.println(ex);
		}
		return false;
	}
	public int changePassword(String oldPass, String newPass){
		int x = 0;
		
		try{
			Statement st = con.createStatement();
			x = st.executeUpdate("update password set password = '"+newPass+"' where password = '"+oldPass+"'");
		}catch(Exception e){
			System.out.println(e);
		}
		return x;
	}

	public void modifyTaxTable(String fileDirectory){
	}

	public void modfyClientVariables(float specialHoliday, float legalHoliday){
	}

	public int generatePayslips(File directory, String client, String psd){
		Statement stmt = null;
		ArrayList<Payslip> payslips = new ArrayList<>();
            
            try{
				String sql = "Select * FROM `client`,`dtr`,`personnel` where client.name = '"+client+"' and personnel.assignment = client.name " + 
							" and dtr.tin = personnel.tin and dtr.periodstartdate = '"+psd+"' order by personnel.name";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				float rotVar = 1.25f;
				float rnsdVar = .10f;
				float lhRate = 30f;
				float lhVar = 1.00f;
				float lhOTVar = 1.30f;
				float lhNSDVar = 1.00f*.10f;
				float lhRDVar = 2.60f;
				float shRate = 30f;
				float shVar = .30f;
				float shOTVar = .30f*.30f;
				float shNSDVar = .30f*.10f;
				float shRDVar = 1.5f;
				try{
					float[] vars= getVariables(client);
					rotVar = vars[0];
					rnsdVar = vars[1];
					lhRate = vars[2];
					lhVar = vars[3];
					lhOTVar = vars[4];
					lhNSDVar = vars[5];
					lhRDVar = vars[6];
					shRate = vars[7];
					shVar = vars[8];
					shOTVar = vars[9];
					shNSDVar = vars[10];
					shRDVar = vars[11];
					System.out.println("modified vars");
				}catch(Exception ex){
					System.out.println("default vars");
				}
				
				float[][] wholeTable = getWholeTaxTable();
				while(rs.next()){
					String tin = rs.getString("TIN");
					
					sql = "Select * FROM `adjustmentsanddeductions`,`personnel` where personnel.tin = adjustmentsanddeductions.tin" +
								 " and personnel.tin = '"+tin+"' and periodstartdate = '"+psd+"'";
					st = con.createStatement();
					ResultSet rs2 = st.executeQuery(sql);
					
					String assignment = rs.getString("assignment");
					String name = rs.getString("personnel.name");
					Date periodStartDate = rs.getDate("PeriodStartDate");
					String position = rs.getString("Position");
					float regularDaysWork = rs.getFloat("RDW");
					float dailyRate = rs.getFloat("DailyRate");
					float late = rs.getFloat("late");
					float regularOvertime = rs.getFloat("ROT");
					float regularNightShiftDifferential = rs.getFloat("RNSD");
					float legalHoliday = rs.getFloat("LH");
					float legalHolidayOvertime = rs.getFloat("LHOT");
					float legalHolidayNightShiftDifferential = rs.getFloat("LHNSD");
					float legalHolidayOnRestDay = rs.getFloat("LHRD");
					float specialHoliday = rs.getFloat("SH");
					float specialHolidayOvertime = rs.getFloat("SHOT");
					float specialHolidayNightShiftDifferential = rs.getFloat("SHNSD");
					float specialHolidayOnRestDay = rs.getFloat("SHRD");
					float colaRate = rs.getFloat("ColaRate");
					float sss = 0;
					float phic = 0;
					float hdmf = 0;
					float sssLoan = 0;
					float hdmfLoan = 0;
					float payrollAdvance = 0;
					float houseRental = 0;
					float uniformAndOthers = 0;	
					float adjustments = 0;
					float transpoAllow = 0;
					String type;
					String taxStatus = rs.getString("taxstatus");
					taxStatus =  taxStatus == null || taxStatus.equals("") ? "s" : taxStatus;
					while(rs2.next()){
						type = rs2.getString("type");
						switch(type){
							case "SSS":
								sss = rs2.getFloat("amount");
							break;
							
							case "PHIC":
								phic = rs2.getFloat("amount");
							break;
							
							case "SSS Loan":
								sssLoan = rs2.getFloat("amount");
							break;
							
							case "HDMF":
								hdmf = rs2.getFloat("amount");
							break;
							
							case "HDMF Loan":
								hdmfLoan = rs2.getFloat("amount");
							break;
							
							case "Payroll Advance":
								payrollAdvance = rs2.getFloat("amount");
							break;
							
							case "House Rental":
								houseRental = rs2.getFloat("amount");
							break;
							
							case "Uniform and Others":
								uniformAndOthers = rs2.getFloat("amount");
							break;
							
							default:
								adjustments += rs2.getFloat("amount");
							break;
						}
					}
					
					float totalDeductions = sss + phic + sssLoan + hdmf + 
											hdmfLoan + payrollAdvance +
											houseRental + uniformAndOthers;
					
					float hourlyRate = dailyRate/8;
					float shHourlyRate = (dailyRate + shRate)/8;
					float lhHourlyRate = (dailyRate + lhRate)/8;
					float basicPay = regularDaysWork * dailyRate;
					float deductionFromTardiness = dailyRate/8/60 * late;
					float colaAllowance = colaRate/30 * regularDaysWork;
					
					float regularPay = basicPay + colaAllowance - deductionFromTardiness;
					float regularOvertimePay = regularOvertime * rotVar * hourlyRate;
					float regularNightShiftDifferentialPay = regularNightShiftDifferential * rnsdVar * hourlyRate;
					float legalHolidayPay = legalHoliday * lhVar * lhHourlyRate;
					float legalHolidayOvertimePay = legalHolidayOvertime * lhOTVar * lhHourlyRate;
					float legalHolidayNightShiftDifferentialPay = legalHolidayNightShiftDifferential * lhNSDVar * lhHourlyRate;
					float legalHolidayOnRestDayPay = legalHolidayOnRestDay * lhRDVar * lhHourlyRate;
					float specialHolidayPay = specialHoliday * shVar * shHourlyRate;
					float specialHolidayOvertimePay = specialHolidayOvertime * shOTVar * shHourlyRate;
					float specialHolidayNightShiftDifferentialPay = specialHolidayNightShiftDifferential * shNSDVar * shHourlyRate;
					float specialHolidayOnRestDayPay = specialHolidayOnRestDay * shHourlyRate * shRDVar;
					float wTax = 0;
					float otPay = regularOvertimePay + 
								legalHolidayOvertimePay + 
								specialHolidayOvertimePay;
					float nsdPay = regularNightShiftDifferentialPay +
									legalHolidayNightShiftDifferentialPay +
									specialHolidayNightShiftDifferentialPay;
					float grossPay = regularPay + legalHolidayPay + specialHolidayPay + 
									otPay + nsdPay +
									adjustments + legalHolidayOnRestDayPay + specialHolidayOnRestDayPay;
					float netPay = grossPay - totalDeductions;
					int taxStatusIndex = 0;;
					for(int i = 0 ; i < this.taxStatus.length; i++){
						if(this.taxStatus[i].toLowerCase().contains(taxStatus)){
							taxStatusIndex = i;
							break;
						}
					}
					int bracket = 0;
					for(int i = 0 ; i < wholeTable.length; i++){
						if(wholeTable[i][taxStatusIndex+2] > netPay){
							bracket = i - 1;
							break;
						}else if(i == wholeTable.length - 1){
							bracket = i;
						}
					}
					wTax = wholeTable[bracket][0] + (wholeTable[bracket][1] * (netPay - wholeTable[bracket][taxStatusIndex+2])) / 100;
					netPay -= wTax;
					payslips.add(new Payslip(tin, assignment,  name, periodStartDate,
					position, regularDaysWork, dailyRate,
					grossPay, late, regularPay,
					regularOvertime, regularOvertimePay,
					regularNightShiftDifferential,
					regularNightShiftDifferentialPay,
					legalHoliday, legalHolidayPay,
					legalHolidayOvertime, legalHolidayOvertimePay,
					legalHolidayNightShiftDifferential,
					legalHolidayNightShiftDifferentialPay,
					legalHolidayOnRestDay, legalHolidayOnRestDayPay,
					specialHoliday, specialHolidayPay,
					specialHolidayOvertime, specialHolidayOvertimePay,
					specialHolidayNightShiftDifferential,
					specialHolidayNightShiftDifferentialPay,
					specialHolidayOnRestDay, specialHolidayOnRestDayPay,
					transpoAllow, adjustments, wTax,
					sss, phic, hdmf, sssLoan,
					hdmfLoan, payrollAdvance, houseRental,
					uniformAndOthers, netPay));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		boolean second = false;
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(directory, "UTF-8");
		}catch(Exception ex){
			System.out.println(ex);
			return 1;
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		for(Payslip p : payslips){
			//Put to database.
			try{
				String sql="REPLACE INTO `Payroll System`.`Payslip`" +
				"(`Assignment`, `Name`, `PeriodStartDate`, `TIN`, `Position`, `RDW`," +
				"`DailyRate`, `GrossPay`, `Late`, `RegularPay`, `ROT`, `ROTPay`," +
				"`RNSD`, `RNSDPay`, `LH`, `LHPay`, `LHOT`, `LHOTPay`, `LHNSD`," +
				"`LHNSDPay`, `LHRD`, `LHRDPay`, `SH`, `SHPay`, `SHOT`, `SHOTPay`," +
				"`SHNSD`, `SHNSDPay`, `SHRD`, `SHRDPay`, `TranspoAllow`, `Adjustments`," +
				"`WTax`, `SSS`, `PHIC`, `HDMF`, `SSSLoan`, `Savings`, `PayrollAdvance`," +
				"`HouseRental`, `UniformAndOthers`, `NetPay`) VALUES " +
				"('"+ p.getAssignment() + "'," + "'" + p.getName() + "'," + 
				"'"+ sdf.format(p.getPeriodStartDate()) + "'," + "'" + p.getTIN() + "'," + 
				"'"+ p.getPosition() + "', " + p.getRegularDaysWork() + ", " + 
				p.getDailyRate() + ", " + p.getGrossPay() + ", " + 
				p.getLate() + ", " + p.getRegularPay() + ", " + 
				p.getRegularOvertime() + ", " + p.getRegularOvertimePay() + ", " + 
				p.getRegularNightShiftDifferential() + ", " + p.getRegularNightShiftDifferentialPay() + ", " + 
				p.getLegalHoliday() + ", " + p.getLegalHolidayPay() + ", " + 
				p.getLegalHolidayOvertime() + ", " + p.getLegalHolidayOvertimePay() + ", " + 
				p.getLegalHolidayNightShiftDifferential() + "," + 
				p.getLegalHolidayNightShiftDifferentialPay() + ", " + 
				p.getLegalHolidayOnRestDay() +", " + p.getLegalHolidayOnRestDayPay() + ", " + 
				p.getSpecialHoliday() + ", " + p.getSpecialHolidayPay() + ", " + 
				p.getSpecialHolidayOvertime() + ", " + p.getSpecialHolidayOvertimePay() + ", " + 
				p.getSpecialHolidayNightShiftDifferential() + ", " + 
				p.getSpecialHolidayNightShiftDifferentialPay() + ", " + 
				p.getSpecialHolidayOnRestDay() + ", " + p.getSpecialHolidayOnRestDayPay() +", " + 
				0 +", " + p.getAdjustments() + ", " + 
				p.getWTax() +", " + p.getSSS() +", " + p.getPHIC() + ", " + p.getHDMF() +", " + 
				p.getSSSLoan() +", " + 0 + ", " + 
				p.getPayrollAdvance() +", " + p.getHouseRental() + ", " + 
				p.getUniformAndOthers() +", " + p.getNetPay() + ");";
				//Transpo Allow and Savings are set to 0 since I still don't know where it comes from.
				stmt=con.prepareStatement(sql);
				stmt.execute(sql);
			}catch(SQLException ex){
				System.out.println(ex);
			}
			
			writer.print("\"888 GALLANT MANPOWER AND MANAGEMENT SERVICES INCORPORATED\",,,,,,,,,\""+p.getAssignment()+"\"");
			writer.println(",,\"888 GALLANT MANPOWER AND MANAGEMENT SERVICES INCORPORATED\",,,,,,,,,\""+p.getAssignment()+"\",");
			writer.print("\"558 Quirino Avenue Brgy. Tambo Paranaque City\",,,,,,,,,,,");
			writer.println("\"558 Quirino Avenue Brgy. Tambo Paranaque City\",,,,,,,,,,");
			String date = sdf.format(p.getPeriodStartDate());
			String printDate = "";
			int month = Integer.parseInt(date.substring(5,7));
			int day = Integer.parseInt(date.substring(8,10));
			int year = Integer.parseInt(date.substring(0,4));
			int sDay = 15;
			
			switch(month){
				case 1:printDate += "Jan. ";
					sDay = day == 1? 15:31;
				break;
				
				case 2:printDate += "Feb. ";
					if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)){
						sDay = day == 1? 15:29;
					}else{
						sDay = day == 1? 15:28;
					}
				break;
				
				case 3:printDate += "March ";
					sDay = day == 1? 15:31;
				break;
				
				case 4:printDate += "April ";
					sDay = day == 1? 15:30;
				break;
					
				case 5:printDate += "May ";
					sDay = day == 1? 15:31;
				break;
				
				case 6:printDate += "June ";
					sDay = day == 1? 15:30;
				break;
				
				case 7:printDate += "July ";
					sDay = day == 1? 15:31;
				break;
				
				case 8:printDate += "Aug. ";
					sDay = day == 1? 15:31;
				break;
				
				case 9:printDate += "Sept. ";
					sDay = day == 1? 15:30;
				break;
				
				case 10:printDate += "Oct. ";
					sDay = day == 1? 15:31;
				break;
				
				case 11:printDate += "Nov. ";
					sDay = day == 1? 15:30;
				break;
				
				case 12:printDate += "Dec. ";
					sDay = day == 1? 15:31;
				break;
			}
			
			printDate += day + "-" + sDay;
			printDate += ", ";
			printDate += year;
			writer.print("\"For the period of " + printDate + "\",,,,,,,,,,,");
			
			writer.print("\"For the period of " + printDate + "\"");
			writer.println();
			
			writer.print("\""+p.getName()+"\"");
			writer.print(",,,");
			writer.print("\""+p.getPosition()+"\""+",,,,,,,,");
			writer.print("\""+p.getName()+"\"");
			writer.print(",,,");
			writer.println("\""+p.getPosition()+"\"");
			
			writer.println();
			
			writer.print("\"Days Worked\",," + p.getRegularDaysWork());
			writer.print(",\"L/H NSD Hrs\",," + p.getLegalHolidayNightShiftDifferential());
			writer.print(",\"W/Tax\",,"+"\""+df.format(p.getWTax())+"\"" );
			writer.print(",,");
			writer.print(",\"Gross Pay\",,"+"\""+df.format(p.getGrossPay())+"\"" );
			writer.println(",\"W/Tax\",,"+"\""+df.format(p.getWTax())+"\"" );
		
			writer.print("\"Daily Rate\",," + p.getDailyRate());
			writer.print(",\"L/H NSD Pay\",," + "\""+df.format(p.getLegalHolidayNightShiftDifferentialPay())+"\"" );
			writer.print(",\"SSS\",,"+p.getSSS());
			writer.print(",,");
			writer.print(",\"Tardiness\",," + "\""+df.format(p.getLate() * p.getDailyRate()/8/60)+"\"" );
			writer.println(",\"SSS\",,"+p.getSSS());
			
			writer.print("\"Gross Pay\",," + "\""+df.format(p.getGrossPay())+"\"" );
			writer.print(",\"SH/RD Worked\",," + p.getSpecialHoliday());
			writer.print(",\"PHIC\",,"+p.getPHIC());
			writer.print(",,");
			writer.print(",\"Regular OT\",,"+p.getRegularOvertime());
			writer.println(",\"PHIC\",,"+p.getPHIC());
			
			writer.print("\"Late\",," + p.getLate());
			writer.print(",\"SH/RD Pay\",," + "\""+df.format(p.getSpecialHolidayPay())+"\"" );
			writer.print(",\"HDMF\",,"+p.getHDMF());
			writer.print(",,");
			writer.print(",\"NSD Pay\",,"+"\""+df.format(p.getRegularNightShiftDifferentialPay())+"\"" );
			writer.println(",\"HDMF\",,"+p.getHDMF());
			
			writer.print("\"Tardiness\",," + "\""+df.format(p.getLate() * p.getDailyRate()/8/60)+"\"" );
			writer.print(",\"SH on RD Hrs\",," + p.getSpecialHolidayOnRestDay());
			writer.print(",\"SSS Loan\",,"+p.getSSSLoan());
			writer.print(",,");
			writer.print(",\"Legal Holiday Pay\",,"+"\""+df.format(p.getLegalHolidayPay())+"\"" );
			writer.println(",\"SSS Loan\",,"+p.getSSSLoan());
			
			writer.print("\"Regular Pay\",," + "\""+df.format(p.getRegularPay())+"\"" );
			writer.print(",\"SH on RD Pay\",," + "\""+df.format(p.getSpecialHolidayOnRestDayPay())+"\"" );
			writer.print(",\"HDMF Loan\",,"+p.getHDMFLoan());
			writer.print(",,");
			writer.print(",\"L/H on RD Pay\",,"+"\""+df.format(p.getLegalHolidayOnRestDayPay())+"\"" );
			writer.println(",\"HDMF Loan\",,"+p.getHDMFLoan());
			
			writer.print("\"OT Hour\",," + p.getRegularOvertime());
			writer.print(",\"SH/RD OT Hrs\",," + p.getSpecialHolidayOvertime());
			writer.print(",\"Payroll Advance\",,"+p.getPayrollAdvance());
			writer.print(",,");
			writer.print(",\"L/H OT Pay\",,"+"\""+df.format(p.getLegalHolidayOvertimePay())+"\"" );
			writer.println(",\"Payroll Advance\",,"+p.getPayrollAdvance());
			
			writer.print("\"OT Pay\",," + "\""+df.format(p.getRegularOvertimePay())+"\"");
			writer.print(",\"SH/RD OT Pay\",," + "\""+df.format(p.getSpecialHolidayOvertimePay())+ "\"");
			writer.print(",\"House Rental\",,"+p.getHouseRental());
			writer.print(",,");
			writer.print(",\"L/H NSD Pay\",,"+"\""+df.format(p.getLegalHolidayNightShiftDifferentialPay())+"\"");
			writer.println(",\"House Rental\",,"+p.getHouseRental());
			
			writer.print("\"NSD Hour\",," + p.getRegularNightShiftDifferential());
			writer.print(",\"SH/RD NSD Hrs\",," + p.getSpecialHolidayNightShiftDifferential());
			writer.print(",\"Uniform & Others\",,"+p.getUniformAndOthers());
			writer.print(",,");
			writer.print(",\"SH RD Pay\",,"+"\""+df.format(p.getSpecialHolidayOnRestDayPay())+"\"" );
			writer.println(",\"Uniform & Others\",,"+p.getUniformAndOthers());
			
			writer.print("\"NSD Pay\",," + "\""+df.format(p.getRegularNightShiftDifferentialPay())+"\"");
			writer.print(",\"SH/RD NSD Pay\",," + p.getSpecialHolidayNightShiftDifferentialPay());
			writer.print(",\"Total Deductions\",,"+"\""+df.format(p.getTotalDeductions())+"\"");
			writer.print(",,");
			writer.print(",\"SH on RD Pay\",,"+"\""+df.format(p.getRegularNightShiftDifferentialPay())+"\"");
			writer.println(",\"Total Deductions\",,"+"\""+df.format(p.getTotalDeductions())+"\"");
			
			writer.print("\"L/H Hrs Worked\",," + p.getLegalHoliday());
			writer.println(",,,,,,,,,\"SH/RD OT Pay\",,"+"\""+df.format( p.getSpecialHolidayOvertimePay())+"\"");
			
			writer.print("\"L/H Pay\",," + "\""+df.format(p.getLegalHolidayPay())+"\"");
			writer.println(",,,,,,,,,\"SH/RD NSD Pay\",,"+"\""+df.format(p.getSpecialHolidayNightShiftDifferentialPay())+"\"" );
			
			writer.print("\"L/H on RD Hrs Worked\",," +p.getRegularNightShiftDifferential());
			writer.print(",\"Adjustments\",," + "\""+df.format(p.getAdjustments())+"\"");
			writer.print(",,,,,");
			writer.println(",\"Adjustments\",,"+"\""+df.format(p.getAdjustments())+"\"" );
			
			writer.println("\"L/H on RD Pay\",," + "\""+df.format(p.getRegularNightShiftDifferentialPay())+"\"");
			
			writer.print("\"L/H OT Hrs\",," + p.getLegalHolidayOvertime());
			writer.print(",\"Gross Pay\",," +"\""+df.format(p.getGrossPay())+"\"");
			writer.print(",\"Net Pay\",,"+"\""+df.format(p.getNetPay())+"\"");
			writer.print(",,");
			writer.print(",\"Gross Pay\",,"+"\""+df.format(p.getGrossPay())+"\"");
			writer.println(",\"Net Pay\",,"+"\""+df.format(p.getNetPay())+"\"");
			
			writer.println("\"L/H on OT Pay\",," + "\""+df.format(p.getLegalHolidayOvertimePay())+"\"");
			
			writer.println();
			
			writer.println("\"I acknowledge to have received the amount stated above and have no further claims for services rendered.\"");
			
			writer.println();
			writer.println("\"___________________________\""+",,,,,"+"\"___________________________\"");
			
			writer.println(",\"Signature\""+",,,,,"+"\"Date\"");		
			
			writer.println();
			writer.println();
		}
		writer.close();
		return 0;
	}

	public void generateSummaryReport(File directory, String client){
	}

	public void backupDate(){
	}

	public ArrayList<String> getSummaryReport(String client, String report, Date periodStartDate){
		return new ArrayList<String>();
	}

	public ArrayList<String> getClientList(){
		Statement stmt = null;
		ArrayList<String> clients = new ArrayList<>();
            
            try{
				String sql="Select * FROM `client` order by name";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					clients.add(rs.getString("Name"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return clients;
	}

	public ArrayList<String> getPersonnelList(String client){
		Statement stmt = null;
		ArrayList<String> personnel = new ArrayList<>();
            
            try{
				String sql="Select Name, TIN FROM `personnel` where assignment = '"+client+"' order by name";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					personnel.add(rs.getString("Name") + " ~ " + rs.getString("TIN"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return personnel;
	}

	public ArrayList<String> getAdjustmentsList(String tin){
		Statement stmt = null;
		ArrayList<String> adjustments = new ArrayList<>();
            
            try{
				String sql="Select * FROM `adjustmentsanddeductions` where `tin` = '"+tin+"' and `periodstartdate` = '"+sdf.format(periodStartDate)+"'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					adjustments.add(rs.getString("type")+" ~ "+rs.getString("amount"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return adjustments;
	}


	/*public String getTIN(String personnelName){
		try{
			String sql="Select `TIN` FROM `personnel` where name = '"+personnelName+"'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			return rs.getString("TIN");
		}catch(Exception ex){
			System.out.println(ex+"!");
		}
		return "";
	}*/
	
	public ArrayList<String> getDateListDTR(String client){
		Statement stmt = null;
		ArrayList<String> dates = new ArrayList<>();
            
            try{
				String sql = "Select distinct periodstartdate FROM `client`,`dtr`,`personnel` where client.name = '"+client+"' and personnel.assignment = client.name "
							+ " and dtr.tin = personnel.tin order by periodstartdate";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					dates.add(rs.getString("periodstartdate"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return dates;
	}
	
	public boolean checkPeriodForDTR(String client, String psd){
		Statement stmt = null;
            
            try{
				String sql = "Select * FROM `client`,`dtr`,`personnel` where client.name = '"+client+"' and personnel.assignment = client.name " + 
							" and dtr.tin = personnel.tin and dtr.periodstartdate = '"+psd+"'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					return true;
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return false;
	}

	public boolean checkPeriodForPayslips(String client, String psd){
		Statement stmt = null;
            
            try{
				String sql = "Select * FROM `client`,`payslip`,`personnel` where client.name = '"+client+"' and personnel.assignment = client.name " + 
							" and payslip.tin = personnel.tin and payslip.periodstartdate = '"+psd+"'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					return true;
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return false;
	}
	
	public ArrayList<String> getDateListPayslips(String client){ ///This should be updated
		Statement stmt = null;
		ArrayList<String> dates = new ArrayList<>();
            
            try{
				String sql = "Select distinct periodstartdate FROM `payslip` where assignment = '"+client+"' order by periodstartdate";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					dates.add(rs.getString("periodstartdate"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return dates;
	}
	
	public ArrayList<String> getColumnName(String report){
		ArrayList<String> column = new ArrayList<>();
		
		if(report.equals("Daily time record summary")){
			column.add("Seq no.");
			column.add("Name");
			column.add("Position");
			column.add("RDW");
			column.add("ROT");
			column.add("RNSD");
			column.add("LH");
			column.add("LHOT");
			column.add("LHNSD");
			column.add("SH");
			column.add("SHOT");
			column.add("SHONSD");
			column.add("LHRD");
			column.add("SHRD");
		}else if(report.equals("Billing summary")){
			column.add("Seq No.");
			column.add("Name");
			column.add("Position");
			column.add("RDW");
			column.add("DailyRate");
			column.add("Allowance");
			column.add("RegularPay");
			column.add("ROT");
			column.add("ROTPay");
			column.add("RNSD");
			column.add("RNSDPay");
			column.add("LH");
			column.add("LHPay");
			column.add("LHOT");
			column.add("LHOTPay");
			column.add("LHNSD");
			column.add("LHNSDPay");
			column.add("SH");
			column.add("SHPay");
			column.add("SHOT");
			column.add("SHOTPay");
			column.add("SHNSD");
			column.add("SHNSDPay");
			column.add("Adjustment");
			column.add("GrossPay");
			column.add("SSS");
			column.add("PHIC");
			column.add("HDMF");
			column.add("SSSLoan");
			column.add("PayrollAdvance");
			column.add("House Rental");
			column.add("Uniforms and others");
			column.add("Savings");
			column.add("NetPay");
		}else if(report.equals("Atm/cash payroll summary")){
			column.add("Name");
    	 	column.add("Gross pay");
    	 	column.add("SSS");
   			column.add("PHIC");
    	 	column.add("HDMF");
    	 	column.add("S&L Loan");
    		column.add("House Rental");
    	 	column.add("S&L Membership saving");
    	 	column.add("Total deduction");
    	 	column.add("Net Pay");
    	 	column.add("ATM");
    	 	column.add("CASH");
    	 	column.add("Total");
    		column.add("DIFF");
		}else if(report.equals("Payroll with total deduction")){
			column.add("Seq No.");
			column.add("Name");
			column.add("Position");
			column.add("RDW");
			column.add("DailyRate");
			column.add("Allowance");
			column.add("RegularPay");
			column.add("ROT");
			column.add("ROTPay");
			column.add("RNSD");
			column.add("RNSDPay");
			column.add("LH");
			column.add("LHPay");
			column.add("LHOT");
			column.add("LHOTPay");
			column.add("LHNSD");
			column.add("LHNSDPay");
			column.add("SH");
			column.add("SHPay");
			column.add("SHOT");
			column.add("SHOTPay");
			column.add("SHNSD");
			column.add("SHNSDPay");
			column.add("Adjustment");
			column.add("GrossPay");
			column.add("SSS");
			column.add("PHIC");
			column.add("HDMF");
			column.add("SSSLoan");
			column.add("PayrollAdvance");
			column.add("House Rental");
			column.add("Uniforms and others");
			column.add("Savings");
			column.add("NetPay");
		}
		return column;
	}
	
	public ArrayList<Object[]> getTableRow(String client, String date, String report){
		ArrayList<Object[]> row = new ArrayList<>();
		Statement stmt = null;
			
			try{
				String sql = "select distinct name, tin from `payslip` where assignment = '"+client+"' and PeriodStartDate = '"+date+"'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				ResultSet rs2;
				if(rs.first()){
					String name,position;
					String tin;
					int j = 0;
					int TRDW = 0,TRegularPay = 0,TROT = 0,TROTPay = 0,TRNSD = 0,TRNSDPay = 0,TLH = 0,TLHPay = 0,TLHOT = 0,
						TLHOTPay = 0,TLHNSD = 0,TLHNSDPay = 0,TSH = 0,TSHPay = 0,TSHOT = 0,TSHOTPay = 0,
						TSHNSD = 0,TSHNSDPay = 0,TLHRD = 0,TLHRDPay = 0,TSHRD = 0,TSHRDPay = 0,TAdjustments = 0,TGrossPay = 0,TSSS = 0,TPHIC = 0,THDMF = 0,TSSSLoan = 0,
						TPayrollAdvance = 0,THouseRental = 0,TUniformAndOthers = 0,TSavings = 0,TNetPay = 0;
					
					do{
						tin = rs.getString("tin");
						name = rs.getString("name");
						sql = "select position,RDW,DailyRate,TranspoAllow,RegularPay,ROT,ROTPay,RNSD,RNSDPay,LH,LHPay," +
							  "LHOT,LHOTPay,LHNSD,LHNSDPay,SH,SHPay,SHOT,SHOTPay,SHNSD,SHNSDPay," +
							  "Adjustments,GrossPay,SSS,PHIC,HDMF,SSSLoan,PayrollAdvance,HouseRental," +
							  "UniformAndOthers,Savings,NetPay,LHRD,LHRDPay,SHRD,SHRDPay from `payslip` where tin = '"+tin+"' and PeriodStartDate = '"+date+"';";
						st = con.createStatement();
						rs2 = st.executeQuery(sql);
						
						position = "";
						int RDW = 0,DailyRate = 0,TranspoAllow = 0,RegularPay = 0,ROT = 0,ROTPay = 0,RNSD = 0,RNSDPay = 0,LH = 0,LHPay = 0,LHOT = 0
		    	 													,LHOTPay = 0,LHNSD = 0,LHNSDPay = 0,SH = 0,SHPay = 0,SHOT = 0,SHOTPay = 0,
																	SHNSD = 0,SHNSDPay = 0,LHRD = 0,LHRDPay = 0,SHRD = 0,SHRDPay = 0,Adjustments = 0,GrossPay = 0,SSS = 0,PHIC = 0,HDMF = 0,SSSLoan = 0,
																	PayrollAdvance = 0,HouseRental = 0,UniformAndOthers = 0,Savings = 0,NetPay = 0;
						if(rs2.next()){
							position = rs2.getString("position");
		    	 			RDW += rs2.getInt(2); DailyRate += rs2.getInt(3); TranspoAllow += rs2.getInt(4);
							RegularPay += rs2.getInt(5); ROT += rs2.getInt("ROT"); ROTPay += rs2.getInt(7); RNSD += rs2.getInt(8);
							RNSDPay += rs2.getInt(9); LH += rs2.getInt(10); LHPay += rs2.getInt(11); LHOT += rs2.getInt(12);
							LHOTPay += rs2.getInt(13); LHNSD += rs2.getInt(14);
							LHNSDPay += rs2.getInt(15); SH += rs2.getInt(16); SHPay += rs2.getInt(17);
							SHOT += rs2.getInt(18); SHOTPay += rs2.getInt(19); SHNSD += rs2.getInt(20); SHNSDPay += rs2.getInt(21);
							Adjustments += rs2.getInt(22); GrossPay += rs2.getInt(23); SSS += rs2.getInt(24); PHIC += rs2.getInt(25);
							HDMF += rs2.getInt(26); SSSLoan += rs2.getInt(27); PayrollAdvance += rs2.getInt(28); HouseRental += rs2.getInt(29);
							UniformAndOthers += rs2.getInt(30); Savings += rs2.getInt(31); NetPay += rs2.getInt(32);LHRD += rs2.getInt(33);
							LHRDPay += rs2.getInt(34); SHRD += rs2.getInt(35); SHRDPay += rs2.getInt(36);
							TRDW += rs2.getInt(2); DailyRate += rs2.getInt(3); TranspoAllow += rs2.getInt(4);
							TRegularPay += rs2.getInt(5); TROT += rs2.getInt(6); TROTPay += rs2.getInt(7); TRNSD += rs2.getInt(8);
							TRNSDPay += rs2.getInt(9); TLH += rs2.getInt(10); TLHPay += rs2.getInt(11); TLHOT += rs2.getInt(12);
							TLHOTPay += rs2.getInt(13); TLHNSD += rs2.getInt(14);
							TLHNSDPay += rs2.getInt(15); TSH += rs2.getInt(16); TSHPay += rs2.getInt(17);
							TSHOT += rs2.getInt(18); TSHOTPay += rs2.getInt(19); TSHNSD += rs2.getInt(20); TSHNSDPay += rs2.getInt(21);
							TAdjustments += rs2.getInt(22); TGrossPay += rs2.getInt(23); TSSS += rs2.getInt(24); TPHIC += rs2.getInt(25);
							THDMF += rs2.getInt(26); TSSSLoan += rs2.getInt(27); TPayrollAdvance += rs2.getInt(28); THouseRental += rs2.getInt(29);
							TUniformAndOthers += rs2.getInt(30); TSavings += rs2.getInt(31); TNetPay += rs2.getInt(32);TLHRD += rs2.getInt(33);
							TLHRDPay += rs2.getInt(34); TSHRD += rs2.getInt(35); TSHRDPay += rs2.getInt(36);
		    	 		}
						
		    	 		if(report.equals(getSummaryReport(0))){
							Object[] data1 = {j,name,position,RDW,ROT,RNSD,LH,LHOT,LHNSD,SH,SHOT,SHNSD,LHRD,SHRD};
							row.add(data1);
						}else if(report.equals(getSummaryReport(1))){
							Object[] data1 = {j,name,
			    	 		position,RDW,DailyRate,TranspoAllow,RegularPay,
			    	 		ROT,ROTPay,RNSD,RNSDPay,LH,LHPay,LHOT,LHOTPay,LHNSD,LHNSDPay,
			    	 		SH,SHPay,SHOT,SHOTPay,SHNSD,SHNSDPay,Adjustments,GrossPay,
			    	 		SSS,PHIC,HDMF,SSSLoan,PayrollAdvance,HouseRental,UniformAndOthers,Savings,NetPay};
			    	 		row.add(data1);
						}else if(report.equals(getSummaryReport(2))){
							Object[] data1 = {name,GrossPay,SSS,PHIC,HDMF,SSSLoan,HouseRental,Savings,0,NetPay,0,0,0,0};
							row.add(data1);
						}else if(report.equals(getSummaryReport(3))){
							Object[] data1 = {j,name,
			    	 		position,RDW,DailyRate,TranspoAllow,RegularPay,
			    	 		ROT,ROTPay,RNSD,RNSDPay,LH,LHPay,LHOT,LHOTPay,LHNSD,LHNSDPay,
			    	 		SH,SHPay,SHOT,SHOTPay,SHNSD,SHNSDPay,Adjustments,GrossPay,
			    	 		SSS,PHIC,HDMF,SSSLoan,PayrollAdvance,HouseRental,UniformAndOthers,Savings,NetPay};
			    	 		row.add(data1);
						}
							j++;
					}while(rs.next());
					
					if(report.equals(getSummaryReport(0))){
						Object[] data1 = {j,"Total"," ",TRDW,TROT,TRNSD,TLH,TLHOT,TLHNSD,TSH,TSHOT,TSHNSD,TLHRD,TSHRD};
	    	 			row.add(data1);
					}else if(report.equals(getSummaryReport(1))){
						Object[] data1 = {j,"TOTAL",
			    	 	"",TRDW,0,0,TRegularPay,
			    	 	TROT,TROTPay,TRNSD,TRNSDPay,TLH,TLHPay,TLHOT,TLHOTPay,TLHNSD,TLHNSDPay,
			    	 	TSH,TSHPay,TSHOT,TSHOTPay,TSHNSD,TSHNSDPay,TAdjustments,TGrossPay,
			    		TSSS,TPHIC,THDMF,TSSSLoan,TPayrollAdvance,THouseRental,TUniformAndOthers,TSavings,TNetPay};
			    	 	row.add(data1);
					}else if(report.equals(getSummaryReport(2))){
						Object[] data1 = {"Total",TGrossPay,TSSS,TPHIC,THDMF,TSSSLoan,THouseRental,TSavings,0,TNetPay,0,0,0,0};
						row.add(data1);
					}else if(report.equals(getSummaryReport(3))){
						Object[] data1 = {j,"TOTAL",
			    	 	"",TRDW,0,0,TRegularPay,
			    	 	TROT,TROTPay,TRNSD,TRNSDPay,TLH,TLHPay,TLHOT,TLHOTPay,TLHNSD,TLHNSDPay,
			    	 	TSH,TSHPay,TSHOT,TSHOTPay,TSHNSD,TSHNSDPay,TAdjustments,TGrossPay,
			    	 	TSSS,TPHIC,THDMF,TSSSLoan,TPayrollAdvance,THouseRental,TUniformAndOthers,TSavings,TNetPay};
			    	 	row.add(data1);
					}
				}
			}catch(Exception ex){
				System.out.println(ex);
			}
			
		return row;
	}
	
	public String getSummaryReport(int i){
		return summaryReports[i];
	}
	
	public String[] getSummaryReports(){
		return summaryReports;
	}
	
	public Date nextTimePeriod(){
		String c = sdf.format(periodStartDate);
		int month = Integer.parseInt(c.substring(5,7));
		int day = Integer.parseInt(c.substring(8,10));
		int year = Integer.parseInt(c.substring(0,4));
		if(day == 1){
			day = 16;
		}else{
			day = 1;
			month++;
			if(month>12){
				month %= 12;
				year ++;
			}
		}
		String extraZeroForMonth = "0";
		String extraZeroForDay = "0";
		if(month > 9){
			extraZeroForMonth = "";
		}
		if(day > 9){
			extraZeroForDay = "";
		}
		String newDate = year + "-" + extraZeroForMonth + month + "-" + extraZeroForDay + day;
		try{
			periodStartDate = sdf.parse(newDate);
		}catch(Exception ex){
			System.out.println(ex);
		}
		return periodStartDate;
	}
	
	private static float tryGetFloat(String s){
		try{
			return Float.parseFloat(s);
		}catch(Exception e){
			return 0.0f;
		}
		
	}
	
	private static String getExtension(String s){
		int dot = s.lastIndexOf(".");
		return s.substring(dot + 1);
	}
	
	public int countClient(){
		try{
			String sql = "SELECT count(*) FROM client";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			return rs.getInt(1);
		}catch(SQLException ex){
			System.out.println(ex);
		}
		return 0;
	}
	
	public int countEmployee(){
		try{
			String sql = "SELECT count(*) FROM client,personnel where personnel.assignment = client.name";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			return rs.getInt(1);
		}catch(SQLException ex){
			System.out.println(ex);
		}
		return 0;
	}
	
	public float[] getVariables(String client) throws Exception{
		Statement stmt = null;
		float variables[] = new float[12];
            try{
				String sql="Select rotVar, rnsdVar, lhRate, lhVar, lhOTVar," +
					"lhNSDVar, lhRDVar, shRate, shVar, shOTVar, shNSDVar," +
					"shRDVar FROM `client` where client.name = '"+client+"';";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()){
					for(int i = 1;i < 13; i++){
						if(rs.getString(i) == null || rs.getString(i) == "null"){
							throw new Exception("No variable\\s");
						}
					}
					for(int i = 1;i < 13; i++){
						variables[i-1] = rs.getFloat(i);
					}
				}
            } catch (Exception ex) {
				throw ex;
            }
		return variables;
	}
	
	public void modifyVariables(float variables[], String client) throws SQLException{
		String sql = "UPDATE client set " +
		"rotVar = "+variables[0]+", rnsdVar = " + variables[1] +
		", lhRate = " + variables[2] +
		", lhVar = "+variables[3] +", lhOTVar = " + variables[4] +
		", lhNSDVar =  " + variables[5] +", lhRDVar = " + variables[6] +
		", shRate = " + variables[7] +", shVar = " + variables[8] +
		", shOTVar = " + variables[9] +", shNSDVar = " + variables[10] +
		", shRDVar = " + variables[11] +" where name = '"+client+"';";
		Statement stmt = con.prepareStatement(sql);
		stmt.execute(sql);
		System.out.println("MODIFIED CLIENT VARIABLES!");
	}
	
	public float[] getTaxTable(int bracket){
		Statement stmt = null;
		float table[] = new float[8];
            try{
				String sql="Select tax, taxpercentover, z, sme, s1me1, s2me2, s3me3," + 
				"s4me4 from taxtable where bracket = "+bracket+";";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()){
					for(int i = 1;i < 9; i++){
						table[i-1] = rs.getFloat(i);
					}
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return table;
	}
	
	public float[][] getWholeTaxTable(){
		int[] brackets = getBracketListInArray();
		float[][] taxTable = new float[brackets.length][8];
		for(int i = 0; i < brackets.length; i++){
			taxTable[i] = getTaxTable(brackets[i]);
		}
		return taxTable;
	}
	
	public void updateTaxTable(int bracket, float[] table) throws SQLException{
		String sql = "UPDATE taxtable set " +
		"tax = "+table[0]+", taxpercentover = " + table[1] +
		", z = " + table[2] +
		", sme = "+table[3] +", s1me1 = " + table[4] +
		", s2me2 =  " + table[5] +", s3me3 = " + table[6] +
		", s4me4 = " + table[7] + " where bracket = "+bracket+";";
		Statement stmt = con.prepareStatement(sql);
		stmt.execute(sql);
		System.out.println("UPDATED TAX TABLE!");
	}
	
	public ArrayList<String> getBracketList(){
		Statement stmt = null;
		ArrayList<String> brackets = new ArrayList<>();
            
            try{
				String sql="Select bracket FROM taxtable order by bracket";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					brackets.add(rs.getString("bracket"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return brackets;
	}
	
	public int[] getBracketListInArray(){
		ArrayList<String> brackets = getBracketList();
		int bracket[] = new int[brackets.size()];
		for(int i = 0; i < brackets.size(); i++){
			bracket[i] = Integer.parseInt(brackets.get(i));
		}
		return bracket;
	}
	
	public String getClient(){
		return client;
	}
}