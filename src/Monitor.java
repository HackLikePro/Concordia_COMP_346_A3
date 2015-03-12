/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
    private enum status{eating, hungry, thinking};
    private boolean isTalking;
    private status []state;
    private int numofcs;
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		this.numofcs = piNumberOfPhilosophers;
		state = new status[piNumberOfPhilosophers];
		for(int i=0; i<piNumberOfPhilosophers;i++)
		{
			state[i]=status.thinking;
		}
		isTalking = false;
		// TODO: set appropriate number of chopsticks based on the # of philosophers
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	public synchronized void test(final int piTID)
	{
		if (state[(piTID + 1)%numofcs] != status.eating &&
				state[(piTID-1+numofcs)%numofcs] != status.eating &&
				state[piTID] == status.hungry)
		{
			state[piTID] = status.eating;
			notifyAll();
		}
	}
	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		// ...
		int phil = piTID -1;
		state[phil] = status.hungry;
		test(phil);
		
		if(state[phil] != status.eating)
		{
			try 
			{
				wait();
				pickUp(piTID);                     
			}
		    catch(InterruptedException e)
			{
				System.out.println("Failing for philosopher number " + piTID + " while picking up the chopstick");
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// ...
        int phil = piTID-1;
        state[phil] = status.thinking;
        notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// ...
		if(isTalking) 
		{
			try 
			{
				wait(); //If someone else is currently talking then this philosopher has to wait
				requestTalk(); //When he gets notified that someone stopped eating or talking, he needs to check if the boolean has been changed to false
			}
			catch(InterruptedException e)
			{
				System.out.println("Error: Philosopher can't talk because someone is already talking");
			}
		}
		isTalking = true; //The philosopher starts talking, we change the boolean to true to stop other philosophers from talking
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// ...
		isTalking = false; //We set the boolean to false
		notifyAll(); //We notify everyone to try to request to talk because this philosopher has finished talking
	}
}

// EOF
