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
	public static final String OWNER_PERMISSIONS = "flag_access";
	public static final String ALLOW = "flag_allow";
	public static final String DENY = "flag_deny";
	public static final String BLOCK_BUILD_EXCEPTIONS = "exceptions_block_build";
	public static final String BLOCK_INTERACT_EXCEPTIONS = "exceptions_block_interact";
	public static final String ENTITY_CREATE_EXCEPTIONS = "exceptions_entity_create";
	public static final String ENTITY_INTERACT_EXCEPTIONS = "exceptions_entity_interact";
	public static final String COMMAND_RESTRICTIONS = "command_restrictions";
	
	protected final Database database;
	
	protected AConfigurationDao(final String tableName) {
		this(tableName, Database.get());
	}
	
	protected AConfigurationDao(final String tableName, final Database database) {
		super(tableName);
		this.database = database;
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
			database.load(this, filters);
			if (hasData()) {
				read = true;
			}
		}
		return read;
	}
	
	public boolean save() throws DatabaseReadException, DatabaseWriteException {
		boolean saved = false;
		if (id == null) {
			database.insert(this);
			saved = true;
		} else {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(ID, id);
			database.update(this, filters);
			saved = true;
		}
		return saved;
	}
	
	public boolean delete() throws DatabaseReadException, DatabaseWriteException {
		boolean deleted = false;
		if (id != null) {
			final Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(ID, id);
			database.remove(this, filters);
			deleted = true;
		}
		return deleted;
	}
	
	public void copyConfiguration(final AConfigurationDao configDao) {
		if (allowList != null) {
			configDao.allowList = new ArrayList<String>(allowList);
		}
		if (blockBuildExceptionList != null) {
			configDao.blockBuildExceptionList = new ArrayList<String>(blockBuildExceptionList);
		}
		if (blockInteractExceptionList != null) {
			configDao.blockInteractExceptionList = new ArrayList<String>(blockInteractExceptionList);
		}
		if (commandRestrictionsList != null) {
			configDao.commandRestrictionsList = new ArrayList<String>(commandRestrictionsList);
		}
		if (denyList != null) {
			configDao.denyList = new ArrayList<String>(denyList);
		}
		if (entityCreateExceptionList != null) {
			configDao.entityCreateExceptionList = new ArrayList<String>(entityCreateExceptionList);
		}
		if (entityInteractExceptionList != null) {
			configDao.entityInteractExceptionList = new ArrayList<String>(entityInteractExceptionList);
		}
		if (ownerPermissionList != null) {
			configDao.ownerPermissionList = new ArrayList<String>(ownerPermissionList);
		}
	}
}
