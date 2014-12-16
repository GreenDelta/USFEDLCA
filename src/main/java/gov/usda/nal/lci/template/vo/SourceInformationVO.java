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
 * Reading  values from Excel File 
 */
public class SourceInformationVO extends BaseVO {
	private final static long serialVersionUID = 1L;
	
	private String emptyColumn;
	private String firstAuthor;
	private String year;
	private String description;
	private String textReference;
	private String doi;
	private String category;
	
	public String getCategory() {
		return this.category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getEmptyColumn() {
		return emptyColumn;
	}
	public void setEmptyColumn(String emptyColumn) {
		this.emptyColumn = emptyColumn;
	}
	public String getFirstAuthor() {
		return firstAuthor;
	}
	public void setFirstAuthor(String firstAuthor) {
		this.firstAuthor = firstAuthor;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTextReference() {
		return textReference;
	}
	public void setTextReference(String textReference) {
		this.textReference = textReference;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	@Override
	public String toString() {
		return "SourceInformationVO [firstAuthor=" + firstAuthor + ", year="
				+ year + ", description=" + description + ", textReference="
				+ textReference + ",category="+category+", doi=" + doi + "]";
	}
	
	
}