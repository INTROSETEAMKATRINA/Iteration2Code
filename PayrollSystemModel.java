/*******************************************************
	 *  Class name: PayrollSystemModel
 	 *  Inheritance:
	 *  Attributes: con, sdf, periodStartdate, client, taxStatus,
	 *				summaryReports
	 *  Methods:	PayrollSystemModel, setPeriodStartDate, addPersonnel,
	 *				addDTR, removePersonnel, getTableColumn, getPesonnelData, 
	 *				addAdjustment, removeAdjustment, checkPassword, changePassword,
	 *				generatePayslips, backupDate, restoreFromBackUp, 
	 *				nextTimePeriod, tryGetFloat, tryGetBigDecimal,
	 *				getExtension, countClient, countEmployee, getVariables,
	 *				modifyVariables, getTaxTable, getWholeTaxTable,
	 *				updateTaxTable, getBracketList, getBracketListInArray
	 *				getClientList, getPersonnelList, getAdjustmentsList, 
	 *				getDateListDTR, checkPeriodForDTR, checkPeriodForPayslips,
	 *				getDateListPayslips, getColumnName, getTableRow, 
	 *				getSummaryReport, getClient, generateSummaryReport
	 *  Functionality: Model
	 *  Visibility: public
	 *******************************************************/

import java.math.BigDecimal;
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

	public boolean addPersonnel(File fileDirectory, Date periodStartDate)
			throws Exception{
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
			BigDecimal sss, sssLoan, phic, hdmf, hdmfLoan, payrollAdvance,
					houseRental, uniformAndOthers;
			BigDecimal dailyRate, colaRate, monthlyRate;
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
					
					BigDecimal rates[] = new BigDecimal[3];
					for(int i = 0; i < 3;i++){
						column++;
						rates[i] = tryGetBigDecimal(
								sheet.getCell(column,row).getContents());
						if(rates[i].signum() < 0){
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
					
					BigDecimal deductions[] = new BigDecimal[8];
					
					for(int i = 0; i < 8;i++){
						column++;
						deductions[i] = tryGetBigDecimal(
								sheet.getCell(column,row).getContents());
						if(deductions[i].signum()<0){
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

					personnels.add(new Personnel(name, position, assignment,employeeStatus,
							tin, taxStatus, sss, sssLoan, phic, hdmf,hdmfLoan, payrollAdvance, houseRental,
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
            sql = "INSERT INTO `Payroll System`.`Client`\n" +
            "(`Name`)\n" +
            "VALUES\n" +
            "(\"" + assignment + "\");";
            stmt = con.prepareStatement(sql);
            stmt.execute(sql);

        }catch (SQLException ex) {
			if(ex.getErrorCode() != 1062){
				System.out.println(ex);
			}
        }

        for( Personnel personnel: personnels ){
            try{
                sql = "REPLACE INTO Personnel" +
                "(Name, Assignment, Position, " +
                "EmployeeStatus, DailyRate, ColaRate, " +
                "MonthlyRate, TIN, TaxStatus)" +
                "VALUES ('"+ personnel.getName() + "',"
                        + " '"+personnel.getAssignment() + "',"
                        + " '"+personnel.getPosition() + "',"
                        + " '"+personnel.getEmployeeStatus() + "',"
                        + " '"+personnel.getDailyRate() + "',"
                        + " '"+personnel.getColaRate() + "',"
                        + " '"+personnel.getMonthlyRate() + "',"
                        + " '"+personnel.getTIN() + "',"
                        + " '"+personnel.getTaxStatus() + "');";
                stmt = con.prepareStatement(sql);
                stmt.execute(sql);
				String pTIN = personnel.getTIN();
				
                if(personnel.getSSS().signum() != 0){
                    this.addAdjustment("SSS", personnel.getSSS(), 
                    		pTIN, periodStartDate);
                }
                if(personnel.getSSSLoan().signum() != 0){                
                    this.addAdjustment("SSS Loan", personnel.getSSSLoan(), 
                    		pTIN, periodStartDate);
                }
                if(personnel.getPHIC().signum() != 0){
                    this.addAdjustment("PHIC", personnel.getPHIC(), 
                    		pTIN, periodStartDate);
                }
                if(personnel.getHDMF().signum() != 0){
                    this.addAdjustment("HDMF", personnel.getHDMF(), 
                    		pTIN, periodStartDate);
                }
                if(personnel.getHDMFLoan().signum() != 0){
                    this.addAdjustment("HDMF Loan", personnel.getHDMFLoan(),
                    		pTIN, periodStartDate);
                }
                if(personnel.getPayrollAdvance().signum() != 0){
                    this.addAdjustment("Payroll Advance", personnel.getPayrollAdvance(),
                    		pTIN, periodStartDate);
                }
                if(personnel.getHouseRental().signum() != 0){
                    this.addAdjustment("House Rental", personnel.getHouseRental(),
                    		pTIN, periodStartDate);
                }
                if(personnel.getUniformAndOthers().signum() != 0){
                    this.addAdjustment("Uniform and Others", personnel.getUniformAndOthers(),
                    		pTIN, periodStartDate);
                }
            } catch (SQLException ex){
				System.out.println(ex);
            }
        }
		this.client = assignment;
		return true;
	}
	
	public void removePersonnel(String client) throws Exception{
		try{
			String sql = "DELETE From client WHERE name = '" + client + "';";
			Statement st = con.createStatement();
			st.execute(sql);
		}catch(Exception ex){
			System.out.println(ex);
			throw new Exception(ex);
		}
	}
	
	public void removePersonnel(String client, String personnel, String TIN)
			throws Exception{
		try{
			String sql = "DELETE From personnel WHERE name = '" + personnel +
						 "' AND assignment = '" + client + "' AND TIN = '" + TIN + "';";
			Statement st = con.createStatement();
			st.execute(sql);
		}catch(Exception ex){
			System.out.println(ex);
			throw new Exception(ex);
		}
	}
	
	public ArrayList<String> getTableColumn(String tableName){
		ArrayList<String> columnName = new ArrayList<String>();
		
		try{
			String sql = "Select COLUMN_NAME name From INFORMATION_SCHEMA.COLUMNS " +
					"WHERE TABLE_NAME = '" + tableName + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				columnName.add(rs.getString("name"));
			}
        } catch (Exception ex) {
			System.out.println(ex);
        }
		return columnName;
	}
	
	public ArrayList<Object[]> getPesonnelData(String client) throws Exception{
		ArrayList<String> columnName = getTableColumn("personnel");
		ArrayList<Object[]> rowData = new ArrayList<>();
		int num = 1;
		
		try{
			String sql = "Select * FROM `personnel` where assignment = '" + client + 
					"' order by name";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				ArrayList<String> data = new ArrayList<String>();
				data.add(Integer.toString(num));
				num++;
				for(String name:columnName){
					data.add(rs.getString(name));
				}
				rowData.add(data.toArray());
			}
        }catch (Exception ex){
			System.out.println(ex);
        }
		if(rowData.size() == 0){
			throw new Exception("No Data Found!");
		}
		return rowData;
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
			BigDecimal regularDaysWorks, regularOvertime, regularNightShiftDifferential,
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
					
					BigDecimal timeWorked[] = new BigDecimal[12];
					
					for(int i = 0; i < 12;i++){
						column++;
						timeWorked[i] = tryGetBigDecimal(
								sheet.getCell(column,row).getContents());
						if(timeWorked[i].signum() < 0){
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
									
				    dtrs.add(new DTR(name, tin, regularDaysWorks, regularOvertime, 
				    		regularNightShiftDifferential, specialHoliday, specialHolidayOvertime,
				    		specialHolidayNightShiftDifferential, legalHoliday, legalHolidayOvertime,
				    		legalHolidayNightShiftDifferential, legalHolidayOnRestDay, 
				    		specialHolidayOnRestDay, late, periodStartDate));
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
			stmt = con.prepareStatement(sql);
			stmt.execute(sql);
        }catch(SQLException ex){
			System.out.println(ex);
		}
			for(DTR dtr : dtrs){
                try{
                    sql = "REPLACE INTO `Payroll System`.`DTR` " +
                    "(`RDW`, `ROT`, `RNSD`, `SH`, `SHOT`, " +
                    "`SHNSD`, `LH`, `LHOT`, `LHNSD`, " +
                    "`PeriodStartDate`, `LHRD`, `SHRD`, " +
					"`late`, `TIN`) VALUES " +
                    "('" + dtr.getRegularDaysWorks() + "', " +
                    "'" + dtr.getRegularOvertime() + "', " +
                    "'" + dtr.getRegularNightShiftDifferential() + "', " +
                    "'" + dtr.getSpecialHoliday() + "', " +
                    "'" + dtr.getSpecialHolidayOvertime() + "', " +
                    "'" + dtr.getSpecialHolidayNightShiftDifferential() + "', " +
                    "'" + dtr.getLegalHoliday() + "', " +
                    "'" + dtr.getLegalHolidayOvertime() + "', " +
                    "'" + dtr.getLegalHolidayNightShiftDifferential() + "', " +
                    "'" + sdf.format(dtr.getPeriodStartDate()) + "', " +
					"'" + dtr.getLegalHolidayOnRestDay() + "', " +
					"'" + dtr.getSpecialHolidayOnRestDay() + "', " +
					"'" + dtr.getLate() + "', " +
                    "'" + dtr.getTIN() + "');";
                    stmt = con.prepareStatement(sql);
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

	public void addAdjustment(String reason, BigDecimal adjustment, String tin, Date periodStartDate) {
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

	public void removeAdjustment(String reason, BigDecimal adjustment, String tin, Date periodStartDate) {
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
			ResultSet rs = st.executeQuery("select password from password" +
					" where password = '" + pass + "'");
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
			x = st.executeUpdate("update password set password = '" +
					newPass + "' where password = '" + oldPass + "'");
		}catch(Exception e){
			System.out.println(e);
		}
		return x;
	}

	public int generatePayslips(File directory, String client, String psd, int minWage){
		Statement stmt = null;
		ArrayList<Payslip> payslips = new ArrayList<>();
            
        try{
			String sql = "Select * FROM `client`,`dtr`,`personnel` where client.name = '"+
					client + "' and personnel.assignment = client.name " + 
					" and dtr.tin = personnel.tin and dtr.periodstartdate = '" +
					psd + "' order by personnel.name";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			BigDecimal rotVar = new BigDecimal("1.25");
			BigDecimal rnsdVar = new BigDecimal(".10");
			BigDecimal lhRate = new BigDecimal("30");
			BigDecimal lhVar = new BigDecimal("1.00");
			BigDecimal lhOTVar = new BigDecimal("1.30");
			BigDecimal lhNSDVar = new BigDecimal(".10");
			BigDecimal lhRDVar = new BigDecimal("2.60");
			BigDecimal shRate = new BigDecimal("30");
			BigDecimal shVar = new BigDecimal(".30");
			BigDecimal shOTVar = new BigDecimal(".09");
			BigDecimal shNSDVar = new BigDecimal(".03");
			BigDecimal shRDVar = new BigDecimal("1.5");
			try{
				BigDecimal[] vars= getVariables(client);
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
				
			BigDecimal[][] wholeTable = getWholeTaxTable();
			while(rs.next()){
				String tin = rs.getString("TIN");
					
				sql = "Select * FROM `adjustmentsanddeductions`,`personnel` " +
						"where personnel.tin = adjustmentsanddeductions.tin" +
						" and personnel.tin = '" + tin + "' and periodstartdate = '" +
						psd + "'";
				st = con.createStatement();
				ResultSet rs2 = st.executeQuery(sql);
					
				String assignment = rs.getString("assignment");
				String name = rs.getString("personnel.name");
				Date periodStartDate = rs.getDate("PeriodStartDate");
				String position = rs.getString("Position");
				BigDecimal regularDaysWork = new BigDecimal(rs.getString("RDW"));
				BigDecimal dailyRate = new BigDecimal(rs.getString("DailyRate"));
				BigDecimal late = new BigDecimal(rs.getString("late"));
				BigDecimal regularOvertime = new BigDecimal(rs.getString("ROT"));
				BigDecimal regularNightShiftDifferential = 
						new BigDecimal(rs.getString("RNSD"));
				BigDecimal legalHoliday = new BigDecimal(rs.getString("LH"));
				BigDecimal legalHolidayOvertime = new BigDecimal(rs.getString("LHOT"));
				BigDecimal legalHolidayNightShiftDifferential = 
						new BigDecimal(rs.getString("LHNSD"));
				BigDecimal legalHolidayOnRestDay = new BigDecimal(rs.getString("LHRD"));
				BigDecimal specialHoliday = new BigDecimal(rs.getString("SH"));
				BigDecimal specialHolidayOvertime = new BigDecimal(rs.getString("SHOT"));
				BigDecimal specialHolidayNightShiftDifferential =
						new BigDecimal(rs.getString("SHNSD"));
				BigDecimal specialHolidayOnRestDay = new BigDecimal(rs.getString("SHRD"));
				BigDecimal colaRate = new BigDecimal(rs.getString("ColaRate"));
				BigDecimal sss = BigDecimal.ZERO;
				BigDecimal phic = BigDecimal.ZERO;
				BigDecimal hdmf = BigDecimal.ZERO;
				BigDecimal sssLoan = BigDecimal.ZERO;
				BigDecimal hdmfLoan = BigDecimal.ZERO;
				BigDecimal payrollAdvance = BigDecimal.ZERO;
				BigDecimal houseRental = BigDecimal.ZERO;
				BigDecimal uniformAndOthers = BigDecimal.ZERO;	
				BigDecimal adjustments = BigDecimal.ZERO;
				BigDecimal transpoAllow = BigDecimal.ZERO;
				String type;
				String taxStatus = rs.getString("taxstatus");
				taxStatus =  taxStatus == null || taxStatus.equals("") ? "s" : taxStatus;
				
				while(rs2.next()){
					type = rs2.getString("type");
					
					switch(type){
						case "SSS":
							sss = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "PHIC":
							phic = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "SSS Loan":
							sssLoan = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "HDMF":
							hdmf = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "HDMF Loan":
							hdmfLoan = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "Payroll Advance":
							payrollAdvance = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "House Rental":
							houseRental = new BigDecimal(rs2.getString("amount"));
							break;
							
						case "Uniform and Others":
							uniformAndOthers = new BigDecimal(rs2.getString("amount"));
							break;
							
						default:
							adjustments = adjustments.add(new BigDecimal(rs2.getString("amount")));
							break;
						}
					}
					
					BigDecimal totalDeductions = sss.add(phic).add(sssLoan).add(hdmf);
					totalDeductions = totalDeductions.add(hdmfLoan).add(payrollAdvance);
					totalDeductions = totalDeductions.add(houseRental).add(uniformAndOthers);
					BigDecimal eight = new BigDecimal("8");
					BigDecimal hourlyRate = dailyRate.
							divide(eight, 10, BigDecimal.ROUND_HALF_UP);
					BigDecimal shHourlyRate = dailyRate.add(shRate).
							divide(eight, 10, BigDecimal.ROUND_HALF_UP);
					BigDecimal lhHourlyRate = dailyRate.add(lhRate).
							divide(eight, 10, BigDecimal.ROUND_HALF_UP);
					BigDecimal basicPay = regularDaysWork.multiply(dailyRate);
					BigDecimal deductionFromTardiness = dailyRate.
							divide(eight, 10, BigDecimal.ROUND_HALF_UP).
							divide(new BigDecimal("60"), 10, BigDecimal.ROUND_HALF_UP).
							multiply(late);
					BigDecimal colaAllowance = colaRate.multiply(regularDaysWork);
					BigDecimal regularPay = basicPay.add(colaAllowance).
							subtract(deductionFromTardiness);
					BigDecimal regularOvertimePay = regularOvertime.multiply(rotVar).
							multiply(hourlyRate);
					BigDecimal regularNightShiftDifferentialPay = 
							regularNightShiftDifferential.multiply(rnsdVar).multiply(hourlyRate);
					BigDecimal legalHolidayPay = legalHoliday.multiply(lhVar).
							multiply(lhHourlyRate);
					BigDecimal legalHolidayOvertimePay = legalHolidayOvertime.multiply(lhOTVar).
							multiply(lhHourlyRate);
					BigDecimal legalHolidayNightShiftDifferentialPay =
							legalHolidayNightShiftDifferential.multiply(lhNSDVar).
							multiply(lhHourlyRate);
					BigDecimal legalHolidayOnRestDayPay = legalHolidayOnRestDay.
							multiply(lhRDVar).multiply(lhHourlyRate);
					BigDecimal specialHolidayPay = specialHoliday.multiply(shVar).
							multiply(shHourlyRate);
					BigDecimal specialHolidayOvertimePay = specialHolidayOvertime.
							multiply(shOTVar).multiply(shHourlyRate);
					BigDecimal specialHolidayNightShiftDifferentialPay = 
							specialHolidayNightShiftDifferential.multiply(shNSDVar).multiply(shHourlyRate);
					BigDecimal specialHolidayOnRestDayPay = 
							specialHolidayOnRestDay.multiply(shHourlyRate).multiply(shRDVar);
					BigDecimal wTax = BigDecimal.ZERO;
					BigDecimal otPay = regularOvertimePay.add(legalHolidayOvertimePay).
							add(specialHolidayOvertimePay);
					BigDecimal nsdPay = regularNightShiftDifferentialPay.
							add(legalHolidayNightShiftDifferentialPay).
							add(specialHolidayNightShiftDifferentialPay);
					BigDecimal grossPay = regularPay.add(legalHolidayPay).
							add(specialHolidayPay).add(otPay).add(nsdPay);
					grossPay = grossPay.add(adjustments).
							add(legalHolidayOnRestDayPay).add(specialHolidayOnRestDayPay);
					BigDecimal netPay = grossPay.subtract(totalDeductions);
					int taxStatusIndex = 0;
					
					if(dailyRate.compareTo(new BigDecimal(minWage + "")) >= 1){
						for(int i = 0 ; i < this.taxStatus.length; i++){
							if(this.taxStatus[i].toLowerCase().contains(taxStatus)){
								taxStatusIndex = i;
								break;
							}
						}
						
						int bracket = 0;
						
						for(int i = 0 ; i < wholeTable.length; i++){
							if(wholeTable[i][taxStatusIndex+2].compareTo(netPay) >= 1){
								bracket = i - 1;
								break;
							}else if(i == wholeTable.length - 1){
								bracket = i;
							}
						}
					wTax = wholeTable[bracket][0].add(wholeTable[bracket][1].
							multiply(netPay.subtract(wholeTable[bracket][taxStatusIndex+2])).
							divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
					}
					netPay = netPay.subtract(wTax);
					payslips.add(new Payslip(tin, assignment, name, periodStartDate,
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
				"('" + p.getAssignment() + "'," + "'" + p.getName() + "'," + 
				"'" + sdf.format(p.getPeriodStartDate()) + "'," + "'" + p.getTIN() + "'," + 
				"'" + p.getPosition() + "', " + p.getRegularDaysWork() + ", " + 
				p.getDailyRate() + ", " + p.getGrossPay() + ", " + 
				p.getLate() + ", " + p.getRegularPay() + ", " + 
				p.getRegularOvertime() + ", " + p.getRegularOvertimePay() + ", " + 
				p.getRegularNightShiftDifferential() + ", " + 
				p.getRegularNightShiftDifferentialPay() + ", " + 
				p.getLegalHoliday() + ", " + p.getLegalHolidayPay() + ", " + 
				p.getLegalHolidayOvertime() + ", " + p.getLegalHolidayOvertimePay() + ", " + 
				p.getLegalHolidayNightShiftDifferential() + "," + 
				p.getLegalHolidayNightShiftDifferentialPay() + ", " + 
				p.getLegalHolidayOnRestDay() + ", " + p.getLegalHolidayOnRestDayPay() + ", " + 
				p.getSpecialHoliday() + ", " + p.getSpecialHolidayPay() + ", " + 
				p.getSpecialHolidayOvertime() + ", " + 
				p.getSpecialHolidayOvertimePay() + ", " + 
				p.getSpecialHolidayNightShiftDifferential() + ", " + 
				p.getSpecialHolidayNightShiftDifferentialPay() + ", " + 
				p.getSpecialHolidayOnRestDay() + ", " + p.getSpecialHolidayOnRestDayPay() + 
				", " + 0 + ", " + p.getAdjustments() + ", " + 
				p.getWTax() + ", " + p.getSSS() + ", " + p.getPHIC() + ", " +
				p.getHDMF() + ", " + p.getSSSLoan() + ", " + 0 + ", " + 
				p.getPayrollAdvance() + ", " + p.getHouseRental() + ", " + 
				p.getUniformAndOthers() + ", " + p.getNetPay() + ");";
				//Transpo Allow and Savings are set to 0 since I still don't know where it comes from.
				stmt=con.prepareStatement(sql);
				stmt.execute(sql);
			}catch(SQLException ex){
				System.out.println(ex);
			}
			
			writer.print("\"888 GALLANT MANPOWER AND MANAGEMENT SERVICES INCORPORATED\"" +
					",,,,,,,,,\"" + p.getAssignment() + "\"");
			writer.println(",,\"888 GALLANT MANPOWER AND MANAGEMENT SERVICES INCORPORATED\"" +
					",,,,,,,,,\"" + p.getAssignment() + "\",");
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
			
			writer.print("\"" + p.getName() + "\"");
			writer.print(",,,");
			writer.print("\"" + p.getPosition() + "\"" + ",,,,,,,,");
			writer.print("\"" + p.getName() + "\"");
			writer.print(",,,");
			writer.println("\"" + p.getPosition() + "\"");
			
			writer.println();
			ArrayList<String> toBePrinted = new ArrayList<String>();
			toBePrinted.add("Days Worked");
			toBePrinted.add(p.getRegularDaysWork().toString());
			toBePrinted.add("L/H NSD Hrs");
			toBePrinted.add(p.getLegalHolidayNightShiftDifferential().toString());
			toBePrinted.add("W/Tax");
			toBePrinted.add(p.getWTax().toString());
			toBePrinted.add("Gross Pay");
			toBePrinted.add(p.getGrossPay().toString());
			toBePrinted.add("W/Tax");
			toBePrinted.add(p.getWTax().toString());

			toBePrinted.add("Daily Rate");
			toBePrinted.add(p.getDailyRate().toString());
			toBePrinted.add("L/H NSD Pay");
			toBePrinted.add(p.getLegalHolidayNightShiftDifferentialPay().toString());
			toBePrinted.add("SSS");
			toBePrinted.add(p.getSSS().toString());
			toBePrinted.add("Tardiness");
			toBePrinted.add(p.getLate().multiply(p.getDailyRate().
					divide(new BigDecimal("8"), 10, BigDecimal.ROUND_HALF_UP).
					divide(new BigDecimal("60"), 10, BigDecimal.ROUND_HALF_UP)).setScale(2,
							BigDecimal.ROUND_HALF_UP).toString());
			toBePrinted.add("SSS");
			toBePrinted.add(p.getSSS().toString());

			toBePrinted.add("Gross Pay");
			toBePrinted.add(p.getGrossPay().toString());
			toBePrinted.add("SH/RD Worked");
			toBePrinted.add(p.getSpecialHoliday().toString());
			toBePrinted.add("PHIC");
			toBePrinted.add(p.getPHIC().toString());
			toBePrinted.add("Regular OT");
			toBePrinted.add(p.getRegularOvertime().toString());
			toBePrinted.add("PHIC");
			toBePrinted.add(p.getPHIC().toString());

			toBePrinted.add("Late");
			toBePrinted.add(p.getLate().toString());
			toBePrinted.add("SH/RD Pay");
			toBePrinted.add(p.getSpecialHolidayPay().toString());
			toBePrinted.add("HDMF");
			toBePrinted.add(p.getHDMF().toString());
			toBePrinted.add("NSD Pay");
			toBePrinted.add(p.getRegularNightShiftDifferentialPay().toString());
			toBePrinted.add("HDMF");
			toBePrinted.add(p.getHDMF().toString());

			toBePrinted.add("Tardiness");
			toBePrinted.add(p.getLate().multiply(p.getDailyRate().
					divide(new BigDecimal("8"), 10, BigDecimal.ROUND_HALF_UP).
					divide(new BigDecimal("60"), 10, BigDecimal.ROUND_HALF_UP)).setScale(2,
							BigDecimal.ROUND_HALF_UP).toString());
			toBePrinted.add("SH on RD Hrs");
			toBePrinted.add(p.getSpecialHolidayOnRestDay().toString());
			toBePrinted.add("SSS Loan");
			toBePrinted.add(p.getSSSLoan().toString());
			toBePrinted.add("Legal Holiday Pay");
			toBePrinted.add(p.getLegalHolidayPay().toString());
			toBePrinted.add("SSS Loan");
			toBePrinted.add(p.getSSSLoan().toString());

			toBePrinted.add("Regular Pay");
			toBePrinted.add(p.getRegularPay().toString());
			toBePrinted.add("SH on RD Pay");
			toBePrinted.add(p.getSpecialHolidayOnRestDayPay().toString());
			toBePrinted.add("HDMF Loan");
			toBePrinted.add(p.getHDMFLoan().toString());
			toBePrinted.add("L/H on RD Pay");
			toBePrinted.add(p.getLegalHolidayOnRestDayPay().toString());
			toBePrinted.add("HDMF Loan");
			toBePrinted.add(p.getHDMFLoan().toString());

			toBePrinted.add("OT Hour");
			toBePrinted.add(p.getRegularOvertime().toString());
			toBePrinted.add("SH/RD OT Hrs");
			toBePrinted.add(p.getSpecialHolidayOvertime().toString());
			toBePrinted.add("Payroll Advance");
			toBePrinted.add(p.getPayrollAdvance().toString());
			toBePrinted.add("L/H OT Pay");
			toBePrinted.add(p.getLegalHolidayOvertimePay().toString());
			toBePrinted.add("Payroll Advance");
			toBePrinted.add(p.getPayrollAdvance().toString());

			toBePrinted.add("OT Pay");
			toBePrinted.add(p.getRegularOvertimePay().toString());
			toBePrinted.add("SH/RD OT Pay");
			toBePrinted.add(p.getSpecialHolidayOvertimePay().toString());
			toBePrinted.add("House Rental");
			toBePrinted.add(p.getHouseRental().toString());
			toBePrinted.add("L/H NSD Pay");
			toBePrinted.add(p.getLegalHolidayNightShiftDifferentialPay().toString());
			toBePrinted.add("House Rental");
			toBePrinted.add(p.getHouseRental().toString());

			toBePrinted.add("NSD Hour");
			toBePrinted.add(p.getRegularNightShiftDifferential().toString());
			toBePrinted.add("SH/RD NSD Hrs");
			toBePrinted.add(p.getSpecialHolidayNightShiftDifferential().toString());
			toBePrinted.add("Uniform & Others");
			toBePrinted.add(p.getUniformAndOthers().toString());
			toBePrinted.add("SH RD Pay");
			toBePrinted.add(p.getSpecialHolidayOnRestDayPay().toString());
			toBePrinted.add("Uniform & Others");
			toBePrinted.add(p.getUniformAndOthers().toString());

			toBePrinted.add("NSD Pay");
			toBePrinted.add(p.getRegularNightShiftDifferentialPay().toString());
			toBePrinted.add("SH/RD NSD Pay");
			toBePrinted.add(p.getSpecialHolidayNightShiftDifferentialPay().toString());
			toBePrinted.add("Total Deductions");
			toBePrinted.add(p.getTotalDeductions().toString());
			toBePrinted.add("SH on RD Pay");
			toBePrinted.add(p.getSpecialHolidayOnRestDayPay().toString());
			toBePrinted.add("Total Deductions");
			toBePrinted.add(p.getTotalDeductions().toString());
			
			for(int i = 0 ; i < toBePrinted.size(); i += 10){
				writer.print("\"" + toBePrinted.get(i)+"\",,\"" + 
						toBePrinted.get(i+1) + "\"");
				writer.print(",\"" + toBePrinted.get(i+2) +"\",,\"" + 
						toBePrinted.get(i+3) +"\"");
				writer.print(",\"" + toBePrinted.get(i+4)+"\",,\"" +
						toBePrinted.get(i+5)  +"\"" );
				writer.print(",,");
				writer.print(",\"" + toBePrinted.get(i+6)+"\",,\"" +
						toBePrinted.get(i+7) + "\"" );
				writer.println(",\"" + toBePrinted.get(i+8) + "\",,\"" + 
						toBePrinted.get(i+9) + "\"" );
			}
			writer.print("\"L/H Hrs Worked\",," + 
					p.getLegalHoliday().toString());
			writer.println(",,,,,,,,,\"SH/RD OT Pay\",," + "\"" + 
					p.getSpecialHolidayOvertimePay().toString()+"\"");
			
			writer.print("\"L/H Pay\",," + "\"" + 
					p.getLegalHolidayPay().toString() + "\"");
			writer.println(",,,,,,,,,\"SH/RD NSD Pay\",," + "\"" + 
					p.getSpecialHolidayNightShiftDifferentialPay().toString()+"\"" );
			
			writer.print("\"L/H on RD Hrs Worked\",," + 
					p.getLegalHolidayOnRestDay().toString());
			writer.print(",\"Adjustments\",," + "\"" +
					p.getAdjustments().toString()+"\"");
			writer.print(",,,,,");
			writer.println(",\"Adjustments\",," + "\"" +
					p.getAdjustments().toString()+"\"" );
			
			writer.println("\"L/H on RD Pay\",," + "\"" + 
					p.getLegalHolidayOnRestDay().toString()+"\"");
			
			writer.print("\"L/H OT Hrs\",," + p.getLegalHolidayOvertime().toString());
			writer.print(",\"Gross Pay\",," +"\""+p.getGrossPay().toString()+"\"");
			writer.print(",\"Net Pay\",,"+"\""+p.getNetPay().toString()+"\"");
			writer.print(",,");
			writer.print(",\"Gross Pay\",,"+"\""+p.getGrossPay().toString()+"\"");
			writer.println(",\"Net Pay\",,"+"\""+p.getNetPay().toString()+"\"");
			
			writer.println("\"L/H on OT Pay\",," + "\"" + 
					p.getLegalHolidayOvertimePay().toString() + "\"");
			
			writer.println();
			
			writer.println("\"I acknowledge to have received the amount stated above" +
					" and have no further claims for services rendered.\"");
			
			writer.println();
			writer.println("\"___________________________\"" + ",,,,," + 
					"\"___________________________\"");
			
			writer.println(",\"Signature\"" + ",,,,," + "\"Date\"");		
			
			writer.println();
			writer.println();
		}
		writer.close();
		return 0;
	}

	public void backupData(File directory) throws Exception{
        ProcessBuilder pb = new ProcessBuilder("mysqldump", "-uroot", "-pp@ssword",
        		"Payroll System", "adjustmentsanddeductions", "client", "dtr", 
        		"payslip", "personnel", "taxtable");
            
        pb.redirectOutput(directory);
    }
        
    public void restoreFromBackUp(File directory) throws Exception{
        ProcessBuilder pb = new ProcessBuilder("mysql", "-uroot", "-pp@ssword", 
        		"Payroll System");
            
        pb.redirectInput(directory);
    }

	public ArrayList<String> getClientList(){
		ArrayList<String> clients = new ArrayList<>();
            
		try{
			String sql = "Select * FROM `client` order by name";
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
		ArrayList<String> personnel = new ArrayList<>();
            
            try{
				String sql="Select Name, TIN FROM `personnel` where assignment = '" + 
						client + "' order by name";
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
		ArrayList<String> adjustments = new ArrayList<>();
            
            try{
				String sql="Select * FROM `adjustmentsanddeductions` where `tin` = '" +
						tin + "' and `periodstartdate` = '" + sdf.format(periodStartDate) + "'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					adjustments.add(rs.getString("type") + " ~ " + rs.getString("amount"));
				}
            } catch (Exception ex) {
				System.out.println(ex);
            }
		return adjustments;
	}
	
	public ArrayList<String> getDateListDTR(String client){
		ArrayList<String> dates = new ArrayList<>();
            
		try{
			String sql = "Select distinct periodstartdate " +
					"FROM `client`,`dtr`,`personnel` where client.name = '" +
					client+"' and personnel.assignment = client.name "
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
		try{
			String sql = "Select * FROM `client`,`dtr`,`personnel` where client.name = '"
					+ client + "' and personnel.assignment = client.name " + 
					" and dtr.tin = personnel.tin and dtr.periodstartdate = '" + psd + "'";
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
		try{
			String sql = "Select * FROM `client`,`payslip`,`personnel` " +
					"where client.name = '" + client + "' and" +
					" personnel.assignment = client.name " + 
					" and payslip.tin = personnel.tin and" +
					" payslip.periodstartdate = '" + psd + "'";
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
	
	public ArrayList<String> getDateListPayslips(String client){
		ArrayList<String> dates = new ArrayList<>();
            
		try{
			String sql = "Select distinct periodstartdate FROM `payslip` " +
					"where assignment = '" + client + "' order by periodstartdate";
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
			column.add("Seq No.");
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
			
		try{
			String sql = "select distinct name, tin from `payslip` " +
					"where assignment = '" + client + "' and PeriodStartDate = '" +
					date + "' order by name";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSet rs2;
			if(rs.first()){
				String name,position;
				String tin;
				int j = 1;
				int TRDW = 0, TRegularPay = 0, TROT = 0, TROTPay = 0, TRNSD = 0, TRNSDPay = 0,
						TLH = 0, TLHPay = 0, TLHOT = 0, TLHOTPay = 0, TLHNSD = 0, TLHNSDPay = 0,
						TSH = 0, TSHPay = 0, TSHOT = 0, TSHOTPay = 0, TSHNSD = 0, TSHNSDPay = 0,
						TLHRD = 0, TSHRD = 0, TAdjustments = 0,
						TGrossPay = 0, TSSS = 0, TPHIC = 0, THDMF = 0, TSSSLoan = 0,
						TPayrollAdvance = 0, THouseRental = 0, TUniformAndOthers = 0,
						TSavings = 0, TNetPay = 0;
					
				do{
					tin = rs.getString("tin");
					name = rs.getString("name");
					sql = "select * from `payslip` where tin = '" + tin + 
							"' and PeriodStartDate = '" + date + "';";
					st = con.createStatement();
					rs2 = st.executeQuery(sql);
						
					position = "";
					int RDW = 0,DailyRate = 0, TranspoAllow = 0, RegularPay = 0, ROT = 0, 
							ROTPay = 0, RNSD = 0, RNSDPay = 0, LH = 0, LHPay = 0, LHOT = 0,
							LHOTPay = 0, LHNSD = 0, LHNSDPay = 0, SH = 0, SHPay = 0, SHOT = 0,
							SHOTPay = 0, SHNSD = 0, SHNSDPay = 0, LHRD = 0, SHRD = 0,
							Adjustments = 0, GrossPay = 0, SSS = 0,PHIC = 0, HDMF = 0,
							SSSLoan = 0, PayrollAdvance = 0, HouseRental = 0, UniformAndOthers = 0,
							Savings = 0, NetPay = 0;
					if(rs2.next()){
						position = rs2.getString("position");
						RDW += rs2.getInt("RDW"); DailyRate += rs2.getInt("DailyRate");
						TranspoAllow += rs2.getInt("TranspoAllow");
						RegularPay += rs2.getInt("RegularPay"); ROT += rs2.getInt("ROT");
						ROTPay += rs2.getInt("ROTPay"); RNSD += rs2.getInt("RNSD");
						RNSDPay += rs2.getInt("RNSDPay"); LH += rs2.getInt("LH"); 
						LHPay += rs2.getInt("LHPay"); LHOT += rs2.getInt("LHOT");
						LHOTPay += rs2.getInt("LHOTPay"); LHNSD += rs2.getInt("LHNSD");
						LHNSDPay += rs2.getInt("LHNSDPay"); SH += rs2.getInt("SH"); 
						SHPay += rs2.getInt("SHPay");
						SHOT += rs2.getInt("SHOT"); SHOTPay += rs2.getInt("SHOTPay"); 
						SHNSD += rs2.getInt("SHNSD"); SHNSDPay += rs2.getInt("SHNSDPay");
						Adjustments += rs2.getInt("Adjustments"); GrossPay += rs2.getInt("GrossPay"); 
						SSS += rs2.getInt("SSS"); PHIC += rs2.getInt("PHIC");
						HDMF += rs2.getInt("HDMF"); SSSLoan += rs2.getInt("SSSLoan"); 
						PayrollAdvance += rs2.getInt("PayrollAdvance"); 
						HouseRental += rs2.getInt("HouseRental");
						UniformAndOthers += rs2.getInt("UniformAndOthers"); 
						Savings += rs2.getInt("Savings"); NetPay += rs2.getInt("NetPay");
						LHRD += rs2.getInt("LHRD");
						SHRD += rs2.getInt("SHRD");
						TRDW += rs2.getInt("RDW"); TranspoAllow += rs2.getInt("TranspoAllow");
						TRegularPay += rs2.getInt("RegularPay"); TROT += rs2.getInt("ROT"); 
						TROTPay += rs2.getInt("ROTPay"); TRNSD += rs2.getInt("RNSD");
						TRNSDPay += rs2.getInt("RNSDPay"); TLH += rs2.getInt("LH"); 
						TLHPay += rs2.getInt("LHPay"); TLHOT += rs2.getInt("LHOT");
						TLHOTPay += rs2.getInt("LHOTPay"); TLHNSD += rs2.getInt("LHNSD");
						TLHNSDPay += rs2.getInt("LHNSDPay"); TSH += rs2.getInt("SH"); 
						TSHPay += rs2.getInt("SHPay");
						TSHOT += rs2.getInt("SHOT"); TSHOTPay += rs2.getInt("SHOTPay"); 
						TSHNSD += rs2.getInt("SHNSD"); TSHNSDPay += rs2.getInt("SHNSDPay");
						TAdjustments += rs2.getInt("Adjustments"); 
						TGrossPay += rs2.getInt("GrossPay");
						TSSS += rs2.getInt("SSS"); TPHIC += rs2.getInt("PHIC");
						THDMF += rs2.getInt("HDMF"); TSSSLoan += rs2.getInt("SSSLoan");
						TPayrollAdvance += rs2.getInt("PayrollAdvance"); 
						THouseRental += rs2.getInt("HouseRental");
						TUniformAndOthers += rs2.getInt("UniformAndOthers"); 
						TSavings += rs2.getInt("Savings"); TNetPay += rs2.getInt("NetPay");
						TLHRD += rs2.getInt("LHRD");
						TSHRD += rs2.getInt("SHRD"); 
		    	 	}
						
					if(report.equals(getSummaryReport(0))){
						Object[] data1 = {j,name,position,RDW,ROT,RNSD,LH,LHOT,
								LHNSD,SH,SHOT,SHNSD,LHRD,SHRD};
						row.add(data1);
					}else if(report.equals(getSummaryReport(1))){
						Object[] data1 = {j,name,
						position,RDW,DailyRate,TranspoAllow,RegularPay,
						ROT,ROTPay,RNSD,RNSDPay,LH,LHPay,LHOT,LHOTPay,LHNSD,LHNSDPay,
						SH,SHPay,SHOT,SHOTPay,SHNSD,SHNSDPay,Adjustments,GrossPay,
						SSS,PHIC,HDMF,SSSLoan,PayrollAdvance,HouseRental,UniformAndOthers,
						Savings,NetPay};
						
						row.add(data1);
					}else if(report.equals(getSummaryReport(2))){
						Object[] data1 = {j,name,GrossPay,SSS,PHIC,HDMF,SSSLoan,HouseRental,
								Savings,0,NetPay,0,0,0,0};
						row.add(data1);
					}else if(report.equals(getSummaryReport(3))){
						Object[] data1 = {j,name,
						position,RDW,DailyRate,TranspoAllow,RegularPay,
						ROT,ROTPay,RNSD,RNSDPay,LH,LHPay,LHOT,LHOTPay,LHNSD,LHNSDPay,
						SH,SHPay,SHOT,SHOTPay,SHNSD,SHNSDPay,Adjustments,GrossPay,
						SSS,PHIC,HDMF,SSSLoan,PayrollAdvance,HouseRental,UniformAndOthers,
						Savings,NetPay};
						row.add(data1);
					}
					j++;
				}while(rs.next());
					
				if(report.equals(getSummaryReport(0))){
					Object[] data1 = {j,"Total"," ",TRDW,TROT,TRNSD,TLH,TLHOT,TLHNSD,TSH,
							TSHOT,TSHNSD,TLHRD,TSHRD};
					row.add(data1);
				}else if(report.equals(getSummaryReport(1))){
					Object[] data1 = {j,"TOTAL", "",TRDW,0,0,TRegularPay,
							TROT,TROTPay,TRNSD,TRNSDPay,TLH,TLHPay,TLHOT,TLHOTPay,TLHNSD,
							TLHNSDPay, TSH,TSHPay,TSHOT,TSHOTPay,TSHNSD,TSHNSDPay,TAdjustments,
							TGrossPay, TSSS,TPHIC,THDMF,TSSSLoan,TPayrollAdvance,THouseRental,
							TUniformAndOthers,TSavings,TNetPay};
					row.add(data1);
				}else if(report.equals(getSummaryReport(2))){
						Object[] data1 = {j,"Total",TGrossPay,TSSS,TPHIC,THDMF,TSSSLoan,
								THouseRental,TSavings,0,TNetPay,0,0,0,0};
						row.add(data1);
				}else if(report.equals(getSummaryReport(3))){
					Object[] data1 = {j,"TOTAL", "",TRDW,0,0,TRegularPay,
			    	 	TROT,TROTPay,TRNSD,TRNSDPay,TLH,TLHPay,TLHOT,TLHOTPay,TLHNSD,TLHNSDPay,
			    	 	TSH,TSHPay,TSHOT,TSHOTPay,TSHNSD,TSHNSDPay,TAdjustments,TGrossPay,
			    	 	TSSS,TPHIC,THDMF,TSSSLoan,TPayrollAdvance,THouseRental,TUniformAndOthers,
			    	 	TSavings,TNetPay};
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
			if(month > 12){
				month %= 12;
				year++;
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
		String newDate = year + "-" + extraZeroForMonth + month + "-" + 
				extraZeroForDay + day;
		try{
			periodStartDate = sdf.parse(newDate);
		}catch(Exception ex){
			System.out.println(ex);
		}
		return periodStartDate;
	}
	
	private static BigDecimal tryGetBigDecimal(String s){
		try{
			return new BigDecimal(s);
		}catch(Exception e){
			return BigDecimal.ZERO;
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
			String sql = "SELECT count(*) FROM client,personnel " +
					"where personnel.assignment = client.name";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			rs.next();
			return rs.getInt(1);
		}catch(SQLException ex){
			System.out.println(ex);
		}
		return 0;
	}
	
	public BigDecimal[] getVariables(String client) throws Exception{
		BigDecimal variables[] = new BigDecimal[12];
		
		try{
			String sql = "Select rotVar, rnsdVar, lhRate, lhVar, lhOTVar," +
					"lhNSDVar, lhRDVar, shRate, shVar, shOTVar, shNSDVar," +
					"shRDVar FROM `client` where client.name = '" + client + "';";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				for(int i = 1;i < 13; i++){
					if(rs.getString(i) == null || rs.getString(i) == "null"){
						throw new Exception("No variable\\s");
					}
				}
				for(int i = 1;i < 13; i++){
					variables[i-1] = new BigDecimal(rs.getString(i));
				}
				}
		} catch (Exception ex) {
			throw ex;
		}
		return variables;
	}
	
	public void modifyVariables(BigDecimal variables[], String client) throws SQLException{
		String sql = "UPDATE client set " +
		"rotVar = " + variables[0].toString() + 
		", rnsdVar = " + variables[1].toString() +
		", lhRate = " + variables[2].toString() +
		", lhVar = "+variables[3].toString() + 
		", lhOTVar = " + variables[4].toString() +
		", lhNSDVar =  " + variables[5].toString() +
		", lhRDVar = " + variables[6].toString() +
		", shRate = " + variables[7].toString() +
		", shVar = " + variables[8].toString() +
		", shOTVar = " + variables[9].toString() +
		", shNSDVar = " + variables[10].toString() +
		", shRDVar = " + variables[11].toString() +
		" where name = '"+client+"';";
		Statement stmt = con.prepareStatement(sql);
		stmt.execute(sql);
		System.out.println("MODIFIED CLIENT VARIABLES!");
	}
	
	public BigDecimal[] getTaxTable(int bracket){ 
		BigDecimal table[] = new BigDecimal[8];
		try{
			String sql = "Select tax, taxpercentover, z, sme, s1me1, s2me2, s3me3," + 
					"s4me4 from taxtable where bracket = " + bracket + ";";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				for(int i = 1;i < 9; i++){
					table[i-1] = new BigDecimal(rs.getString(i));
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return table;
	}
	
	public BigDecimal[][] getWholeTaxTable(){
		int[] brackets = getBracketListInArray();
		BigDecimal[][] taxTable = new BigDecimal[brackets.length][8];
		
		for(int i = 0; i < brackets.length; i++){
			taxTable[i] = getTaxTable(brackets[i]);
		}
		return taxTable;
	}
	
	public void updateTaxTable(int bracket, BigDecimal[] table) throws SQLException{
		String sql = "UPDATE taxtable set " +
				"tax = " + table[0] + ", taxpercentover = " + table[1] +
				", z = " + table[2] +
				", sme = " + table[3] +", s1me1 = " + table[4] +
				", s2me2 =  " + table[5] +", s3me3 = " + table[6] +
				", s4me4 = " + table[7] + " where bracket = " + bracket + ";";
		Statement stmt = con.prepareStatement(sql);
		stmt.execute(sql);
		System.out.println("UPDATED TAX TABLE!");
	}
	
	public ArrayList<String> getBracketList(){
		ArrayList<String> brackets = new ArrayList<>();
            
		try{
			String sql = "Select bracket FROM taxtable order by bracket";
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
	
	public int generateSummaryReport(File directory, String date,String client,
			String report){
		PrintWriter writer = null;
		ArrayList<Object[]> data = getTableRow(client,date,report);
		ArrayList<String> columnHeader = getColumnName(report);
		int day = Integer.parseInt(date.substring(8,10));
		int i = 0,j = 0;
		try{
			writer = new PrintWriter(directory, "UTF-8");
		}catch(Exception ex){
			System.out.println(ex);
			return 1;
		}
		writer.print("\"888 GALLANT MANPOWER AND MANAGEMENT SERVICES INCORPORATED\"");
		writer.println();
		writer.print("\"Payroll - \"" + client);
		writer.println();
		
		if(day == 1)
			writer.println("\"For the preiod of \"" + date + "-15");
		else
			writer.println("\"For the preiod of \"" + date + "-30");
		writer.println();
		
		for(i = 0; i < columnHeader.size(); i++){
			if(columnHeader.get(i).equals("Name")){
				writer.print(columnHeader.get(i) + ",,,");
			}else if(columnHeader.get(i).equals("Position")){
				writer.print(columnHeader.get(i) + ",,");
			}else{
				writer.print(columnHeader.get(i) + ",");
			}
		}
		writer.println();
		
		for(Object[] t : data){
			j++;
			if(j == data.size()){
				writer.println();
				for(i = 0; i < t.length; i++){
					if(i == 1){
						writer.print("\"" + t[i] + "\"" + ",,,");
					}else if(i == 2){
						writer.print("\"" + t[i] + "\"" + ",,");
					}else{
						writer.print(t[i] + ",");
					}
				}
			}else{
				for(i = 0; i < t.length; i++){
					if(i == 1){
						writer.print("\"" + t[i] + "\"" + ",,,");
					}else if(i == 2){
						writer.print("\"" + t[i] + "\"" + ",,");
					}else
					writer.print(t[i] + ",");
				}
			}
			writer.println();
		}
		writer.println();
		writer.print("Prepared by: " + ",,,,,,,,,," + "Checked by: ");
		writer.close();
		return 0;
	}
}
