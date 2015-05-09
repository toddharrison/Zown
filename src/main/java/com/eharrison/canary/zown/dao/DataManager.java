package com.eharrison.canary.zown.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

import com.eharrison.canary.zown.ZownPlugin;
import com.eharrison.canary.zown.api.IConfigurable;
import com.eharrison.canary.zown.api.IConfiguration;
import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.Point;
import com.eharrison.canary.zown.api.impl.Configuration;
import com.eharrison.canary.zown.api.impl.MinecraftMapper;
import com.eharrison.canary.zown.api.impl.Template;
import com.eharrison.canary.zown.api.impl.TemplateManager;
import com.eharrison.canary.zown.api.impl.Tree;
import com.eharrison.canary.zown.api.impl.Zown;
import com.eharrison.canary.zown.api.impl.ZownManager;

public class DataManager {
	private final Database database;
	
	public DataManager() {
		this(Database.get());
	}
	
	protected DataManager(final Database database) {
		this.database = database;
	}
	
	public void loadTemplates(final TemplateManager templateManager) throws DatabaseReadException {
		TemplateDao templateDao = new TemplateDao();
		final List<DataAccess> datasets = new ArrayList<DataAccess>();
		final Map<String, Object> filters = new HashMap<String, Object>();
		database.loadAll(templateDao, datasets, filters);
		
		for (final DataAccess dataset : datasets) {
			templateDao = (TemplateDao) dataset;
			
			final Template template = templateManager.addTemplate(templateDao.templateName);
			if (template == null) {
				// Attempted to create duplicate template
				ZownPlugin.LOG.warn("Tried to load a duplicate template: " + templateDao.templateName);
			} else {
				loadConfiguration(template, templateDao);
				ZownPlugin.LOG.debug("Loaded template " + templateDao.templateName);
			}
		}
	}
	
	public void loadZowns(final World world, final ITemplateManager templateManager,
			final ZownManager zownManager) throws DatabaseReadException, DatabaseWriteException {
		final String worldName = world.getFqName();
		
		final ZownDao worldZown = new ZownDao();
		worldZown.worldName = worldName;
		worldZown.zownName = worldName;
		if (worldZown.read()) {
			loadZowns(world, templateManager, zownManager, worldZown);
		} else {
			zownManager.getZown(world);
			ZownPlugin.LOG.info("Created world zown for " + worldName);
		}
	}
	
	private void loadZowns(final World world, final ITemplateManager templateManager,
			final ZownManager zownManager, final ZownDao zownDao) throws DatabaseReadException {
		if (zownDao != null && zownDao.hasData()) {
			// Load the referenced template
			ITemplate template = null;
			if (zownDao.templateName != null && !zownDao.templateName.equals("null")) {
				template = templateManager.getTemplate(zownDao.templateName);
				if (template == null) {
					ZownPlugin.LOG.warn("The specified template " + zownDao.templateName + " doesn't exist");
				}
			}
			
			// Parse bounds
			Point p1 = null;
			if (zownDao.minPointString != null) {
				p1 = Point.parse(zownDao.minPointString);
			}
			Point p2 = null;
			if (zownDao.maxPointString != null) {
				p2 = Point.parse(zownDao.maxPointString);
			}
			
			// Add the zown to the manager
			final Tree<Zown> zownTree = zownManager.addZown(world, zownDao.parentZownName,
					zownDao.zownName, template, p1, p2);
			if (zownTree == null) {
				// Attempted to create duplicate zown
				ZownPlugin.LOG.warn("Tried to load a bad zown: " + zownDao.zownName);
			} else {
				final Zown zown = zownTree.getData();
				
				if (template != null) {
					if (zownDao.templateOverride != null && zownDao.templateOverride) {
						zown.setTemplate((Template) template, true);
						loadConfiguration(zown, zownDao);
					} else {
						zown.setTemplate((Template) template, false);
					}
				} else {
					loadConfiguration(zown, zownDao);
				}
				
				if (zownDao.ownerList != null) {
					zown.getOwnerUUIDs().addAll(zownDao.ownerList);
				}
				if (zownDao.memberList != null) {
					zown.getMemberUUIDs().addAll(zownDao.memberList);
				}
				if (zownDao.entryExclusions != null) {
					zown.getEntryExclusionUUIDs().addAll(zownDao.entryExclusions);
				}
				
				zown.setWelcomeMessage(zownDao.welcomeMessage);
				zown.setFarewellMessage(zownDao.farewellMessage);
				
				ZownPlugin.LOG.debug("Loaded zown " + zownDao.zownName);
				
				// Load child zowns recursively
				final ZownDao childZownDao = new ZownDao();
				final List<DataAccess> datasets = new ArrayList<DataAccess>();
				final Map<String, Object> filters = new HashMap<String, Object>();
				filters.put(ZownDao.PARENT_ZOWN_NAME, zownDao.zownName);
				database.loadAll(childZownDao, datasets, filters);
				for (final DataAccess da : datasets) {
					loadZowns(world, templateManager, zownManager, (ZownDao) da);
				}
			}
		}
	}
	
