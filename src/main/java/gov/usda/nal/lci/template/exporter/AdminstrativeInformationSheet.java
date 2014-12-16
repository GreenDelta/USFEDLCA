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
public class AdminstrativeInformationSheet extends TemplateSheet {
	
	private static final Map<String,Short> rows;
	static
	{
		rows=new HashMap<String,Short>();
		rows.put("intendedapp", Short.valueOf("3"));
		rows.put("owner", Short.valueOf("4"));
		rows.put("generator", Short.valueOf("5"));
		rows.put("documentor", Short.valueOf("6"));
		rows.put("publication", Short.valueOf("7"));
		rows.put("restrictions", Short.valueOf("8"));
		rows.put("project", Short.valueOf("9"));
		rows.put("version", Short.valueOf("10"));
		rows.put("copyright", Short.valueOf("11"));
		
	}
	public AdminstrativeInformationSheet(Sheet s)
	{
		super(s);
	}
	/* 
	 * Put fields from Process p into attributes of the Sheet @ memory value s
	 */
	@Override
	public void updateSheet(Process p) throws TemplateExportException {
		Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		try {
			s.getRow(rows.get("intendedapp")).getCell(3).setCellValue(p.getDocumentation().getIntendedApplication());
			s.getRow(rows.get("owner")).getCell(3).setCellValue(isNotNull(p.getDocumentation().getDataSetOwner())?p.getDocumentation().getDataSetOwner().getName():TemplateSheet.DEFAULT_STRING);
			s.getRow(rows.get("generator")).getCell(3).setCellValue(isNotNull(p.getDocumentation().getDataGenerator())?p.getDocumentation().getDataGenerator().getName():TemplateSheet.DEFAULT_STRING);
			s.getRow(rows.get("documentor")).getCell(3).setCellValue(isNotNull(p.getDocumentation().getDataDocumentor())?p.getDocumentation().getDataDocumentor().getName():TemplateSheet.DEFAULT_STRING);
			s.getRow(rows.get("publication")).getCell(3).setCellValue(isNotNull(p.getDocumentation().getPublication())?p.getDocumentation().getPublication().getName():TemplateSheet.DEFAULT_STRING);
			s.getRow(rows.get("restrictions")).getCell(3).setCellValue(p.getDocumentation().getRestrictions());
			s.getRow(rows.get("project")).getCell(3).setCellValue(p.getDocumentation().getProject());
			s.getRow(rows.get("copyright")).getCell(3).setCellValue(isNotNull(p.getDocumentation().isCopyright())&&p.getDocumentation().isCopyright()?"true":"false");
		}
		catch ( Exception e)
		{
			throw new TemplateExportException("Unexpected error occured",e);
		}
	}

}
