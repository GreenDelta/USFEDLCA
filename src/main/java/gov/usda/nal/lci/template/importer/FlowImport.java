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

import java.util.HashMap;
import java.util.Map;

import org.openlca.core.model.Category;
import org.openlca.core.model.Flow;
import org.openlca.core.model.FlowPropertyFactor;
import org.openlca.core.model.FlowType;
import org.openlca.core.model.ModelType;
import org.openlca.core.model.Unit;
import org.openlca.core.model.UnitGroup;
import org.openlca.io.maps.FlowMapEntry;
import org.openlca.io.maps.FlowMap;
import org.openlca.io.KeyGen;
import org.openlca.io.UnitMapping;
import org.openlca.io.UnitMappingEntry;

import gov.usda.nal.lci.template.domain.Exchange;
import gov.usda.nal.lci.template.keys.UsdaKeyGen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns flows from the database or creates them using unit- and
 * flow-mappings. Flows can be imported from EcoSpold 1 exchanges representing
 * process inputs or outputs, exchanges representing impact assessment factors,
 * or reference functions of processes.
 */
class FlowImport {

	private Logger log = LoggerFactory.getLogger(getClass());
	private DB db;
	private UnitMapping unitMapping;
	private FlowMap flowMap;
	private Map<String, FlowBucket> cache = new HashMap<String, FlowBucket>();


	public FlowImport(DB db,UnitMapping unitMapping, FlowMap flowMap){ 
		this.db = db;
		this.unitMapping = unitMapping;
		this.flowMap = flowMap;
	}

	/** Import a flow from a process import or export. */
	public FlowBucket handleProcessExchange(Exchange exchange) throws Exception {
		String flowKey = UsdaKeyGen.forFlow(exchange);
		return handleExchange(flowKey, exchange);
	}

	/** Import a flow from an exchange. */
	private FlowBucket handleExchange(String flowKey, Exchange exchange)
			throws Exception {
		FlowBucket created;
		try {
			FlowBucket cached = getCachedOrMapped(flowKey);
			if (cached != null)
				return cached;
			FlowBucket db = getDbFlow(flowKey, exchange);
			if (db != null)
				return cache(flowKey, db);
			Flow flow = new Flow();
			flow.setRefId(flowKey);
			flow.setName(exchange.getFlowName());
			flow.setDescription(exchange.getDescription());
			mapExchangeData(exchange, flow);
			String unit = exchange.getUnit();

			created = createFlow(flowKey, flow, unit);

		} catch (Exception e) {
			throw new Exception(" Error " + e);
		}
		return cache(flowKey, created);
	}

	
	/** Get a cached or mapped flow: no unit info is required.*/ 
	private FlowBucket getCachedOrMapped(String flowKey) {
		FlowBucket cached = cache.get(flowKey);
		if (cached != null)
			return cached;
		FlowBucket mapped = getMappedFlow(flowKey);
		if (mapped != null)
			return cache(flowKey, mapped);
		return null;
	}

	/** Cache the bucket for the generated key. */
	private FlowBucket cache(String flowKey, FlowBucket bucket) {
		if (bucket == null || !bucket.isValid()) {
			log.warn("Could not create valid flow {}", flowKey);
			return null;
		}
		cache.put(flowKey, bucket);
		return bucket;
	}

	/** Try to find a flow from the mapping tables. */
	private FlowBucket getMappedFlow(String genKey) {
		FlowMapEntry entry = flowMap.getEntry(genKey);
		if (entry == null)
			return null;
		Flow flow = db.get(Flow.class, entry.getOpenlcaFlowKey());
		if (flow == null)
			return null;
		FlowBucket bucket = new FlowBucket();
		bucket.conversionFactor = entry.getConversionFactor();
		bucket.flow = flow;
		bucket.flowProperty = flow.getReferenceFactor();
		Unit unit = getReferenceUnit(bucket.flowProperty);
		bucket.unit = unit;
		if (!bucket.isValid()) {
			log.warn("invalid flow mapping for {}", genKey);
			return null;
		}
		return bucket;
	}

	private Unit getReferenceUnit(FlowPropertyFactor flowProperty) {
		if (flowProperty == null || flowProperty.getFlowProperty() == null)
			return null;
		UnitGroup group = flowProperty.getFlowProperty().getUnitGroup();
		if (group == null)
			return null;
		return group.getReferenceUnit();
	}

	private FlowBucket getDbFlow(String flowKey, Exchange inExchange) {
		Flow flow = db.findFlow(inExchange, flowKey,
				unitMapping.getEntry(inExchange.getUnit()));
		if (flow == null)
			return null;
		return createBucket(flow, inExchange.getUnit());
	}

	private void mapExchangeData(Exchange inExchange, Flow flow) {
		flow.setCasNumber(inExchange.getCasNumber());
		flow.setFormula(inExchange.getFormula());
		FlowType flowType = Mapper.getFlowType(inExchange);
		flow.setFlowType(flowType);
		Category flowCategory = db.getPutCategory(ModelType.FLOW,flowType,
				inExchange.getCategory(), inExchange.getSubCategory());
		if (flowCategory != null)
			flow.setCategory(flowCategory);
		String locationCode = inExchange.getLocation();
		if (locationCode != null) {
			String locKey = KeyGen.get(locationCode);
			flow.setLocation(db.findLocation(locationCode, locKey));
		}
		
	}

	/** Creates a new flow and inserts it in the database. */
	private FlowBucket createFlow(String flowKey, Flow flow, String unit) throws Exception {
		UnitMappingEntry entry = unitMapping.getEntry(unit);
		if (entry == null || !entry.isValid())
			return null;
		flow.setReferenceFlowProperty(entry.getFlowProperty());
		FlowPropertyFactor factor = new FlowPropertyFactor();
		factor.setFlowProperty(entry.getFlowProperty());
		factor.setConversionFactor(1.0);
		flow.getFlowPropertyFactors().add(factor);
		db.put(flow, flowKey);
		return createBucket(flow, unit);
	}

	/** Create the flow bucket for the given flow and unit. */
	private FlowBucket createBucket(Flow flow, String unit) {
		UnitMappingEntry mapEntry = unitMapping.getEntry(unit);
		if (mapEntry == null || !mapEntry.isValid())
			return null;
		FlowPropertyFactor factor = flow.getFactor(mapEntry.getFlowProperty());
		if (factor == null) {
			log.error("The unit/property for flow {}/{} "
					+ "changed in the database", flow, unit);
			return null;
		}
		FlowBucket bucket = new FlowBucket();
		bucket.conversionFactor = 1.0;
		bucket.flow = flow;
		bucket.flowProperty = factor;
		bucket.unit = mapEntry.getUnit();
		return bucket;
	}

}
