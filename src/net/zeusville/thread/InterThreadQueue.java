package net.zeusville.thread;

import java.util.LinkedList;
import java.util.List;

public class InterThreadQueue
{
	/**
	 * This is the underlying queue for storing the objects.  It is assumed that it is not thread-safe.
	 * This is a List in case our subclass wants to use a different implementation
	 * @see createList(int)
	 **/
	protected List mQueue;
	protected int mMaxSize;

	/**
	 * This is true if this queue has closed down (or is in the process of doing so).
	 **/
	protected boolean mClosed = false;

	/**
	 * This is true if this queue is in the process of shutting down, but hasn't shut down completely.
	 **/
	protected boolean mClosing = false;

	// Statistics about the queue
	protected InterThreadQueueListener mStats;

	/**
	 * This is the denominator to use for the initial size.
	 * That is, the initial size is calculated as MaxSize/INITIAL_SIZE_DENOM.
	 **/
	protected static final float INITIAL_SIZE_DENOM = 4;

	/**
	 * Creates an inter-thread FIFO queue.
	 * @param maxSize the maximum objects to hold
	 * @aListener an object to receive notification about what the queue is doing
	 * @exception IllegalArgumentException if the maxSize is <= 0 because you need at least one storage place
	  	for two threads to interact
	 **/
	public InterThreadQueue( int maxSize, InterThreadQueueListener aListener) 
	{
		if (maxSize <= 0)
		{
			throw new IllegalArgumentException("Can not create an inter-thread queue with maxSize less than one.");
		}
		mMaxSize = maxSize;
		mQueue = createList((int)Math.ceil((double)(maxSize/INITIAL_SIZE_DENOM)));
		mStats = aListener;
	}

	/**
	 * Creates an inter-thread FIFO queue and creates a InterThreadQueueStatistics as the Listener.
	 * @param maxSize the maximum objects to hold
	 * @exception IllegalArgumentException if the maxSize is <= 0 because you need at least one storage place
	  	for two threads to interact
	 **/
	public InterThreadQueue( int maxSize ) 
	{
		this(maxSize, new InterThreadQueueStatistics());
		((InterThreadQueueStatistics)mStats).setOwner(this);
	}

	/**
	 * Creates an inter-thread FIFO queue and creates a InterThreadQueueStatistics as the Listener.
	 * The max size is set to 1, which means basically all put()'s will wait for each get().
	 **/
	public InterThreadQueue() 
	{
		this( 1 );
	}

	/**
	 * This should be overridden if the subclass desires a different underlying List implementation.
	 **/
	protected List createList(int initialSize)
	{
		//A LinkedList doesn't have a concept of initial size.
		return new LinkedList();
	}

	/**
	 * Returns the number of objects waiting to be utilized by a get()ing thread.
	 * This should not be synchronized because it is called by InterThreadQueueStats.
	 **/
	public int size()
	{
		return mQueue.size();
	}

	/**
	 * Adds an Object to the queue, but will wait up to "timeout" milliseconds if the queue is full.
	 * If "timeout" is less than zero, it will wait forever.  If "timeout" is zero, it won't wait at all.
	 * Note that if the queue is closed, the listener does not receive notification of this attempt.
	 **/
	public void put( Object obj, int timeout ) 
		throws InterThreadQueueTimeoutException
	{
		if ( isClosing() )
		{
			return;
		}

		//Notify listener outside of synchronized block in case the listener takes lots of time
		Object momento = mStats.putAttempted(obj);
		synchronized(this)
		{
			//If the caller doesn't want to wait at all and the queue is full
			if (timeout == 0 && (mQueue.size() >= mMaxSize))
			{
				mStats.putTimedOut(obj, momento);
				// The wait timed out; throw an exception
				throw new InterThreadQueueTimeoutException();
			}
			// If the queue is full, wait until it isn't
			long timeWaitStarted = System.currentTimeMillis();
			long timeWaited = 0;
			while ( mQueue.size() >= mMaxSize )
			{
				try
				{
					mStats.putWaited(obj, momento);
					if (timeout > 0)
					{
						//Only wait for the remainder of "timeout"
						wait(timeout - timeWaited);
					}
					else
					{
						wait();
					}
				}
				catch ( InterruptedException ie ) {	};
				timeWaited = System.currentTimeMillis() - timeWaitStarted;

				//If it closed while I was waiting
				if ( mClosing )
				{
					return;
				}

				//If I only wanted to wait "timeout" milliseconds, and that was exceeded
				if ( timeout > 0 && timeWaited >= timeout )
				{
					mStats.putTimedOut(obj, momento);
					// The wait timed out; throw an exception
					throw new InterThreadQueueTimeoutException();
				}
			}
			// Add the item to the end of the queue
			mQueue.add( obj );

			// Notify any threads that were waiting because the queue was empty
			notifyAll();
		}

		mStats.putSucceeded(obj, momento);

	}

	public void put( Object obj ) 
		throws InterThreadQueueTimeoutException
	{
		put( obj, -1 );
	}

