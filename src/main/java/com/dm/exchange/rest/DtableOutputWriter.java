package com.dm.exchange.rest;

import com.dm.exchange.rest.bean.DtableOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class DtableOutputWriter implements MessageBodyWriter<DtableOutput> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return DtableOutput.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(DtableOutput t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(DtableOutput dtableOutput, Class<?> type, Type type1,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> multivaluedMap,
            OutputStream output)
            throws IOException, WebApplicationException {
        JsonGenerator generator = Json.createGenerator(output);
        generator.writeStartObject()
                .write("draw", dtableOutput.getDraw())
                .write("recordsFiltered", dtableOutput.getRecordsFiltered())
                .write("recordsTotal", dtableOutput.getRecordsTotal())
                .writeStartArray("data");
        for(Object[] datArr : dtableOutput.getData()){
            generator.writeStartArray();
            for(Object dat : datArr){
                generator.write(dat.toString());
            }
            generator.writeEnd();
        }
        generator.writeEnd().writeEnd();
        generator.flush();
    }
}
