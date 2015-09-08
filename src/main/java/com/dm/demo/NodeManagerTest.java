package com.dm.demo;

import com.dm.entity.Node;
import com.dm.service.NodeManager;
import com.dm.util.ConnUtil;
import com.dm.util.JpaUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

public class NodeManagerTest {
	
  public static void main(String[] args){
	  testBuildTree();
  }

  public static void testCreateTree() {
    NodeManager.getInstance().createTree("C:/dev/my-project");
  }

  public static void testPrintTree() {
    NodeManager.getInstance().printTree("7eccf02d-fc55-4968-adcd-450179f76c5a");
  }

  public static void testBuildTree() {
    StringBuilder builder = new StringBuilder();
    builder.append("with recursive temp as(select a.* from tb_tree a where pid is null union all\n");
    builder.append("select t.* from tb_tree t, temp tp where t.pid = tp.id\n");
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

  public static void testParseCheckTree() {
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
