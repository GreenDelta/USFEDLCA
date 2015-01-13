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
/*
 * split' MetaDataVO' meta data view object to processInformationVO,modellingAndValidationVO,
 * 	sourceDataVO, administrationInformationVO
 * 
 * convert values form VO to pojo
 * 
 */
import gov.usda.nal.lci.template.excel.MappingCells;
import gov.usda.nal.lci.template.support.IDataSet;
import gov.usda.nal.lci.template.support.UsdaTemplateDataSet;
import gov.usda.nal.lci.template.vo.ActorVO;
import gov.usda.nal.lci.template.vo.AdministrativeInformationVO;
import gov.usda.nal.lci.template.vo.CostsVO;
import gov.usda.nal.lci.template.vo.ExchangeDataVO;
import gov.usda.nal.lci.template.vo.ISICDataVO;
import gov.usda.nal.lci.template.vo.ModelingAndValidationVO;
import gov.usda.nal.lci.template.vo.ParametersVO;
import gov.usda.nal.lci.template.vo.ProcessInformationVO;
import gov.usda.nal.lci.template.vo.SourceInformationVO;
import gov.usda.nal.lci.template.vo.AllocationVO;
import gov.usda.nal.lci.template.domain.AdministrativeInformation;
import gov.usda.nal.lci.template.domain.Costs;
import gov.usda.nal.lci.template.domain.InternationalStandardIndustrialClassification;
import gov.usda.nal.lci.template.domain.ModellingAndValidation;
import gov.usda.nal.lci.template.domain.Parameter;
import gov.usda.nal.lci.template.domain.Person;
import gov.usda.nal.lci.template.domain.ProcessName;
import gov.usda.nal.lci.template.domain.ReferenceFunction;
import gov.usda.nal.lci.template.domain.Source;
import gov.usda.nal.lci.template.domain.Exchange;
import gov.usda.nal.lci.template.domain.Geography;
import gov.usda.nal.lci.template.domain.ProcessInformation;
import gov.usda.nal.lci.template.domain.Technology;
import gov.usda.nal.lci.template.domain.Time;
import gov.usda.nal.lci.template.domain.Allocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlca.core.model.AllocationMethod;
import org.openlca.core.model.UncertaintyType;
/**
 * <code>ProcessVOData</code> is a class for transferring the contents of the worksheets which have been
 * stored in "VO" objects to "domain" objects which are then written to the database in {@link
 * gov.usda.nal.lci.template.importer.USFedLCATemplateImport}.
 * @author Y.Radchenko
 *
 */
public class ProcessVOData {

	private static final Log LOG = LogFactory.getLog(ProcessVOData.class);
	private ProcessInformationVO generalInfoVO;
	private AdministrativeInformationVO admVO;
	private ModelingAndValidationVO modVO;
	private List<ExchangeDataVO> exchangeDataVO;
	private List<ParametersVO> parameterVO;
	private List<ActorVO> actorVO;
	private List<SourceInformationVO> sourceVO;
	private List<CostsVO> costsVO;
	private List<AllocationVO> allocationVO;
	private IDataSet dataSet = new UsdaTemplateDataSet();

	public IDataSet getDataSet() {
		return dataSet;
	}

