/*******************************************************
	 *  Class name: Personnel
 	 *  Inheritance:
	 *  Attributes: name, position, assignment,
     *				employeeStatus, tin, taxStatus,
     *				sss, sssLoan, phic, hdmf, hdmloan,
     *				payrollAdvance, houseRental,
     *				uniformAndOthers, dailyrate, colorate,
     *				monthlyRate, dtr
	 *  Methods:	getName, getPosition, setPosition,
     *				getDailyRate, setDailyRate, getAssignment,
     *				setAssignment, getColaRate, setColaRate,
     *				getTaxStatus, setTaxStatus, setDTR, 
     *				getTIN, getEmployeeStatus, getMonthlyRate,
     *				getSSS, getSSSLoan, getPHIC,
     *				getHDMF, getHDMFLoan, getPayrollAdvance,
     *				getHouseRental, getUniformAndOthers
	 *  Functionality: Model
	 *  Visibility: public
	 *******************************************************/

import java.math.BigDecimal;
public class Personnel {
	private String name;
	private String position;
	private String assignment;
	private String employeeStatus;
	private String tin;
	private String taxStatus;
	private BigDecimal sss;
	private BigDecimal sssLoan;
	private BigDecimal phic;
	private BigDecimal hdmf;
	private BigDecimal hdmfLoan;
	private BigDecimal payrollAdvance;
	private BigDecimal houseRental;
	private BigDecimal uniformAndOthers;
	private BigDecimal dailyRate;
	private BigDecimal colaRate;
	private BigDecimal monthlyRate;

    public Personnel(String name, String position, String assignment, 
    		String employeeStatus, String tin, String taxStatus,
    		BigDecimal sss, BigDecimal sssLoan, BigDecimal phic, BigDecimal hdmf,
    		BigDecimal hdmfLoan, BigDecimal payrollAdvance, BigDecimal houseRental,
    		BigDecimal uniformAndOthers, BigDecimal dailyRate, BigDecimal colaRate,
    		BigDecimal monthlyRate) {
		this.name = name;
		this.position = position;
		this.assignment = assignment;
		this.employeeStatus = employeeStatus;
		this.tin = tin;
		this.taxStatus = taxStatus;
		this.sss = sss;
		this.sssLoan = sssLoan;
		this.phic = phic;
		this.hdmf = hdmf;
		this.hdmfLoan = hdmfLoan;
		this.payrollAdvance = payrollAdvance;
		this.houseRental = houseRental;
		this.uniformAndOthers = uniformAndOthers;
		this.dailyRate = dailyRate;
		this.colaRate = colaRate;
		this.monthlyRate = monthlyRate;
    }
    
	public String getName(){
		return name;
	}
	
	public String getPosition(){
		return position;
	}
	
	public void setPosition(String position){
		this.position = position;
	}
	
	public BigDecimal getDailyRate(){
		return dailyRate;
	}
	
	public void setDailyRate(BigDecimal dailyRate){
		this.dailyRate = dailyRate;
	}
	
	public String getAssignment(){
		return assignment;
	}
	
	public void setAssignment(String assignment){
		this.assignment = assignment;
	}
	
	public BigDecimal getColaRate(){
		return colaRate;
	}
	
	public void setColaRate(BigDecimal colaRate){
		this.colaRate = colaRate;
	}
	
	public String getTaxStatus(){
		return taxStatus;
	}
	
	public void setTaxStatus(String taxStatus){
		this.taxStatus = taxStatus;
	}
    
    public String getTIN(){
     	return tin;
    }
    
    public String getEmployeeStatus(){
        return employeeStatus;
    }
    
    public BigDecimal getMonthlyRate(){
   		return monthlyRate;
    }
    
    public BigDecimal getSSS(){
    	return sss;
    }
    
    public BigDecimal getSSSLoan(){
        return sssLoan;
    }
    
   	public BigDecimal getPHIC(){
    	return phic;
    }
    
    public BigDecimal getHDMF(){
        return hdmf;
    }
    
    public BigDecimal getHDMFLoan(){
        return hdmfLoan;
    }
    
    public BigDecimal getPayrollAdvance(){
      	return payrollAdvance;
    }
    
    public BigDecimal getHouseRental(){
        return houseRental;
    }
    
    public BigDecimal getUniformAndOthers(){
        return uniformAndOthers;
    }
}