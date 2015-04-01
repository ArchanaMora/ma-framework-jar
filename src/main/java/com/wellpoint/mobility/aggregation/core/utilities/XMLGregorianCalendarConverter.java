package com.wellpoint.mobility.aggregation.core.utilities;

import java.lang.reflect.Type;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class XMLGregorianCalendarConverter
{
	/**
     * Logger
     */
	private Logger logger = Logger.getLogger(Serializer.class);

	@SuppressWarnings("rawtypes")
	public static class Serializer implements JsonSerializer {
		
		
        public Serializer() {
            super();
        }
        @Override
        public JsonElement serialize(Object t, Type type,
                JsonSerializationContext jsonSerializationContext) {
            XMLGregorianCalendar xgcal = (XMLGregorianCalendar) t;
            return new JsonPrimitive(xgcal.toXMLFormat());
        }

    }
    @SuppressWarnings("rawtypes")
	public static class Deserializer implements JsonDeserializer {

    	@Override
        public Object deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) {
            try {
                String obj  = jsonElement.getAsString();
                GregorianCalendar calendar = new GregorianCalendar();
                int year, month, day;
                year = Integer.parseInt(obj.substring(0,4));
                month = Integer.parseInt(obj.substring(5,7));
                day = Integer.parseInt(obj.substring(8,10));
 			
                XMLGregorianCalendar xmlGregCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                xmlGregCalendar.setYear(year);
                xmlGregCalendar.setMonth(month);
                xmlGregCalendar.setDay(day);
                return xmlGregCalendar;
            } catch (Exception e) {
            	e.printStackTrace();
                return null;
            }
        }
    }
}