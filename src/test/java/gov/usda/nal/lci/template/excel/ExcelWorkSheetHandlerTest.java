package gov.usda.nal.lci.template.excel;

/**
 * Demonstration of Generic Excel File (XLSX) Reading using Apache POI
 * 
 *
 */
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;

import gov.usda.nal.lci.template.consts.Consts;
import gov.usda.nal.lci.template.excel.ExcelReader;
import gov.usda.nal.lci.template.excel.ExcelWorkSheetHandler;
import gov.usda.nal.lci.template.vo.ExchangeDataVO;
import gov.usda.nal.lci.template.vo.MetaDataVO;
import gov.usda.nal.lci.template.vo.ParametersVO;

public class ExcelWorkSheetHandlerTest {

	private static final Log LOG = LogFactory
			.getLog(ExcelWorkSheetHandlerTest.class);

	public static void main(String[] args) throws Exception {

		String SAMPLE_DATA_FILE_PATH =
		 "src/main/resources/excel2olca_template_olca_8.xlsx";

		File file = new File(SAMPLE_DATA_FILE_PATH);
		InputStream inputStream = new FileInputStream(file);

		OPCPackage pkg = null;

		try {
			// The package open,represents a
			// container that can store multiple data objects.
			pkg = OPCPackage.open(inputStream);
			

			// Excel columns Mapping to ExchangeDataVO.java
			Map<String, String> cellMappingExchangeData = new HashMap<String, String>();

			cellMappingExchangeData.put("A", "emptyColumn");
			cellMappingExchangeData.put("B", "inputGroup");
			cellMappingExchangeData.put("C", "outputGroup");
			cellMappingExchangeData.put("D", "flowName"); 
			cellMappingExchangeData.put("E", "category");

			assignExcelDataToVO(pkg, cellMappingExchangeData,
					ExchangeDataVO.class);

			// Parameters: Excel Cell Mapping
			Map<String, String> cellMappingParameters = new HashMap<String, String>();

			cellMappingParameters.put("A", "emptyColumn");
			cellMappingParameters.put("B", "rowNumber");
			cellMappingParameters.put("C", "parameterDescription");
			cellMappingParameters.put("D", "parameterName");
			cellMappingParameters.put("E", "mathematicalRelation");
			cellMappingParameters.put("F", "resultingValue");
			cellMappingParameters.put("G", "uncertaintyDistributionType");
			cellMappingParameters.put("H", "mean");
			cellMappingParameters.put("I", "geo");
			cellMappingParameters.put("J", "minimumValue");
			cellMappingParameters.put("K", "maximumValue");
			

			 assignExcelDataToVO(pkg, cellMappingParameters,
			 ParametersVO.class);

			
			// Parameters: Excel Cell Mapping
			Map<String, String> cellMappingMetaData = new HashMap<String, String>();

			cellMappingMetaData.put("A", "metaType");
			cellMappingMetaData.put("B", "metaName");
			cellMappingMetaData.put("C", "metaValue");
			cellMappingMetaData.put("D", "metaDescription");

			 assignExcelDataToVO(pkg, cellMappingMetaData,MetaDataVO.class);
			 
		} finally {
			IOUtils.closeQuietly(inputStream);
			try {
				if (null != pkg) {
					pkg.close();
				}
			} catch (IOException e) {
				// just ignore IO exception
			}
		}

	}

	public static void assignExcelDataToVO(OPCPackage pkg,
			Map<String, String> cellMapping, Class<?> type) throws Exception {

		try {

			String className = type.getSimpleName();
			ExcelWorkSheetHandler<ExchangeDataVO> workSheetExchange = null;
			ExcelWorkSheetHandler<ParametersVO> workSheetParameter = null;

			if (className.equals("ExchangeDataVO")) {
				workSheetExchange = new ExcelWorkSheetHandler<ExchangeDataVO>(
						ExchangeDataVO.class, cellMapping, 3);

				LOG.info("Start reading excel spreadsheet Exchange Data into ExchangeDataVO");
				ExcelReader excelReader = new ExcelReader(pkg,
						workSheetExchange);
				
				// get process from spreadsheet #2
				excelReader.process(Consts.EXCHANGES_PAGE);

				if (workSheetExchange.getValueList().isEmpty()) {
					// No data present
					LOG.error("SheetExchange.getValueList() is empty");
				} else {

					LOG.info(workSheetExchange.getValueList().size()
							+ " no. of records read from given excel worksheet Exchange Data successfully.");
					displayExchangeDataList(workSheetExchange.getValueList());
				}

			} else if (className.equals("ParametersVO")) {
				workSheetParameter = new ExcelWorkSheetHandler<ParametersVO>(
						ParametersVO.class, cellMapping, 3);

				LOG.info("Start reading excel spreadsheet Parameters into ParametersVO");
				ExcelReader excelReader = new ExcelReader(pkg,
						workSheetParameter);
				// sheetCallback1);
				// get process from spreadsheet #3
				excelReader.process(Consts.PARAMETERS_PAGE);

				if (workSheetParameter.getValueList().isEmpty()) {
					// No data present
					LOG.error("SheetParameter.getValueList() is empty");
				} else {

					LOG.info(workSheetParameter.getValueList().size()
							+ " no. of records read from given excel worksheet Parameter successfully.");
					 displayParametersDataList(workSheetParameter.getValueList());

				}

			
			}

			// } catch (RuntimeException are) {
			// LOG.error(are.getMessage(), are.getCause());
		} catch (InvalidFormatException ife) {
			LOG.error(ife.getMessage(), ife.getCause());
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage(), ioe.getCause());
		}

	}

	private static void displayExchangeDataList(
			List<ExchangeDataVO> exchangeDataList) {
		System.out.println("<<< test  ExchangeDataVO >>>");
		System.out
				.println("inputGroup\tOutputGroup\tshortDesc\t   \tflowName\tflowLocation\tCategory");
		System.out
				.println("---\t-----------\t---------\t---------\t----------\t---\t------------\t----------\t-------");
		for (ExchangeDataVO d : exchangeDataList) {
			System.out.println(String.format(
					"%s\t%s\t%-8s\t%-8s\t%-20s\t%-20s\t%s\t%s", 
				    d.getInputGroup(), d.getOutputGroup(),
				    d.getFlowName(),d.getCategory()));
		}
	}

	private static void displayParametersDataList(
			List<ParametersVO> parameterDataList) {

		System.out.println("<<< test  ParameterVO >>>");
		System.out
				.println("numrow\tparameter Desc    \tparameter Name\tMathematical\tResulting Value\tUnv.DistributionType\t\tMean\tGeo\tMinValue");
		System.out
				.println("-----\t-------------------\t----------\t------------\t-------------\t\t---\t\t------");
		for (ParametersVO d : parameterDataList) {
			/*System.out.println(String.format(
					"%-2s\t%-25s\t%-25s\t%s\t%s\t%s\t%s\t%s\t%s",
					d.getRowNumber(), d.getParameterDescription(),
					d.getParameterName(), d.getMathematicalRelation(),
					d.getResultingValue(), d.getUncertaintyDistributionType(),
					d.getMean(), d.getGeo(), d.getMinimumValue()));*/

		}

	}

	

}
