package com.eharrison.canary.zown.command;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eharrison.canary.zown.api.Point;

public class ParameterTokenizerTest {
	@Test
	public void test() {
		final ParameterTokenizer pTokens = new ParameterTokenizer(new String[] {
				"foo", "bar", "42", "1", "1", "1"
		});
		
		assertEquals("foo", pTokens.readString());
		assertEquals("bar", pTokens.readString());
		assertEquals(42, pTokens.readInteger());
		assertEquals(new Point(1, 1, 1), pTokens.readPoint());
	}
}
