package com.goodformentertainment.canary.zown.api.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemplateTest {
    private static final String TEMPLATE_NAME = "templateName";

    private Template template;

    @Before
    public void init() {
        template = new Template(TEMPLATE_NAME);
    }

    @Test
    public void testName() {
        assertEquals(TEMPLATE_NAME, template.getName());
    }

    @Test
    public void testRename() {
        template.setName("newName");
        assertEquals("newName", template.getName());
    }

    @Test
    public void testHasConfiguration() {
        assertNotNull(template.getConfiguration());
    }

    @Test
    public void testAddAndRemoveZown() {
        assertTrue(template.getZowns().isEmpty());
        final Zown zown = new Zown("zown");
        assertTrue(template.addZown(zown));
        assertEquals(template, zown.getTemplate());
        assertEquals(1, template.getZowns().size());
        assertFalse(template.removeZown(new Zown("zown2")));
        assertFalse(template.getZowns().isEmpty());
        assertTrue(template.removeZown(zown));
        assertNull(zown.getTemplate());
        assertTrue(template.getZowns().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFailureOnAddZownDirectly() {
        assertTrue(template.getZowns().isEmpty());
        template.getZowns().add(new Zown("zown"));
    }
}
