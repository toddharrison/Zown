package com.goodformentertainment.canary.zown.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.canarymod.database.Database;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMockSupport;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.goodformentertainment.canary.zown.dao.TemplateDao;

@Ignore
public class TemplateDaoTest extends EasyMockSupport {
	private TemplateDao templateDao;
	
	private Database database;
	
	@BeforeClass
	@AfterClass
	public static void clean() throws IOException {
		final File configDir = new File("config");
		if (configDir.exists()) {
			FileUtils.deleteDirectory(configDir);
		}
		final File dbDir = new File("db");
		if (dbDir.exists()) {
			FileUtils.deleteDirectory(dbDir);
		}
	}
	
	@Before
	public void init() {
		database = createMock(Database.class);
		templateDao = new TemplateDao(database);
	}
	
	@Test
	public void read() throws Exception {
		replayAll();
		
		assertFalse(templateDao.read());
		
		verifyAll();
	}
	
	@Test
	public void save() {
		assertTrue(true);
	}
	
	@Test
	public void delete() {
		assertTrue(true);
	}
}
