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
import java.util.List;



import gov.usda.nal.lci.model.SubmissionMetaElements;
import gov.usda.nal.lci.template.domain.Costs;
import gov.usda.nal.lci.template.domain.Person;
import gov.usda.nal.lci.template.excel.MappingCells;
import gov.usda.nal.lci.template.support.IDataSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlca.core.model.Actor;
import org.openlca.core.model.CostCategory;
import org.openlca.core.model.FlowType;
import org.openlca.core.model.Parameter;
import org.openlca.core.model.ParameterScope;
import org.openlca.core.model.Process;
import org.openlca.core.model.ProcessCostEntry;
import org.openlca.core.model.ProcessDocumentation;
import org.openlca.core.model.ProcessType;
import org.openlca.core.model.Source;
import org.openlca.core.model.Uncertainty;
import org.openlca.core.model.UncertaintyType;
/**
 * The Mapper class transfers entities to a Process.
 * @author Y Radchenko
 *
 */
class Mapper {
	private static final Log LOG = LogFactory.getLog(MappingCells.class);
	public static void mapPerson(Person inPerson, Actor ioActor) {

		if (StringUtils.isNotBlank(inPerson.getName()))
			ioActor.setName(inPerson.getName());
		if (StringUtils.isNotBlank(inPerson.getDescription()))
			ioActor.setDescription(inPerson.getDescription());
		if (StringUtils.isNotBlank(inPerson.getAddress()))
			ioActor.setAddress(inPerson.getAddress());
		if (StringUtils.isNotBlank(inPerson.getCountry()))
			ioActor.setCountry(inPerson.getCountry());
		if (StringUtils.isNotBlank(inPerson.getEmail()))
			ioActor.setEmail(inPerson.getEmail());
		if (StringUtils.isNotBlank(inPerson.getWebsite()))
			ioActor.setWebsite(inPerson.getWebsite());
		if (StringUtils.isNotBlank(inPerson.getTelefax()))
			ioActor.setTelefax(inPerson.getTelefax());
		if (StringUtils.isNotBlank(inPerson.getTelephone()))
			ioActor.setTelephone(inPerson.getTelephone());
		if (StringUtils.isNotBlank(inPerson.getCity()))
			ioActor.setCity(inPerson.getCity());
		if (StringUtils.isNotBlank(inPerson.getZipcode()))
			ioActor.setZipCode(inPerson.getZipcode());
	}

	public static void mapSource(
			gov.usda.nal.lci.template.domain.Source inSource, Source ioSource) {

		if (StringUtils.isNotBlank(inSource.getFirstAuthor()))
			ioSource.setName(inSource.getFirstAuthor() + " "
					+ inSource.getYear());
		if (StringUtils.isNotBlank(inSource.getDescription()))
			ioSource.setDescription(inSource.getDescription());
		if (StringUtils.isNotBlank(inSource.getTextReference()))
			ioSource.setTextReference(inSource.getTextReference());
		if (StringUtils.isNotBlank(inSource.getYear()))
			ioSource.setYear(Short.parseShort(inSource.getYear()));
		if (StringUtils.isNotBlank(inSource.getDoi()))
			ioSource.setDoi(inSource.getDoi());
	}

	public static FlowType getFlowType(
			gov.usda.nal.lci.template.domain.Exchange inExchange) {

		if (inExchange.getInputGroup() != null) {
			return FlowTypes.forInputGroup(inExchange.getInputGroup());
		} else if (inExchange.getOutputGroup() != null) {
			return FlowTypes.forOutputGroup(inExchange.getOutputGroup());
		} else {
			return FlowType.ELEMENTARY_FLOW;
		}
	}
	/**
	 * Transfers Parameter entities to a Process.  Parameters with null values are ignored.
	 * @param inParameters
	 * @param ioProcess
	 */
	public static void mapParametersEntities(
			List<gov.usda.nal.lci.template.domain.Parameter> inParameters,
			Process ioProcess) {
		for (gov.usda.nal.lci.template.domain.Parameter inParameter : inParameters) {
			if ( inParameter.getValue() == null || inParameter.getValue() == "" ) {
				LOG.info("Parameter value is blank and will be ignored: "+inParameter.getName());
				continue;
			}
			Parameter outParameter = new Parameter();

			if (StringUtils.isNotBlank(inParameter.getName()))
				outParameter.setName(inParameter.getName());

			if (StringUtils.isNotBlank(inParameter.getDescription()))
				outParameter.setDescription(inParameter.getDescription());

			if (StringUtils.isNotBlank(inParameter.getFormula())) {
				outParameter.setFormula(inParameter.getFormula());
				outParameter.setInputParameter(false);
			} else
				outParameter.setInputParameter(true);

			if (StringUtils.isNotBlank(inParameter.getValue()))
				outParameter
						.setValue(Double.parseDouble(inParameter.getValue()));

			// tbl_parameters.distribution_type
			if (inParameter.getUncertaintyType() != null) {
				Uncertainty uncertainty = new Uncertainty();
				uncertainty.setDistributionType(inParameter
						.getUncertaintyType());
				if (inParameter.getUncertaintyType().equals(
						UncertaintyType.LOG_NORMAL)) {
					double gmean = inParameter.getExpectedValue();
					uncertainty.setParameter2Value(Double.valueOf(inParameter.getDispersion()!=null?inParameter.getDispersion():0));
					uncertainty.setParameter1Value(gmean);
				
					outParameter.setUncertainty(uncertainty);
				}
				else if (inParameter.getUncertaintyType().equals(
						UncertaintyType.NORMAL)) {
					uncertainty.setParameter1Value(Double.valueOf(inParameter.getExpectedValue()!=null?inParameter.getExpectedValue():0));
					uncertainty.setParameter2Value(Double.valueOf(inParameter.getDispersion()!=null?inParameter.getDispersion():0));
					
					outParameter.setUncertainty(uncertainty);
				}
				else if (inParameter.getUncertaintyType().equals(
						UncertaintyType.TRIANGLE)) {
					uncertainty.setParameter1Value(Double.valueOf(inParameter.getMinimumValue()!=null?inParameter.getMinimumValue():0));
					uncertainty.setParameter2Value(Double.valueOf(inParameter.getExpectedValue()!=null?inParameter.getExpectedValue():0));
					uncertainty.setParameter3Value(Double.valueOf(inParameter.getMaximumValue()!=null?inParameter.getMaximumValue():0));
					outParameter.setUncertainty(uncertainty);
				}
				else if (inParameter.getUncertaintyType().equals(
						UncertaintyType.UNIFORM)) {
					uncertainty.setParameter1Value(inParameter
							.getMinimumValue());
					uncertainty.setParameter2Value(inParameter
							.getMaximumValue());
					outParameter.setUncertainty(uncertainty);
				}

			}
			outParameter.setScope(ParameterScope.PROCESS);

			ioProcess.getParameters().add(outParameter);

		}
	}
	/**
	 * Transfers Cost entities to the Process.  Any costs without amounts
	 * are ignored.
	 * @param inCosts
	 * @param modelProcess
	 * @see gov.usda.nal.lci.template.domain.Costs
	 */
	public static void mapCostCategoryEntities(List<Costs> inCosts,
			Process modelProcess) {

		for (gov.usda.nal.lci.template.domain.Costs inCost : inCosts) {
			if ( inCost.getAmount() == null ) { 
				LOG.info("Cost amount is blank and will not be added to the database "+inCost.getCostCategory());
				continue;
			}
			CostCategory modelCostCategory = new CostCategory();
			modelCostCategory.setName(inCost.getCostCategory());
			modelCostCategory.setFix(inCost.getCostFixed());

			ProcessCostEntry processCostEntry = new ProcessCostEntry();
			processCostEntry.setCostCategory(modelCostCategory);
			processCostEntry.setAmount(inCost.getAmount());

			if (modelProcess.getQuantitativeReference() != null)
				processCostEntry.setExchange(modelProcess
						.getQuantitativeReference());

			modelProcess.getCostEntries().add(processCostEntry);
		}

	}

