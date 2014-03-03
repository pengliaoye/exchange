package com.dm.demo;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class JndiDemo {

	public static void main(String[] args) throws NamingException {
		Properties properties = new Properties();
		properties.put( Context.INITIAL_CONTEXT_FACTORY, 
		  "com.sun.jndi.ldap.LdapCtxFactory" );
		properties.put( Context.PROVIDER_URL, "ldap://ldap.uow.edu.au:389");
		properties.put( Context.REFERRAL, "ignore" );
		
		InitialDirContext context = new InitialDirContext( properties );
		
		// Create the search controls
		SearchControls searchCtls = new SearchControls();

		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		// specify the LDAP search filter, just users
		String searchFilter = "objectClass=inetorgperson";

		// Specify the attributes to return
		String returnedAtts[]={"uid", "cn", "sn", "mail"};
		searchCtls.setReturningAttributes(returnedAtts);

		NamingEnumeration<SearchResult> answer = context.search( "ou=people, o=University of Wollongong, c=au", searchFilter, 
		  searchCtls );
		int i = 1;
		while(answer.hasMoreElements()){
			i++;
			SearchResult sr = answer.nextElement();
			Attributes attrs = sr.getAttributes();
			String cn = (String)attrs.get("cn").get();
			String mail = (String)attrs.get("mail").get();
			System.out.println("cn = " + cn + ", mail = " + mail);
		}
		System.out.println("sum="+i);
	}

}