	/**
	 * @param exchangeDataVO
	 *            List<ExchangeDataVO> includes exchanges from spreadsheet
	 * @param parameterVO
	 *            List<ParametersVO> includes parameters from spreadsheet
	 * @param metaDataVO
	 *            List<MetaDataVO> includes meta data spreadsheet
	 */
	public ProcessVOData(List<ExchangeDataVO> exchangeDataVO,
			ProcessInformationVO generalInfoVO,
			AdministrativeInformationVO admVO, ModelingAndValidationVO modVO,
			List<ParametersVO> parameterVO, List<AllocationVO> allocationVO,
			List<ActorVO> actorVO, List<SourceInformationVO> sourceVO,
			List<CostsVO> costsVO) {
		
		this.exchangeDataVO = exchangeDataVO;
		this.generalInfoVO  = generalInfoVO;
		this.admVO = admVO;
		this.modVO = modVO;
		this.parameterVO = parameterVO;
		this.allocationVO=allocationVO;
		this.actorVO  = actorVO;
		this.sourceVO = sourceVO;
		this.costsVO  = costsVO;
	}
	/**
	 * Constructor which uses {@link gov.usda.nal.lci.template.excel.MappingCells} objects
	 * @param cells
	 */
public ProcessVOData(MappingCells cells){
	this.exchangeDataVO = cells.getExchangeData();
	this.generalInfoVO  = cells.getProcessData();
	this.admVO = cells.getAdministrationData();
	this.modVO = cells.getModelingAndValidationData();
	this.parameterVO = cells.getParametersData();
	this.actorVO  = cells.getActorData();
	this.sourceVO = cells.getSourceInformationData();
	this.costsVO  = cells.getCostsDataVO();
	this.allocationVO=cells.getAllocationVO();
}
/**
 * <code>run</code> converts VO objects which have been parsed from the worksheets into "domain" objects.  The domain objects
 * are then assigned to the dataSet and later written to the database 
 * @see gov.usda.nal.lci.template.importer.USFedLCATemplateImport#parseProcessDataSet.
 */
	public void run() {
		AdministrativeInformation adm = makeAdministrativeInformation(admVO);
		ModellingAndValidation mod    = makeModelingAndValidation(modVO);
		List<Parameter> parameters    = makeParameter(parameterVO);
		List<Person> persons          = makeActorInfo(actorVO);
		List<Source> sources          = makeSourceInfo(sourceVO);
		List<Costs> costs			  = makeCosts(costsVO);
		ProcessInformation process    = makeProcessInfo(generalInfoVO, parameters, mod, adm, costs);
		List<Exchange> exchanges      = makeExchange(exchangeDataVO, generalInfoVO);
		List<Allocation> allocations = makeAllocationList(allocationVO);
		ReferenceFunction refFunction = makeReferenceFunction(process,
				exchanges, modVO);
		dataSet.initializeData(persons, sources, process, exchanges,
				parameters, allocations,refFunction);

	}

	/**
	 * @param process
	 *            ProcessInformation
	 * @param exchanges
	 *            List<Exchange>
	 * @param modVO
	 *            ModelingAndValidationVO
	 * @return ReferenceFunction
	 */
	public ReferenceFunction makeReferenceFunction(ProcessInformation process,
			List<Exchange> exchanges, ModelingAndValidationVO modVO) {
		ReferenceFunction refFunction = new ReferenceFunction();

		refFunction.setBaseName(process.getProcessName().getBaseName());
		refFunction.setCategory(process.getIsic().getCategory());
		refFunction.setSubcategory(process.getIsic().getSubcategory());
		String location = process.getGeography().getLocation() != null ? process
				.getGeography().getLocation() : "GLO";
		refFunction.setLocation(location);

		for (Exchange exchange : exchanges) {
			if ((exchange.getCategory() != null && exchange.getCategory().equalsIgnoreCase(refFunction.getCategory()))
					&& (exchange.getSubCategory() != null && exchange.getSubCategory().equalsIgnoreCase(
							refFunction.getSubcategory()))) {
				refFunction.setUnit(exchange.getUnit());
			}

		}
		refFunction.setProcessType(modVO.getProcessType());
		refFunction.setGeneralComment(process.getDescription());
		refFunction
				.setInfrastructureProcess(process.isInfrastructureProcess());
		return refFunction;

	}

	/**
	 * @param voList
	 * @return list of ParametersVO objects
	 */
	private List<Parameter> makeParameter(List<ParametersVO> voList) {

		List<Parameter> parameterList = new ArrayList<Parameter>();

		for (ParametersVO vo : voList) {

			Parameter parameter = new Parameter();

			if (StringUtils.isNotBlank(vo.getName())) {
				parameter.setName(vo.getName());
			}

			if (StringUtils.isNotBlank(vo.getDescription())) {
				parameter.setDescription(vo.getDescription());
			}

			if (StringUtils.isNotBlank(vo.getFormula())) {
				parameter.setFormula(vo.getFormula());
			}

			if (StringUtils.isNotBlank(vo.getResultingValue())) {
				parameter.setValue(vo.getResultingValue());
			}

			if (StringUtils.isNotBlank(vo.getUncertaintyType())) {

				parameter.setUncertaintyType(this.convertUncertaintyType(vo.getUncertaintyType()));
			}
			//mean,or goemean
			if (StringUtils.isNotBlank(vo.getExpectedValue())) {
				parameter.setExpectedValue(Double.parseDouble(vo
						.getExpectedValue()));
			}
			//geo std
			if (StringUtils.isNotBlank(vo.getDispersion())) {
				parameter.setDispersion(Double.parseDouble(vo
						.getDispersion()));
			}
			if (StringUtils.isNotBlank(vo.getMaximumValue())) {
				parameter.setMaximumValue(Double.parseDouble(vo
						.getMaximumValue()));
			}
			if (StringUtils.isNotBlank(vo.getMinimumValue())) {
				parameter.setMinimumValue(Double.parseDouble(vo
						.getMinimumValue()));
			}

			parameterList.add(parameter);

		}

		return parameterList;

	}

