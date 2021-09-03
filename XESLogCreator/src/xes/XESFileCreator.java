/**
 * @(#) XESFileCreator.java
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

import static xes.XESFormatter.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class creates the {@code .xes} file and initializes it with all the
 * required information according to XES standard.
 * 
 */
public class XESFileCreator {

	/* Rename the file as wished */
	static String fileName = "eventlog.xes";

	/* Modify the path of where the file is created as wished */
	static String path = "GeneratedEventLogs/" + fileName;

	/**
	 * Creates the file with the given name at the given location.
	 * 
	 * @return the newly created file
	 * @throws IOException
	 *             if file cannot be created
	 */
	public static File createFile() throws IOException {
		File f = new File(path);
		System.out.println("XES file created");
		return f;
	}

	/**
	 * Initializes the file with all the data that is required at the beginning
	 * of the XES file, before adding the traces and events.
	 * <p>
	 * First, the xml and xes versions are added using the
	 * {@link xes.XESFormatter#beginFile()} method.
	 * <p>
	 * Next, the extensions required by the keys are added.
	 * <p>
	 * Further on, the global attributes for the traces and events are added. These
	 * define the format that the traces and events should have inside the event
	 * log.
	 * <p>
	 * Then, the classifier is added. The classifier defines what combination of
	 * attributes should be used for classifying events.
	 * <p>
	 * Lastly, the process name is added.
	 * 
	 * @see xes.XESFormatter#beginFile()
	 * @param fwrite
	 *            the File Writer in use
	 * @throws IOException
	 *             if string cannot be written to file
	 */
	public static void InitializeFile(FileWriter fwrite) throws IOException {
		String initialData = beginFile()
				+ addTabs(1)
				+ addExtension("Lifecycle", "lifecycle",
						"http://www.xes-standard.org/lifecycle.xesext")
				+ addTabs(1)
				+ addExtension("Time", "time",
						"http://www.xes-standard.org/time.xesext")
				+ addTabs(1)
				+ addExtension("Concept", "concept",
						"http://www.xes-standard.org/concept.xesext")
				+ addTabs(1)
				+ beginGlobalScope("trace")
				
				/**
				 * <p>
				 * <i>Specific to the ATM Withdrawal process!<i>
				 * <p>
				 */
				+ addTabs(2)
				+ addKey("string", "PIN", "UNKNOWN")
				+ addTabs(2)
				+ addKey("string", "Amount", "UNKNOWN")
				+ addTabs(2)
				+ addKey("string", "concept:instance", "UNKNOWN")
				+ addTabs(1)
				+ endGlobalScope()
				+ addTabs(1)
				+ beginGlobalScope("event")
				+ addTabs(2)
				+ addKey("int", "InstanceId", "UNKNOWN")
				+ addTabs(2)
				+ addKey("int", "ScopeId", "UNKNOWN")
				+ addTabs(2)
				+ addKey("string", "concept:name", "UNKNOWN")
				+ addTabs(2)
				+ addKey("string", "EventId", "UNKNOWN")
				+ addTabs(2)
				+ addKey("string", "lifecycle:transition", "UNKNOWN")
				+ addTabs(2)
				+ addKey("date", "time:timestamp", "1970-01-01 00:00:00.0")
				+ addTabs(1)
				+ endGlobalScope()
				+ addTabs(1)
				+ addClassifier("(Name AND Transition)",
						"concept:name lifecycle:transition");

		fwrite.write(initialData);
		System.out.println("XES file initialized");
	}
}
