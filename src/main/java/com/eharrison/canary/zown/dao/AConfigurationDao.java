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

public abstract class AConfigurationDao extends DataAccess {
	public static final String ID = "id";
	public static final String OWNER_PERMISSIONS = "owner_permissions";
	public static final String ALLOW = "allow";
	public static final String DENY = "deny";
	public static final String BLOCK_BUILD_EXCEPTIONS = "block_build_exceptions";
	public static final String BLOCK_INTERACT_EXCEPTIONS = "block_interact_exceptions";
	public static final String ENTITY_CREATE_EXCEPTIONS = "entity_create_exceptions";
	public static final String ENTITY_INTERACT_EXCEPTIONS = "entity_interact_exceptions";
	public static final String COMMAND_RESTRICTIONS = "command_restrictions";
	
	protected AConfigurationDao(final String tableName) {
		super(tableName);
	}
	
	@Column(columnName = OWNER_PERMISSIONS, dataType = DataType.STRING, isList = true)
	public List<String> ownerPermissionList;
	
	@Column(columnName = ALLOW, dataType = DataType.STRING, isList = true)
	public List<String> allowList;
	
	@Column(columnName = DENY, dataType = DataType.STRING, isList = true)
	public List<String> denyList;
	
	@Column(columnName = BLOCK_BUILD_EXCEPTIONS, dataType = DataType.STRING, isList = true)
	public List<String> blockBuildExceptionList;
	
	@Column(columnName = BLOCK_INTERACT_EXCEPTIONS, dataType = DataType.STRING, isList = true)
	public List<String> blockInteractExceptionList;
	
	@Column(columnName = ENTITY_CREATE_EXCEPTIONS, dataType = DataType.STRING, isList = true)
	public List<String> entityCreateExceptionList;
	
	@Column(columnName = ENTITY_INTERACT_EXCEPTIONS, dataType = DataType.STRING, isList = true)
	public List<String> entityInteractExceptionList;
	
	@Column(columnName = COMMAND_RESTRICTIONS, dataType = DataType.STRING, isList = true)
	public List<String> commandRestrictionsList;
	
	public boolean read() throws DatabaseReadException {
		boolean read = false;
		if (id != null) {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(ID, id);
			Database.get().load(this, filters);
			if (hasData()) {
				read = true;
			}
		}
		return read;
	}
	
	public boolean save() throws DatabaseReadException, DatabaseWriteException {
		boolean saved = false;
		if (id == null) {
			Database.get().insert(this);
			saved = true;
		} else {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(ID, id);
			Database.get().update(this, filters);
			saved = true;
		}
		return saved;
	}
	
	public boolean delete() throws DatabaseReadException, DatabaseWriteException {
		boolean deleted = false;
		if (id != null) {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(ID, id);
			Database.get().remove(this, filters);
			deleted = true;
		}
		return deleted;
	}
	
	public void copyConfiguration(final AConfigurationDao configDao) {
		configDao.allowList = new ArrayList<String>(allowList);
		configDao.blockBuildExceptionList = new ArrayList<String>(blockBuildExceptionList);
		configDao.blockInteractExceptionList = new ArrayList<String>(blockInteractExceptionList);
		configDao.commandRestrictionsList = new ArrayList<String>(commandRestrictionsList);
		configDao.denyList = new ArrayList<String>(denyList);
		configDao.entityCreateExceptionList = new ArrayList<String>(entityCreateExceptionList);
		configDao.entityInteractExceptionList = new ArrayList<String>(entityInteractExceptionList);
		configDao.ownerPermissionList = new ArrayList<String>(ownerPermissionList);
	}
}
