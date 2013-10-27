package com.dm.jaxb;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JaxbDemo {

	/**
	 * @param args
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			JAXBException {
		JaxbDemo demo = new JaxbDemo();
		InputStream is = JaxbDemo.class.getResourceAsStream("hello.xml");
		GreetingListType greetingListType = demo.unmarshal(
				GreetingListType.class, is);
		List<GreetingType> greetingList = greetingListType.getGreeting();
		for (GreetingType gt : greetingList) {
			System.out.println(gt.getLanguage() + " " + gt.getText());			
		}
	}

	public <T> T unmarshal(Class<T> docClass, InputStream inputStream)
			throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		JAXBElement<T> doc = (JAXBElement<T>) u.unmarshal(inputStream);
		return doc.getValue();
	}

}