	public static ProcessDocumentation mapProcessDocumentationEntities(
			IDataSet dataSet) {

		ProcessDocumentation documentation = new ProcessDocumentation();

		//documentation.setVersion(dataSet.getProcessInformation().getVersion());
		
		documentation.setTime(dataSet.getProcessInformation().getTimeInfo()
				.getTimeComment());
		documentation.setValidFrom(dataSet.getProcessInformation()
				.getTimeInfo().getStartDate());
		documentation.setValidUntil(dataSet.getProcessInformation()
				.getTimeInfo().getEndDate());
		documentation.setCopyright(dataSet.getProcessInformation()
				.getAdministrativeInformation().isCopyright());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getAdministrativeInformation().getProject()))
			documentation.setProject(dataSet.getProcessInformation()
					.getAdministrativeInformation().getProject());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getAdministrativeInformation().getIntendedApplication()))
			documentation.setIntendedApplication(dataSet
					.getProcessInformation().getAdministrativeInformation()
					.getIntendedApplication());

		documentation.setRestrictions(dataSet.getProcessInformation()
				.getAdministrativeInformation().getAccessUseRestrictions());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getLciMethod()))
			documentation.setInventoryMethod(dataSet.getProcessInformation()
					.getModelingAndValidation().getLciMethod());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getModelingConstants()))
			documentation.setModelingConstants(dataSet.getProcessInformation()
					.getModelingAndValidation().getModelingConstants());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getDataCompleteness()))
			documentation.setCompleteness(dataSet.getProcessInformation()
					.getModelingAndValidation().getDataCompleteness());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getDataSelection()))
			documentation.setDataSelection(dataSet.getProcessInformation()
					.getModelingAndValidation().getDataSelection());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getDataTreatment()))
			documentation.setDataTreatment(dataSet.getProcessInformation()
					.getModelingAndValidation().getDataTreatment());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getSampling()))
			documentation.setSampling(dataSet.getProcessInformation()
					.getModelingAndValidation().getSampling());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getDataCollectionPeriod()))
			documentation.setDataCollectionPeriod(dataSet
					.getProcessInformation().getModelingAndValidation()
					.getDataCollectionPeriod());

		// if(StringUtils.isNotBlank(dataSet.getProcessInformation().getModelingAndValidation().getReviewer()))
		// ask documentation.?
		// reviewer(dataSet.getProcessInformation().getModelingAndValidation().getReviewer());

		if (StringUtils.isNotBlank(dataSet.getProcessInformation()
				.getModelingAndValidation().getDataSetOtherEvaluation()))
			documentation.setReviewDetails(dataSet.getProcessInformation()
					.getModelingAndValidation().getDataSetOtherEvaluation());
		return documentation;

	}

	public static ProcessType getProcessType() {
		return ProcessType.UNIT_PROCESS;
	}

	public static SubmissionMetaElements mapSubmissionDocumentationEntities(
			IDataSet dataSet) {

		SubmissionMetaElements submission = new SubmissionMetaElements();
		submission.setBaseName(dataSet.getProcessInformation().getProcessName()
				.getBaseName());
		submission.setTreatment(dataSet.getProcessInformation()
				.getProcessName().getTreatmentStandardsRoutes());
		submission.setMixType(dataSet.getProcessInformation().getProcessName()
				.getMixType());

		submission.setMassBalance(dataSet.getProcessInformation()
				.getModelingAndValidation().getMassBalance());

		return submission;
	}

}
