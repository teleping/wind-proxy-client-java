package com.bs.data.wind.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple WindProxy.
 */
public class WindProxyTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public WindProxyTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(WindProxyTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */

	public void testWsd() {
		WindData data = null;
		String codes = "600036.SH";
		String fields = "industry_CSRC12,close,high,open,eps_basic,eps_diluted";
		String startTime = "2017-01-01";
		String endTime = "2017-03-01";
		String options = "Fill=Previous";
		data = WindProxy.wsd(codes, fields, startTime, endTime, options);
		assertTrue(data.getMapRows().size() == 37);
	}

	public void testEdb() {
		WindData data = null;
		String codes = "M0061580,M0061581";
		String startTime = "2015-10-01";
		String endTime = "2016-01-01";
		String options = "Fill=Previous";
		data = WindProxy.edb(codes, startTime, endTime, options);
		assertTrue(data.getMapRows().size() == 8);
	}
}
