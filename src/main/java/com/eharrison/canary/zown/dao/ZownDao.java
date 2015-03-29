package com.eharrison.canary.zown.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canarymod.database.Column;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

public class ZownDao extends DataAccess {
	public static final String WORLD_NAME = "world_name";
	public static final String ZOWN_NAME = "zown_name";
	public static final String PARENT_ZOWN_NAME = "parent_zown_name";
	public static final String TEMPLATE_NAME = "template_name";
	public static final String MIN_POINT = "min_point";
	public static final String MAX_POINT = "max_point";
	public static final String OWNERS = "owners";
	public static final String MEMBERS = "members";
	public static final String OWNER_PERMISSIONS = "owner_permissions";
	public static final String ALLOW = "allow";
	public static final String DENY = "deny";
	public static final String BUILD_EXCEPTIONS = "build_exceptions";
	public static final String INTERACT_EXCEPTIONS = "interact_exceptions";
	public static final String COMMAND_RESTRICTIONS = "command_restrictions";
	
	public static ZownDao getZownDao(final String world, final String zown)
			throws DatabaseReadException {
		final ZownDao zownDao = new ZownDao();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ZownDao.WORLD_NAME, world);
		filters.put(ZownDao.ZOWN_NAME, zown);
		
		Database.get().load(zownDao, filters);
		
		if (zownDao.hasData()) {
			return zownDao;
		} else {
			return null;
		}
	}
	
	public ZownDao() {
		super("zown");
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
	
	@Column(columnName = MIN_POINT, dataType = DataType.STRING)
	public String minPointString;
	
	@Column(columnName = MAX_POINT, dataType = DataType.STRING)
	public String maxPointString;
	
	@Column(columnName = OWNERS, dataType = DataType.STRING, isList = true)
	public List<String> ownerList;
	
	@Column(columnName = MEMBERS, dataType = DataType.STRING, isList = true)
	public List<String> memberList;
	
	@Column(columnName = OWNER_PERMISSIONS, dataType = DataType.STRING, isList = true)
	public List<String> ownerPermissionList;
	
	@Column(columnName = ALLOW, dataType = DataType.STRING, isList = true)
	public List<String> allowList;
	
	@Column(columnName = DENY, dataType = DataType.STRING, isList = true)
	public List<String> denyList;
	
	@Column(columnName = BUILD_EXCEPTIONS, dataType = DataType.STRING, isList = true)
	public List<String> buildExceptionList;
	
	@Column(columnName = INTERACT_EXCEPTIONS, dataType = DataType.STRING, isList = true)
	public List<String> interactExceptionList;
	
	@Column(columnName = COMMAND_RESTRICTIONS, dataType = DataType.STRING, isList = true)
	public List<String> commandRestrictionsList;
	
	public void update() throws DatabaseWriteException {
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ZownDao.WORLD_NAME, worldName);
		filters.put(ZownDao.ZOWN_NAME, zownName);
		Database.get().update(this, filters);
	}
}
