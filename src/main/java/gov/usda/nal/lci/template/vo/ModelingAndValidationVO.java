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
 * ValueObject for Reading a Value from Excel File 
 */
public class ModelingAndValidationVO extends BaseVO {
	private final static long serialVersionUID = 1L;
	
	private String metaType;
	private String processType;
	private String lciMethods;
	private String modelingConstants;
	private String dataCompleteness;
	private String massBalance;
	private String dataSelection;
	private String dataTreatment;
	private String samplingProcedure;
	private String dataCollectionPeriod;
	private String reviewer;
	private String dataSetOtherEvaluation;
	private String sources;
	
	public String getMetaType() {
		return metaType;
	}
	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getLciMethods() {
		return lciMethods;
	}
	public void setLciMethods(String lciMethods) {
		this.lciMethods = lciMethods;
	}
	public String getModelingConstants() {
		return modelingConstants;
	}
	public void setModelingConstants(String modelingConstants) {
		this.modelingConstants = modelingConstants;
	}
	public String getDataCompleteness() {
		return dataCompleteness;
	}
	public void setDataCompleteness(String dataCompleteness) {
		this.dataCompleteness = dataCompleteness;
	}
	
	public String getMassBalance() {
		return massBalance;
	}
	public void setMassBalance(String massBalance) {
		this.massBalance = massBalance;
	}
	public String getDataSelection() {
		return dataSelection;
	}
	public void setDataSelection(String dataSelection) {
		this.dataSelection = dataSelection;
	}
	public String getDataTreatment() {
		return dataTreatment;
	}
	public void setDataTreatment(String dataTreatment) {
		this.dataTreatment = dataTreatment;
	}
	public String getSamplingProcedure() {
		return samplingProcedure;
	}
	public void setSamplingProcedure(String samplingProcedure) {
		this.samplingProcedure = samplingProcedure;
	}
	public String getDataCollectionPeriod() {
		return dataCollectionPeriod;
	}
	public void setDataCollectionPeriod(String dataCollectionPeriod) {
		this.dataCollectionPeriod = dataCollectionPeriod;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getDataSetOtherEvaluation() {
		return dataSetOtherEvaluation;
	}
	public void setDataSetOtherEvaluation(String dataSetOtherEvaluation) {
		this.dataSetOtherEvaluation = dataSetOtherEvaluation;
	}
	public String getSources() {
		return sources;
	}
	public void setSources(String sources) {
		this.sources = sources;
	}
	
	
}