package sparkml.titanickaggle;

import java.io.Serializable;

import lombok.Data;

@Data
public class TitanicInputBean implements Serializable {
	private static final long serialVersionUID = 5841184139219554050L;

	private Integer passengerId;
	private Double survived;
	private Integer pClass;
	private String name;
	private String sex;
	private Integer age;
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
	public Double getSurvived() {
		return survived;
	}
	public void setSurvived(Double survived) {
		this.survived = survived;
	}
	public Integer getpClass() {
		return pClass;
	}
	public void setpClass(Integer pClass) {
		this.pClass = pClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
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