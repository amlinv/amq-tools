package com.amlinv.activemq.util;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DestroyDestination
{
	private static final Logger	LOG = LoggerFactory.getLogger(DestroyDestination.class);

	protected String			url;
	protected ActiveMQConnection		conn;
	protected ActiveMQDestination		dest;

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

		try
		{
			if ( args.length < 2 )
			{
				this.usage();
				System.exit(1);
			}

			url = args[0];

			connect();

			iter = 1;
			while ( iter < args.length )
			{
				this.destroyDestination(args[iter]);
				iter++;
			}

			disconnect();
		}
		catch ( Exception exc )
		{
			exc.printStackTrace();
		}
	}

	protected void	usage ()
	{
		System.out.println("Usage: DestroyDestination url dest-name ...\n");
		System.out.println("Use fully-qualified destination names with prefixes queue:// or topic://");
	}

	/**
	 * Create a JMS connection for testing Queue operations.
	 */
	protected void	connect ()
	throws Exception
	{
		conn = ActiveMQConnection.makeConnection(url);
	}

	/**
	 * Close the JMS connection used for testing Queue operations.
	 */
	protected void	disconnect ()
	throws Exception
	{
		conn.close();
		conn = null;
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
