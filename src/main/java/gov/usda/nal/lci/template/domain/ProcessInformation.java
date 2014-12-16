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
import java.util.Date;
import java.util.List;


public class ProcessInformation implements Serializable {

	private final static long serialVersionUID = 1L;

	private String version;
	private String description; 
	private boolean infrastructureProcess;
	private Time timeInfo;
	private ProcessName processName;
	private Geography geography;
	private InternationalStandardIndustrialClassification isic;
	private Technology technology;
	private List<Parameter> parameters;
	private AdministrativeInformation administrativeInformation;
	private ModellingAndValidation modelingAndValidation;
	private List<Costs> costs;
	
	/**
	 * General information.DataSet Version
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isInfrastructureProcess() {
		return infrastructureProcess;
	}

	public void setInfrastructureProcess(boolean infrastructureProcess) {
		this.infrastructureProcess = infrastructureProcess;
	}

	public Time getTimeInfo() {
		return timeInfo;
	}

	public void setTimeInfo(Time timeInfo) {
		this.timeInfo = timeInfo;
	}

	
	/**
	 * complete process name
	 */
	public ProcessName getProcessName() {
		return processName;
	}

	public void setProcessName(ProcessName processName) {
		this.processName = processName;
	}

	/**
	 * 
	 */
	public Geography getGeography() {
		return geography;
	}

	

	public void setGeography(Geography geography) {
		this.geography = geography;
	}

	public InternationalStandardIndustrialClassification getIsic() {
		return isic;
	}

	public void setIsic(InternationalStandardIndustrialClassification isic) {
		this.isic = isic;
	}
	/**
	 * Intended applications
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Technology getTechnology() {
		return technology;
	}

	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	/**
	 * 
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	/**
	 * 
	 */

	public AdministrativeInformation getAdministrativeInformation() {
		return administrativeInformation;
	}

	public void setAdministrativeInformation(
			AdministrativeInformation administrativeInformation) {
		this.administrativeInformation = administrativeInformation;
	}

	public ModellingAndValidation getModelingAndValidation() {
		return modelingAndValidation;
	}

	public void setModelingAndValidation(
			ModellingAndValidation modelingAndValidation) {
		this.modelingAndValidation = modelingAndValidation;
	}

	public List<Costs> getCosts() {
		return costs;
	}

	public void setCosts(List<Costs> costs) {
		this.costs = costs;
	}
	
	
	
}
