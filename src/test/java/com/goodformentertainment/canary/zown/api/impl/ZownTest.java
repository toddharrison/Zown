package com.goodformentertainment.canary.zown.api.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import net.canarymod.api.entity.living.humanoid.Player;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.goodformentertainment.canary.zown.api.Point;
import com.goodformentertainment.canary.zown.api.impl.Template;
import com.goodformentertainment.canary.zown.api.impl.Zown;

public class ZownTest extends EasyMockSupport {
	private static final String ZOWN_NAME = "zownName";
	
	private Template template;
	private Zown zown;
	
	private Player playerMock;
	
	@Before
	public void init() {
		playerMock = createMock(Player.class);
		template = new Template("template");
		zown = new Zown(ZOWN_NAME);
	}
	
	@Test
	public void testName() {
		assertEquals(ZOWN_NAME, zown.getName());
	}
	
	@Test
	public void testRename() {
		zown.setName("newName");
		assertEquals("newName", zown.getName());
	}
	
	@Test
	public void testHasConfiguration() {
		assertNotNull(zown.getConfiguration());
	}
	
	@Test
	public void testLoadAndRemoveTemplate() {
		final Template template = new Template("template");
		template.getConfiguration().addCommandRestriction("foo");
		
		assertFalse(zown.getConfiguration().hasCommandRestriction("foo"));
		zown.loadTemplate(template);
		assertEquals(template, zown.getTemplate());
		assertTrue(zown.getConfiguration().hasCommandRestriction("foo"));
		
		zown.setTemplate(null);
		assertEquals(null, zown.getTemplate());
		assertTrue(zown.getConfiguration().hasCommandRestriction("foo"));
		
		final Template template2 = new Template("template2");
		template2.getConfiguration().addCommandRestriction("bar");
		zown.setTemplate(template2);
		assertEquals(template2, zown.getTemplate());
		assertTrue(zown.getConfiguration().hasCommandRestriction("foo"));
		assertFalse(zown.getConfiguration().hasCommandRestriction("bar"));
		
		zown.loadTemplate(template2);
		assertEquals(template2, zown.getTemplate());
		assertFalse(zown.getConfiguration().hasCommandRestriction("foo"));
		assertTrue(zown.getConfiguration().hasCommandRestriction("bar"));
	}
	
	@Test
	public void testMembership() {
		expect(playerMock.getUUIDString()).andReturn("foo").anyTimes();
		replayAll();
		
		// Verify adding once
		assertFalse(zown.isMember(playerMock));
		assertFalse(zown.isOwnerOrMember(playerMock));
		assertTrue(zown.addMember(playerMock));
		assertFalse(zown.addMember(playerMock));
		assertTrue(zown.isOwnerOrMember(playerMock));
		
		// Set to owner
		assertFalse(zown.isOwner(playerMock));
		assertTrue(zown.addOwner(playerMock));
		assertFalse(zown.addOwner(playerMock));
		assertFalse(zown.isMember(playerMock));
		assertTrue(zown.isOwner(playerMock));
		assertTrue(zown.isOwnerOrMember(playerMock));
		assertFalse(zown.addMember(playerMock));
		
		// Remove
		assertFalse(zown.removeMember(playerMock));
		assertTrue(zown.isOwner(playerMock));
		assertTrue(zown.removeOwner(playerMock));
		assertFalse(zown.isOwnerOrMember(playerMock));
		assertFalse(zown.removeOwner(playerMock));
		
		verifyAll();
	}
	
	@Test
	public void testGlobalZown() {
		assertNull(zown.getMinPoint());
		assertNull(zown.getMaxPoint());
		
		assertTrue(zown.contains(new Point(0, 0, 0)));
		assertTrue(zown.contains(new Point(1000, -10, 50)));
		assertTrue(zown.contains(new Zown("bar")));
		assertTrue(zown.contains(new Zown("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
		
		assertFalse(zown.intersects(new Zown("bar")));
		assertFalse(zown.intersects(new Zown("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
	}
	
	@Test
	public void testZownMembership() {
		final Zown zown = new Zown("foo", new Point(5, 5, 5), new Point(10, 10, 10));
		assertEquals(new Point(5, 5, 5), zown.getMinPoint());
		assertEquals(new Point(10, 10, 10), zown.getMaxPoint());
		
		assertFalse(zown.contains(new Point(0, 0, 0)));
		assertFalse(zown.contains(new Point(4, 4, 4)));
		assertTrue(zown.contains(new Point(5, 5, 5)));
		assertTrue(zown.contains(new Point(10, 10, 10)));
		assertFalse(zown.contains(new Point(11, 11, 11)));
		assertTrue(zown.contains(new Point(5, 5, 5), new Point(10, 10, 10)));
		assertFalse(zown.contains(new Point(5, 5, 5), new Point(11, 11, 11)));
		assertFalse(zown.contains(new Zown("bar")));
		assertFalse(zown.contains(new Zown("baz", new Point(0, 0, 0), new Point(4, 4, 4))));
		assertFalse(zown.contains(new Zown("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
		assertTrue(zown.contains(new Zown("baz", new Point(6, 6, 6), new Point(7, 7, 7))));
		assertTrue(zown.contains(new Zown("baz", new Point(5, 5, 5), new Point(10, 10, 10))));
		
		assertFalse(zown.intersects(new Point(0, 0, 0), new Point(4, 4, 4)));
		assertTrue(zown.intersects(new Point(0, 0, 0), new Point(5, 5, 5)));
		assertFalse(zown.intersects(new Zown("bar")));
		assertFalse(zown.intersects(new Zown("baz", new Point(0, 0, 0), new Point(4, 4, 4))));
		assertTrue(zown.intersects(new Zown("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
		assertFalse(zown.intersects(new Zown("baz", new Point(6, 6, 6), new Point(7, 7, 7))));
		assertFalse(zown.intersects(new Zown("baz", new Point(5, 5, 5), new Point(10, 10, 10))));
	}
}
