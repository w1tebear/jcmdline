/*
 * FileParam_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: FileParam_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   FileParam_UT
 *   
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is the Java jcmdline (command line management) package.
 *
 * The Initial Developer of the Original Code is Lynne Lawrence.
 * 
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):  Lynne Lawrence <lynneglawrence02@gmail.com>
 *
 * ***** END LICENSE BLOCK *****
 */

package jcmdline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jcmdline.CmdLineException;
import jcmdline.FileParam;
import jcmdline.Parameter;

/**
 * Unit test code for FileParam
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: FileParam_UT.java,v 1.2 2002/12/07
 *          14:30:49 lglawrence Exp $
 */
public class FileParamTest extends BetterTestCase {

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public FileParamTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, FileParamTest.class);
	}

	/**
	 * Test constructor taking tag, desc
	 */
	public void testCtor1() {
		String tag = "infile";
		String desc = "description of the file param'g";
		FileParam p = new FileParam(tag, desc);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertEquals("attributes not set correctly", FileParam.NO_ATTRIBUTES, p
				.getAttributes());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
	}

	/**
	 * Test constructor taking tag, desc, optional
	 */
	public void testCtor2() {
		String tag = "infile";
		String desc = "description of the file param'g";
		FileParam p = new FileParam(tag, desc, FileParam.OPTIONAL);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertEquals("attributes not set correctly", FileParam.NO_ATTRIBUTES, p
				.getAttributes());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertTrue("optional flag not set correctly to FileParam.OPTIONAL", p
				.isOptional());
		p = new FileParam(tag, desc, FileParam.REQUIRED);
		assertTrue("optional flag not set correctly to FileParam.REQUIRED", !p
				.isOptional());
	}

	/**
	 * Test constructor taking tag, desc, attributes
	 */
	public void testCtor3() {
		String tag = "infile";
		String desc = "description of the file param'g";
		int attr = FileParam.IS_DIR;
		FileParam p = new FileParam(tag, desc, attr);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertEquals("attributes not set correctly", attr, p.getAttributes());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
	}

	/**
	 * Test constructor taking tag, desc, attr, optional ind.
	 */
	public void testCtor4() {
		String tag = "infile";
		String desc = "description of the file param'g";
		int attr = FileParam.IS_DIR;
		FileParam p = new FileParam(tag, desc, attr, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set to true correctly", desc, p.getDesc());
		assertEquals("attributes not set correctly", attr, p.getAttributes());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		p = new FileParam(tag, desc, attr, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
	}

	/**
	 * Test constructor taking tag, desc, attr, optional, and multi-valued
	 */
	public void testCtor5() {
		String tag = "infile";
		String desc = "description of the file param'g";
		int attr = FileParam.IS_DIR;
		FileParam p = new FileParam(tag, desc, attr, true, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertEquals("attributes not set correctly", attr, p.getAttributes());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set to true correctly", p
				.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		p = new FileParam(tag, desc, attr, false, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
		assertTrue("multiValued flag not set to false correctly", !p
				.isMultiValued());
	}

	/**
	 * Test constructor taking tag, desc, attr, optional multiValued, and hidden
	 */
	public void testCtor6() {
		String tag = "infile";
		String desc = "description of the file param'g";
		int attr = FileParam.IS_DIR;
		FileParam p = new FileParam(tag, desc, attr, true, true, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertEquals("attributes not set correctly", attr, p.getAttributes());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set to true correctly", p
				.isMultiValued());
		assertTrue("hidden flag not set to true correctly", p.isHidden());
		p = new FileParam(tag, desc, attr, false, false, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
		assertTrue("multiValued flag not set to false correctly", !p
				.isMultiValued());
		assertTrue("hidden flag not set to false correctly", !p.isHidden());
	}

	/**
	 * Tests getValue()
	 */
	public void testGetFile() throws CmdLineException {
		debug("Starting testIntValue()");
		FileParam p = new FileParam("infile", "myDesc");
		File f1 = new File("myfile");
		p.addValue(f1);
		assertEquals("getValue() returned wrong value", f1, p.getValue());

		p = new FileParam("infile", "myDesc");
		p.setMultiValued(Parameter.MULTI_VALUED);
		File f2 = new File("otherfile");
		p.addValue(f1);
		p.addValue(f2);
		assertEquals("getValue() returned wrong value for multi-valued", f1, p
				.getValue());

		p = new FileParam("infile", "myDesc");
		File f = p.getValue();
		assertNull("getValue() on param with no value set did not return null",
				f);
	}
	
	
	File tmpfile = null;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Tests getValue()
     * @throws IOException 
	 */
	public void testFileDoesntExist() throws CmdLineException, IOException {
	    debug("Starting testIntValue()");
	    FileParam p = new FileParam("outfile", "myDesc", FileParam.DOESNT_EXIST);
	    
	    File f1 = new File("file_that_does_not_exist");
	    p.addValue(f1);
	    assertEquals("getValue() returned wrong value", f1, p.getValue());
	    
	    p = new FileParam("outfile", "myDesc", FileParam.DOESNT_EXIST);
	    f1 = File.createTempFile("testFileDoesntExist", null);
	    f1.deleteOnExit();
	    
	    try {
            p.addValue(f1);
            fail("Adding File value for file that exists does not throw CmdLineException");
        } catch (CmdLineException e) {
            // expected
            assertTrue("Wrong error message received", e.getMessage().contains("must be a non-existing"));
        }
	    
	    p = new FileParam("outfile", "myDesc", FileParam.DOESNT_EXIST);
	    
	    try {
	        p.addStringValue(f1.getAbsolutePath());
	        fail("Adding filename value for file that exists does not throw CmdLineException");
	    } catch (CmdLineException e) {
	        // expected
	        assertTrue("Wrong error message received", e.getMessage().contains("must be a non-existing"));
	    }
	}

	/**
	 * Tests getValues()
	 */
	public void testGetValues() throws CmdLineException {
		FileParam p = new FileParam("infile", "myDesc");
		p.setMultiValued(Parameter.MULTI_VALUED);
		File f1 = new File("myfile");
		File f2 = new File("otherfile");
		p.addValue(f1);
		p.addValue(f2);
		List<File> vals = p.getValues();
		assertEquals("getValues() returned array of wrong size", 2, vals.size());
		assertEquals("getValues() has wrong value for index 0", f1, vals
				.get(0));
		assertEquals("getValues() has wrong value for index 1", f2, vals
				.get(1));
	}

	/**
	 * Tests setAttributes()/attrSpecified()
	 */
	public void testSetAttributes() {
		FileParam p = new FileParam("infile", "myDesc");
		p.setAttributes(FileParam.IS_DIR & FileParam.IS_READABLE);
		assertTrue("IS_DIR did not get set properly", p
				.attrSpecified(FileParam.IS_DIR));
		assertTrue("IS_READABLE did not get set properly", p
				.attrSpecified(FileParam.IS_READABLE));
		assertTrue("IS_WRITEABLE is set, but shouldn't be", !p
				.attrSpecified(FileParam.IS_WRITEABLE));
		assertTrue("IS_FILE is set, but shouldn't be", !p
				.attrSpecified(FileParam.IS_FILE));
		assertTrue("DOESNT_EXIST is set, but shouldn't be", !p
				.attrSpecified(FileParam.DOESNT_EXIST));
		assertTrue("EXISTS is set, but shouldn't be", !p
				.attrSpecified(FileParam.EXISTS));
	}
}
