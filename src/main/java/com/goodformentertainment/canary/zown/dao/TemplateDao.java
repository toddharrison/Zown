package com.goodformentertainment.canary.zown.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canarymod.database.Column;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

import com.goodformentertainment.canary.zown.ZownPlugin;

public class TemplateDao extends AConfigurationDao {
	public static final String TEMPLATE_NAME = "template_name";
	
	public TemplateDao() {
		super("zown_template");
	}
	
	public TemplateDao(final Database database) {
		super("zown_template", database);
	}
	
	@Override
	public DataAccess getInstance() {
		return new TemplateDao();
	}
	
	@Column(columnName = TEMPLATE_NAME, dataType = DataType.STRING)
	public String templateName;
	
	@Override
	public boolean read() throws DatabaseReadException {
		boolean read = false;
		if (templateName != null) {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(TEMPLATE_NAME, templateName);
			database.load(this, filters);
			if (hasData()) {
				read = true;
			}
		}
		return read;
	}
	
	@Override
	public boolean save() throws DatabaseReadException, DatabaseWriteException {
		boolean saved = false;
		
		final TemplateDao templateDao = new TemplateDao();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(TEMPLATE_NAME, templateName);
		database.load(templateDao, filters);
		
		if (templateDao.hasData()) {
			// Record exists with same template name
			if (templateDao.id.equals(id)) {
				// Same record, update it
				saved = super.save();
			} else {
				// Different id, can't use the same template name
			}
		} else {
			// Specified name is not present
			if (id == null) {
				// New record, create it
				saved = super.save();
				
				// TODO remove when ID is automatically loaded
				database.load(templateDao, filters);
				id = templateDao.id;
			} else {
				// Existing record, update previous values
				filters = new HashMap<String, Object>();
				filters.put(ID, id);
				database.load(templateDao, filters);
				
				if (templateDao.hasData() && !templateDao.templateName.equals(templateName)) {
					updateTemplateNameReferences(templateDao.templateName, templateName);
				}
				saved = super.save();
			}
		}
		
		return saved;
	}
	
	@Override
	public boolean delete() throws DatabaseReadException, DatabaseWriteException {
		final boolean deleted = super.delete();
		if (deleted) {
			removeTemplateReferences(templateName);
		}
		return deleted;
	}
	
	private void updateTemplateNameReferences(final String oldName, final String newName)
			throws DatabaseReadException, DatabaseWriteException {
		ZownDao zownDao = new ZownDao();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ZownDao.TEMPLATE_NAME, oldName);
		final List<DataAccess> datasets = new ArrayList<DataAccess>();
		database.loadAll(zownDao, datasets, filters);
		
		for (final DataAccess da : datasets) {
			zownDao = (ZownDao) da;
			zownDao.templateName = newName;
			if (!zownDao.save()) {
				ZownPlugin.LOG.error("Error saving template name update to zown " + zownDao.id);
			}
		}
	}
	
	private void removeTemplateReferences(final String template) throws DatabaseReadException,
			DatabaseWriteException {
		ZownDao zownDao = new ZownDao();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ZownDao.TEMPLATE_NAME, template);
		final List<DataAccess> datasets = new ArrayList<DataAccess>();
		database.loadAll(zownDao, datasets, filters);
		
		for (final DataAccess da : datasets) {
			zownDao = (ZownDao) da;
			zownDao.templateName = null;
			copyConfiguration(zownDao);
			if (!zownDao.save()) {
				ZownPlugin.LOG.error("Error saving template removal to zown " + zownDao.id);
			}
		}
	}
}
