package com.dm.system;

import junit.framework.TestCase;

public class NodeManagerTest extends TestCase {

	public void testCreateTree() {
		NodeManager.getInstance().createTree("D:\\share\\JavaProjects\\hibernate\\hibernate_basemapping\\");
	}
   
	public void testPrintTree() {
		NodeManager.getInstance().printTree(1);
	}

}
