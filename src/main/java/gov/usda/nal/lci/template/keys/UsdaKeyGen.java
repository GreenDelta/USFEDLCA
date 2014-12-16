package gov.usda.nal.lci.template.keys;
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

import gov.usda.nal.lci.template.domain.Exchange;
import gov.usda.nal.lci.template.domain.Person;
import gov.usda.nal.lci.template.support.IDataSet;
import org.openlca.io.KeyGen;

/**
 * Generator for name-based UUIDs (version 3) for EcoSpold 01 entities.
 * 
 * See <a href="http://openlca.org/documentation/index.php/OpenLCA_Help">
 * http://openlca.org/documentation/index.php/OpenLCA_Help</a> TODO: update link
 */
public class UsdaKeyGen {

	private UsdaKeyGen() {
	}

	public static String forFlow(Exchange exchange) {
		if (exchange == null)
			return KeyGen.get("");
		if (exchange.isElementaryFlow())
			return forElementaryFlow(exchange);
		return forProductFlow(exchange);
	}

	public static String forElementaryFlow(Exchange exchange) {
		if (exchange == null)
			return KeyGen.get("");
		String[] vals = new String[4];
		vals[0] = exchange.getCategory();
		vals[1] = exchange.getSubCategory();
		vals[2] = exchange.getFlowName();
		vals[3] = exchange.getUnit();
		return KeyGen.get(vals);
	}

	public static String forProductFlow(Exchange exchange) {
		if (exchange == null)
			return KeyGen.get("");
		String[] vals = new String[6];
		vals[0] = exchange.getCategory();
		vals[1] = exchange.getSubCategory();
		vals[2] = exchange.getFlowName();
		vals[3] = exchange.getUnit();
		vals[4] = exchange.getLocation() != null ? exchange.getLocation()
				: "GLO";
		boolean inf = exchange.isInfrastructureProcess(); //ask
		vals[5] = inf ? "true" : "false";
		return KeyGen.get(vals);
	}

/*	public static String forProduct(DataSet dataSet) {
		if (dataSet == null || dataSet.getReferenceFunction() == null)
			return KeyGen.get("");
		String[] vals = new String[6];
		fillDatasetAttributes(dataSet, vals);
		return KeyGen.get(vals);
	}
*/
	public static String forProcess(IDataSet dataSet) {
		if (dataSet == null || dataSet.getReferenceFunction() == null)
			return KeyGen.get("");
		String[] vals = new String[7];
		vals[0] = dataSet.getReferenceFunction().getCategory();	    
		vals[1] = dataSet.getReferenceFunction().getSubcategory();   
		vals[2] = dataSet.getReferenceFunction().getBaseName();		
		vals[3] = dataSet.getReferenceFunction().getUnit();      
		//String location = dataSet.getProcessInformation().getGeography() != null ? dataSet.
				//getProcessInformation().getGeography().getLocation() : "GLO";
		vals[4] = dataSet.getReferenceFunction().getLocation();
		boolean inf = dataSet.getReferenceFunction().isInfrastructureProcess(); 
		vals[5] = inf ? "true" : "false";
		vals[6] = dataSet.getReferenceFunction().getProcessType();
		return KeyGen.get(vals);
	}

	public static String forPerson(Person person) {
		if (person == null)
			return KeyGen.get("");
		return KeyGen.get(person.getName(), person.getAddress());
	}

	public static String forSource(gov.usda.nal.lci.template.domain.Source source) {
		if (source == null)
			return KeyGen.get("");
		String[] vals = new String[3];
		vals[0] = source.getFirstAuthor();
		vals[1] = source.getTextReference();  //title
		vals[2] = source.getYear() != null ? source.getYear() : "";
		return KeyGen.get(vals);
	}
	
	
	

}
