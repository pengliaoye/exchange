/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.dm.jaxp;

import java.io.FileOutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.util.Date;
import javax.xml.namespace.QName;

/**
 * HelloWorld sample is used to demonstrate the use of STAX Writer api's.
 *
 * @author <a href="mailto:Jeff.Suttor@Sun.com">Jeff Suttor</a>
 */

public class HelloWorld {
    
    private static String filename = null;
    
    private static void printUsage() {
        System.out.println("usage: java -Djava.endorsed.dirs=<jaxp dist/lib directory> stax.writer.HelloWorld -f <outputFileName>");
    }
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try{
            String fileName = null;
            try{
                if(args[0].equals("-f")){
                    fileName = args[1];
                }
                else{
                    printUsage() ;
                    return;
                }
            }catch(Exception ex){
                printUsage() ;
                return;
            }
            
            XMLOutputFactory xof =  XMLOutputFactory.newInstance();
            XMLStreamWriter xtw = null;
            xtw = xof.createXMLStreamWriter(new FileOutputStream(fileName),"utf-8"); 
            xtw.writeStartDocument("utf-8","1.0");
            xtw.writeComment("StAX Sample: writer.HelloWorld");
            xtw.writeStartElement("hello");
            xtw.writeDefaultNamespace("http://samples");
            xtw.writeCharacters("this crazy");
            xtw.writeEmptyElement("world");
            xtw.writeEndElement();
            xtw.writeEndDocument();
            xtw.flush();
            xtw.close();
        }catch(Exception ex){
            ex.printStackTrace();
            System.err.println("Exception occurred while running Hello World samples");
        }
        System.out.println("Done");
    }
}