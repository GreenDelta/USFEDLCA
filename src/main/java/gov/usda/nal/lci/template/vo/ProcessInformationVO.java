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
 * "General Information"
 */

public class ProcessInformationVO extends BaseVO {
	private final static long serialVersionUID = 1L;

	private String metaType;
	private String baseName;
	private String treatmentStandardsRoutes; 
	private String locationType;
	private String mixType; 
	private String quantitativeProcessProperties;  //quantitativeFlowProperties; 
	private String description;
	private String codeIsic;  
	private Boolean infrastructureProcess;
	private String quantiativeReference;   
	private String startDate;
	private String endDate; 
	private String timeComment;
	private String location;
	private String geographyComment;
	private String technologyComment;
	private ISICDataVO isicVO;
	
	public ISICDataVO getIsicVO() {
		return this.isicVO;
	}
	public void setIsicVO(ISICDataVO isicVO) {
		this.isicVO = isicVO;
	}
	public String getMetaType() {
		return metaType;
	}
	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
	public String getBaseName() {
		return baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public String getTreatmentStandardsRoutes() {
		return treatmentStandardsRoutes;
	}
	public void setTreatmentStandardsRoutes(String treatmentStandardsRoutes) {
		this.treatmentStandardsRoutes = treatmentStandardsRoutes;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getMixType() {
		return mixType;
	}
	public void setMixType(String mixType) {
		this.mixType = mixType;
	}
	public String getQuantitativeProcessProperties() {
		return quantitativeProcessProperties;
	}
	public void setQuantitativeProcessProperties(
			String quantitativeProcessProperties) {
		this.quantitativeProcessProperties = quantitativeProcessProperties;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCodeIsic() {
		return codeIsic;
	}
	public void setCodeIsic(String codeIsic) {
		this.codeIsic = codeIsic;
	}
	public Boolean getInfrastructureProcess() {
		return infrastructureProcess;
	}
	public void setInfrastructureProcess(Boolean infrastructureProcess) {
		this.infrastructureProcess = infrastructureProcess;
	}
	public String getQuantiativeReference() {
		return quantiativeReference;
	}
	public void setQuantiativeReference(String quantiativeReference) {
		this.quantiativeReference = quantiativeReference;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getTimeComment() {
		return timeComment;
	}
	public void setTimeComment(String timeComment) {
		this.timeComment = timeComment;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getGeographyComment() {
		return geographyComment;
	}
	public void setGeographyComment(String geographyComment) {
		this.geographyComment = geographyComment;
	}
	public String getTechnologyComment() {
		return technologyComment;
	}
	public void setTechnologyComment(String technologyComment) {
		this.technologyComment = technologyComment;
	}
	@Override
	public String toString() {
		return "ProcessInformationVO [baseName=" + baseName
				+ ", treatmentStandardsRoutes=" + treatmentStandardsRoutes
				+ ", locationType=" + locationType + ", mixType=" + mixType
				+ ", quantitativeProcessProperties="
				+ quantitativeProcessProperties + ", description="
				+ description + ", codeIsic=" + codeIsic
				+ ", infrastructureProcess=" + infrastructureProcess
				+ ", quantiativeReference=" + quantiativeReference
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", location=" + location + ", geographyComment="
				+ geographyComment + ", technologyComment=" + technologyComment
				+ "]";
	}
	
	
}	