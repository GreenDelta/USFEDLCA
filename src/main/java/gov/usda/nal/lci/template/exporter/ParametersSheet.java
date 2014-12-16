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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openlca.core.model.Process;
import org.openlca.core.model.Parameter;
import org.openlca.core.model.Uncertainty;
import org.openlca.core.model.UncertaintyType;

/**
 * @author Gary.Moore
 *
 */
public class ParametersSheet extends TemplateSheet {

	public ParametersSheet(Sheet s)
	{
		super(s);
	}
	private static final Map<String,Short> cols;
	static
	{
		cols=new HashMap<String,Short>();
		cols.put("name", Short.valueOf("1"));
		cols.put("value", Short.valueOf("2"));
		cols.put("uncertainty", Short.valueOf("3"));
		cols.put("formula", Short.valueOf("4"));
		cols.put("expected", Short.valueOf("5"));
		cols.put("dispersion", Short.valueOf("6"));
		cols.put("min", Short.valueOf("7"));
		cols.put("max", Short.valueOf("8"));
		cols.put("description", Short.valueOf("9"));
		
	}
	/* (non-Javadoc)
	 * @see gov.usda.nal.lci.template.exporter.TemplateSheet#updateSheet(org.openlca.core.model.Process, org.apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	public void updateSheet(Process p) throws TemplateExportException {
		 Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		try {
			int fr=4;
			int rn=fr;
			for (Parameter parm : p.getParameters())
			{
				Row r=s.getRow(rn++);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(fr),rn-1);
				else if ( r.getCell(cols.get("name")) == null ) // add cells to the row if it has none
					insertCells(s.getRow(fr),r);
				r.getCell(cols.get("name")).setCellValue(parm.getName());
				r.getCell(cols.get("description")).setCellValue(parm.getDescription());
				r.getCell(cols.get("value")).setCellValue(parm.getValue());
				r.getCell(cols.get("formula")).setCellValue(parm.getFormula());
				if ( isNotNull(parm.getUncertainty()) && isNotNull(parm.getUncertainty().getParameter1Value()))
					r.getCell(cols.get("min")).setCellValue(parm.getUncertainty().getParameter1Value());
				if ( isNotNull(parm.getUncertainty()) && isNotNull(parm.getUncertainty().getParameter3Value()))
					r.getCell(cols.get("max")).setCellValue(parm.getUncertainty().getParameter3Value());
				if ( isNotNull(parm.getUncertainty()) && isNotNull(parm.getUncertainty().getParameter2Value()))
					r.getCell(cols.get("expected")).setCellValue(parm.getUncertainty().getParameter2Value());
				if ( isNotNull(parm.getUncertainty()) && isNotNull(parm.getUncertainty().getDistributionType()))
					r.getCell(cols.get("uncertainty")).setCellValue(parm.getUncertainty().getDistributionType().toString());
			
			}
		}
		catch ( Exception e) {
			throw new TemplateExportException("Unexpected error occured",e);
		}

	}
	
}
