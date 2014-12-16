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
import org.openlca.core.model.Exchange;
import org.openlca.core.model.Uncertainty;
import org.openlca.core.model.UncertaintyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ExchangeAmount class maps the amount and uncertainty distribution parameters of an EcoSpold 01
 * exchange to an openLCA exchange.
 */
class ExchangeAmount {

	private Exchange olcaExchange;
	private gov.usda.nal.lci.template.domain.Exchange esExchange;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public ExchangeAmount(Exchange olcaExchange, gov.usda.nal.lci.template.domain.Exchange esExchange) {
		this.olcaExchange = olcaExchange;
		this.esExchange = esExchange;
	}

	public void map(double factor) {
		try {
			double mean = esExchange.getAmountValue() * factor;
			olcaExchange.setAmountValue(mean);
			if (esExchange.getUncertaintyType() != null)
				setUncertaintyValues(mean);
		} catch (Exception e) {
			log.error("Mapping uncertainty distribution failed", e);
		}
	}

	private void setUncertaintyValues(double mean) {
		
		if (esExchange.getUncertaintyType().equals(UncertaintyType.LOG_NORMAL))
			mapLogNormal(mean, esExchange.getStandardDeviation95());
		if (esExchange.getUncertaintyType().equals(UncertaintyType.NORMAL))
			mapNormal(mean, esExchange.getStandardDeviation95());
		if (esExchange.getUncertaintyType().equals(UncertaintyType.TRIANGLE))
			mapTriangle(mean, esExchange.getMinValue(),
					esExchange.getMinValue());
		if (esExchange.getUncertaintyType().equals(UncertaintyType.UNIFORM))
			mapUniform(esExchange.getMinValue(), esExchange.getMaxValue());
		
	}

	private void mapUniform(Double min, Double max) {
		if (min == null || max == null)
			return;
		olcaExchange.setUncertainty(Uncertainty.uniform(min, max));
	}

	private void mapTriangle(double mean, Double min, Double max) {
		if (min == null || max == null)
			return;
		Double mostLikely = esExchange.getMostLikelyValue();
		if (mostLikely == null)
			mostLikely = 3 * mean - min - max;
		olcaExchange.setUncertainty(Uncertainty.triangle(min, mostLikely, max));
	}

	private void mapNormal(double mean, Double sd) {
		if (sd == null)
			return;
		olcaExchange.setUncertainty(Uncertainty.normal(mean, sd / 2));
	}

	private void mapLogNormal(double gmean, Double sd) {
		if (sd == null)
			return;
		olcaExchange
				.setUncertainty(Uncertainty.logNormal(gmean, Math.sqrt(sd)));
	}

}
