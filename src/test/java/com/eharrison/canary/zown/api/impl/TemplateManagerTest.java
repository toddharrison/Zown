package com.eharrison.canary.zown.api.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.eharrison.canary.zown.dao.DataManager;

public class TemplateManagerTest extends EasyMockSupport {
	private DataManager dataManagerMock;
	
	@Before
	public void init() {
		dataManagerMock = createMock(DataManager.class);
	}
	
	@Test
	public void testCreateAndRemoveTemplate() throws Exception {
		dataManagerMock.loadTemplates(isA(TemplateManager.class));
		expect(dataManagerMock.saveTemplate(isA(Template.class))).andReturn(true);
		expect(dataManagerMock.removeTemplate(isA(Template.class))).andReturn(true);
		replayAll();
		
		final TemplateManager templateManager = new TemplateManager(dataManagerMock);
		
		assertNull(templateManager.getTemplate("foo"));
		
		final Template template = templateManager.createTemplate("foo");
		assertNotNull(template);
		assertEquals("foo", template.getName());
		assertNull(templateManager.createTemplate("foo"));
		
		assertNotNull(templateManager.getTemplate("foo"));
		
		assertTrue(templateManager.removeTemplate("foo"));
		assertFalse(templateManager.removeTemplate("foo"));
		assertNull(templateManager.getTemplate("foo"));
		
		verifyAll();
	}
	
	@Test
	public void testRenameTemplate() throws Exception {
		dataManagerMock.loadTemplates(isA(TemplateManager.class));
		expect(dataManagerMock.saveTemplate(isA(Template.class))).andReturn(true);
		expect(dataManagerMock.saveTemplate(isA(Template.class), eq("foo"))).andReturn(true);
		replayAll();
		
		final TemplateManager templateManager = new TemplateManager(dataManagerMock);
		
		assertNotNull(templateManager.createTemplate("foo"));
		assertNotNull(templateManager.getTemplate("foo"));
		
		assertTrue(templateManager.renameTemplate("foo", "bar"));
		assertNull(templateManager.getTemplate("foo"));
		assertNotNull(templateManager.getTemplate("bar"));
		
		verifyAll();
	}
}
