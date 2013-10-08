package com.dm.demo;

import java.io.StringReader;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonGenerator;

import com.dm.entity.GeneralProperty;

public class JsonDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	public void objectToJson(){
		GeneralProperty genaralProp = new GeneralProperty();
		genaralProp.setName("abc");
		genaralProp.setValue("123");
		StringWriter writer = new StringWriter();
		JsonGenerator gen = Json.createGenerator(writer);
		gen.writeStartObject()
				.write("id", genaralProp.getId())
				.write("name", genaralProp.getName())
				.write("value", genaralProp.getValue())
			.writeEnd();		
		gen.close();
		System.out.println(writer.toString());
	}
	
	public void jsonToObj(){
		StringReader reader = new StringReader("123");
		JsonReader jsonReader = Json.createReader(reader);
		JsonStructure jsonst = jsonReader.read();
	}

}
