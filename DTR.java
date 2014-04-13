/*******************************************************
	 *  Class name: DTR
 	 *  Inheritance: 
	 *  Attributes: name, tin, regularDaysWorks, regularOvertime,
	 *				regularNightShiftDifferential, specialHoliday,
	 *				specialHoliday, specialHolidayOvertime, specialHolidayNightShiftDifferential,
	 *				legalHoliday, legalHolidayOvertime, legalHolidayNightShiftDifferential,
	 *				legalHolidayOnRestDay, specialHolidayOnRestDay, late, periodStartDate
	 *  Methods:	DTR, getRegularDaysWorks, setRegularDaysWorks,
	 *				getRegularOvertime, setRegularOvertime, getRegularNightShiftDifferential
	 *				setRegularNightShiftDifferential, getSpecialHoliday, setSpecialHoliday
	 *				getSpecialHolidayOvertime, setSpecialHolidayOvertime, getSpecialHolidayNightShiftDifferential
	 *				setSpecialHolidayNightShiftDifferential, getLegalHoliday, setLegalHoliday,
	 *				getLegalHolidayOvertime, setLegalHolidayOvertime, getLegalHolidayNightShiftDifferential,
	 *				setLegalHolidayNightShiftDifferential, getPeriodStartDate, setPeriodStartDate,
	 *				getLegalHolidayOnRestDay, getSpecialHolidayOnRestDay, getTIN, getLate
	 *  Functionality: Model
	 *  Visibility: public
	 *******************************************************/

	 
import java.util.Date;
import java.math.BigDecimal;


public class DTR {

	private String name;
	private String tin;
	private BigDecimal regularDaysWorks;
	private BigDecimal regularOvertime;
	private BigDecimal regularNightShiftDifferential;
	private BigDecimal specialHoliday;
	private BigDecimal specialHolidayOvertime;
	private BigDecimal specialHolidayNightShiftDifferential;
	private BigDecimal legalHoliday;
	private BigDecimal legalHolidayOvertime;
	private BigDecimal legalHolidayNightShiftDifferential;
	private BigDecimal legalHolidayOnRestDay;
	private BigDecimal specialHolidayOnRestDay;
	private BigDecimal late;
	private Date  periodStartDate;

	
	public DTR(String name, String tin, BigDecimal regularDaysWorks, BigDecimal regularOvertime, BigDecimal regularNightShiftDifferential,
			   BigDecimal specialHoliday, BigDecimal specialHolidayOvertime, BigDecimal specialHolidayNightShiftDifferential,
			   BigDecimal legalHoliday, BigDecimal legalHolidayOvertime, BigDecimal legalHolidayNightShiftDifferential, 
			   BigDecimal legalHolidayOnRestDay, BigDecimal specialHolidayOnRestDay, BigDecimal late, Date periodStartDate){
		this.name = name;
		this.tin = tin;
		this.regularDaysWorks = regularDaysWorks;
		this.regularOvertime = regularOvertime;
		this.regularNightShiftDifferential = regularNightShiftDifferential;
		this.specialHoliday = specialHoliday;
		this.specialHolidayOvertime = specialHolidayOvertime;
		this.specialHolidayNightShiftDifferential = specialHolidayNightShiftDifferential;
		this.legalHoliday = legalHoliday;
		this.legalHolidayOvertime = legalHolidayOvertime;
		this.legalHolidayNightShiftDifferential = legalHolidayNightShiftDifferential;
		this.legalHolidayOnRestDay = legalHolidayOnRestDay;
		this.specialHolidayOnRestDay = specialHolidayOnRestDay;
		this.late = late;
		this.periodStartDate = periodStartDate;
	}
	
	public BigDecimal getRegularDaysWorks(){
		return regularDaysWorks;
	}
	
	public void setRegularDaysWorks(BigDecimal regularDaysWorks){
		this.regularDaysWorks = regularDaysWorks;
	}
	
	public BigDecimal getRegularOvertime(){
		return regularOvertime;
	}
	
	public void setRegularOvertime(BigDecimal regularOvertime){
		this.regularOvertime = regularOvertime;
	}
	
	public BigDecimal getRegularNightShiftDifferential(){
		return regularNightShiftDifferential;
	}
	
	public void setRegularNightShiftDifferential(BigDecimal regularNightShiftDifferential){
		this.regularNightShiftDifferential = regularNightShiftDifferential;
	}
	
	public BigDecimal getSpecialHoliday(){
		return specialHoliday;
	}
	
	public void setSpecialHoliday(BigDecimal specialHoliday){
		this.specialHoliday = specialHoliday;
	}
	
	public BigDecimal getSpecialHolidayOvertime(){
		return specialHolidayOvertime;
	}
	
	public void setSpecialHolidayOvertime(BigDecimal specialHolidayOvertime){
		this.specialHolidayOvertime = specialHolidayOvertime;
	}
	
	public BigDecimal getSpecialHolidayNightShiftDifferential(){
		return specialHolidayNightShiftDifferential;
	}
	
	public void setSpecialHolidayNightShiftDifferential(BigDecimal specialHolidayNightShiftDifferential){
		this.specialHolidayNightShiftDifferential = specialHolidayNightShiftDifferential;
	}
	
	public BigDecimal getLegalHoliday(){
		return legalHoliday;
	}
	
	public void setLegalHoliday(BigDecimal legalHoliday){
		this.legalHoliday = legalHoliday;
	}
	
	public BigDecimal getLegalHolidayOvertime(){
		return legalHolidayOvertime;
	}
	
	public void setLegalHolidayOvertime(BigDecimal legalHolidayOvertime){
		this.legalHolidayOvertime = legalHolidayOvertime;
	}
	
	public BigDecimal getLegalHolidayNightShiftDifferential(){
		return legalHolidayNightShiftDifferential;
	}
	
	public void setLegalHolidayNightShiftDifferential(BigDecimal legalHolidayNightShiftDifferential){
		this.legalHolidayNightShiftDifferential = legalHolidayNightShiftDifferential;
	}
	
	public Date getPeriodStartDate(){
		return periodStartDate;
	}
	
	public void setPeriodStartDate(Date periodStartDate){
		this.periodStartDate = periodStartDate;
	}
      
	public BigDecimal getLegalHolidayOnRestDay(){
		return legalHolidayOnRestDay;
	}
	
	public BigDecimal getSpecialHolidayOnRestDay(){
		return specialHolidayOnRestDay;
	}
	
	public String getTIN(){
        return tin;
    }
	
	public BigDecimal getLate(){
		return late;
	}
        
}
