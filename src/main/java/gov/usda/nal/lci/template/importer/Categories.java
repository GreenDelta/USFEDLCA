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
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openlca.core.database.CategoryDao;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.Category;
import org.openlca.core.model.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class for the import, export, and display of categories.
 */
public class Categories {

	private Categories() {
	}

	public static Category findOrAddChild(IDatabase database, Category parent,
			String childName) {
		for (Category child : parent.getChildCategories()) {
			if (StringUtils.equalsIgnoreCase(child.getName(), childName))
				return child;
		}
		try {
			Category child = new Category();
			child.setModelType(parent.getModelType());
			child.setName(childName);
			child.setRefId(UUID.randomUUID().toString());
			child.setParentCategory(parent);
			parent.getChildCategories().add(child);
			CategoryDao dao = new CategoryDao(database);
			dao.insert(child);
			database.createDao(Category.class).update(parent);
			return child;
		} catch (Exception e) {
			Logger log = LoggerFactory.getLogger(Categories.class);
			log.error("failed to add child", e);
			return null;
		}
	}

	public static Category findOrCreateRoot(IDatabase database, ModelType type,
			String name) {
		Category root = findRoot(database, type, name);
		if (root != null)
			return root;
		else
			return createRoot(database, type, name);
	}

	/**
	 * Creates a new root category with the given name and of the given type and
	 * inserts it into the given database.
	 */
	public static Category createRoot(IDatabase database, ModelType type,
			String name) {
		try {
			Category category = new Category();
			category.setRefId(UUID.randomUUID().toString());
			category.setModelType(type);
			category.setName(name);
			database.createDao(Category.class).insert(category);
			return category;
		} catch (Exception e) {
			Logger log = LoggerFactory.getLogger(Categories.class);
			log.error("failed to insert root category " + name + " / " + type,
					e);
			return null;
		}
	}

	/**
	 * Returns a root category with the given name and type from the database or
	 * null if no such category exists. The case of the names are ignored.
	 */
	public static Category findRoot(IDatabase database, ModelType type,
			String name) {
		if (database == null || type == null || name == null)
			return null;
		try {
			CategoryDao dao = new CategoryDao(database);
			for (Category root : dao.getRootCategories(type)) {
				if (StringUtils.equalsIgnoreCase(root.getName(), name))
					return root;
			}
			return null;
		} catch (Exception e) {
			Logger log = LoggerFactory.getLogger(Categories.class);
			log.error("failed to search root category " + name + " " + type, e);
			return null;
		}
	}
}
