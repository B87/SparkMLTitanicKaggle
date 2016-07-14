package sparkml.titanickaggle;

import java.io.Serializable;

public class TitanicInputTestingBean implements Serializable {
	private static final long serialVersionUID = 7102392052500211686L;
	
	private Integer passengerId;
	private Integer pClass;
	//private String name;
	private String sex;
	private Double age;
	private Integer sibsSp;
	private Integer pArch;
	private String ticket;
	private Double fare;
	private String cabin;
	private String embarked;
	
	public Integer getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(Integer passengerId) {
		this.passengerId = passengerId;
	}
	public Integer getpClass() {
		return pClass;
	}
	public void setpClass(Integer pClass) {
		this.pClass = pClass;
	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Double getAge() {
		return age;
	}
	public void setAge(Double age) {
		this.age = age;
	}
	public Integer getSibsSp() {
		return sibsSp;
	}
	public void setSibsSp(Integer sibsSp) {
		this.sibsSp = sibsSp;
	}
	public Integer getpArch() {
		return pArch;
	}
	public void setpArch(Integer pArch) {
		this.pArch = pArch;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}
	public String getCabin() {
		return cabin;
	}
	public void setCabin(String cabin) {
		this.cabin = cabin;
	}
	public String getEmbarked() {
		return embarked;
	}
	public void setEmbarked(String embarked) {
		this.embarked = embarked;
	}
}