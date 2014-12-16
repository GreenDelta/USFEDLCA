package gov.usda.nal.lci.template.importer;
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
import org.openlca.core.model.FlowType;

class FlowTypes {

	private FlowTypes() {
	}

	public static FlowType forInputGroup(int inputGroup) {
		switch (inputGroup) {
		case 1:
			return FlowType.PRODUCT_FLOW;
		case 2:
			return FlowType.PRODUCT_FLOW;
		case 3:
			return FlowType.PRODUCT_FLOW;
		case 4:
			return FlowType.ELEMENTARY_FLOW;
		case 5:
			return FlowType.PRODUCT_FLOW;
		default:
			return null;
		}
	}
	
	public static FlowType forOutputGroup(int outputGroup) {
		switch (outputGroup) {
		case 0:
			return FlowType.PRODUCT_FLOW;
		case 1:
			return FlowType.PRODUCT_FLOW;
		case 2:
			return FlowType.PRODUCT_FLOW;
		case 3:
			return FlowType.WASTE_FLOW;
		case 4:
			return FlowType.ELEMENTARY_FLOW;
		default:
			return null;
		}
	}
}