	public boolean saveTemplate(final Template template) throws DatabaseReadException,
			DatabaseWriteException {
		boolean saved = false;
		if (template != null) {
			saved = saveTemplate(template, template.getName());
		}
		return saved;
	}
	
	public boolean saveZown(final World world, final Tree<Zown> zown) throws DatabaseReadException,
			DatabaseWriteException {
		boolean saved = false;
		if (zown != null) {
			saved = saveZown(world, zown, zown.getData().getName());
		}
		return saved;
	}
	
	public boolean saveTemplate(final Template template, final String originalName)
			throws DatabaseReadException, DatabaseWriteException {
		boolean saved = false;
		if (template != null) {
			final TemplateDao templateDao = new TemplateDao();
			templateDao.templateName = originalName;
			
			// Populate the templateDao id if it exists in the database
			templateDao.read();
			
			// Set the templateDao data
			templateDao.templateName = template.getName();
			saveConfiguration(template, templateDao);
			
			saved = templateDao.save();
		}
		return saved;
	}
	
	public boolean saveZown(final World world, final Tree<Zown> zownTree, final String originalName)
			throws DatabaseReadException, DatabaseWriteException {
		boolean saved = false;
		if (zownTree != null) {
			final ZownDao zownDao = new ZownDao();
			zownDao.worldName = world.getFqName();
			zownDao.zownName = originalName;
			
			// Populate the zownDao id if it exists in the database
			zownDao.read();
			
			// Set the zownDao data
			if (!zownTree.isRoot()) {
				zownDao.parentZownName = zownTree.getParent().getData().getName();
			}
			final Zown zown = zownTree.getData();
			zownDao.zownName = zown.getName();
			if (zown.getTemplate() != null) {
				zownDao.templateName = zown.getTemplate().getName();
			}
			zownDao.ownerList = new ArrayList<String>(zown.getOwnerUUIDs());
			zownDao.memberList = new ArrayList<String>(zown.getMemberUUIDs());
			zownDao.entryExclusions = new ArrayList<String>(zown.getEntryExclusionUUIDs());
			if (zown.getMinPoint() != null) {
				zownDao.minPointString = zown.getMinPoint().toString();
			}
			if (zown.getMaxPoint() != null) {
				zownDao.maxPointString = zown.getMaxPoint().toString();
			}
			
			if (zown.overridesConfiguration()) {
				saveConfiguration(zown, zownDao);
				zownDao.templateOverride = true;
			} else {
				clearConfiguration(zown, zownDao);
				zownDao.templateOverride = false;
			}
			
			zownDao.welcomeMessage = zown.getWelcomeMessage();
			zownDao.farewellMessage = zown.getFarewellMessage();
			
			saved = zownDao.save();
		}
		return saved;
	}
	
	public boolean removeTemplate(final Template template) throws DatabaseReadException,
			DatabaseWriteException {
		final boolean removed = false;
		if (template != null) {
			final TemplateDao templateDao = new TemplateDao();
			templateDao.templateName = template.getName();
			
			// Populate the templateDao id if it exists in the database
			templateDao.read();
			
			templateDao.delete();
		}
		return removed;
	}
	
	public boolean removeZown(final World world, final Tree<? extends IZown> zown)
			throws DatabaseReadException, DatabaseWriteException {
		final boolean removed = false;
		if (zown != null) {
			final ZownDao zownDao = new ZownDao();
			zownDao.worldName = world.getFqName();
			zownDao.zownName = zown.getData().getName();
			
			// Populate the zownDao id if it exists in the database
			zownDao.read();
			
			zownDao.delete();
		}
		return removed;
	}
	
