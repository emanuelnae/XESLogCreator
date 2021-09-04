/**
 * @(#) EventLogGenerator.java
 * 
 * @author Emanuel Nae
 * @version 1.1
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

package main;

import static sql.MySQLConnection.*;
import static xes.Event.*;
import static xes.XESFileCreator.*;
import static xes.XESFormatter.*;
import static xes.XESEventWriter.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains the main method of the project. It implements an
 * application that retrieves data in the form of events from a MySql database
 * that uses Apache ODE's SQL schema and creates a {@code .xes} event log file.
 * <p>
 * The log file consists of various events that were extracted from the
 * database, then filtered and formatted according to XES standards in order to
 * serve as input for predictive monitoring tools.
 */
public class EventLogGenerator {

	/* The file in {@code .xes} format that will be created */
	private static File xesFile;

	/* The file writer to be used for writing to the XES file */
	private static FileWriter fileWriter;

	/* The result set for the {@code ODE_EVENT} table of the MySql database */
	private static ResultSet eventDataSet;

	/* The result set for the {@code ODE_MESSAGE} table of the MySql database */
	private static ResultSet messageDataSet;

	/**
	 * This is the main method that performs the operations aforementioned and
	 * generates the event log. It first creates the XES file and the file
	 * writer, then initializes it. A connection to the MySQL database and its
	 * {@code ODE_EVENT} and {@code ODE_MESSAGE} tables is established. Next,
	 * the first event is parsed in order to define its trace, required by the
	 * XES format. Further on, the remaining of the event data set is parsed.
	 * The event is read from the result set and a check is performed to
	 * determine if it represents a new instance of the BPEL process. If so, the
	 * trace tag is closed and a new one is started. As the event data set
	 * contains various types of events, a filter is required to determine
	 * whether the event should be added to the XES file or not. More
	 * information about this filter can be found in the description of the
	 * {@link xes.Event#eventFilter()} method. Lastly, after the whole result
	 * set was parsed, the XES file is ended and the File Writer is closed.
	 * 
	 * @param args
	 *            Unused
	 * @exception IOException
	 *                on File or File Writer error
	 * @exception SQLException
	 *                on MySQL Connection or MySQL Result Set error
	 * @exception ClassNotFoundException
	 *                if the JDBC driver cannot be found
	 * @see IOException
	 * @see SQLException
	 * @see ClassNotFoundException
	 */
	public static void main(String[] args) throws Exception {

		try {
			xesFile = createFile();
			fileWriter = new FileWriter(xesFile);
			InitializeFile(fileWriter);
			connectToDB();
			eventDataSet = getEventDataSet();

			/* Process the first event in order to start the trace tag */
			eventDataSet.next();
			readEvent(eventDataSet);
			getProcessName();

			/**
			 * <b>Extra functionality</b>
			 * Comment out the next two lines.
			 */
			messageDataSet = getMessageDataSet(getEventMessageExchangeId());
			readProcessInput(messageDataSet);

			writeTrace(fileWriter);

			while (eventDataSet.next()) {
				readEvent(eventDataSet);

				/* Check if the event is from a new instance of the process */
				if (newInstanceCheck(eventType)) {
					System.out.println("New process instance found");
					closeTrace(fileWriter);
					
					/**
					 * <b>Extra functionality</b>
					 * Comment out the next two lines.
					 */
					messageDataSet = getMessageDataSet(getEventMessageExchangeId());
					readProcessInput(messageDataSet);
					
					writeTrace(fileWriter);
				}

				/* Filter the unwanted events */
				if (eventFilter()) {
					writeEvent(fileWriter);
				}
			}

			fileWriter.write(endFile());
			fileWriter.close();
			System.out.print("Application completed");
		} catch (SQLException e) {
			System.out.println(e);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
