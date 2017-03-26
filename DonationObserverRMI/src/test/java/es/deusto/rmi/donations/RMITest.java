package es.deusto.rmi.donations;


import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;


import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.io.IOException;

import donations.server.*;
import donations.client.Donor;

/**
<<<<<<< HEAD
 * Unit test for simple App.
=======
 * Unit test for simple App. 
>>>>>>> branch 'master' of https://github.com/rebecacortazar/DonationsObserverRMI.git
 */
public class RMITest {
	// Properties are hard-coded because we want the test to be executed without external interaction
	private String[] arg = {"127.0.0.1", "1099", "TestDonationCollector"};
	private static String cwd = RMITest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	private static Thread rmiRegistryThread = null;
	private static Thread rmiServerThread = null;

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(RMITest.class);
	}


	@BeforeClass static public void setUp() {
		// Launch the RMI registry
		class RMIRegistryRunnable implements Runnable {

			public void run() {
				try {
					java.rmi.registry.LocateRegistry.createRegistry(1099);
					System.out.println("RMI registry ready.");
				} catch (Exception e) {
					System.out.println("Exception starting RMI registry:");
					e.printStackTrace();
				}	
			}
		}
		
		rmiRegistryThread = new Thread(new RMIRegistryRunnable());
		rmiRegistryThread.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		
		class RMIServerRunnable implements Runnable {

			public void run() {
				System.out.println("This is a test to check how mvn test executes this test without external interaction; JVM properties by program");
				System.out.println("**************: " + cwd);
				System.setProperty("java.rmi.server.codebase", "file:" + cwd);
				System.setProperty("java.security.policy", "target\\classes\\security\\java.policy");

				if (System.getSecurityManager() == null) {
					System.setSecurityManager(new SecurityManager());
				}

				String name = "//127.0.0.1:1099/TestDonationCollector";
				System.out.println(" * TestServer name: " + name);

				try {
					ICollector doncollector = new DonationCollector();
					Naming.rebind(name, doncollector);
				} catch (RemoteException re) {
					System.err.println(" # Collector RemoteException: " + re.getMessage());
					re.printStackTrace();
					System.exit(-1);
				} catch (MalformedURLException murle) {
					System.err.println(" # Collector MalformedURLException: " + murle.getMessage());
					murle.printStackTrace();
					System.exit(-1);
				}
			}
		}
		rmiServerThread = new Thread(new RMIServerRunnable());
		rmiServerThread.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	
	}


	@SuppressWarnings("unused")
	@Test public void testRMIApp() {
		try {
			Donor donor = new Donor();
			donor.start(arg);

			java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader ( System.in );
			java.io.BufferedReader stdin = new java.io.BufferedReader ( inputStreamReader );
			String line  = stdin.readLine();

		} catch (RemoteException re) {
			System.err.println(" # Collector RemoteException: " + re.getMessage());
			re.printStackTrace();
			System.exit(-1);
		}  catch (IOException ioe) {
			System.err.println(" # Collector console: " + ioe.getMessage());
			ioe.printStackTrace();
			System.exit(-1);
		} 

		assertTrue( true );
	}

	@AfterClass static public void tearDown() {
		try	{
			rmiServerThread.join();
			rmiRegistryThread.join();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
