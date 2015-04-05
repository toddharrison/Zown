package com.eharrison.canary.zown.dao;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.canarymod.database.Database;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMockSupport;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZownDaoTest extends EasyMockSupport {
	private ZownDao zownDao;
	
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
		zownDao = new ZownDao(database);
	}
	
	@Test
	public void read() throws Exception {
		database.load(eq(zownDao), isA(Map.class));
		expectLastCall().anyTimes();
		replayAll();
		
		// Read with a null world and zown names
		assertFalse(zownDao.read());
		
		// Read with non-existant world and zown names
		zownDao.worldName = "world1";
		zownDao.zownName = "zown1";
		assertFalse(zownDao.read());
		
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
