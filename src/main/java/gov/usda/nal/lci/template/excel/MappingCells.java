package gov.usda.nal.lci.template.excel;
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
import gov.usda.nal.lci.template.consts.Consts;
import gov.usda.nal.lci.template.vo.ActorVO;
import gov.usda.nal.lci.template.vo.AdministrativeInformationVO;
import gov.usda.nal.lci.template.vo.CostsVO;
import gov.usda.nal.lci.template.vo.ModelingAndValidationVO;
import gov.usda.nal.lci.template.vo.ExchangeDataVO;
import gov.usda.nal.lci.template.vo.ISICDataVO;
import gov.usda.nal.lci.template.vo.MetaDataVO;
import gov.usda.nal.lci.template.vo.ParametersVO;
import gov.usda.nal.lci.template.vo.ProcessInformationVO;
import gov.usda.nal.lci.template.vo.SourceInformationVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
/**
 * Map spreadsheets to java vo
 * @author Y Radchenko
 * refactored by gmoore 28 August 2014
 */
public class MappingCells {
	private static final Log LOG = LogFactory.getLog(MappingCells.class);
	private InputStream template;
	private List<ExchangeDataVO> exchangeDataVO;
	private List<ParametersVO> parameterDataVO;
	private List<ActorVO> actorVO;
	private List<SourceInformationVO> sourceVO;
	private ProcessInformationVO generalInfoVO;
	private AdministrativeInformationVO admVO;
	private ModelingAndValidationVO modVO;
	private List<ISICDataVO> isicDataVO;
	private List<MetaDataVO> metaDataVO;
	private List<CostsVO> costsVO;

	public MappingCells(InputStream template)
	{
		this.template=template;
	}
	public ProcessInformationVO getProcessData() {
		return generalInfoVO;
	}

	public AdministrativeInformationVO getAdministrationData() {
		return admVO;
	}

	public ModelingAndValidationVO getModelingAndValidationData() {
		return modVO;
	}

	public List<ExchangeDataVO> getExchangeData() {
		return exchangeDataVO;
	}

	public List<ParametersVO> getParametersData() {
		return parameterDataVO;
	}

	public List<ActorVO> getActorData() {
		return actorVO;
	}

	public List<SourceInformationVO> getSourceInformationData() {
		return sourceVO;
	}

	public List<ISICDataVO> getIsicDataVO() {
		return isicDataVO;
	}
	