	private void loadConfiguration(final IConfigurable configurable, final AConfigurationDao dao) {
		final IConfiguration config = configurable.getConfiguration();
		
		// Set owner permissions
		if (dao.ownerPermissionList != null) {
			for (final String perm : dao.ownerPermissionList) {
				config.addOwnerPermission(perm);
			}
		}
		
		// Set flags
		if (dao.allowList != null) {
			for (final String allow : dao.allowList) {
				config.setFlag(allow, true);
			}
		}
		if (dao.denyList != null) {
			for (final String deny : dao.denyList) {
				config.setFlag(deny, false);
			}
		}
		
		// Set command restrictions
		if (dao.commandRestrictionsList != null) {
			for (final String cmd : dao.commandRestrictionsList) {
				config.addCommandRestriction(cmd);
			}
		}
		
		// Set block interface
		if (dao.blockBuildExceptionList != null) {
			for (final String block : dao.blockBuildExceptionList) {
				final BlockType blockType = MinecraftMapper.getBlockType(block);
				if (blockType == null) {
					ZownPlugin.LOG.warn("Error adding " + block + " to the block build exclusions");
				} else {
					config.addBlockBuildExclusion(blockType);
				}
			}
		}
		if (dao.blockInteractExceptionList != null) {
			for (final String block : dao.blockInteractExceptionList) {
				final BlockType blockType = MinecraftMapper.getBlockType(block);
				if (blockType == null) {
					ZownPlugin.LOG.warn("Error adding " + block + " to the block interaction exclusions");
				} else {
					config.addBlockInteractExclusion(blockType);
				}
			}
		}
		
		// Set entity interface
		if (dao.entityCreateExceptionList != null) {
			for (final String entity : dao.entityCreateExceptionList) {
				final Class<? extends Entity> entityClass = MinecraftMapper.getEntityClass(entity);
				if (entityClass == null) {
					ZownPlugin.LOG.warn("Error adding " + entity + " to the entity create exclusions");
				} else {
					config.addEntityCreateExclusion(entityClass);
				}
			}
		}
		if (dao.entityInteractExceptionList != null) {
			for (final String entity : dao.entityInteractExceptionList) {
				final Class<? extends Entity> entityClass = MinecraftMapper.getEntityClass(entity);
				if (entityClass == null) {
					ZownPlugin.LOG.warn("Error adding " + entity + " to the entity interaction exclusions");
				} else {
					config.addEntityInteractExclusion(entityClass);
				}
			}
		}
	}
	
	private void saveConfiguration(final IConfigurable configurable, final AConfigurationDao dao) {
		final Configuration config = (Configuration) configurable.getConfiguration();
		
		// Set owner permissions
		dao.ownerPermissionList = new ArrayList<String>(config.getOwnerPermissions());
		
		// Set flags
		final Map<String, Boolean> flags = config.getFlags();
		dao.allowList = new ArrayList<String>();
		dao.denyList = new ArrayList<String>();
		for (final String flag : flags.keySet()) {
			if (flags.get(flag)) {
				dao.allowList.add(flag);
			} else {
				dao.denyList.add(flag);
			}
		}
		
		// Set command restrictions
		dao.commandRestrictionsList = new ArrayList<String>(config.getCommandRestrictions());
		
		// Set block interface
		dao.blockBuildExceptionList = new ArrayList<String>();
		for (final BlockType blockType : config.getBlockBuildExclusions()) {
			dao.blockBuildExceptionList.add(MinecraftMapper.getBlock(blockType));
		}
		dao.blockInteractExceptionList = new ArrayList<String>();
		for (final BlockType blockType : config.getBlockInteractExclusions()) {
			dao.blockInteractExceptionList.add(MinecraftMapper.getBlock(blockType));
		}
		
		// Set entity interface
		dao.entityCreateExceptionList = new ArrayList<String>();
		for (final Class<? extends Entity> entityClass : config.getEntityCreateExclusions()) {
			dao.entityCreateExceptionList.add(MinecraftMapper.getEntity(entityClass));
		}
		dao.entityInteractExceptionList = new ArrayList<String>();
		for (final Class<? extends Entity> entityClass : config.getEntityInteractExclusions()) {
			dao.entityInteractExceptionList.add(MinecraftMapper.getEntity(entityClass));
		}
	}
	
	private void clearConfiguration(final IConfigurable configurable, final AConfigurationDao dao) {
		// Set owner permissions
		dao.ownerPermissionList = new ArrayList<String>();
		
		// Set flags
		dao.allowList = new ArrayList<String>();
		dao.denyList = new ArrayList<String>();
		
		// Set command restrictions
		dao.commandRestrictionsList = new ArrayList<String>();
		
		// Set block interface
		dao.blockBuildExceptionList = new ArrayList<String>();
		dao.blockInteractExceptionList = new ArrayList<String>();
		
		// Set entity interface
		dao.entityCreateExceptionList = new ArrayList<String>();
		dao.entityInteractExceptionList = new ArrayList<String>();
	}
}
