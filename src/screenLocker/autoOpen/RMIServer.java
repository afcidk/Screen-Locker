package screenLocker.autoOpen;

import java.rmi.server.UnicastRemoteObject;

import screenLocker.LockerTimer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;

public class RMIServer extends UnicastRemoteObject implements RmiServerIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static RMIServer _server;
	private static boolean _alive;

	public RMIServer() throws RemoteException {
		super(0);
	}

	public boolean IsAlive() {
		return _alive;
	}

	public int GetRemainTime() {
		return LockerTimer.getLargeTime();
	}

	public static RMIServer StartServer() {
		_alive = true;
		/** create Registry **/
		try {
			LocateRegistry.createRegistry(1099);
			System.out.println("created ReOpenServer");
		} catch (RemoteException e) {
			System.out.println("cannot create RMI registry");
			e.printStackTrace();
		}

		/** create Server, this will invoke a new thread **/
		if (_server == null) {
			try {
				_server = new RMIServer();
				Naming.rebind("//localhost/ReOpenServer", _server);
			} catch (Exception e) {
				System.out.println("cannot create or rebind new server");
				e.printStackTrace();
			}
		}
		return _server;
	}

	public void CloseServer() {
		_alive = false;
		try {
			Naming.unbind("//localhost/ReOpenServer");
		} catch (RemoteException | MalformedURLException | NotBoundException e1) {
			try {
			} catch (Exception e2) {

			}
		}

		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			try {
			} catch (Exception e2) {

			}
		}
	}
}
