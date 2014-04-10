package com.dm.system;

import javax.annotation.Resource;
import javax.enterprise.inject.Model;
import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

@Model
public class JcrBean {
	
	@Resource(name = "jcr/Repository")
	private Repository repository;
	
	public void testJcr(){
		Credentials credentials = new SimpleCredentials("admin", "admin".toCharArray());
		try {
			Session session = repository.login(credentials, null);
			Workspace ws = session.getWorkspace();
			ws.getNamespaceRegistry().registerNamespace("wiki", "http://www.barik.net/wiki/1.0");
			Node node = session.getRootNode();
			System.out.println(node.getPrimaryNodeType().getName());
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
