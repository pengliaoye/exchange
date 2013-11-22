/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dm.system;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;

import com.dm.entity.Node;
import com.dm.service.NodeManager;
import com.dm.util.JpaUtils;

/**
 * 
 * @author Administrator
 */
@Model
public class CheckTreeBean {

	private static final Logger logger = Logger.getLogger(CheckTreeBean.class
			.getName());

	public String getTreeData() {
		EntityManager em = JpaUtils.getEntityManager();
		String result = null;
		try {
			Node node = em.find(Node.class, 1);
			result = NodeManager.getInstance().parseCheckTree(node);
		} catch (RuntimeException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			JpaUtils.closeEm(em);
		}
		return result;
	}

}
