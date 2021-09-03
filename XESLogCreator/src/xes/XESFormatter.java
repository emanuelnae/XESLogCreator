/**
 * @(#) XESFormatter.java
 * 
 * @author Emanuel Nae
 * @version 1.1
 * @since 10 Aug 2021
 * 
 * Copyright (c) 2021, Emanuel Nae - Student 2931931
 * Computing Science Department, Faculty of Science and Engineering, University of Groningen
 * All rights reserved.
 * 
 * This software is the proprietary information of University of Groningen.
 * It shall only be used within the Faculty or only in
 * accordance with the terms of the license agreement you entered into
 * with University of Groningen.
 */

package xes;

/**
 * This class contains the required format of the tags, extensions, classifiers
 * and keys according to XES standard.
 */
public class XESFormatter {

	/**
	 * Returns a string that consists of the xml version and encoding used, the
	 * opening log tag that marks the start of the event log and the xes version
	 * and features.
	 * 
	 * @return start of file tags
	 */
	public static String beginFile() {
		String string = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n"
				+ "<log xes.version=\"1849.2016\" xes.features=\"\">" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of closing the trace and log tags.
	 * 
	 * @return closing trace and log tags
	 */
	public static String endFile() {
		String string = addTabs(1) + endTrace() + "</log>";
		System.out.println("Trace closed\nEvent log ended");
		return string;
	}

	/**
	 * Returns a string that consists of adding an extension. An extension
	 * comprises a name, a prefix and an uri.
	 * 
	 * @param name
	 *            name of extension
	 * @param prefix
	 *            prefix of extension
	 * @param uri
	 *            uri of extension
	 * @return extension string
	 */
	public static String addExtension(String name, String prefix, String uri) {
		String string = "<extension name=\"" + name + "\" prefix=\"" + prefix
				+ "\" uri=\"" + uri + "\"/>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of opening the global scope tag.
	 * 
	 * @param scope
	 *            the scope, can be trace or event
	 * @return starting global scope tag
	 */
	public static String beginGlobalScope(String scope) {
		String string = "<global scope=\"" + scope + "\">" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of closing the global scope tag.
	 * 
	 * @return closing global scope tag
	 */
	public static String endGlobalScope() {
		String string = "</global>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of adding a key. A key comprises a type,
	 * an attribute and a value.
	 * 
	 * @param type
	 *            data type of key
	 * @param attribute
	 *            attribute of {@code type}
	 * @param value
	 *            value of attribute
	 * @return key line
	 */
	public static String addKey(String type, String attribute, String value) {
		String string = "<" + type + " key=\"" + attribute + "\" value=\""
				+ value + "\"/>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of adding a classifier. A classifier
	 * comprises of a name and keys.
	 * 
	 * @param name
	 *            name of classifier
	 * @param keys
	 *            keys used for classifying
	 * @return classifier line
	 */
	public static String addClassifier(String name, String keys) {
		String string = "<classifier name=\"" + name + "\" keys=\"" + keys
				+ "\"/>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of opening the trace tag.
	 * 
	 * @return opening trace tag
	 */
	public static String beginTrace() {
		String string = "<trace>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of closing the trace tag.
	 * 
	 * @return closing trace tag
	 */
	public static String endTrace() {
		String string = "</trace>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of opening the event tag.
	 * 
	 * @return opening event tag
	 */
	public static String beginEvent() {
		String string = "<event>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of closing the event tag.
	 * 
	 * @return closing event tag
	 */
	public static String endEvent() {
		String string = "</event>" + "\n";
		return string;
	}

	/**
	 * Returns a string that consists of tabs required to indent the lines. It
	 * takes as input an {@code int n} and adds to string {@code n} tabs
	 * characters.
	 * 
	 * @param n
	 *            the number of tabs
	 * @return tab string
	 */
	public static String addTabs(int n) {
		String string = "";
		
		for (int i = 0; i < n; i++) {
			string = string + "\t";
		}
		return string;
	}

}
