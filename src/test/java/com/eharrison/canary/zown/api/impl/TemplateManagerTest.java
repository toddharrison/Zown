package com.eharrison.canary.zown.api.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TemplateManagerTest {
	private TemplateManager templateManager;
	
	@Before
	public void init() {
		templateManager = new TemplateManager();
	}
	
	@Test
	public void testCreateAndRemoveTemplate() {
		assertNull(templateManager.getTemplate("foo"));
		
		final Template template = templateManager.createTemplate("foo");
		assertNotNull(template);
		assertEquals("foo", template.getName());
		assertNull(templateManager.createTemplate("foo"));
		
		assertNotNull(templateManager.getTemplate("foo"));
		
		assertTrue(templateManager.removeTemplate("foo"));
		assertFalse(templateManager.removeTemplate("foo"));
		assertNull(templateManager.getTemplate("foo"));
	}
	
	@Test
	public void testRenameTemplate() {
		assertNotNull(templateManager.createTemplate("foo"));
		assertNotNull(templateManager.getTemplate("foo"));
		
		assertTrue(templateManager.renameTemplate("foo", "bar"));
		assertNull(templateManager.getTemplate("foo"));
		assertNotNull(templateManager.getTemplate("bar"));
		
		assertTrue(templateManager.removeTemplate("bar"));
	}
}
