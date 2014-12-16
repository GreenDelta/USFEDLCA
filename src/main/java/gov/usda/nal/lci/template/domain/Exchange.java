package gov.usda.nal.lci.template.domain;
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

import org.openlca.core.model.UncertaintyType;

/*
 * based on EcoSpold01
 */
	
public class Exchange implements Serializable {
	
	private final static long serialVersionUID = 1L;
	
	private Integer inputGroup;		
	private Integer outputGroup;   
	private String flowName;
	private String category;
	private String subCategory;	 //flowProperty
	private String unit;
	private Double amountValue;
	private String parameterName;
	private String defaultProvider;
	private String dataQualityComment;
	private String location;
	private String casNumber;
	private String description;	
    private String formula;			  
	private String unitFlowPropertyName;       // "unitName/FlowPropertyName"
	private String referenceUnit;
	private Double conversionFactor;
	private UncertaintyType uncertaintyType ;
	private Double expectedValue;
	private Double dispersion;
	private Double minValue;		
	private Double maxValue;	
	private Double physical;
	private Double economic;
	private Double causal;
	private boolean isElementaryFlow;
	private Double standardDeviation95; //ask
	private Double mostLikelyValue;    //ask
	private boolean isInfrastructureProcess;
	
	public Integer getInputGroup() {
		return inputGroup;
	}
	public void setInputGroup(Integer inputGroup) {
		this.inputGroup = inputGroup;
	}
	public Integer getOutputGroup() {
		return outputGroup;
	}
	public void setOutputGroup(Integer outputGroup) {
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
	/*
	 * correspond to units in a unit group
	 */
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getAmountValue() {
		return amountValue;
	}
	public void setAmountValue(Double amountValue) {
		this.amountValue = amountValue;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getDefaultProvider() {
		return defaultProvider;
	}
	public void setDefaultProvider(String defaultProvider) {
		this.defaultProvider = defaultProvider;
	}
	public String getDataQualityComment() {
		return dataQualityComment;
	}
	public void setDataQualityComment(String dataQualityComment) {
		this.dataQualityComment = dataQualityComment;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCasNumber() {
		return casNumber;
	}
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getUnitFlowPropertyName() {
		return unitFlowPropertyName;
	}
	public void setUnitFlowPropertyName(String unitFlowPropertyName) {
		this.unitFlowPropertyName = unitFlowPropertyName;
	}
	public String getReferenceUnit() {
		return referenceUnit;
	}
	public void setReferenceUnit(String referenceUnit) {
		this.referenceUnit = referenceUnit;
	}
	public Double getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}
	
	public UncertaintyType getUncertaintyType() {
		return uncertaintyType;
	}
	public void setUncertaintyType(UncertaintyType uncertaintyType) {
		this.uncertaintyType = uncertaintyType;
	}
	public Double getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(Double expectedValue) {
		this.expectedValue = expectedValue;
	}
	public Double getDispersion() {
		return dispersion;
	}
	public void setDispersion(Double dispersion) {
		this.dispersion = dispersion;
	}
	public Double getMinValue() {
		return minValue;
	}
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	public Double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	public Double getPhysical() {
		return physical;
	}
	public void setPhysical(Double physical) {
		this.physical = physical;
	}
	public Double getEconomic() {
		return economic;
	}
	public void setEconomic(Double economic) {
		this.economic = economic;
	}
	public Double getCausal() {
		return causal;
	}
	public void setCausal(Double causal) {
		this.causal = causal;
	}
	
	public boolean isElementaryFlow() {
		return isElementaryFlow;
	}
	public void setElementaryFlow(boolean isElementaryFlow) {
		this.isElementaryFlow = isElementaryFlow;
	}
	
	public Double getMostLikelyValue() {
		return mostLikelyValue;
	}
	public void setMostLikelyValue(Double mostLikelyValue) {
		this.mostLikelyValue = mostLikelyValue;
	}
	public Double getStandardDeviation95() {
		return standardDeviation95;
	}
	public void setStandardDeviation95(Double standardDeviation95) {
		this.standardDeviation95 = standardDeviation95;
	}
	
	public boolean isInfrastructureProcess() {
		return isInfrastructureProcess;
	}
	public void setInfrastructureProcess(boolean isInfrastructureProcess) {
		this.isInfrastructureProcess = isInfrastructureProcess;
	}
	@Override
	public String toString() {
		return "Exchange [inputGroup=" + inputGroup + ", outputGroup="
				+ outputGroup + ", flowName=" + flowName + ", category="
				+ category + ", subCategory=" + subCategory + ", unit=" + unit
				+ ", amountValue=" + amountValue + ", parameterName="
				+ parameterName + ", defaultProvider=" + defaultProvider
				+ ", dataQualityComment=" + dataQualityComment + ", location="
				+ location + ", casNumber=" + casNumber + ", description="
				+ description + ", formula=" + formula
				+ ", unitFlowPropertyName=" + unitFlowPropertyName
				+ ", referenceUnit=" + referenceUnit + ", conversionFactor="
				+ conversionFactor + ", uncertaintyType=" + uncertaintyType
				+ ", expectedValue=" + expectedValue + ", dispersion="
				+ dispersion + ", minValue=" + minValue + ", maxValue="
				+ maxValue + ", physical=" + physical + ", economic="
				+ economic + ", causal=" + causal + "]";
	}

}