/**
 * OWASP Enterprise Security API (ESAPI)
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * http://www.owasp.org/esapi.
 *
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The ESAPI is published by OWASP under the LGPL. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */
package com.dm.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.errors.AuthenticationException;

public class SimpleESAPIFilter implements Filter {

	private static final Logger logger = ESAPI.getLogger( "SimpleESAPIFilter" );

	private static final String[] ignore = { "password" };

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * placed into service. The servlet container calls the init method exactly
	 * once after instantiating the filter. The init method must complete
	 * successfully before the filter is asked to do any filtering work.
	 * 
	 * @param filterConfig
	 *            configuration object
	 */
	public void init(FilterConfig filterConfig) {
//		String path = filterConfig.getInitParameter("resourceDirectory");
//		// FIXME: consider allowing a per-webapp ESAPI instance
//		// String path = filterConfig.getServletContext().getRealPath("/");
//		// path += "WEB-INF/resources";
//		ESAPI.securityConfiguration().setResourceDirectory( path );
	}

	/**
	 * The doFilter method of the Filter is called by the container each time a
	 * request/response pair is passed through the chain due to a client request
	 * for a resource at the end of the chain. The FilterChain passed in to this
	 * method allows the Filter to pass on the request and response to the next
	 * entity in the chain.
	 * 
	 * @param request
	 *            Request object to be processed
	 * @param response
	 *            Response object
	 * @param chain
	 *            current FilterChain
	 * @exception IOException
	 *                if any occurs
	 * @throws ServletException
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		try {
			// register request and response in ESAPI (usually done through login)
			ESAPI.httpUtilities().setCurrentHTTP(request, response);
			
			// log this request, obfuscating any parameter named password
			ESAPI.httpUtilities().logHTTPRequest(ESAPI.httpUtilities().getCurrentRequest(), logger, Arrays.asList(ignore));
			
			// CD 2011-04-30: if the user can be identified via the session ID, the following will set "currentUser" 
			// in the Authenticator for consistent user identification across all requests in log messages
			HttpSession session = ESAPI.httpUtilities().getCurrentRequest().getSession(false);
			if(session != null){
				try{
					//ESAPI.authenticator().login();
				}
				catch (Exception e) {
					// noop
					ESAPI.authenticator(); // for breakpoint debugging only
				}
			}
			else{
				//noop
				ESAPI.authenticator(); // for breakpoint debugging only
			}

			// forward this request on to the web application
			chain.doFilter(request, response);
		} catch (Exception e) {
			logger.error( Logger.SECURITY_FAILURE, "Error in ESAPI security filter: " + e.getMessage(), e );
			request.setAttribute("message", e.getMessage() );
		} finally {
			// VERY IMPORTANT
			// clear out the ThreadLocal variables in the authenticator
			// some containers could possibly reuse this thread without clearing the User
			// CD 2011-04-12: ATTENTION: this will only work if this filter is correctly configured in web.xml !!!
			ESAPI.authenticator().clearCurrent();
		}
	}

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * taken out of service. This method is only called once all threads within
	 * the filter's doFilter method have exited or after a timeout period has
	 * passed. After the web container calls this method, it will not call the
	 * doFilter method again on this instance of the filter.
	 */
	public void destroy() {
		// finalize
	}

}