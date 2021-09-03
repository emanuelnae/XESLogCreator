/**
 * @(#) Event.java
 * 
 * @author Emanuel Nae
 * @version 1.2
 * @since 26 Aug 2021
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * This class contains all the attributes of the events retrieved from Apache
 * ODE's internal MySQL database and the methods required for reading the
 * events, retrieving event string data, filtering events and checking for
 * events that contain new instances.
 * <p>
 * The {@code eventID} represents the event id retrieved from the database. It
 * is of {@code int} data type and is used to identify a certain event.
 * <p>
 * The {@code eventDetails} represents the detail string retrieved from the
 * database and contains various details of the event, such as the Activity Id,
 * Activity Type, Declaration Id and others.
 * <p>
 * The {@code scopeId} represents the scope id retrieved from the database. It
 * is of {@code int} data type and is used to identify a certain scope. A scope
 * is a BPEL element that allows partners, process data or handlers to be
 * declared locally.
 * <p>
 * The {@code timestamp} represents the timestamp retrieved from the database
 * and is of {@code Timestamp} data type. It represents the date and time at
 * which the event happened.
 * <p>
 * The {@code eventType} represents the type of the event retrieved from the
 * database. It is of {@code String} data type and consists of various states
 * such as enabled, started, modified, ended of the process instances,
 * activities, scopes or variables.
 * <p>
 * The {@code instanceId} represents the id of an instance retrieved from the
 * database. It is of {@code int} data type and is used to identify instances.
 * <p>
 * The {@code ActivityName} represents the name of the activity occurred in the
 * current event. It is of {@code String} data type and can have various values
 * related to the process.
 * <p>
 * The {@code eventLifecycle} represents the life cycle state of the event. It
 * is of {@code String} data type and the filtered events can have the "started"
 * or "ended" state.
 * <p>
 * The {@code messageId} represents the message exchange id of a process
 * instance. It is of {@code String} data type and consists of a mix of letters
 * and digits. This is used in the application to retrieve the instance input
 * variables from the {@code ODE_MESSAGE} table as it is the only common
 * variable of the two tables.
 * <p>
 * <i>Specific to the ATM Withdrawal process!</i>
 * <p>
 * As this is an ATM Withdrawal process, the user needs to enter the pin and the
 * requested amount. The {@code inputPin} and {@code inputAmount} represent the
 * input variables for an instance of the process and they are being traced in
 * the event log in order to improve the upcoming predictions.
 * <p>
 * The {@code processName} represents the name of the process retrieved from the
 * database. It is of {@code String} data type and is used to identify
 * processes.
 */
public class Event {
	public static int eventId;
	public static String eventDetails;
	public static int scopeId;
	public static Timestamp timestamp;
	public static String eventType;
	public static int instanceId;
	public static String ActivityName;
	public static String eventLifecycle;
	public static String messageId;
	public static String inputPin;
	public static String inputAmount;
	public static String processName;

	/**
	 * This method assigns values retrieved from the result set to the event
	 * variables.
	 * 
	 * @param resultSet the Result Set in use
	 * @throws SQLException
	 *             if data cannot be retrieved from result set
	 */
	public static void readEvent(ResultSet resultSet) throws SQLException {
		eventId = resultSet.getInt("EVENT_ID");
		eventDetails = resultSet.getString("DETAIL");
		scopeId = resultSet.getInt("SCOPE_ID");
		timestamp = resultSet.getTimestamp("TSTAMP");
		eventType = resultSet.getString("TYPE");
		instanceId = resultSet.getInt("INSTANCE_ID");
		System.out.println("Event retrieved");
	}

