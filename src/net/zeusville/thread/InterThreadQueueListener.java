/**
 * Copyright (c) 2009 jesus m. rodriguez
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */
package net.zeusville.thread;

/**
 * This interface is for objects that want to listen for events raised by an
 * inter-thread queue. The class that implements these methods should attempt to
 * do as little processing as possible because the inter-thread queue may have
 * called these from within the mutex. These methods <I>should not be
 * synchronized or block</I> because of possible deadlock conditions. For
 * example, you should only have one listener per queue, and no thread should
 * access the listener while it has the queue locked.
 **/
public interface InterThreadQueueListener {

    /**
     * This is called when a thread first attempts to put anObject on the queue.
     * It is possible that the queue is full and the attempt must wait.
     * @return a Momento, which is used for tracking this thread
     **/
    public Object putAttempted(Object anObject);

    /**
     * This is called when a thread has to wait before placing the Object on the
     * queue. This could be inferred from calling getAttempted() followed by
     * getSucceeded() or getTimedOut() (within some threshold).
     **/
    public void putWaited(Object anObject, Object theMomento);

    /**
     * This is called when a thread has succeeded in placing the Object on the
     * queue.
     **/
    public void putSucceeded(Object anObject, Object theMomento);

    /**
     * This is called when a thread has timed out while attempting to place the
     * Object on the queue.
     **/
    public void putTimedOut(Object anObject, Object theMomento);

    /**
     * This is called when a thread is attempting to retrieve an Object from the
     * queue. It is possible that there are no objects in the queue, and it must
     * wait.
     * @return a Momento, which is used for tracking this thread
     **/
    public Object getAttempted();

    /**
     * This is called when a thread has to wait before retrieving an Object from
     * the queue. This could be inferred from calling getAttempted() followed by
     * getSucceeded() or getTimedOut() (within some threshold).
     **/
    public void getWaited(Object theMomento);

    /**
     * This is called when a thread has succeeded in retrieving an Object from
     * the queue.
     **/
    public void getSucceeded(Object anObject, Object theMomento);

    /**
     * This is called when a thread has timed out while attempting to retrieve
     * an Object from the queue.
     **/
    public void getTimedOut(Object theMomento);

    /**
     * This is called when the queue begins closing
     **/
    public void queueClosing();

    /**
     * This is called when the queue is closed
     **/
    public void queueClosed();

}