	/**
	 * Return the object at the head of the queue.
	 * If there's nothing in the queue, the call will pend until
	 * there is  something in the queue, the timeout value is reached,
	 * or the queue is closed.
	 * The timeout is the number of milliseconds to wait for something to show up in the queue.
	 * If the timeout is less than zero, wait forever.  If it is exactly 0, don't wait at all.
	 * @return the head of the queue, or null if either the timeout is reached, or the queue is closed
	 * @exception InterThreadQueueTimeoutException if the timeout is reached
	 **/
	public Object get(int timeout) 
		throws InterThreadQueueTimeoutException
	{
		Object retObject = null;

		if ( isClosed() )
		{
			return null;
		}

		Object momento = mStats.getAttempted();
		synchronized(this)
		{
			//If the caller doesn't want to wait, and the queue is empty, return right away
			if (timeout == 0 && mQueue.isEmpty())
			{
				mStats.getTimedOut(momento);
				return null;
			}
			long timeWaitStarted = System.currentTimeMillis();
			long timeWaited = 0;
			do
			{
				if (!mQueue.isEmpty())
				{
					// Get the object at the head of the queue
					retObject = mQueue.remove(0);
					//If I just took the last one and we were closing
					if (mClosing && mQueue.isEmpty())
					{
						mClosed = true;
					}
				}
				else
				{
					// Wait until something is put in the queue
					try
					{
						//mStats.mGetsWaited++;
						mStats.getWaited(momento);
						if (timeout > 0)
						{
							//Only wait for the remainder of "timeout"
							wait(timeout - timeWaited);
						}
						else
						{
							wait();
						}
					}
					catch ( InterruptedException ie ) {};
					timeWaited = System.currentTimeMillis() - timeWaitStarted;

					//If it got closed while I was waiting
					if ( mClosed )
					{
						return null;
					}

					//If I only wanted to wait "timeout" milliseconds, and that was exceeded
					if ( timeout > 0 && timeWaited >= timeout )
					{
						mStats.getTimedOut(momento);
						// The wait timed out; throw an exception
						throw new InterThreadQueueTimeoutException();
					}
				}
			}
			while( retObject == null );
			// Notify any threads that were waiting because the queue was full
			notifyAll();

		}//end of synchronized
		
		// Update the various counters and stats
		mStats.getSucceeded(retObject, momento);

		return retObject;
	}
	
	public synchronized Object get() 
		throws InterThreadQueueTimeoutException
	{
		return get( -1 );
	}

	public boolean isEmpty()
	{
		return mQueue.isEmpty();		
	}

	/**
	 * This tells the queue to close, which means it immediately stops accepting put()'s.
	 * When all items in the queue have been retrieved via get(), no more get()'s will be accepted either.
	 * It is possible for no more get()'s to be received and still have objects in the queue.
	 * In this case, it will never actually close all the way.
	 * @see #closeAndWait
	 **/
	public synchronized void close()
	{
		mClosing = true;
		notifyAll();	// Wake up any waiters
	}

	/**
	 * This closes the queue, but blocks the calling thread until all the items in the
	 * queue are retrieved.  If "timeout" is zero or less, it will wait forever, otherwise
	 * it will throw a InterThreadQueueTimeoutException if the timeout is exceeded.
	 * @param timeout the time to wait in milliseconds, or forever if this is  0 or less
	 * @exception InterThreadQueueTimeoutException if the timeout is exceeded
	 **/
	public synchronized void closeAndWait(int timeout) throws InterThreadQueueTimeoutException
	{
		close();
		long timeWaitStarted = System.currentTimeMillis();
		long timeWaited = 0;
		while (!mClosed)
		{
			try
			{
				if (timeout > 0)
				{
					//Only wait for the remainder of "timeout"
					wait(timeout - timeWaited);
				}
				else
				{
					wait();
				}
			}
			catch ( InterruptedException ie ) {	};
			timeWaited = System.currentTimeMillis() - timeWaitStarted;

			//If I only wanted to wait "timeout" milliseconds, and that was exceeded
			if ( timeout > 0 && timeWaited >= timeout )
			{
				// The wait timed out; throw an exception
				throw new InterThreadQueueTimeoutException();
			}
		}
	}

	/**
	 * This closes the queue, but blocks the calling thread until all the items in the
	 * queue are retrieved.  If the queue doesn't get emptied, it will wait forever.
	 **/
	public void closeAndWait()
	{
		try
		{
			closeAndWait( -1 );
		}
		catch (InterThreadQueueTimeoutException e)
		{
			//can't happen
		}
	}

	/**
	 * This does a hard close, where outstanding and new put()'s and get()'s will fail immediately.
	 * There is no need to wait because as soon as outstanding threads are notify()'d, they will return.
	 * It is the responsibility of the get() thread(s) to detect that the queue is really closed
	 * and do the right thing, like shutting itself down.
	 **/
	public synchronized void closeImmediately()
	{
		mClosed = true;
		mClosing = true;
		notifyAll();
	}

	/**
	 * Returns true if this is closed to more put() operations.
	 **/
	public synchronized boolean isClosing()
	{
		return mClosing;
	}

	/**
	 * Returns true if this is closed to more get() operations.
	 **/
	public synchronized boolean isClosed()
	{
		return mClosed;
	}

	public InterThreadQueueListener getStatistics()
	{
		return mStats;
	}
}

