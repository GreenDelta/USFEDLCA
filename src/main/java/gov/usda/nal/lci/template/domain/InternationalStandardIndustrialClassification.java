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
public class InternationalStandardIndustrialClassification {

	    
	    private String isicCode;
	    private String subcategory;
	    private String category;
	    
	    public InternationalStandardIndustrialClassification(String code,String cat,String subcat)
	    {
	    	this.setIsicCode(code);
	    	this.setCategory(cat);
	    	this.setSubcategory(subcat);
	    }

		public String getIsicCode() {
			return isicCode;
		}
		public void setIsicCode(String isicCode) {
			this.isicCode = isicCode;
		}
		public String getSubcategory() {
			return subcategory;
		}
		public void setSubcategory(String subcategory) {
			this.subcategory = subcategory;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
	    
	    

}
