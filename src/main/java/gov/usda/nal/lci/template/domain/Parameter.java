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

public class Parameter implements Serializable {

	private final static long serialVersionUID = 1L;

	private String description;
	private String name;
	private String formula;
	private String value;
	private UncertaintyType uncertaintyType;
	private Double expectedValue; //meanValue;
	private Double dispersion;   //geo std
	private Double minimumValue;
	private Double maximumValue;


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Mathematical Relation
	 * 
	 * @return formula
	 */
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * Resulting value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Uncertainty distribution type
	 * @return uncertainty
	 */
	public UncertaintyType getUncertaintyType() {
		return uncertaintyType;
	}

	public void setUncertaintyType(UncertaintyType uncertaintyType) {
		this.uncertaintyType = uncertaintyType;
	}

	

	public Double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Double minimumValue) {
		this.minimumValue = minimumValue;
	}

	public Double getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(Double maximumValue) {
		this.maximumValue = maximumValue;

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
	
}
