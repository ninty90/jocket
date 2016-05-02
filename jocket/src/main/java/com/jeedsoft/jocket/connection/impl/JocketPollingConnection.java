package com.jeedsoft.jocket.connection.impl;

import java.io.IOException;

import javax.servlet.AsyncContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeedsoft.jocket.connection.JocketConnection;
import com.jeedsoft.jocket.connection.JocketStub;
import com.jeedsoft.jocket.event.JocketEvent;
import com.jeedsoft.jocket.servlet.JocketPollingServlet;

public class JocketPollingConnection extends JocketConnection
{
	private static final Logger logger = LoggerFactory.getLogger(JocketPollingConnection.class);
	
	public static final long POLLING_INTERVAL = 25_000;
	
	private AsyncContext context;
	
	public JocketPollingConnection(String id)
	{
		setId(id);
	}

	public JocketPollingConnection(String id, AsyncContext context)
	{
		setId(id);
		this.context = context;
	}
	
	public JocketPollingConnection(JocketStub stub)
	{
		setId(stub.getId());
		setStub(stub);
	}

	public AsyncContext getPollingContext()
	{
		return context;
	}

	public void setPollingContext(AsyncContext context)
	{
		this.context = context;
	}

	@Override
	public void onEvent(String connectionId, JocketEvent event)
	{
		try {
			JocketPollingServlet.downstream(this, event);
		}
		catch (IOException e) {
			logger.error("[Jocket] Failed to send message: cid={}, event={}", connectionId, event);
		}
	}
}
