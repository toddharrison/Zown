package com.eharrison.canary.zown.dao;

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

import com.eharrison.canary.zown.ZownPlugin;

public class ZownDao extends AConfigurationDao {
	public static final String WORLD_NAME = "world_name";
	public static final String ZOWN_NAME = "zown_name";
	public static final String PARENT_ZOWN_NAME = "parent_zown_name";
	public static final String TEMPLATE_NAME = "template_name";
	public static final String TEMPLATE_OVERRIDE = "template_override";
	public static final String MIN_POINT = "min_point";
	public static final String MAX_POINT = "max_point";
	public static final String OWNERS = "owners";
	public static final String MEMBERS = "members";
	
	public ZownDao() {
		super("zown");
	}
	
	protected ZownDao(final Database database) {
		super("zown", database);
	}
	
	@Override
	public DataAccess getInstance() {
		return new ZownDao();
	}
	
	@Column(columnName = WORLD_NAME, dataType = DataType.STRING)
	public String worldName;
	
	@Column(columnName = ZOWN_NAME, dataType = DataType.STRING)
	public String zownName;
	
	@Column(columnName = PARENT_ZOWN_NAME, dataType = DataType.STRING)
	public String parentZownName;
	
	@Column(columnName = TEMPLATE_NAME, dataType = DataType.STRING)
	public String templateName;
	
	@Column(columnName = TEMPLATE_OVERRIDE, dataType = DataType.BOOLEAN)
	public Boolean templateOverride;
	
	@Column(columnName = MIN_POINT, dataType = DataType.STRING)
	public String minPointString;
	
	@Column(columnName = MAX_POINT, dataType = DataType.STRING)
	public String maxPointString;
	
	@Column(columnName = OWNERS, dataType = DataType.STRING, isList = true)
	public List<String> ownerList;
	
	@Column(columnName = MEMBERS, dataType = DataType.STRING, isList = true)
	public List<String> memberList;
	
	@Override
	public boolean read() throws DatabaseReadException {
		boolean read = false;
		if (worldName != null) {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(WORLD_NAME, worldName);
			filters.put(ZOWN_NAME, zownName);
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
		
		final ZownDao zownDao = new ZownDao();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(WORLD_NAME, worldName);
		filters.put(ZOWN_NAME, zownName);
		database.load(zownDao, filters);
		
		if (zownDao.hasData()) {
			// Record exists with same world and zown names
			if (zownDao.id.equals(id)) {
				// Same record, update it
				saved = super.save();
			} else {
				// Different id, can't use the same world and zown names
			}
		} else {
			// Specified names are not present
			if (id == null) {
				// New record, create it
				saved = super.save();
				
				// TODO remove when ID is automatically loaded
				database.load(zownDao, filters);
				id = zownDao.id;
			} else {
				// Existing record, update previous values
				filters = new HashMap<String, Object>();
				filters.put(ID, id);
				database.load(zownDao, filters);
				
				if (zownDao.hasData() && !zownDao.zownName.equals(zownName)) {
					updateParentNameReferences(zownDao.zownName, zownName);
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
			removeChildZowns(zownName);
		}
		return deleted;
	}
	
	private void updateParentNameReferences(final String oldName, final String newName)
			throws DatabaseReadException, DatabaseWriteException {
		ZownDao zownDao = new ZownDao();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(WORLD_NAME, worldName);
		filters.put(PARENT_ZOWN_NAME, oldName);
		final List<DataAccess> datasets = new ArrayList<DataAccess>();
		database.loadAll(zownDao, datasets, filters);
		
		for (final DataAccess da : datasets) {
			zownDao = (ZownDao) da;
			zownDao.parentZownName = newName;
			if (!zownDao.save()) {
				ZownPlugin.LOG.error("Error saving parent name update to zown " + zownDao.id);
			}
		}
	}
	
	private void removeChildZowns(final String parent) throws DatabaseReadException,
			DatabaseWriteException {
		ZownDao zownDao = new ZownDao();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(WORLD_NAME, worldName);
		filters.put(PARENT_ZOWN_NAME, parent);
		final List<DataAccess> datasets = new ArrayList<DataAccess>();
		database.loadAll(zownDao, datasets, filters);
		
		for (final DataAccess da : datasets) {
			zownDao = (ZownDao) da;
			zownDao.delete();
		}
	}
}
