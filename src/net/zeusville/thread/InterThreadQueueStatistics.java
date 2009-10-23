package net.zeusville.thread;

/**
 * This uses the InterThreadQueueListener methods to gather statistics about an operating
 * inter-thread queue.  It doesn't use the momento to differentiate between specific
 * accesses to the queue.
 * Should this be synchronized to make toString() a coherent look at the state (i.e., with
 * no other methods going on)?
 **/
class InterThreadQueueStatistics implements InterThreadQueueListener
{

	// Statistics about the queue
	public int mPuts;
	public int mPutsWaited;
	public int mGets;
	public int mGetsWaited;
	public int mPeakSize;
	public long mTotalSize;

	protected InterThreadQueue mOwner;

	public InterThreadQueueStatistics() 
	{
		super();
	}

	/**
	 * This must be called before any of the methods in InterThreadQueueListener.
	 **/
	public void setOwner(InterThreadQueue aQueue)
	{
		mOwner = aQueue;
	}

	// we don't hand out momento's, and we don't track attempts
	public Object putAttempted(Object anObject)
	{
		return null;
	}
	public void putWaited(Object anObject, Object theMomento)
	{
		mPutsWaited++;
	}
	public void putSucceeded(Object anObject, Object theMomento)
	{
		int size = mOwner.size();
		mTotalSize += size;
		if ( size > mPeakSize )
		{
			mPeakSize = size;
		}
		mPuts++;
	}
	public void putTimedOut(Object anObject, Object theMomento) {}
	
	// we don't hand out momento's, and we don't track attempts
	public Object getAttempted()
	{
		return null;
	}

	public void getWaited(Object theMomento)
	{
		mGetsWaited++;
	}

	public void getSucceeded(Object anObject, Object theMomento)
	{
		mTotalSize += mOwner.size();
		mGets++;
	}

	// We don't need to do anything with these
	public void getTimedOut(Object theMomento) { }
	public void queueClosing() { }
	public void queueClosed() { }

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		//String nl = System.getProperty("line.separator");
		sb.append("Puts: ");
		sb.append(mPuts );
		sb.append(" Puts that waited: ");
	 	sb.append( mPutsWaited );
		sb.append(" Gets: ");
	 	sb.append( mGets );
		sb.append( " Gets that waited: ");
	 	sb.append( mGetsWaited );
		sb.append(" Average queue size: " );
		int total = mPuts + mGets;
		if (total <= 0)
		{
			sb.append("0");
		}
		else
		{
			sb.append((double)mTotalSize/total );
		}
		sb.append(" Peak queue size: ");
	 	sb.append( mPeakSize );
		return sb.toString();
	}
}

