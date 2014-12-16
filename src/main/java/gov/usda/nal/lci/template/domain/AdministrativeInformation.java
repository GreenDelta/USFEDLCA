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

public class AdministrativeInformation implements Serializable {
	private final static long serialVersionUID = 1L;
	
	private String intendedApplication;
	private String dataOwner;
	private String dataGenerator;
	private String dataDocumentor;
	private String publication;
	private String accessUseRestrictions;
	private String project;
	private String version;
	private boolean copyright;
	
	public String getIntendedApplication() {
		return intendedApplication;
	}
	public void setIntendedApplication(String intendedApplication) {
		this.intendedApplication = intendedApplication;
	}
	public String getDataOwner() {
		return dataOwner;
	}
	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
	}
	public String getDataGenerator() {
		return dataGenerator;
	}
	public void setDataGenerator(String dataGenerator) {
		this.dataGenerator = dataGenerator;
	}
	public String getDataDocumentor() {
		return dataDocumentor;
	}
	public void setDataDocumentor(String dataDocumentor) {
		this.dataDocumentor = dataDocumentor;
	}
	public String getPublication() {
		return publication;
	}
	public void setPublication(String publication) {
		this.publication = publication;
	}
	public String getAccessUseRestrictions() {
		return accessUseRestrictions;
	}
	public void setAccessUseRestrictions(String accessUseRestrictions) {
		this.accessUseRestrictions = accessUseRestrictions;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isCopyright() {
		return copyright;
	}
	public void setCopyright(boolean copyright) {
		this.copyright = copyright;
	}
	@Override
	public String toString() {
		return "AdministrativeInformation [intendedApplication="
				+ intendedApplication + ", dataOwner=" + dataOwner
				+ ", dataGenerator=" + dataGenerator + ", dataDocumentor="
				+ dataDocumentor + ", publication=" + publication
				+ ", accessUseRestrictions=" + accessUseRestrictions
				+ ", project=" + project + ", version=" + version
				+ ", copyright=" + copyright + "]";
	}  
	
	

}