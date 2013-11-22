package com.dm.system;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

import com.dm.entity.Node;
import com.dm.service.NodeManager;
import com.dm.util.JpaUtils;

public class NodeManagerTest {

	@Test
	public void testCreateTree() {
		NodeManager.getInstance().createTree(
				"C:\\Users\\Administrator\\git\\exchange\\src\\main\\java");
	}

	@Test
	public void testPrintTree() {
		NodeManager.getInstance().printTree(3);
	}

	@Test
	public void testBuildTree() {
		EntityManager em = JpaUtils.getEntityManager();
		StringBuilder builder = new StringBuilder();
		builder.append("with recursive temp as(select a.* from tree a union all\n");
		builder.append("select t.* from tree t, temp tp where t.pid = tp.id\n");
		builder.append(") select * from temp");
		String sqlString = builder.toString();
		try {
			Query query = em.createNativeQuery(sqlString, Node.class);
			List nodes = query.getResultList();
			NodeManager nodeMg = NodeManager.getInstance();
			Node root = nodeMg.buildTree(nodes);
			nodeMg.printNode(root);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			JpaUtils.closeEm(em);
		}
	}

	@Test
	public void testParseCheckTree() {
		EntityManager em = JpaUtils.getEntityManager();
		try {
			Node node = em.find(Node.class, 1);
			String result = NodeManager.getInstance().parseCheckTree(node);
			System.out.println(result);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			JpaUtils.closeEm(em);
		}
	}

}