	/**
	 * @param vo
	 *            ModellingAndValidationVO
	 * @return domain.ModellingAndValidation
	 */
	private ModellingAndValidation makeModelingAndValidation(
			ModelingAndValidationVO vo) {
		ModellingAndValidation mod = new ModellingAndValidation();

		if (StringUtils.isNotBlank(vo.getLciMethods()))
			mod.setLciMethod(vo.getLciMethods());
		
		if (StringUtils.isNotBlank(vo.getModelingConstants()))
			mod.setModelingConstants(vo.getModelingConstants());
		
		if (StringUtils.isNotBlank(vo.getDataCompleteness()))
			mod.setDataCompleteness(vo.getDataCompleteness());

		if (StringUtils.isNotBlank(vo.getMassBalance()))
			mod.setMassBalance(vo.getMassBalance());
		
		if (StringUtils.isNotBlank(vo.getDataSelection()))
			mod.setDataSelection(vo.getDataSelection());

		if (StringUtils.isNotBlank(vo.getDataTreatment()))
			mod.setDataTreatment(vo.getDataTreatment());

		if (StringUtils.isNotBlank(vo.getSamplingProcedure()))
			mod.setSampling(vo.getSamplingProcedure());

		if (StringUtils.isNotBlank(vo.getDataCollectionPeriod()))
			mod.setDataCollectionPeriod(vo.getDataCollectionPeriod());

		if (StringUtils.isNotBlank(vo.getReviewer()))
			mod.setReviewer(vo.getReviewer());

		if (StringUtils.isNotBlank(vo.getDataSetOtherEvaluation()))
			mod.setDataSetOtherEvaluation(vo.getDataSetOtherEvaluation());
		return mod;

	}

	/**
	 * @param vo
	 *            AdministrativeInformationVO
	 * @return domain.AdministrativeInformation
	 */
	private AdministrativeInformation makeAdministrativeInformation(
			AdministrativeInformationVO vo) {

		AdministrativeInformation adm = new AdministrativeInformation();
		adm.setIntendedApplication(vo.getIntendedApplication());
		adm.setDataOwner(vo.getDataOwner());
		adm.setDataDocumentor(vo.getDataDocumentor());
		adm.setDataGenerator(vo.getDataGenerator());
		adm.setPublication(vo.getPublication());
		adm.setAccessUseRestrictions(vo.getAccessUseRestrictions());
		adm.setProject(vo.getProject());
		adm.setVersion(vo.getVersion());
		adm.setCopyright(Boolean.parseBoolean(vo.getCopyright()));
		return adm;

	}

	/**
	 * @param admVO
	 *            AdministrativeInformationVO
	 * @param generalInfoVO
	 *            ProcessInformationVO
	 * @return domain.Person
	 */
	public List<Person> makeActorInfo(List<ActorVO> list) {

		List<Person> persons = new ArrayList<Person>();
		for (ActorVO vo : list) {
			Person person = new Person();
			person.setAddress(vo.getAddress());
			person.setEmail(vo.getEmail());
			person.setName(vo.getName());
			person.setTelephone(vo.getTelephone());
			person.setWebsite(vo.getWebsite());
			person.setDescription(vo.getDescription());
			person.setCity(vo.getCity());
			person.setCountry(vo.getCountry());
			person.setZipcode(vo.getZipcode());
			persons.add(person);
		}
		return persons;
	}

