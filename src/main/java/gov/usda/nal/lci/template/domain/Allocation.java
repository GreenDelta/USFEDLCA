package gov.usda.nal.lci.template.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openlca.core.model.AllocationMethod;

/**
 * Contains all information about allocation procedure, allocation parameters
 * and allocation factors applied on a multi-output process.  The Allocation object is 
 * functionally equivalent to {@link org.openlca.ecospold.process.Allocation} used in 
 * for Ecospold V1 import processes.
 * @author G. Mooore
 */
public class Allocation implements Serializable {

	private final static long serialVersionUID = 1L;
	protected String referenceToInputOutput;
	protected String referenceToCoProduct;
	protected AllocationMethod allocationMethod;
	protected float value;

	/**
	 * Gets the value of the referenceToInputOutput property.
	 
	 */
	
	public String getReferenceToInputOutput() {
		
		return this.referenceToInputOutput;
	}

	public void setReferenceToInputOutput(String referenceToInputOutput) {
		this.referenceToInputOutput = referenceToInputOutput;
	}

	/**
	 * Gets the value of the referenceToCoProduct property.
	 * 
	 */
	
	public String getReferenceToCoProduct() {
		return referenceToCoProduct;
	}

	/**
	 * Sets the value of the referenceToCoProduct property.
	 * 
	 */
	
	public void setReferenceToCoProduct(String value) {
		this.referenceToCoProduct = value;
	}

	/**
	 * Gets the value of the allocationMethod property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	
	public AllocationMethod getAllocationMethod() {
		
		return allocationMethod;
	}

	/**
	 * Sets the value of the allocationMethod property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	
	public void setAllocationMethod(AllocationMethod value) {
		this.allocationMethod = value;
	}

	/**
	 * Gets the value of the fraction property.
	 * 
	 */
	
	public float getValue() {
		return this.value;
	}

	/**
	 * Sets the value of the fraction property.
	 * 
	 */
	
	public void setValue(float value) {
		this.value = value;
	}

	
}
