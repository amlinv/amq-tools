/**
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *   
 *    http://www.apache.org/licenses/LICENSE-2.0
 *   
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.amlinv.mbus.util;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@BusUtil
public class DestroyDestination
{
	private static final Logger	LOG = LoggerFactory.getLogger(DestroyDestination.class);

	protected String			url;
	protected ActiveMQConnection		conn;
	protected ActiveMQDestination		dest;
	private String username;
	private String password;

	/**
	 *
	 */
	public static void	main (String[] args)
	{
		DestroyDestination	main_obj;
		int			iter;

		try
		{
			main_obj = new DestroyDestination();
			main_obj.execCmdline(args);
		}
		catch ( Exception exc )
		{
			exc.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void	execCmdline (String[] args)
	throws Exception
	{
		int			iter;

		String[] remainingArgs = parseCommandline(args);

		try
		{
			if ( remainingArgs.length < 2 )
			{
				this.usage();
				System.exit(1);
			}

			url = remainingArgs[0];

			connect();

			iter = 1;
			while ( iter < remainingArgs.length )
			{
				this.destroyDestination(remainingArgs[iter]);
				iter++;
			}
		}
		catch ( Exception exc )
		{
			exc.printStackTrace();
		}
		finally
		{
			disconnect();
		}
	}

	private String[] parseCommandline(String[] args) {
		List<String> remainingList = new LinkedList<String>();

		for (String oneArg : args) {
			if (oneArg.startsWith("jmsuser=")) {
				this.username = oneArg.substring(8);
			} else if (oneArg.startsWith("jmspassword=")) {
				this.password = oneArg.substring(12);
			} else {
				remainingList.add(oneArg);
			}
		}

		return remainingList.toArray(new String[remainingList.size()]);
	}

	protected void	usage ()
	{
		System.out.println("Usage: DestroyDestination [jmsuser=<user>] [jmspassword=<password>] url dest-name ...\n");
		System.out.println("Use fully-qualified destination names with prefixes queue:// or topic://");
	}

	/**
	 * Create a JMS connection for testing Queue operations.
	 */
	protected void	connect ()
	throws Exception
	{
		if (this.username != null) {
			conn = ActiveMQConnection.makeConnection(this.username, this.password, url);
		} else {
			conn = ActiveMQConnection.makeConnection(url);
		}
	}

	/**
	 * Close the JMS connection used for testing Queue operations.
	 */
	protected void	disconnect ()
	{
		try {
			conn.close();
			conn = null;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	protected void	destroyDestination (String destName)
	throws Exception
	{
		if ( destName.startsWith("queue://") )
		{
			conn.destroyDestination(new ActiveMQQueue(destName.substring(8)));
		}
		else if ( destName.startsWith("topic://") )
		{
			conn.destroyDestination(new ActiveMQTopic(destName.substring(8)));
		}
		else
		{
			throw	new RuntimeException("Invalid destination name; needs prefix queue:// or topic://");
		}
	}
}
