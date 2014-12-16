package gov.usda.nal.lci.template.importer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openlca.core.TestSession;
import org.openlca.io.maps.MapType;
import org.openlca.core.model.Actor;
import org.openlca.core.model.Exchange;
import org.openlca.core.model.Location;
import org.openlca.core.model.Process;
import org.openlca.core.model.ProcessDocumentation;
import org.openlca.core.model.Source;
import org.openlca.io.maps.FlowMap;
import org.openlca.io.UnitMapping;
import org.openlca.io.KeyGen;

import gov.usda.nal.lci.template.domain.Geography;
import gov.usda.nal.lci.template.domain.ProcessInformation;
import gov.usda.nal.lci.template.domain.ReferenceFunction;
import gov.usda.nal.lci.template.keys.UsdaKeyGen;
import gov.usda.nal.lci.template.support.IDataSet;
import gov.usda.nal.lci.template.support.UsdaTemplateDataSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Test;
import org.openlca.core.database.IDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class UsdaTemplateImportTest {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private IDatabase mysqlDatabase = TestSession.getMySQLDatabase();
	private DB db = new DB(mysqlDatabase);

	@Test
	public void testInsertPersons() {
		gov.usda.nal.lci.template.domain.Person person = new gov.usda.nal.lci.template.domain.Person();
		person.setName("Test Actor");
		person.setAddress("NAL");
		person.setCity("Beltsville");
		String genKey = UsdaKeyGen.forPerson(person);
		if (db.findActor(person, genKey) != null) {
			log.info("Actor {} already exists", genKey);
		} else {
			try {
				Actor actor = new Actor();
				actor.setRefId(genKey);
				Mapper.mapPerson(person, actor);
				db.put(actor, genKey);
				log.info("Person {} has been saved ", genKey);
			} catch (Exception e) {
				log.info("<<Error save Actor " + e);
			}
		}

	}

	@Test
	public void testInsertSources() {
		gov.usda.nal.lci.template.domain.Source isource = new gov.usda.nal.lci.template.domain.Source();
		String firstAuthor = "John Smith";
		String textReference = "text reference";
		String year = "2001";

		isource.setFirstAuthor(firstAuthor);
		isource.setTextReference(textReference);
		isource.setYear(year);

		String genKey = UsdaKeyGen.forSource(isource);
		if (db.findSource(isource, genKey) != null) {
			log.info("Source {} already exists", genKey);

		} else {
			try {
				Source oSource = new Source();
				oSource.setRefId(genKey);
				Mapper.mapSource(isource, oSource);
				db.put(oSource, genKey);
				log.info("Source {} has been saved ", genKey);
			} catch (Exception e) {
				log.info("<<Error save Source " + e);
			}
		}

	}

	@Test
	public void testInsertLocations() {
		ProcessInformation processInformation = new ProcessInformation();
		Geography geography = new Geography();
		geography.setLocation("MD-USA");
		processInformation.setGeography(geography);
		if (processInformation.getGeography() != null)
			insertLocation(processInformation.getGeography().getLocation());

	}
	
	private void insertLocation(String locationCode) {
		if (locationCode == null)
			return;
		String genKey = KeyGen.get(locationCode);
		if (db.findLocation(locationCode, genKey) != null) {
			log.info("Location {} already exists", genKey);
		} else {
			try {
				Location location = new Location();
				location.setRefId(genKey);
				location.setName(locationCode);
				location.setCode(locationCode);
				db.put(location, genKey);
				log.info("Location {} has been saved ", genKey);
			} catch (Exception e) {
				log.info("<<Error save Location " + e);
			}
		}
	}

	@Test
	public void testInsertProcess() {
		// ***common data
		ReferenceFunction referenceFunction = new ReferenceFunction();
		String baseName = "heat";
		String category = "Electricity, gas, steam and air conditioning supply";
		String subcategory = "ISIC 3530: Steam and air conditioning supply";
		String location = "US-WA";
		String unit = "MJ";
		boolean isInfrastructureProcess = false;
		String generalComment = "This unit/ gate-to-gate ...";
		String processType = "Unit process";

		referenceFunction.setBaseName(baseName);
		referenceFunction.setCategory(category);
		referenceFunction.setSubcategory(subcategory);
		referenceFunction.setLocation(location);
		referenceFunction.setUnit(unit);
		referenceFunction.isInfrastructureProcess();
		referenceFunction.setGeneralComment(generalComment);
		referenceFunction.setProcessType(processType);
		referenceFunction.setLocation(location);

		IDataSet dataSet = new UsdaTemplateDataSet();
		// ***parameter data
		List<gov.usda.nal.lci.template.domain.Parameter> parameters = new ArrayList<gov.usda.nal.lci.template.domain.Parameter>();
		gov.usda.nal.lci.template.domain.Parameter parameter = new gov.usda.nal.lci.template.domain.Parameter();
		parameter.setName("01fuelcarbonlbperlb");
		parameter.setDescription("Fuel carbon content (lb/lb)");
		parameter.setFormula(null);
		// ? parameter.setValue(0.8514);
		/*
		 * if (StringUtils.isNotBlank(vo.getUncertaintyType())) {
		 * 
		 * //UncertaintyType uncertaintyType = UncertaintyType.valueOf(vo //
		 * .getUncertaintyType()); UncertaintyType uncertaintyType =
		 * UncertaintyType.LOG_NORMAL;
		 * parameter.setUncertaintyType(uncertaintyType); }
		 */
		// parameter.setMaximumValue(null);
		// parameter.setMinimumValue(null);
		parameters.add(parameter);

		// *** Person
		List<gov.usda.nal.lci.template.domain.Person> persons = new ArrayList<gov.usda.nal.lci.template.domain.Person>();
		gov.usda.nal.lci.template.domain.Person person = new gov.usda.nal.lci.template.domain.Person();
		person.setName("Test Author");
		persons.add(person);
		// ***Source
		List<gov.usda.nal.lci.template.domain.Source> sources = new ArrayList<gov.usda.nal.lci.template.domain.Source>();
		gov.usda.nal.lci.template.domain.Source source = new gov.usda.nal.lci.template.domain.Source();
		source.setFirstAuthor("John Smith");
		sources.add(source);
		// ***ProcessInformation
		// =================================yel test
		gov.usda.nal.lci.template.domain.ProcessInformation processInfo = new gov.usda.nal.lci.template.domain.ProcessInformation();
		processInfo.setVersion("01.00.000");
		Geography geography = new Geography();
		geography.setLocation("US-WA");
		geography
				.setText("The process is assumed to be operated in the State of Washington.");
		processInfo.setGeography(geography);
		//
		List<gov.usda.nal.lci.template.domain.Exchange> exchanges = new ArrayList<gov.usda.nal.lci.template.domain.Exchange>();
		// store data in a memory
		dataSet.initializeData(persons, sources, processInfo, exchanges,
				parameters, referenceFunction);

		String processId = UsdaKeyGen.forProcess(dataSet);
		Process process = db.get(Process.class, processId);
		if (process != null) {
			log.info("Process {} already exists ", processId);
			return;
		}
		try {
			process = new Process();
			process.setRefId(processId);

			ProcessDocumentation documentation = mapProcessDocumentationEntities(dataSet);
			process.setDocumentation(documentation);

			if (dataSet.getReferenceFunction() != null) {
				process.setDescription(generalComment);
				process.setInfrastructureProcess(isInfrastructureProcess);
			}

			process.setProcessType(Mapper.getProcessType());

			mapTimeAndGeographyEntities(dataSet, process, documentation);

			documentation
					.setTechnology("Technology:Fuel oil industrial boiler, with sodium carbonate wet scrubber; less than 29 ");

			mapExchangesEntities(dataSet.getExchanges(), process);
			// if (process.getQuantitativeReference() == null) = Exchange
			// createProductFromRefFun(dataSet, process); //

			// ymapParametersEntities(dataSet.getParameters(), process);

			/*
			 * yif (dataSet.getReferenceFunction() != null)
			 * mapProcessEntities(dataSet, process, documentation);
			 */

			/*
			 * if (dataSet.getAllocations() != null &&
			 * dataSet.getAllocations().size() > 0) { mapAllocations(process,
			 * dataSet.getAllocations());
			 * process.setDefaultAllocationMethod(AllocationMethod.CAUSAL); ask
			 * }
			 */
			// y Mapper.mapModellingAndValidation(dataSet, documentation); //ASK
			// DATA
			// yMapper.mapAdminInfo(dataSet, documentation);
			// y mapActors(documentation, dataSet);
			// mapSources(documentation, dataSet);

			db.put(process, processId);
			log.info("Process {} has been saved ", processId);
		} catch (Exception e) {
			log.info("<<Error save Process" + e);
		}
		// localExchangeCache.clear();
	}

	private ProcessDocumentation mapProcessDocumentationEntities(
			IDataSet dataSet) {

		ProcessDocumentation documentation = new ProcessDocumentation();

		documentation.setCopyright(false);
		if (StringUtils
				.isNotBlank("Data were prepared by the University of Washington Design"))
			documentation
					.setProject("Project:Data were prepared by the University of Washington Design");
		if (StringUtils
				.isNotBlank("Data development is demonstrated by parameterization"))
			documentation
					.setDataTreatment("DataTreatment:Data development is demonstrated by parameterization");
		if (StringUtils
				.isNotBlank("This dataset can be used for any type of LCA "))
			documentation
					.setIntendedApplication("IntendedApplication:This dataset can be used for any type of LCA ");
		Date startDate = convertDate("05/01/2010");
		documentation.setValidFrom(startDate);
		Date endDate = convertDate("05/01/2010");
		documentation.setValidUntil(endDate);
		documentation
				.setRestrictions("AccessUseRestrictions: Use of the datasets in the U.S. Department of Agricultu");
		documentation
				.setSampling("Sampling: Boundary conditions were defined to only include data from the USEPA");
		return documentation;

	}

	private void mapTimeAndGeographyEntities(IDataSet dataSet, Process process,
			ProcessDocumentation documentation) {
		// ask ProcessTime processTime = new
		// ProcessTime(dataSet.getTimePeriod());
		// processTime.map(documentation);
		if (dataSet.getProcessInformation().getGeography() != null) {
			String locationCode = dataSet.getProcessInformation()
					.getGeography().getLocation();
			if (locationCode != null) {
				String genKey = KeyGen.get(locationCode);
				process.setLocation(db.findLocation(locationCode, genKey));
			}
			documentation.setGeography(dataSet.getProcessInformation()
					.getGeography().getText());
		}
	}

	private void mapExchangesEntities(
			List<gov.usda.nal.lci.template.domain.Exchange> inExchanges,
			Process ioProcess) {
		FlowMap flowMap = new FlowMap(MapType.ES1_FLOW);
		UnitMapping unitMapping = UnitMapping.createDefault(mysqlDatabase);
		FlowImport flowImport = new FlowImport(db, unitMapping, flowMap);

		for (gov.usda.nal.lci.template.domain.Exchange inExchange : inExchanges) {
			try {
				FlowBucket flow = flowImport.handleProcessExchange(inExchange);
				if (flow == null || !flow.isValid()) {
					log.error("Could not import flow {}", inExchange);
					continue;
				}
				Exchange outExchange = new Exchange();
				outExchange.setPedigreeUncertainty(inExchange
						.getDataQualityComment());
				outExchange.setFlow(flow.flow);
				outExchange.setUnit(flow.unit);
				outExchange.setFlowPropertyFactor(flow.flowProperty);
				outExchange.setInput(inExchange.getInputGroup() != null);
				outExchange.setAmountFormula(inExchange.getParameterName());
				ExchangeAmount exchangeAmount = new ExchangeAmount(outExchange,
						inExchange);
				exchangeAmount.map(flow.conversionFactor);
				ioProcess.getExchanges().add(outExchange);
				if (ioProcess.getQuantitativeReference() == null
						&& inExchange.getOutputGroup() != null
						&& (inExchange.getOutputGroup() == 0 || inExchange
								.getOutputGroup() == 2)) {
					ioProcess.setQuantitativeReference(outExchange);
				}
			} catch (Exception e) {

			}
		}
	}

	@SuppressWarnings("static-access")
	private Date convertDate(String sDate) {

		try {
			String[] dateFormats = new String[] { "MM/dd/yyyy", "yyyy-MM-dd" };
			DateUtils du = new DateUtils();
			Date convertedDate = du.parseDate(sDate, dateFormats);

			return convertedDate;

		} catch (Exception e) {
			log.error("Cannot convert date: " + sDate, e);
			return null;
		}
	}
	

}