	/**
	 * @param sourceList
	 *            SourceInformationVO
	 * @return domain.Source
	 */
	private List<Source> makeSourceInfo(List<SourceInformationVO> sourceList) {

		List<Source> sources = new ArrayList<Source>();

		for (SourceInformationVO sourceVO : sourceList) {
			if ( sourceVO.getDescription() != null )
			{
				Source source = new Source();
				source.setFirstAuthor(sourceVO.getFirstAuthor());
				source.setYear(sourceVO.getYear());
				source.setDescription(sourceVO.getDescription());
				source.setTextReference(sourceVO.getTextReference());
				source.setDoi(sourceVO.getDoi());
				sources.add(source);
			}

		}
		return sources;

	}
	
	/**
	 * @param costsList
	 * @return domain.costs
	 */
	private List<Costs> makeCosts(List<CostsVO> costsList) {

		List<Costs> costs = new ArrayList<Costs>();

		for (CostsVO costVO : costsList) {
			if ( costVO.getAmount() != null )
			{
				Costs cost = new Costs();
				cost.setCostCategory(costVO.getCostCategory());
				cost.setAmount(Double.parseDouble(costVO.getAmount()));
				cost.setCostFixed(Boolean.parseBoolean(costVO.getCostFixed()));
				costs.add(cost);
			}
			else
				LOG.info("Cost amount is blank and will not be added to the database "+costVO.getCostCategory());
		}
		return costs;

	}

