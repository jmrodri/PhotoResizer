package net.zeusville.thread;

public class InterThreadQueueTimeoutException extends Exception
{
	public InterThreadQueueTimeoutException()
	{
		super();
	}
	public InterThreadQueueTimeoutException(String message)
	{
		super(message);
	}
}