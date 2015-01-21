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
import org.openlca.core.model.FlowType;
import org.openlca.core.model.Process;
import org.openlca.core.model.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Gary.Moore
 *
 */
public class InputOutputSheet extends TemplateSheet {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final Map<String,Short> cols;
	static
	{
		cols=new HashMap<String,Short>();
		cols.put("ig", Short.valueOf("1"));
		cols.put("og", Short.valueOf("2"));
		cols.put("flow", Short.valueOf("3"));
		cols.put("category", Short.valueOf("4"));
		cols.put("subcategory", Short.valueOf("5"));
		cols.put("location", Short.valueOf("6"));
		cols.put("amount", Short.valueOf("7"));
		cols.put("unit", Short.valueOf("8"));
		cols.put("param", Short.valueOf("9"));
		cols.put("provider", Short.valueOf("10"));
		cols.put("uncertainty", Short.valueOf("18"));
		cols.put("cas", Short.valueOf("19"));
		cols.put("description", Short.valueOf("20"));
		cols.put("formula", Short.valueOf("21"));
		cols.put("fproperty", Short.valueOf("22"));
		cols.put("refunit", Short.valueOf("23"));
		cols.put("conversion", Short.valueOf("24"));
		cols.put("distribution", Short.valueOf("25"));
		cols.put("expected", Short.valueOf("26"));
		cols.put("dispersion", Short.valueOf("27"));
		cols.put("min", Short.valueOf("28"));
		cols.put("max", Short.valueOf("29"));
		cols.put("allocation-physical", Short.valueOf("30"));
		cols.put("allocation-economic", Short.valueOf("31"));
		cols.put("allocation-casual", Short.valueOf("32"));
		
	}
	public InputOutputSheet(Sheet s)
	{
		super(s);
	}
	/* (non-Javadoc)
	 * @see gov.usda.nal.lci.template.exporter.TemplateSheet#updateSheet(org.openlca.core.model.Process, org.apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	public void updateSheet(Process p) throws TemplateExportException {
	
		Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		try {
			int fr=3;
			int rn=fr;
			
			
			for (Exchange e : p.getExchanges() )
			{
				Row r=s.getRow(rn++);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(fr),rn-1);
				else if ( r.getCell(cols.get("ig")) == null ) // add cells to the row if it has none
					insertCells(s.getRow(fr),r);
				if ( e.isInput() )
					r.getCell(cols.get("ig")).setCellValue(getInputGroup(e));
				else
					r.getCell(cols.get("og")).setCellValue(getOutputGroup(e,p.getQuantitativeReference().getId()));
				r.getCell(cols.get("flow")).setCellValue(e.getFlow().getName());
				r.getCell(cols.get("category")).setCellValue(getParentCategory(e.getFlow().getCategory()));
				r.getCell(cols.get("subcategory")).setCellValue(getSubCategory(e.getFlow().getCategory()));
				if ( isNotNull(e.getFlow().getLocation()))
					r.getCell(cols.get("location")).setCellValue(e.getFlow().getLocation().getCode());
				r.getCell(cols.get("amount")).setCellValue(e.getAmountValue());
				r.getCell(cols.get("unit")).setCellValue(e.getUnit().getName());
				r.getCell(cols.get("param")).setCellValue(e.getAmountFormula());
				r.getCell(cols.get("provider")).setCellValue(e.getDefaultProviderId());
				r.getCell(cols.get("cas")).setCellValue(e.getFlow().getCasNumber());
				r.getCell(cols.get("formula")).setCellValue(e.getFlow().getFormula());
				r.getCell(cols.get("description")).setCellValue(e.getFlow().getDescription());
				r.getCell(cols.get("uncertainty")).setCellValue(e.getPedigreeUncertainty());
				r.getCell(cols.get("fproperty")).setCellValue(e.getFlow().getReferenceFlowProperty().getName());
				r.getCell(cols.get("refunit")).setCellValue(e.getFlow().getReferenceFlowProperty().getUnitGroup().getReferenceUnit().getName());
				if ( isNotNull(e.getUncertainty()) && isNotNull(e.getUncertainty().getParameter1Value()))
					r.getCell(cols.get("min")).setCellValue(e.getUncertainty().getParameter1Value());
				if ( isNotNull(e.getUncertainty()) && isNotNull(e.getUncertainty().getParameter3Value()))
					r.getCell(cols.get("max")).setCellValue(e.getUncertainty().getParameter3Value());
				if ( isNotNull(e.getUncertainty()) && isNotNull(e.getUncertainty().getParameter2Value()))
					r.getCell(cols.get("expected")).setCellValue(e.getUncertainty().getParameter2Value());
				if ( isNotNull(e.getUncertainty()) && isNotNull(e.getUncertainty().getDistributionType()))
					r.getCell(cols.get("distribution")).setCellValue(e.getUncertainty().getDistributionType().toString());
			}
		}
		catch ( Exception e) {
			throw new TemplateExportException("Unexpected error occured",e);
		}
		

	}
	
}
