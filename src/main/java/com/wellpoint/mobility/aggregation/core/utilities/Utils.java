/**
* Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
* Author: frank.garber@wellpoint.com
* 
*/
package com.wellpoint.mobility.aggregation.core.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.wellpoint.mobility.aggregation.persistence.domain.MapOfBlob;


/**
 * A collection of static Utility methods and constants.
 * 
 * @author frank.garber@wellpoint.com
 *
 */
public class Utils {
	
	private Utils(){}	// Keep people from instantiating this class
	
	public static final long MS_IN_SECOND = 1000;
	public static final long MS_IN_MINUTE = MS_IN_SECOND * 60;
	public static final long MS_IN_HOUR = MS_IN_MINUTE * 60;
	public static final long MS_IN_DAY = MS_IN_HOUR * 24;
	
			
	/**
	 * 
	 * Iterates over the collection calling toString() on each element.
	 * 
	 * @param collection
	 *            non-null collection
	 * 
	 * @param delimiter
	 *            non-null, maybe an empty string used to delimit each elements
	 *            toString() value
	 * 
	 * @param separator
	 *            non-null, maybe an empty string used to separate each element
	 * 
	 * @return a StringBuilder with the stringified collection
	 */
	public static <T> StringBuilder toString(final Collection<T> collection, final String delimiter, final String separator) {

		final StringBuilder sb = new StringBuilder(collection.size() * 128);

		boolean first = true;

		for (final T object : collection) {
			if (first) {
				first = false;
			} else {
				sb.append(separator);
			}

			sb.append(delimiter).append(object.toString()).append(delimiter);
		} // of for

		return sb;

	} // of toString(Collection<Object>, String, String)

	
	
	/**
	 * 
	 * Iterates over the collection calling toString() on each element. It uses
	 * the default delimiter of "'" and separator of ",". 
	 * 
	 * @param collection non-null collection
	 * 
	 * 
	 * @return a StringBuilder with the stringified collection
	 */
	public static <T> StringBuilder toString(final Collection<T> collection) {

		return toString(collection, "'", ",");

	} // of toString(Collection<Object>)

	
	
	/**
	 * 
	 * Iterates over the maps keys calling toString() on each element.
	 * 
	 * @param map
	 *            non-null map
	 * 
	 * @param delimiter
	 *            non-null, maybe an empty string used to delimit each elements
	 *            toString() value
	 * 
	 * @param separator
	 *            non-null, maybe an empty string used to separate each element
	 * 
	 * @return a StringBuilder with the stringified map
	 */
	public static <K, V> StringBuilder toString(final Map<K, V> map, final String delimiter, final String separator) {

		final StringBuilder sb = new StringBuilder(map.size() * 128);

		boolean first = true;

		final Set<K> keySet = map.keySet();
		for (final K key : keySet) {
			if (first) {
				first = false;
			} else {
				sb.append(separator);
			}

			final V value = map.get(key);
			sb.append(delimiter).append(key.toString()).append(":").append(value.toString()).append(delimiter);
		} // of for

		return sb;

	} // of toString(Map<K,V>, String, String)

	
	/**
	 * 
	 * Iterates over the maps keys calling toString() on each element.
	 * 
	 * @param map non-null map. It uses the default delimiter of "'" and separator of ",". 
	 * 
	 * @return a StringBuilder with the stringified map
	 */
	public static <K, V> StringBuilder toString(final Map<K, V> map) {

		return toString(map, "'", ",");

	} // of toString(Map<K,V>)	

	
	
	public static byte[] toByteArray(final Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			return bos.toByteArray();
		}
		finally {
			try {
				if (out != null) {
					out.close();
				}
			}
			catch (IOException ex) {
				// ignore close exception
			}
			try {
				bos.close();
			}
			catch (IOException ex) {
				// ignore close exception
			}
		} // of finally
	}

	
	public static Object toObject(final byte[] byteArray) throws Exception {
		Object returnObject = null;
		
		if (byteArray != null && byteArray.length > 0) {
			returnObject = (new ObjectInputStream(new ByteArrayInputStream(byteArray))).readObject();
		}
		
		return returnObject;

	}
	
	
} // of class
