/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dm.system;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;

import com.dm.entity.Node;
import com.dm.service.NodeManager;
import com.dm.util.ConnUtil;

/**
 * 
 * @author Administrator
 */
@Model
public class CheckTreeBean {

	private static final Logger logger = Logger.getLogger(CheckTreeBean.class
			.getName());

	public String getTreeData() {
		String result = null;
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
			result = nodeMg.parseCheckTree(root);
		} catch (SQLException | ClassNotFoundException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return result;
	}

}
