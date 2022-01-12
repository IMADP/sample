package com.sample.api.service;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * JsonMapper
 *
 * Encapsulates a global object mapper and provides convenience methods for json conversion.
 *
 * @author Anthony DePalma
 */
public final class JsonMapper {

	// the object mapper
	private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

	/**
	 * Creates an object mapper that can be used for all conversions.
	 *
	 * @return ObjectMapper
	 */
	private static ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		return mapper;
	}

	/**
	 * Returns the object mapper instance.
	 *
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	/**
	 * Returns a new ObjectNode which can be used to construct dynamic json objects.
	 *
	 * @return ObjectNode
	 */
	public static ObjectNode newObject() {
		return OBJECT_MAPPER.createObjectNode();
	}

	/**
	 * Returns a new ArrayNode which can be used to construct dynamic json objects.
	 *
	 * @return ArrayNode
	 */
	public static ArrayNode newArray() {
		return OBJECT_MAPPER.createArrayNode();
	}

	/**
	 * Converts an object to json.
	 *
	 * @param object
	 * @return String
	 */
	public static String toJson(Object object) {
		try
		{
			return OBJECT_MAPPER.writeValueAsString(object);
		}
		catch (JsonProcessingException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts a json string to an object.
	 *
	 * @param json
	 * @param objectClass
	 * @return T
	 */
	public static <T> T fromJson(String json, Class<T> objectClass) {
		try
		{
			if(json == null)
				return null;

			return OBJECT_MAPPER.readValue(json, objectClass);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts a json string to an object.
	 *
	 * @param json
	 * @param typeReference
	 * @return T
	 */
	public static <T> T fromJson(String json, TypeReference<T> typeReference) {
		try
		{
			if(json == null)
				return null;

			return OBJECT_MAPPER.readValue(json, typeReference);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * The correct way to convert a node to a json string.
	 *
	 * @param object
	 * @return String
	 */
	public static String toString(ObjectNode object) {
		try
		{
			if(object == null)
				return null;

			return OBJECT_MAPPER.writeValueAsString(object);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

}