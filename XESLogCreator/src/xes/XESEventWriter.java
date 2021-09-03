/**
 * @(#) XESEventWriter.java
 * 
 * @author Emanuel Nae
 * @version 1.2
 * @since 27 Aug 2021
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

import static xes.Event.processName;
import static xes.XESFormatter.*;
import static xes.Event.*;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains methods for writing the events formatted according to XES
 * standard to the file.
 * <p>
 * Note 1: the new line character is not shown anywhere in the strings as it was
 * implemented in the {@link xes.XESFormatter#addKey(String, String, String)}
 * method.
 * <p>
 * Note 2: the {@link xes.XESFormatter#addTabs(int)} is used to indent the lines
 * accordingly.
 * 
 * @see xes.XESFormatter#addKey(String, String, String)
 * @see xes.XESFormatter#addTabs(int)
 */
public class XESEventWriter {

	/**
	 * Writes the event and its attributes to the file.
	 * <p>
	 * In XES, an event is contained between the {@code <event>} and the
	 * {@code </event>} tags. These tags are added with the
	 * {@link xes.XESFormatter#beginEvent()} and
	 * {@link xes.XESFormatter#endEvent()} methods. Within these tags, the keys
	 * containing various data about the event are added.
	 * <p>
	 * Only three keys are mandatory for creating an event log, which are the
	 * event id, the time stamp and the activity name. However, the instance id,
	 * scope id and event life cycle state where added in order to provide as
	 * much information as could be extracted.
	 * <p>
	 * Refer to {@link xes.Event} for the definitions of the aforementioned
	 * variables.
	 * 
	 * @see xes.XESFormatter#beginEvent()
	 * @see xes.XESFormatter#endEvent()
	 * @param fwrite
	 *            the File Writer in use
	 * @throws IOException
	 *             if string cannot be written to file
	 */
	public static void writeEvent(FileWriter fwrite) throws IOException {
		String eventString = addTabs(2)
				+ beginEvent()
				+ addTabs(3)
				+ addKey("string", processName + "Id", processName + "_"
						+ Integer.toString(instanceId))
				+ addTabs(3)
				+ addKey("int", "ScopeId", Integer.toString(scopeId))
				+ addTabs(3)
				+ addKey("string", "concept:name", getActivityName())
				+ addTabs(3)
				+ addKey("string", "EventId",
						processName + "State_" + Integer.toString(eventId))
				+ addTabs(3)
				+ addKey("string", "lifecycle:transition", setEventLifecycle())
				+ addTabs(3)
				+ addKey("date", "time:timestamp", timestamp.toString())
				+ addTabs(2) + endEvent();
		fwrite.write(eventString);
		System.out.println("Event passed filter -> Added to XES event log");
	}

	/**
	 * When a new process instance is detected, a new {@code <trace>} tag needs
	 * to be opened and a key is added in order to be traced. This is
	 * accomplished by using the {@link xes.XESFormatter#beginTrace()} method
	 * and the {@link xes.XESFormatter#addKey(String, String, String)} method
	 * which adds the {@code instanceId} key, as that is the value that tells us
	 * that a new set of events is coming for the specific process instance.
	 * 
	 * @param fwrite
	 *            the File Writer in use
	 * @param rs
	 *            the result set in use
	 * @throws IOException
	 *             if string cannot be written to file
	 * @throws SQLException
	 *             if cannot retrieve result set
	 */
	public static void writeTrace(FileWriter fwrite, ResultSet rs)
			throws IOException, SQLException {
		readProcessInput(rs);
		String string = addTabs(1)
				+ beginTrace()
				
				/**
				 * <b>Extra functionality</b>
				 * <i>Specific to the ATM Withdrawal process!</i>
				 */
				+ addTabs(2)
				+ addKey("string", "PIN", inputPin)
				+ addTabs(2)
				+ addKey("string", "Amount", inputAmount)
				+ addTabs(2)
				+ addKey("string", "concept:instance", Integer.toString(instanceId));
		fwrite.write(string);
		System.out.println("New trace started");
	}

	/**
	 * Closes the {@code <trace>} tag with the
	 * {@link xes.XESFormatter#endTrace()} method.
	 * 
	 * @param fwrite
	 *            the File Writer in use
	 * @throws IOException
	 *             if string cannot be written to file
	 */
	public static void closeTrace(FileWriter fwrite) throws IOException {
		fwrite.write(addTabs(1) + endTrace());
		System.out.println("Trace closed");
	}
}
