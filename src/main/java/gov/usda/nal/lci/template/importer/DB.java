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
import gov.usda.nal.lci.template.domain.Person;




import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openlca.core.database.BaseEntityDao;
import org.openlca.core.database.CategoryDao;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.Actor;
import org.openlca.core.model.Category;
import org.openlca.core.model.Flow;
import org.openlca.core.model.Location;
import org.openlca.core.model.ModelType;
import org.openlca.core.model.FlowType;
import org.openlca.core.model.RootEntity;
import org.openlca.core.model.Source;
import org.openlca.io.UnitMappingEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper for the database access and (semantic) search of entities for the
 * import of template data sets.
 */
class DB {

	private IDatabase database;
	private DBSearch search;
	private Logger log = LoggerFactory.getLogger(getClass());

	private Map<String, Category> categories = new HashMap<String, Category>();
	private Map<String, Actor> actors = new HashMap<String, Actor>();
	private Map<String, Source> sources = new HashMap<String, Source>();
	private Map<String, Location> locations = new HashMap<String, Location>();
	private Map<String, Flow> flows = new HashMap<String, Flow>();

	public DB(IDatabase database) {
		this.database = database;
		this.search = new DBSearch(database);
	}

	public Category getPutCategory(ModelType type, FlowType flowtype,String parentName,
			String name) {
		String key = StringUtils.join(new Object[] { type.name(), parentName,
				name }, "/");
		Category category = categories.get(key);
		if (category != null)
			return category;
		try {
			CategoryDao dao = new CategoryDao(database);
			Category parent = null;
			Category rootParent=null;
			// force a root category of "Elementary flows" if necessary
			if ( flowtype == FlowType.ELEMENTARY_FLOW && parentName != null && !parentName.equalsIgnoreCase("Elementary flows")) {
				java.util.List<Category> roots=dao.getRootCategories(type);
				for ( Category r : roots )
					if ( r.getName().equalsIgnoreCase("Elementary flows")) {
						rootParent=r;
						break;
					}
			}
			
			if (rootParent == null && parentName != null) {
				parent = Categories.findRoot(database, type, parentName);
				if (parent == null)
					parent = Categories.createRoot(database, type, parentName);
			}
			else if ( rootParent != null && parentName != null )
				parent=Categories.findOrAddChild(database,rootParent,parentName);
			category = parent;
			if (name != null) {
				if (parent != null)
					category = Categories.findOrAddChild(database, parent, name);
				else
					category = Categories.createRoot(database, type, name);
			}
			categories.put(key, category);
			return category;
		} catch (Exception e) {
			log.error("Failed to get category " + key, e);
			return null;
		}
	}

	public Category getPutCategory(Category root, String parentName, String name) {
		String key = StringUtils.join(new Object[] { root.getName(),
				parentName, name }, "/");
		Category category = categories.get(key);
		if (category != null)
			return category;
		try {
			Category parent = root;
			if (parentName != null)
				parent = Categories.findOrAddChild(database, root, parentName);
			Category cat = parent;
			if (name != null)
				cat = Categories.findOrAddChild(database, parent, name);
			return cacheReturn(key, cat);
		} catch (Exception e) {
			log.error("Failed to find or add category", e);
			return null;
		}
	}

	private Category cacheReturn(String key, Category category) {
		categories.put(key, category);
		return category;
	}

	public Actor findActor(Person person, String genKey) {
		Actor actor = get(Actor.class, actors, genKey);
		if (actor != null)
			return actor;
		actor = search.findActor(person);
		if (actor != null)
			actors.put(genKey, actor);
		return actor;
	}

	public Source findSource(gov.usda.nal.lci.template.domain.Source eSource, String genKey) {
		Source source = get(Source.class, sources, genKey);
		if (source != null)
			return source;
		source = search.findSource(eSource);
		if (source != null)
			sources.put(genKey, source);
		return source;
	}

	public Location findLocation(String locationCode, String genKey) {
		Location location = get(Location.class, locations, genKey);
		if (location != null)
			return location;
		location = search.findLocation(locationCode);
		if (location != null)
			locations.put(genKey, location);
		return location;
	}

	public Flow findFlow(gov.usda.nal.lci.template.domain.Exchange exchange, String genKey,
			UnitMappingEntry unitMapping) {
		Flow flow = get(Flow.class, flows, genKey);
		if (flow != null)
			return flow;
		flow = search.findFlow(exchange, unitMapping);
		if (flow != null)
			flows.put(genKey, flow);
		return flow;
	}

	/*public Flow findFlow(IDataSet dataSet, String genKey,
			UnitMappingEntry unitMapping) {
		Flow flow = get(Flow.class, flows, genKey);
		if (flow != null)
			return flow;
		flow = search.findFlow(dataSet, unitMapping);
		if (flow != null)
			flows.put(genKey, flow);
		return flow;
	}
*/
	private <T extends RootEntity> T get(Class<T> type, Map<String, T> cache,
			String genKey) {
		T entity = cache.get(genKey);
		if (entity != null)
			return entity;
		entity = get(type, genKey);
		if (entity != null)
			cache.put(genKey, entity);
		return entity;
	}

	public <T extends RootEntity> T get(Class<T> type, String id) {
		try {
			BaseEntityDao<T> dao = new BaseEntityDao<T>(type, database);
			return dao.getForRefId(id);
		} catch (Exception e) {
			log.error("Failed to query database for " + type + " id=" + id, e);
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void put(T entity, String genKey) throws Exception {
		if (entity == null)
			return;
		try {
			Class<T> clazz = (Class<T>) entity.getClass();
			database.createDao(clazz).insert(entity);
			Map cache = getCache(entity);
			if (cache != null)
				cache.put(genKey, entity);
		} catch (Exception e) {
			 throw new Exception("Failed to save entity " + entity + " id=" + genKey, e);
			
		}
	}

	private Map<String, ?> getCache(Object entity) {
		if (entity instanceof Actor)
			return actors;
		if (entity instanceof Source)
			return sources;
		if (entity instanceof Location)
			return locations;
		if (entity instanceof Flow)
			return flows;
		return null;
	}
}