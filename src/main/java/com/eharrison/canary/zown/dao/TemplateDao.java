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

public class TemplateDao extends DataAccess {
	public static final String TEMPLATE_NAME = "template_name";
	public static final String OWNER_PERMISSIONS = "owner_permissions";
	public static final String ALLOW = "allow";
	public static final String DENY = "deny";
	public static final String BUILD_EXCEPTIONS = "build_exceptions";
	public static final String INTERACT_EXCEPTIONS = "interact_exceptions";
	public static final String COMMAND_RESTRICTIONS = "command_restrictions";
	
	public static TemplateDao getTemplateDao(final String template) throws DatabaseReadException {
		final TemplateDao templateDao = new TemplateDao();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ZownDao.TEMPLATE_NAME, template);
		
		Database.get().load(templateDao, filters);
		
		if (templateDao.hasData()) {
			return templateDao;
		} else {
			return null;
		}
	}
	
	public TemplateDao() {
		super("zown_template");
	}
	
	@Override
	public DataAccess getInstance() {
		return new TemplateDao();
	}
	
	@Column(columnName = TEMPLATE_NAME, dataType = DataType.STRING)
	public String templateName;
	
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
		filters.put(ZownDao.TEMPLATE_NAME, templateName);
		Database.get().update(this, filters);
	}
}
