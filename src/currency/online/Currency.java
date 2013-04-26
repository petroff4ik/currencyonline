/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

/**
 *
 * @author petroff
 */
public class Currency {

	private String charCode;
	private String name;
	private Double value;
	private String date;

	public Currency(String charCode, String name, Double value, String date) {
		this.charCode = charCode;
		this.name = name;
		this.value = value;
		this.date = date;
	}

	public Currency() {
	}

	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return this.name;
	}

	public String getCharCode() {
		return this.charCode;
	}

	public Double getValue() {
		return this.value;
	}

	public String getDate() {
		return this.date;
	}
}