	public List<CostsVO> getCostsDataVO() {
		return costsVO;
	}
	/**
	 * <code>loadCellMappings</code> gets cell mappings from the mapping.json config file.  
	 * <p>The program identifies cells in each sheet by so-called mappings stored in mapping.json.  
	 * Each mapping consists of a column location and name,
	 * e.g. "A","Description"
	 * </p>
	 * <p>gmoore 28 Aug 2014</p>
	 * @return a map of cell mappings for each sheet in the template
	 * @throws IOException
	 */
	private Map<String,Map> loadCellMappings() throws IOException {
		java.net.URL url=getClass().getResource("mapping.json");
		JsonParser parser = Json.createParser(new java.io.InputStreamReader(url.openStream()));
		Map<String,Map> mappings=new HashMap<String,Map>();
		String mapkey="";
		String cellkey="";
		Map<String,String> cellmappings=null;
		boolean inArray=false;
		LOG.debug("Parsing mapping.json config");
		while (parser.hasNext()) {
			   JsonParser.Event event = parser.next();
			   switch(event) {
			    	case START_ARRAY:
			    	  inArray=true;
			    	  cellmappings=new HashMap<String,String>();
			    	  break;
			    	case END_ARRAY:
			    	  inArray=false;
			    	  mappings.put(mapkey, cellmappings);
			    	  break;
			    	case KEY_NAME:
			    	  if ( !inArray )
			    	  	mapkey=parser.getString();
			    	  else
			    		  cellkey=parser.getString();
			        break;
			    	case VALUE_FALSE:
			    	case VALUE_NULL:
			    	case VALUE_TRUE:
			    	case VALUE_STRING:
			    	case VALUE_NUMBER:
			    	  if ( inArray )
			    		  cellmappings.put(cellkey,parser.getString());
			    	break;
			        default:
			    	  break;
			   }
			  
		}
		parser.close();
		return mappings;
	}
	@SuppressWarnings("unchecked")
	public void run() throws FileNotFoundException, IOException, Exception {
		OPCPackage pkg = null;

		try {
			// The package open,represents a
			// container that can store multiple data objects.
			pkg = OPCPackage.open(this.template);
			Map<String,Map> map=this.loadCellMappings();
			exchangeDataVO = getSheetExchangeData(pkg, map.get("exchanges"),ExchangeDataVO.class);
			generalInfoVO = getProcessInfoFromMetaData(getSheetMetaData(pkg, map.get("metaData"),MetaDataVO.class, Consts.GENERAL_INFORMATION_PAGE));
			admVO = getAdministrativeInformationFromMetaData(getSheetMetaData(pkg, map.get("metaData"),MetaDataVO.class, Consts.ADMINISTRATIVE_INFORMATION_PAGE));
			modVO = getModelingAndValidationFromMetaData(getSheetMetaData(pkg, map.get("metaData"),MetaDataVO.class, Consts.MODELING_VALIDATION_PAGE));
			parameterDataVO = getSheetParameterData(pkg, map.get("parameters"),ParametersVO.class);
			actorVO = getSheetActorData(pkg, map.get("actors"), ActorVO.class);
			sourceVO = getSheetSourceInformationData(pkg, map.get("sources"),SourceInformationVO.class);
			costsVO = getSheetCostsData(pkg, map.get("costs"),CostsVO.class);

		} catch (FileNotFoundException fe) {
			throw new FileNotFoundException(fe.getMessage());

		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage(), ioe.getCause());

		} catch (Exception e) {
			throw new Exception(e.getMessage(), e.getCause());

		} finally {
			try {
				if (null != pkg) {
					pkg.close();
				}
			} catch (IOException eio) {
				// just ignore IO exception
			}

		}

	}

	
	/**
	 * read excel speadsheet Exchange into ExchangeDataVO
	 */
	private List<ExchangeDataVO> getSheetExchangeData(OPCPackage pkg,
			Map<String, String> cellMapping, Class<ExchangeDataVO> exchangeData)
			throws Exception {

		ExcelWorkSheetHandler<ExchangeDataVO> workSheetExchange = null;
		workSheetExchange = new ExcelWorkSheetHandler<ExchangeDataVO>(
				ExchangeDataVO.class, cellMapping, 3);

		LOG.debug("Start reading excel spreadsheet Exchange Data into ExchangeDataVO");
		ExcelReader excelReader = new ExcelReader(pkg, workSheetExchange);

		// get process from spreadsheet #
		excelReader.process(Consts.EXCHANGES_PAGE);

		if (workSheetExchange.getValueList().isEmpty()) {
			// No data present
			LOG.debug("SheetExchange.getValueList() is empty");
		} else {

			LOG.debug(workSheetExchange.getValueList().size()
					+ " no. of records read from given excel worksheet Exchange Data successfully.");
		}

		return workSheetExchange.getValueList();

	}

	/**
	 * read excel speadsheet Parameters into ParametersVO
	 */
	private List<ParametersVO> getSheetParameterData(OPCPackage pkg,
			Map<String, String> cellMapping, Class<ParametersVO> parameterData)
			throws Exception {

		ExcelWorkSheetHandler<ParametersVO> workSheetParameter = null;
		workSheetParameter = new ExcelWorkSheetHandler<ParametersVO>(
				ParametersVO.class, cellMapping, 4);

		LOG.debug("Start reading excel speadsheet Parameters into ParametersVO");
		ExcelReader excelReader = new ExcelReader(pkg, workSheetParameter);
		// get process from spreadsheet #5
		excelReader.process(Consts.PARAMETERS_PAGE);

		if (workSheetParameter.getValueList().isEmpty()) {
			// No data present
			LOG.error("SheetParameter.getValueList() is empty");
		} else {

			LOG.debug(workSheetParameter.getValueList().size()
					+ " no. of records read from given excel worksheet Parameter successfully.");

		}
		return workSheetParameter.getValueList();

	}
	

	/**
	 * GeneralInfo/Administrative/ModelingAndValidation have the same page structure
	 * read excel speadsheet Meta Data into MetaDataVO
	 */
	private List<MetaDataVO> getSheetMetaData(OPCPackage pkg,
			Map<String, String> cellMapping, Class<MetaDataVO> metaData,
			int spreadsheet) throws Exception {

		ExcelWorkSheetHandler<MetaDataVO> workSheetMetaData  = new ExcelWorkSheetHandler<MetaDataVO>(
				MetaDataVO.class, cellMapping, 3);

		LOG.debug("Start reading excel spreadsheet #" + spreadsheet
				+ " MetaData into MetaDataVO");
		ExcelReader excelReader = new ExcelReader(pkg, workSheetMetaData);

		// get process from spreadsheet #
		excelReader.process(spreadsheet);

		if (workSheetMetaData.getValueList().isEmpty()) {
			// No data present
			LOG.error("SheetMetaData.getValueList() is empty");
		} else {

			LOG.debug(workSheetMetaData.getValueList().size()
					+ " no. of records read from given excel worksheet MetaData #"
					+ spreadsheet + " successfully.");
		}

		List<MetaDataVO> metaDataVO = updateMetaDataList(workSheetMetaData
				.getValueList());

		return metaDataVO;

	}

	
	/**
	 * get ProcessInformation information from meta data "General information"
	 * 
	 * @param metaDataList
	 *            stores meta data information
	 * @return ProcessInformation/General information
	 */
	private ProcessInformationVO getProcessInfoFromMetaData(
			List<MetaDataVO> metaDataList) {

		ProcessInformationVO process = new ProcessInformationVO();
		process.setIsicVO(new ISICDataVO());
		for (MetaDataVO d : metaDataList) {

			process.setMetaType(Consts.META_PROCESS_INFO);
			if (StringUtils.isNotBlank(d.getMetaValue())) {

				if (d.getMetaName().trim().equalsIgnoreCase("Base name"))
					process.setBaseName(d.getMetaValue());
				else if (d.getMetaName().trim().startsWith("Treatment"))
					process.setTreatmentStandardsRoutes(d.getMetaValue());
				else if ( d.getMetaName().trim().contains("level 0"))
						process.getIsicVO().setCategory(d.getMetaValue());
				else if ( d.getMetaName().trim().contains("level 1"))
						process.getIsicVO().setSubcategory(d.getMetaValue());
				else if (d.getMetaName().trim().equalsIgnoreCase("Location type")) 
					process.setLocationType(d.getMetaValue());
				else if (d.getMetaName().trim().equalsIgnoreCase("Mix type"))
					process.setMixType(d.getMetaValue());
				else if (d.getMetaName().trim().startsWith("Quantitative product")) 
					process.setQuantitativeProcessProperties(d.getMetaValue());
				else if (d.getMetaName().trim().equalsIgnoreCase("Description"))
					process.setDescription(d.getMetaValue());
				else if (d.getMetaName().trim().startsWith("ISIC")) 
					process.setCodeIsic(d.getMetaValue());
				else if (d.getMetaName().trim().startsWith("Infrastructure"))
					process.setInfrastructureProcess(Boolean.valueOf(d.getMetaValue()));
				else if (d.getMetaName().trim().startsWith("Quantitative reference"))
					process.setQuantiativeReference(d.getMetaValue());
				else if (d.getMetaName().trim().startsWith("Start date")) 
					process.setStartDate(d.getMetaValue());
				else if (d.getMetaName().trim().startsWith("End date"))
					process.setEndDate(d.getMetaValue());
				else if (d.getMetaName().trim().equalsIgnoreCase("Time Comment"))
					process.setTimeComment(d.getMetaValue());
				else if (d.getMetaName().trim().equalsIgnoreCase("Location")) 
					process.setLocation(d.getMetaValue());
				else if (d.getMetaName().equalsIgnoreCase("Geography Comment"))
					process.setGeographyComment(d.getMetaValue());
				else if (d.getMetaName().trim().equalsIgnoreCase("Technology Comment"))
					process.setTechnologyComment(d.getMetaValue());

			}

		}
		return process;
	}

	/**
	 * get Administrative information from meta data
	 * "Administrative information"
	 * @param metaDataList
	 *            stores meta data information
	 * @return administrative information
	 */
	private AdministrativeInformationVO getAdministrativeInformationFromMetaData(
			List<MetaDataVO> metaDataList) {

		AdministrativeInformationVO adm = new AdministrativeInformationVO();

		for (MetaDataVO d : metaDataList) {

			adm.setMetaType(Consts.META_ADMINISTRATIVE_INFO);

			if (StringUtils.isNotBlank(d.getMetaValue())) {
				if (d.getMetaName().trim().startsWith("Intended application")) {
					adm.setIntendedApplication(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("Data set owner")) {
					adm.setDataOwner(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("Data generator")) {
					adm.setDataGenerator(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Data documentor")) {
					adm.setDataDocumentor(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("Publication")) {
					adm.setPublication(d.getMetaValue());
				}
				if (d.getMetaName().trim()
						.equalsIgnoreCase("Access and use restrictions")) {
					adm.setAccessUseRestrictions(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("Project")) {
					adm.setProject(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("Version")) {
					adm.setVersion(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("Copyright")) {
					adm.setCopyright(d.getMetaValue());
				}

				continue;
			}
		}

		return adm;

	}

	/**
	 * get ModelingAndValidation information from meta data
	 * "Modeling And Validation"
	 * 
	 * @param metaDataList
	 *            stores all meta data information
	 * @return ModelingAndValidation
	 */
	private ModelingAndValidationVO getModelingAndValidationFromMetaData(
			List<MetaDataVO> metaDataList) {

		ModelingAndValidationVO mod = new ModelingAndValidationVO();

		for (MetaDataVO d : metaDataList) {

			mod.setMetaType(Consts.META_MODELING_VALIDATION);
			if (StringUtils.isNotBlank(d.getMetaValue())) {
				if (d.getMetaName().trim().startsWith("Process type")) {
					mod.setProcessType(d.getMetaValue());
				}
				if (d.getMetaName().trim().equalsIgnoreCase("LCI method")) {
					mod.setLciMethods(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Modeling constants")) {
					mod.setModelingConstants(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Data completeness")) {
					mod.setDataCompleteness(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Mass balance")) {
					mod.setMassBalance(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Data selection")) {
					mod.setDataSelection(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Data treatment")) {
					mod.setDataTreatment(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Sampling procedure")) {
					mod.setSamplingProcedure(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Data collection period")) {
					mod.setDataCollectionPeriod(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Reviewer")) {
					mod.setReviewer(d.getMetaValue());
				}
				if (d.getMetaName().trim()
						.startsWith("Data set other evaluation")) {
					mod.setDataSetOtherEvaluation(d.getMetaValue());
				}
				if (d.getMetaName().trim().startsWith("Sources")) {
					mod.setSources(d.getMetaValue());
				}

			}

			continue;

		}

		return mod;
	}

	/**
	 * 
	 * @param metaDataList
	 *            MetaDataVO
	 * @return MetaDataVO relevant to General information or
	 * Administration or Modeling and Validation
	 */
	private static List<MetaDataVO> updateMetaDataList(
			List<MetaDataVO> metaDataList) {

		List<MetaDataVO> vo = new ArrayList<MetaDataVO>();
		for (MetaDataVO d : metaDataList) {
			d.setMetaName(d.getMetaName());
			d.setMetaValue(d.getMetaValue());
			d.setMetaDescription(d.getMetaDescription());
			vo.add(d);

		}

		return vo;

	}

	/**
	 * read excel speadsheet Actor into ActorVO
	 */
	public static List<ActorVO> getSheetActorData(OPCPackage pkg,
			Map<String, String> cellMapping, Class<ActorVO> actorData)
			throws Exception {

		ExcelWorkSheetHandler<ActorVO> workSheetActor = null;
		workSheetActor = new ExcelWorkSheetHandler<ActorVO>(ActorVO.class,
				cellMapping, 3);

		LOG.debug("Start reading excel spreadsheet Actor into ActorVO");
		ExcelReader excelReader = new ExcelReader(pkg, workSheetActor);

		// get process from spreadsheet #7
		excelReader.process(Consts.ACTORS_PAGE);

		if (workSheetActor.getValueList().isEmpty()) {
			// No data present
			LOG.error("SheetActor.getValueList() is empty");
		} else {

			LOG.debug(workSheetActor.getValueList().size()
					+ " no. of records read from given excel worksheet Actor successfully.");
		}

		return workSheetActor.getValueList();

	}

	/**
	 * read excel speadsheet Sources into SourceInformationVO
	 */
	public static List<SourceInformationVO> getSheetSourceInformationData(
			OPCPackage pkg, Map<String, String> cellMapping,
			Class<SourceInformationVO> sourceData) throws Exception {

		ExcelWorkSheetHandler<SourceInformationVO> workSheetSource = null;
		workSheetSource = new ExcelWorkSheetHandler<SourceInformationVO>(
				SourceInformationVO.class, cellMapping, 3);

		LOG.debug("Start reading excel spreadsheet Sources into SourceInfromationVO");
		ExcelReader excelReader = new ExcelReader(pkg, workSheetSource);

		// get process from spreadsheet #8
		excelReader.process(Consts.SOURCES_PAGE);

		if (workSheetSource.getValueList().isEmpty()) {
			// No data present
			LOG.error("SheetSources.getValueList() is empty");
		} else {

			LOG.debug(workSheetSource.getValueList().size()
					+ " no. of records read from given excel worksheet Sources successfully.");
		}

		return workSheetSource.getValueList();

	}
	
	public static List<CostsVO> getSheetCostsData(OPCPackage pkg, Map<String, String> cellMapping,
			Class<CostsVO> costsData)throws Exception{
		
		ExcelWorkSheetHandler<CostsVO> workSheetCost = null;
		workSheetCost = new ExcelWorkSheetHandler<CostsVO>(
				CostsVO.class, cellMapping, 3);

		LOG.debug("Start reading excel spreadsheet Costs into CostsVO");
		ExcelReader excelReader = new ExcelReader(pkg, workSheetCost);

		// get process from spreadsheet #8
		excelReader.process(Consts.COSTS_PAGE);

		if (workSheetCost.getValueList().isEmpty()) {
			// No data present
			LOG.error("SheetCosts.getValueList() is empty");
		} else {
			LOG.debug(workSheetCost.getValueList().size()
					+ " no. of records read from given excel worksheet Costs successfully.");
		}

		return workSheetCost.getValueList();

		
	}

}
