package com.dm.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import com.dm.entity.Node;
import com.dm.service.NodeManager;
import com.dm.util.ConnUtil;
import com.dm.util.JpaUtils;

public class NodeManagerTest {

	public void testCreateTree() {
		NodeManager.getInstance().createTree("C:\\tmp");
	}

	public void testPrintTree() {
		NodeManager.getInstance().printTree(3);
	}

	public void testBuildTree() {
		StringBuilder builder = new StringBuilder();
		builder.append("with recursive temp as(select a.* from tree a where pid is null union all\n");
		builder.append("select t.* from tree t, temp tp where t.pid = tp.id\n");
		builder.append(") select * from temp");
		String sqlString = builder.toString();
		try (Connection conn = ConnUtil.getConn();
				PreparedStatement ps = conn.prepareStatement(sqlString)) {
			ResultSet rs = ps.executeQuery();
			NodeManager nodeMg = NodeManager.getInstance();
			Node root = nodeMg.buildTree(rs);
			nodeMg.printNode(root);
		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

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
