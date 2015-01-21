package gov.usda.nal.lci.template.support;
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
import java.util.List;

import gov.usda.nal.lci.template.domain.Exchange;
import gov.usda.nal.lci.template.domain.Parameter;
import gov.usda.nal.lci.template.domain.Person;
import gov.usda.nal.lci.template.domain.ProcessInformation;
import gov.usda.nal.lci.template.domain.ReferenceFunction;
import gov.usda.nal.lci.template.domain.Source;
import gov.usda.nal.lci.template.domain.Allocation;
import gov.usda.nal.lci.template.support.IDataSet;

public class UsdaTemplateDataSet implements IDataSet {

	private List<Person> persons;
	private List<Source> sources;
	private ProcessInformation process;
	private List<Exchange> exchanges;
	private ReferenceFunction refFunction;
	private List<Parameter> parameters;
	private List<Allocation> allocations;

	public UsdaTemplateDataSet() {

	}

	public void initializeData(List<Person> persons, List<Source> sources,
			ProcessInformation process, List<Exchange> exchanges,
			List<Parameter> parameters,List<Allocation> allocations,ReferenceFunction refFunction) {
		
		this.persons = persons;
		this.sources = sources;
		this.process = process;
		this.exchanges = exchanges;
		this.parameters = parameters;
		this.refFunction = refFunction;
		this.allocations=allocations;
	}

	public ReferenceFunction getReferenceFunction() {
		return this.refFunction;
	}

	public List<Person> getPersons() {
		return this.persons;
	}

	public List<Source> getSources() {
		return this.sources;
	}

	public ProcessInformation getProcessInformation() {
		return this.process;
	}

	public List<Exchange> getExchanges() {
		return this.exchanges;
	}

	public List<Parameter> getParameters() {
		return this.parameters;
	}
	public List<Allocation> getAllocations()
	{
		return this.allocations;
	}

}