	/**
	 * 
	 * @param generalInfoVO
	 *            ProcessInformationVO
	 * @param isic
	 *            InternationalStandardIndustrialClassification
	 * @param parameters
	 *            Parameter object
	 * @param modelingAndValidation
	 *            ModellingAndValidation
	 * @param administrativeInformation
	 *            AdministrativeInformation
	 * @return domain.ProcessInformation object
	 */
	public ProcessInformation makeProcessInfo(
			ProcessInformationVO generalInfoVO,
			List<Parameter> parameters,
			ModellingAndValidation modelingAndValidation,
			AdministrativeInformation admInformation, List<Costs> costs) {
		ProcessInformation process = new ProcessInformation();
		process.setVersion(admInformation.getVersion());
		ProcessName processName = new ProcessName();
		processName.setBaseName(generalInfoVO.getBaseName());
		processName.setTreatmentStandardsRoutes(generalInfoVO
				.getTreatmentStandardsRoutes());
		processName.setLocationType(generalInfoVO.getLocationType());
		processName.setMixType(generalInfoVO.getMixType());
		processName.setQuantitativeFlowProperties(generalInfoVO
				.getQuantitativeProcessProperties());
		process.setInfrastructureProcess(generalInfoVO
				.getInfrastructureProcess());
		process.setProcessName(processName);

		process.setDescription(generalInfoVO.getDescription());
		Time timeinfo = new Time();
		timeinfo.setStartDate(convertDate(generalInfoVO.getStartDate()));
		timeinfo.setEndDate(convertDate(generalInfoVO.getEndDate()));
		
		timeinfo.setTimeComment(generalInfoVO.getTimeComment());
		process.setTimeInfo(timeinfo);
		
		Geography geography = new Geography();
		geography.setLocation(generalInfoVO.getLocation());
		geography.setText(generalInfoVO.getGeographyComment());
		process.setGeography(geography);

		process.setIsic(new InternationalStandardIndustrialClassification(generalInfoVO.getIsicVO().getIsicCode(),generalInfoVO.getIsicVO().getCategory(),generalInfoVO.getIsicVO().getSubcategory()));
		process.setDescription(generalInfoVO.getDescription());

		Technology technology = new Technology();
		technology.setText(generalInfoVO.getTechnologyComment());
		process.setTechnology(technology);

		process.setAdministrativeInformation(admInformation);
		process.setModelingAndValidation(modelingAndValidation);
		process.setParameters(parameters);
		process.setCosts(costs);

		return process;

	}
	/**
	 * Transfers the contents which have been parsed from the template's Allocation worksheet and stored as {@link gov.usda.nal.lci.template.vo.AllocationVO} objects 
	 * into a list of {@link gov.usda.nal.lci.template.domain.Allocation} objects.  The domain object list is then used to build AllocationFactors objects before insertion into 
	 * the database in {@link gov.usda.nal.lci.template.importer.USFedLCATemplateImport#mapAllocations}.
	 *
	 * Important: There are "hardcoded" dependencies in the AllocationVO list and the cell locations from which they are derived in the
	 * template's Allocation worksheet.  If the cell locations change in the worksheet then most likely the
	 * structure and locations of the AllocationVO list will need to change.  For example:  co-products are located in offset 0 of the AllocationVO list because that's the order in which they
	 * appear in the worksheet.
	 * @param allocationVO
	 * @return List<Allocation> -- list of Allocation objects
	 */
	private List<Allocation> makeAllocationList(List<AllocationVO> allocationVO)
	{
		List<Allocation> allocations=new ArrayList<Allocation>();
		allocations.addAll(makeAllocations(allocationVO.get(1),allocationVO.get(0),AllocationMethod.PHYSICAL));
		allocations.addAll(makeAllocations(allocationVO.get(2),allocationVO.get(0),AllocationMethod.ECONOMIC));
		allocations.addAll(makeCausalAllocations(allocationVO,allocationVO.get(0)));
		return allocations;
	}
	/**
	 * <code>makeCausalAlloctions</code> creates a list of {@link gov.usda.nal.lci.template.domain.Allocation}
	 * objects with an AllocationMethod of CAUSAL.
	 * @param avol
	 * @param coprods
	 * @return
	 */
	private List<Allocation> makeCausalAllocations(List<AllocationVO> avol,AllocationVO coprods)
	{
		List<Allocation> al=new ArrayList<Allocation>();
		
		if ( coprods.getCoprod() != null )
		{
			for ( int i=7;i<avol.size();i++)
			{
				if ( avol.get(i).getFlowName() != null )
					al.addAll(makeAllocations(avol.get(i),coprods,AllocationMethod.CAUSAL));
			}
		}
		return al;
	}
	/**
	 * For each co-product which has been parsed from the template's Allocation worksheet, we build an
	 * {@link gov.usda.nal.lci.template.domain.Allocation} object and add it to the list.  Note that co-products
	 * and exchanges (flows) are stored as String objects which will are used later to link to exchanges
	 * prior to insertion of AllocationFactors into the database.  
	 * @
	 * @param avo  -- the list of AllocationVO objects parsed from the template worksheet
	 * @param coprods -- the list of coproducts parsed from the AllocationVO list at offset 0
	 * @param method
	 * @return
	 */
	private List<Allocation> makeAllocations(AllocationVO avo,AllocationVO coprods,AllocationMethod method)
	{
		List<Allocation> al=new ArrayList<Allocation>();
		if ( avo.getCoprod() != null ) {
			Allocation a=new Allocation();
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setAllocationMethod(method);
			a.setReferenceToCoProduct(coprods.getCoprod());
			a.setValue(Float.valueOf(avo.getCoprod()));
			al.add(a);
		}
		if ( avo.getCoprod1() != null ) {
			Allocation a=new Allocation();
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setAllocationMethod(method);
			a.setReferenceToCoProduct(coprods.getCoprod1());
			a.setValue(Float.valueOf(avo.getCoprod1()));
			al.add(a);
		}
		if ( avo.getCoprod2() != null ) {
			Allocation a=new Allocation();
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setAllocationMethod(method);
			a.setReferenceToCoProduct(coprods.getCoprod2());
			a.setValue(Float.valueOf(avo.getCoprod2()));
			al.add(a);
		}
		if ( avo.getCoprod3() != null ) {
			Allocation a=new Allocation();
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setAllocationMethod(method);
			a.setReferenceToCoProduct(coprods.getCoprod3());
			a.setValue(Float.valueOf(avo.getCoprod3()));
			al.add(a);
		}
		if ( avo.getCoprod4() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod4());
			a.setValue(Float.valueOf(avo.getCoprod4()));
			al.add(a);
		}
		if ( avo.getCoprod5() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod5());
			a.setValue(Float.valueOf(avo.getCoprod5()));
			al.add(a);
		}
		if ( avo.getCoprod6() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod6());
			a.setValue(Float.valueOf(avo.getCoprod6()));
			al.add(a);
		}
		if ( avo.getCoprod7() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod7());
			a.setValue(Float.valueOf(avo.getCoprod7()));
			al.add(a);
		}
		if ( avo.getCoprod8() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod8());
			a.setValue(Float.valueOf(avo.getCoprod8()));
			al.add(a);
		}
		if ( avo.getCoprod9() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod9());
			a.setValue(Float.valueOf(avo.getCoprod9()));
			al.add(a);
		}
		if ( avo.getCoprod10() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod10());
			a.setValue(Float.valueOf(avo.getCoprod10()));
			al.add(a);
		}
		if ( avo.getCoprod11() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod11());
			a.setValue(Float.valueOf(avo.getCoprod11()));
			al.add(a);
		}
		if ( avo.getCoprod12() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod12());
			a.setValue(Float.valueOf(avo.getCoprod12()));
			al.add(a);
		}
		if ( avo.getCoprod13() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			a.setReferenceToCoProduct(coprods.getCoprod13());
			a.setValue(Float.valueOf(avo.getCoprod13()));
			al.add(a);
		}
		if ( avo.getCoprod14() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod14());
			a.setValue(Float.valueOf(avo.getCoprod14()));
			al.add(a);
		}
		if ( avo.getCoprod15() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod15());
			a.setValue(Float.valueOf(avo.getCoprod15()));
			al.add(a);
		}
		if ( avo.getCoprod16() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod16());
			a.setValue(Float.valueOf(avo.getCoprod16()));
			al.add(a);
		}
		if ( avo.getCoprod17() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod17());
			a.setValue(Float.valueOf(avo.getCoprod17()));
			al.add(a);
		}
		if ( avo.getCoprod18() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod18());
			a.setValue(Float.valueOf(avo.getCoprod18()));
			al.add(a);
		}
		if ( avo.getCoprod19() != null ) {
			Allocation a=new Allocation();
			a.setAllocationMethod(method);
			if ( avo.getFlowName() != null )
				a.setReferenceToInputOutput(avo.getFlowName());
			a.setReferenceToCoProduct(coprods.getCoprod19());
			a.setValue(Float.valueOf(avo.getCoprod19()));
			al.add(a);
		}
		return al;
	}
	/**
	 * mapping domain.Exchange fields
	 * 
	 * @param voList
	 *            stores all exchange information
	 * @return /domain.Exchange
	 */
	public List<Exchange> makeExchange(List<ExchangeDataVO> voList,
			ProcessInformationVO generalInfoVO) {

		List<Exchange> exchanges = new ArrayList<Exchange>();

		for (ExchangeDataVO vo : voList) {

			Exchange exchange = new Exchange();
			if (StringUtils.isNotBlank(vo.getInputGroup())) {
				exchange.setInputGroup(Integer.parseInt(vo.getInputGroup()));
			}
			if (StringUtils.isNotBlank(vo.getOutputGroup())) {
				exchange.setOutputGroup(Integer.parseInt(vo.getOutputGroup()));
			}
			if (StringUtils.isNotBlank(vo.getFlowName())) {
				exchange.setFlowName(vo.getFlowName());
			}
			if (StringUtils.isNotBlank(vo.getCategory())) {
				exchange.setCategory(vo.getCategory());
			}
			if (StringUtils.isNotBlank(vo.getSubCategory())) {
				exchange.setSubCategory(vo.getSubCategory());
			}
			if (StringUtils.isNotBlank(vo.getUnit())) {
				exchange.setUnit(vo.getUnit());
			}
			if (StringUtils.isNotBlank(vo.getAmount())) {
				exchange.setAmountValue(Double.parseDouble(vo.getAmount()));
			}
			if (StringUtils.isNotBlank(vo.getParameterName())) {
				exchange.setParameterName(vo.getParameterName());
			}
			if (StringUtils.isNotBlank(vo.getProvider())) {
				exchange.setDefaultProvider(vo.getProvider());
			}
			if (StringUtils.isNotBlank(vo.getDataQualityComment())) {
				exchange.setDataQualityComment(vo.getDataQualityComment());
			}
			if (StringUtils.isNotBlank(vo.getFlowLocation())) {
				exchange.setLocation(vo.getFlowLocation());
			}
			if (StringUtils.isNotBlank(vo.getCasNumber())) {
				exchange.setCasNumber(vo.getCasNumber());
			}
			if (StringUtils.isNotBlank(vo.getShortDescription())) {
				exchange.setDescription(vo.getShortDescription());
			}
			if (StringUtils.isNotBlank(vo.getFormula())) {
				exchange.setFormula(vo.getFormula());
			}
			if (StringUtils.isNotBlank(vo.getUnitNameFlowPropertyName())) {
				exchange.setUnitFlowPropertyName(vo
						.getUnitNameFlowPropertyName());
			}
			if (StringUtils.isNotBlank(vo.getRefUnit())) {
				exchange.setReferenceUnit(vo.getRefUnit());
			}
			if (StringUtils.isNotBlank(vo.getConversionFactor())) {
				exchange.setConversionFactor(Double.parseDouble(vo
						.getConversionFactor()));
			}

			if (StringUtils.isNotBlank(vo.getUncertaintyType())) {

				 exchange.setUncertaintyType(convertUncertaintyType(vo.getUncertaintyType()));
			}
	
			if (StringUtils.isNotBlank(vo.getExpectedValue())) {
				exchange.setExpectedValue(Double.parseDouble(vo
						.getExpectedValue()));
			}
			if (StringUtils.isNotBlank(vo.getDispersion())) {
				exchange.setDispersion(Double.parseDouble(vo.getDispersion()));
			}
			if (StringUtils.isNotBlank(vo.getMinimumValue())) {
				exchange.setMinValue(Double.parseDouble(vo.getMinimumValue()));
			}
			if (StringUtils.isNotBlank(vo.getMaximumValue())) {
				exchange.setMaxValue(Double.parseDouble(vo.getMaximumValue()));
			}
			if (StringUtils.isNotBlank(vo.getPhysical())) {
				exchange.setPhysical(Double.parseDouble(vo.getPhysical()));
			}
			if (StringUtils.isNotBlank(vo.getEconomic())) {
				exchange.setEconomic(Double.parseDouble(vo.getEconomic()));
			}
			if (StringUtils.isNotBlank(vo.getCausal())) {
				exchange.setCausal(Double.parseDouble(vo.getCausal()));
			}

			exchange.setInfrastructureProcess(Boolean.valueOf(generalInfoVO
					.getInfrastructureProcess()));

			// set up isElementaryFlow:
			if (exchange.getInputGroup() == null
					&& exchange.getOutputGroup() == null) {
				exchange.setElementaryFlow(true);
			} else if (exchange.getInputGroup() != null
					&& exchange.getInputGroup() == 4) {
				exchange.setElementaryFlow(true);
			} else if (exchange.getOutputGroup() != null
					&& exchange.getOutputGroup() == 4) {
				exchange.setElementaryFlow(true);
			} else
				exchange.setElementaryFlow(false);

			exchange.setStandardDeviation95(null);
			exchange.setMostLikelyValue(null);// ?? need in ExchangeAmount.java
			exchanges.add(exchange);

		}
		return exchanges;
	}


	/**
	 * Convert String date to Date
	 * 
	 * @param sDate
	 *            String
	 * @return Date
	 */
	@SuppressWarnings("static-access")
	private Date convertDate(String sDate) {

		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(sDate);

		} catch (Exception e) {
			LOG.error("Cannot convert date: " + sDate, e);
			return null;
		}
	}
	private UncertaintyType convertUncertaintyType(String type)
	{
		UncertaintyType uncertaintyType=null;
		if ( StringUtils.isNotBlank(type))
		{
			String t=type.toUpperCase();
			if ( t.contains("LOG"))
				uncertaintyType=UncertaintyType.LOG_NORMAL;
			else if (t.contains("NORMAL") )
				uncertaintyType=UncertaintyType.NORMAL;
			else if ( t.contains("UNIFORM"))
				uncertaintyType=UncertaintyType.NORMAL;
			else if ( t.contains("TRIANGLE"))
				uncertaintyType=UncertaintyType.TRIANGLE;
			else if ( t.contains("NONE") || t.contains("NO "))
				uncertaintyType=UncertaintyType.NONE;
		}
		return uncertaintyType;
	}
	
}
