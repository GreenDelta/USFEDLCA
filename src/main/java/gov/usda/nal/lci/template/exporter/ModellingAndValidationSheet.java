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
package gov.usda.nal.lci.template.exporter;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.openlca.core.model.Process;

/**
 * @author Gary.Moore
 *
 */
public class ModellingAndValidationSheet extends TemplateSheet {
	private static final Map<String,Short> rows;
	static
	{
		rows=new HashMap<String,Short>();
		rows.put("ptype", Short.valueOf("3"));
		rows.put("method", Short.valueOf("4"));
		rows.put("constants", Short.valueOf("5"));
		rows.put("completeness", Short.valueOf("6"));
		rows.put("balance", Short.valueOf("7"));
		rows.put("selection", Short.valueOf("8"));
		rows.put("treatment", Short.valueOf("9"));
		rows.put("sampling", Short.valueOf("11"));
		rows.put("period", Short.valueOf("12"));
		rows.put("reviewer", Short.valueOf("14"));
		rows.put("othereval", Short.valueOf("15"));
	}
	public ModellingAndValidationSheet(Sheet s)
	{
		super(s);
	}
	/* (non-Javadoc)
	 * @see gov.usda.nal.lci.template.exporter.TemplateSheet#updateSheet(org.openlca.core.model.Process, org.apache.poi.ss.usermodel.Sheet)
	 */
	public void updateSheet(Process p) throws TemplateExportException {
		Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		try {
			s.getRow(rows.get("ptype")).getCell(3).setCellValue(p.getProcessType().name());
			s.getRow(rows.get("method")).getCell(3).setCellValue(p.getDocumentation().getInventoryMethod());
			s.getRow(rows.get("constants")).getCell(3).setCellValue(p.getDocumentation().getModelingConstants());
			s.getRow(rows.get("completeness")).getCell(3).setCellValue(p.getDocumentation().getCompleteness());
			s.getRow(rows.get("treatment")).getCell(3).setCellValue(p.getDocumentation().getDataTreatment());
			s.getRow(rows.get("sampling")).getCell(3).setCellValue(p.getDocumentation().getSampling());
			s.getRow(rows.get("period")).getCell(3).setCellValue(p.getDocumentation().getDataCollectionPeriod());
			s.getRow(rows.get("reviewer")).getCell(3).setCellValue(isNotNull(p.getDocumentation().getReviewer())?p.getDocumentation().getReviewer().getName():TemplateSheet.DEFAULT_STRING);
			s.getRow(rows.get("othereval")).getCell(3).setCellValue(p.getDocumentation().getReviewDetails());
		}
		catch ( Exception e) {
			throw new TemplateExportException("Unexpected error occured",e);
		}
	}

}