	/**
	 * Reads the values of the Pin and Amount variables entered by the user.
	 * This is being accomplished by searching for the first appearance of the
	 * strings "PIN" and "Amount" in the {@code messageData} retrieved
	 * from the {@code messageDataSet}. Then, the {@code messageData}
	 * string is parsed until the {@code inputPin} and {@code inputAmount} were
	 * obtained.
	 * <p>
	 * <b>Extra functionality</b>
	 * <i>Specific to the ATM Withdrawal process!<i>
	 * <p>
	 * @param resultSet
	 *            the result set being used
	 * @throws SQLException
	 *             if data cannot be retrieved from result set
	 */
	public static void readProcessInput(ResultSet resultSet)
			throws SQLException {
		inputPin = "";
		inputAmount = "";
		resultSet.next();
		String messageData = resultSet.getString(1);
		int index = messageData.indexOf("PIN");
		index = index + "PIN:".length();

		/* The next character after the pin value is "<" */
		while (messageData.charAt(index) != '<') {
			inputPin = inputPin + messageData.charAt(index);
			index++;
		}
		index = messageData.indexOf("Amount");
		index = index + "Amount:".length();

		/* The next character after the amount value is "<" */
		while (messageData.charAt(index) != '<') {
			inputAmount = inputAmount + messageData.charAt(index);
			index++;
		}
	}

	/**
	 * Filters the events based on 3 conditions.The first one is the event
	 * should have an activity name in the retrieved {@code eventDetails}
	 * string. The second one is the eventType should not have the state
	 * "Enabled". The third condition filters the events related to "Assign"
	 * activities of the process.
	 * <p>
	 * By doing so, all the remaining events have an activity name, they have
	 * the "Started" or "Ended" state and are not related to the numerous
	 * "Assign" activities.
	 * 
	 * @return boolean
	 */
	public static boolean eventFilter() {
		return (eventDetails.contains("ActivityName")
				&& !eventType.contains("Enabled") && !getActivityName()
				.contains("Assign"));
	}

	/**
	 * Extracts the activity name from the {@code eventDetails} string by
	 * looking for the first appearance of "ActivityName" in the string.
	 * 
	 * @return the activity name
	 */
	public static String getActivityName() {
		ActivityName = "";
		int index = eventDetails.indexOf("ActivityName");

		/* Index gets increased by the length of "ActivityName = " */
		index = index + "ActivityName = ".length();

		do {
			ActivityName = ActivityName + eventDetails.charAt(index);
			index++;

			/* The next character after the activity name is new line. */
		} while (eventDetails.charAt(index) != '\n');
		return ActivityName;
	}

	/**
	 * Extracts the message exchange id from the {@code eventDetails} string by
	 * looking for the first appearance of "MessageExchangeId" in the string.
	 * <p>
	 * <i>Specific to the ATM Withdrawal process! Can also be used for other processes. </i>
	 * <p>
	 * @return the message exchange id
	 */
	public static String getEventMessageExchangeId() {
		messageId = "";
		int index = eventDetails.indexOf("MessageExchangeId");
		index = index + "MessageExchangeId = ".length();
		
		do {
			messageId = messageId + eventDetails.charAt(index);
			index++;

			/* The next character after the message exchange id is new line. */
		} while (eventDetails.charAt(index) != '\n');
		return messageId;
	}

	/**
	 * Returns the event life cycle state based on the {@code eventType} string
	 * containing "Start" or "End".
	 * 
	 * @return the event life cycle
	 */
	public static String setEventLifecycle() {
		eventLifecycle = "";

		if (eventType.contains("Start")) {
			eventLifecycle = "start";
		}
		if (eventType.contains("End")) {
			eventLifecycle = "complete";
		}
		return eventLifecycle;
	}

	/**
	 * Returns whether a new instance of the process started by checking if the
	 * {@code eventType} string contains "NewProcessInstanceEvent".
	 * 
	 * @param eventType the event type
	 * @return if a new instance started
	 */
	public static boolean newInstanceCheck(String eventType) {
		return (eventType.contains("NewProcessInstanceEvent"));
	}

	/**
	 * Extracts the process name from the {@code eventDetails} string by looking
	 * for the first appearance of the "}" char in the string.
	 * 
	 */
	public static void getProcessName() {
		processName = "";
		int index = eventDetails.indexOf("}");

		/* Index gets increased by 1, the size of the "}" char */
		index++;

		do {
			processName = processName + eventDetails.charAt(index);
			index++;

			/* The next character after the message exchange id is new line. */
		} while (eventDetails.charAt(index) != '\n');
		System.out.println("Process name extracted");
	}
}
