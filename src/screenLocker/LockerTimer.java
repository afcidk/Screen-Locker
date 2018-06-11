package screenLocker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimerTask;
import java.util.List;


public class LockerTimer extends TimerTask{
	
	private static Hashtable<String, Integer> _applications = new Hashtable<String, Integer>();
	private String _path;
	private LockerTimer _lLockerTimer = new LockerTimer();
	
	public LockerTimer() throws FileNotFoundException, IOException {
		_path = this.getClass().getResource("").getPath();
		_path = _path.substring(0, _path.lastIndexOf("screenLocker/"));
		_path = _path + "time.txt";
		File _checkFile = new File(_path);
		if(_checkFile.exists()) {
			ObjectInputStream _ois = new ObjectInputStream(new FileInputStream(_path));
			try {
				while(true) {
					String _string = (String)_ois.readObject();
					int _num = _ois.readInt();
					_applications.put(_string, _num);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			_ois.close();
        }
	}

	public int getTime(String _application) throws IOException, ClassNotFoundException {	
		for(int _i = 0; _i < _applications.size(); _i++) {
			return _applications.get(_application);
		}
		return -1;
	}
	
	/*return the maximum time value in applications*/
	public static int getLargeTime() throws IOException, ClassNotFoundException {
		Enumeration _e = _applications.keys();
		int _maxtime = 0;
		while(_e. hasMoreElements()) {
			String _s = _e.nextElement().toString();
			int _num = _applications.get(_s);
			if(_maxtime < _num) {
				_maxtime = _num;
			}
		}
		return _maxtime;
	}
	
	public void setTime(String _application, int _time) throws IOException {
		_applications.put(_application, _time*3600);
		ObjectOutputStream _oos = new ObjectOutputStream(new FileOutputStream(_path));
		Enumeration _e = _applications.keys();
		while(_e. hasMoreElements()){
			String _s= _e.nextElement().toString();
			_oos.writeObject(_s);
			_oos.writeInt(_applications.get(_s));
		}
		_oos.flush(); 
		_oos.close();
	}
	
	public static List<String> BlackList() {
		List<String> _blacklist = new ArrayList<>();
		Enumeration<String> _e = _applications.keys();
		while(_e.hasMoreElements()) {
			String _s = _e.nextElement().toString();
			_blacklist.add(_s);
		}
		System.out.println("list = " + _blacklist);	
		return _blacklist;	
	}
	
	public void setAddOneHour(String _punishedApp) {
		try {  
			Enumeration _e = _applications.keys();
			ObjectOutputStream _oos = new ObjectOutputStream(new FileOutputStream(_path));
			_oos.writeObject(_punishedApp);
			int _newtime = _applications.get(_punishedApp)+3600;
			_oos.writeInt(_newtime);
			_applications.put(_punishedApp, _newtime);
			System.out.println("addten: " + _punishedApp + _newtime);
			_oos.flush(); 
			_oos.close();
		} catch (IOException ex) {                       
			
		}
	}
	
	private int _twomin = 0;
	public void run() {
		Enumeration _e = _applications.keys();
		while(_e. hasMoreElements()){
			String _s= _e.nextElement().toString();
			int _secondtime = _applications.get(_s);
			_secondtime--;
			if(_secondtime > 0) {
				_applications.remove(_s);
				_applications.put(_s, _secondtime);
			}
			else {
				_applications.remove(_s);
			}
		}
		_twomin++;
		if(_twomin == 120) {
			_twomin = 0;
			try {        
				ObjectOutputStream _oos = new ObjectOutputStream(new FileOutputStream(_path));
				Enumeration _e2 = _applications.keys();
				while(_e2. hasMoreElements()){
					String _s= _e2.nextElement().toString();
					_oos.writeObject(_s);
					_oos.writeInt(_applications.get(_s));
				}
				_oos.flush(); 
				_oos.close();
			} catch (IOException ex) {                       
			
			}
		}
	}
}
