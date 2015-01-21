package gov.usda.nal.lci.template.vo;
/** ===========================================================================
*
*                            PUBLIC DOMAIN NOTICE
*               		National Agriculture Library
*
*  This software/database is a "United States Government Work" under the
*  terms of the United States Copyright Act.  It was written as part of
*  the author's official duties as a United States Government employee and
*  thus cannot be copyrighted.  This software/database is freely available
*  to the public for use. The National Agriculture Library and the U.S.
*  Government have not placed any restriction on its use or reproduction.
*
*  Although all reasonable efforts have been taken to ensure the accuracy
*  and reliability of the software and data, the NAL and the U.S.
*  Government do not and cannot warrant the performance or results that
*  may be obtained by using this software or data. The NAL and the U.S.
*  Government disclaim all warranties, express or implied, including
*  warranties of performance, merchantability or fitness for any particular
*  purpose.
*
*  Please cite the author in any work or product based on this material.
*
*===========================================================================
*/
import java.io.Serializable;

/**
 * ValueObject for Reading a Value from Excel File (XLSM) 
 */
public class ParametersVO extends BaseVO {
	private final static long serialVersionUID = 1L;
	
	private String name;
	private String resultingValue;
	private String uncertaintyType;
	private String formula;
	private String expectedValue;
	private String dispersion;
	private String minimumValue;
	private String maximumValue; 
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResultingValue() {
		return resultingValue;
	}
	public void setResultingValue(String resultingValue) {
		this.resultingValue = resultingValue;
	}
	public String getUncertaintyType() {
		return uncertaintyType;
	}
	public void setUncertaintyType(String uncertaintyType) {
		this.uncertaintyType = uncertaintyType;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}
	public String getDispersion() {
		return dispersion;
	}
	public void setDispersion(String dispersion) {
		this.dispersion = dispersion;
	}
	public String getMinimumValue() {
		return minimumValue;
	}
	public void setMinimumValue(String minimumValue) {
		this.minimumValue = minimumValue;
	}
	public String getMaximumValue() {
		return maximumValue;
	}
	public void setMaximumValue(String maximumValue) {
		this.maximumValue = maximumValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "ParametersVO [name=" + name + ", resultingValue="
				+ resultingValue + ", uncertaintyType="
				+ uncertaintyType + ", formula=" + formula
				+ ", expectedValue=" + expectedValue + ", dispersion="
				+ dispersion + ", minimumValue=" + minimumValue
				+ ", maximumValue=" + maximumValue + ", description="
				+ description + "]";
	}
	
	
}
