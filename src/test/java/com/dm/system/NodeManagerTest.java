package com.dm.system;

import junit.framework.TestCase;

import com.dm.service.NodeManager;

public class NodeManagerTest extends TestCase {

	public void testCreateTree() {
		NodeManager.getInstance().createTree("C:\\workspace\\");
	}
   
	public void testPrintTree() {
		NodeManager.getInstance().printTree(3);
	}

}
