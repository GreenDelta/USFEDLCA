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
/* MAP TO  EcoSpold01.Exchange
 * 
 */

import java.io.Serializable;

/**
 * Reading values from Excel spreadsheet (inputsoutputs)
 */
public class ExchangeDataVO extends BaseVO {
	
	private final static long serialVersionUID = 1L;
	private String emptyColumn;
	private String inputGroup;
	private String outputGroup;
	private String flowName; 
	private String category;
	private String subCategory;
	private String unit;
	private String amount;
	private String parameterName;
	private String provider;
	private String dataQualityComment;
	private String flowLocation;
	private String casNumber;
	private String shortDescription;
	private String formula;
	private String unitNameFlowPropertyName;
	private String refUnit;
	private String conversionFactor;
	private String uncertaintyType;
	private String expectedValue;
	private String dispersion;
	private String minimumValue;
	private String maximumValue;
	private String physical;
	private String economic;
	private String causal;
	
	
	public String getEmptyColumn() {
		return emptyColumn;
	}
	public void setEmptyColumn(String emptyColumn) {
		this.emptyColumn = emptyColumn;
	}
	public String getInputGroup() {
		return inputGroup;
	}
	public void setInputGroup(String inputGroup) {
		this.inputGroup = inputGroup;
	}
	public String getOutputGroup() {
		return outputGroup;
	}
	public void setOutputGroup(String outputGroup) {
		this.outputGroup = outputGroup;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getDataQualityComment() {
		return dataQualityComment;
	}
	public void setDataQualityComment(String dataQualityComment) {
		this.dataQualityComment = dataQualityComment;
	}
	
	public String getCasNumber() {
		return casNumber;
	}
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getUnitNameFlowPropertyName() {
		return unitNameFlowPropertyName;
	}
	public void setUnitNameFlowPropertyName(String unitNameFlowPropertyName) {
		this.unitNameFlowPropertyName = unitNameFlowPropertyName;
	}
	public String getRefUnit() {
		return refUnit;
	}
	public void setRefUnit(String refUnit) {
		this.refUnit = refUnit;
	}
	public String getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(String conversionFactor) {
		this.conversionFactor = conversionFactor;
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
	public String getPhysical() {
		return physical;
	}
	public void setPhysical(String physical) {
		this.physical = physical;
	}
	public String getEconomic() {
		return economic;
	}
	public void setEconomic(String economic) {
		this.economic = economic;
	}
	public String getCausal() {
		return causal;
	}
	public void setCausal(String causal) {
		this.causal = causal;
	}
	public String getFlowLocation() {
		return flowLocation;
	}
	public void setFlowLocation(String flowLocation) {
		this.flowLocation = flowLocation;
	}
	public String getUncertaintyType() {
		return uncertaintyType;
	}
	public void setUncertaintyType(String uncertaintyType) {
		this.uncertaintyType = uncertaintyType;
	}
	
	
	@Override
	public String toString() {
		return "ExchangeDataVO [emptyColumn=" + emptyColumn + ", inputGroup="
				+ inputGroup + ", outputGroup=" + outputGroup + ", flowName="
				+ flowName + ", category=" + category + ", subCategory="
				+ subCategory + ", unit=" + unit + ", amount=" + amount
				+ ", parameterName=" + parameterName + ", provider=" + provider
				+ ", dataQualityComment=" + dataQualityComment
				+ ", flowLocation=" + flowLocation + ", casNumber=" + casNumber
				+ ", shortDescription=" + shortDescription + ", formula="
				+ formula + ", unitNameFlowPropertyName="
				+ unitNameFlowPropertyName + ", refUnit=" + refUnit
				+ ", conversionFactor=" + conversionFactor
				+ ", uncertaintyType="
				+ uncertaintyType + ", expectedValue="
				+ expectedValue + ", dispersion=" + dispersion
				+ ", minimumValue=" + minimumValue + ", maximumValue="
				+ maximumValue + ", physical=" + physical + ", economic="
				+ economic + ", causal=" + causal + "]";
	}
	
	
}