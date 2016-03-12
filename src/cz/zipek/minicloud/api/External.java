/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api;

import cz.zipek.minicloud.api.events.DeleteEvent;
import cz.zipek.minicloud.api.events.UpdateEvent;
import cz.zipek.minicloud.api.events.LoginEvent;
import cz.zipek.minicloud.api.events.LogoutEvent;
import cz.zipek.minicloud.api.events.LoginFailedEvent;
import cz.zipek.minicloud.api.events.FilesEvent;
import cz.zipek.minicloud.api.events.UpdateConflictEvent;
import cz.zipek.minicloud.api.events.SynckeyEvent;
import cz.zipek.minicloud.api.events.PleaseLoginEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class External extends Eventor<Event> {

	/**
	 * @return the url
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param aServer the url to server
	 */
	public final void setServer(String aServer) {
		server = aServer;
	}
	
	public String getApiUrl() {
		return getServer() + "/api.php";
	}

	class codes {
		public static final int OK = 100;
		public static final int LOGIN_OK = 101;
		public static final int LOGOUT_OK = 102;
		public static final int FILES_OK = 103;
		public static final int UPDATE_OK = 104;
		public static final int DELETE_OK = 105;
		public static final int UPLOAD_OK = 106;
		public static final int SYNCKEY_OK = 108;

		public static final int PLEASE_LOGIN = 500;
		public static final int LOGIN_FAILED = 501;
		public static final int UPLOAD_INVALID_EXTENSION = 502;
		public static final int UPLOAD_TOO_BIG = 503;
		public static final int UPLOAD_FAILED = 504;
		public static final int UPDATE_CONFLICT = 505;

		public static final int API_ERROR = 400;
		public static final int UKNOWN_ACTION = 404;
	}

	private final Map<Integer, Class> events = new HashMap<>();

	private String server = "http://minicloud.zipek.cz";

	private long actionCounter;
	
	/**
	 * Used for parametrized thread
	 */
	class ParamThread extends Thread {

		public final String params;

		ParamThread(String params) {
			this.params = params;
		}
	}

	public External() {
		this.actionCounter = 0;
		
		events.put(codes.LOGIN_OK, LoginEvent.class);
		events.put(codes.FILES_OK, FilesEvent.class);
		events.put(codes.DELETE_OK, DeleteEvent.class);
		events.put(codes.LOGOUT_OK, LogoutEvent.class);
		events.put(codes.UPDATE_OK, UpdateEvent.class);
		events.put(codes.LOGIN_FAILED, LoginFailedEvent.class);
		events.put(codes.PLEASE_LOGIN, PleaseLoginEvent.class);
		events.put(codes.UPDATE_CONFLICT, UpdateConflictEvent.class);
		events.put(codes.SYNCKEY_OK, SynckeyEvent.class);
	}

	public External(String server) {
		this();
		
		this.setServer(server);
	}

	private String md5(String what) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return md5(what.getBytes());
	}
	
	private String md5(char[] what) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return md5(toBytes(what));
	}
	
	private byte[] toBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
				byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}
	
	private String md5(byte[] what) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hash = md.digest(what);
		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				result.append("0").append(Integer.toHexString((0xFF & hash[i])));
			} else {
				result.append(Integer.toHexString(0xFF & hash[i]));
			}
		}
		return result.toString();
	}

	public Thread request(String params) {		
		Thread request = new ParamThread(params) {
			@Override
			public void run() {
				try {
					JSONObject res = loadResponse(this.params);
					dispatchResponse(res);
				} catch (JSONException | IOException excalibur) {
					Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, excalibur);
				}
			}
		};
		request.start();
		return request;
	}

	private void dispatchResponse(JSONObject response) throws JSONException {
		int code = response.getInt("code");
		Class handler = events.get(code);
		
		if (handler != null) {
			try {
				fireEvent((Event)(
					handler
						.getDeclaredConstructor(External.class, JSONObject.class, int.class)
						.newInstance(this, response, code)
				));
			} catch (NoSuchMethodException |
					SecurityException |
					InstantiationException |
					IllegalAccessException |
					IllegalArgumentException |
					InvocationTargetException ex) {
				Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
				
				//We need to fire something
				fireEvent(new Event(this, response, code));
			}
		} else {
			fireEvent(new Event(this, response, code));
		}
	}

	private JSONObject loadResponse(String params) throws IOException, JSONException {
		URL urlsort = new URL(getApiUrl());
		HttpURLConnection conn = (HttpURLConnection) urlsort.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		//conn.setRequestProperty("Content-Length", "" + Integer.toString(request.getBytes().length));

		try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
			out.writeBytes(params);
			out.flush();
		} catch (Exception e) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

		String response;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			response = "";
			String line;
			while ((line = in.readLine()) != null) {
				response += line;
			}
		} catch (Exception e) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
		
		return new JSONObject(response);
	}

	public String login(String login, char[] password, String action_id, boolean wait) {
		try {
			Thread request = request(
					String.format(
							"action=login&login=%s&password=%s&action_id=%s",
							URLEncoder.encode(login, "UTF-8"),
							md5(password),
							URLEncoder.encode(action_id, "UTF-8")
					)
			);
			if (wait) {
				try {
					request.join();
				} catch (InterruptedException ex) {
					Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
		
	public String login(String login, char[] password, boolean wait) {
		return login(login, password, Long.toString(this.actionCounter++), wait);
	}
	
	public String login(String login, char[] password, String action_id) {
		return login(login, password, action_id, false);
	}
	
	public String login(String login, char[] password) {
		return login(login, password, Long.toString(this.actionCounter++), false);
	}

	/**
	 *
	 * @param session_id
	 * @param action_id
	 * @return 
	 */
	public String files(String session_id, String action_id) {
		try {
			request(
					String.format(
							"action=files&session_id=%s&action_id=%s",
							URLEncoder.encode(session_id, "UTF-8"),
							URLEncoder.encode(action_id, "UTF-8")
					)
			);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
	
	/**
	 *
	 * @param session_id
	 * @return 
	 */
	public String files(String session_id) {
		return files(session_id, Long.toString(this.actionCounter++));
	}
	
	public String delete(String session_id, List<File> files, String action_id) {
		try {
			StringBuilder params = new StringBuilder();
			params.append(String.format(
					"action=delete&session_id=%s&action_id=%s",
					URLEncoder.encode(session_id, "UTF-8"),
					URLEncoder.encode(action_id, "UTF-8")
			));
			for (File file : files) {
				params.append("&files[]=");
				params.append(file.getId());
			}
			request(params.toString());
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
	
	public String delete(String session_id, List<File> files) {
		return delete(session_id, files, Long.toString(this.actionCounter++));
	}
	
	/**
	 *
	 * @param session_id
	 * @param file
	 * @param folder
	 * @param action_id
	 * @return 
	 */
	public String move(String session_id, File file, String folder, String action_id) {
		try {
			request(
					String.format(
							"action=update&session_id=%s&action_id=%s&id=%s&folder=%s",
							URLEncoder.encode(session_id, "UTF-8"),
							URLEncoder.encode(action_id, "UTF-8"),
							Integer.toString(file.getId()),
							URLEncoder.encode(folder, "UTF-8")
					)
			);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
	
	/**
	 *
	 * @param session_id
	 * @param file
	 * @param folder
	 * @return 
	 */
	public String move(String session_id, File file, String folder) {
		return move(session_id, file, folder, Long.toString(actionCounter++));
	}
	
	/**
	 *
	 * @param session_id
	 * @param file
	 * @param name
	 * @param note
	 * @param action_id
	 * @return 
	 */
	public String update(String session_id, File file, String name, String note, String action_id) {
		try {
			request(
					String.format(
							"action=update&session_id=%s&action_id=%s&id=%s&name=%s&note=%s",
							URLEncoder.encode(session_id, "UTF-8"),
							URLEncoder.encode(action_id, "UTF-8"),
							Integer.toString(file.getId()),
							URLEncoder.encode(name, "UTF-8"),
							URLEncoder.encode(note, "UTF-8")
					)
			);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
	
	/**
	 *
	 * @param session_id
	 * @param file
	 * @param name
	 * @param note
	 * @return 
	 */
	public String update(String session_id, File file, String name, String note) {
		return update(session_id, file, name, note, Long.toString(actionCounter++));
	}
	
	/**
	 *
	 * @param session_id
	 * @param action_id
	 * @return 
	 */
	public String synckey(String session_id, String action_id) {
		try {
			request(
					String.format(
							"action=synckey&session_id=%s&action_id=%s",
							URLEncoder.encode(session_id, "UTF-8"),
							URLEncoder.encode(action_id, "UTF-8")
					)
			);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
	
	/**
	 *
	 * @param session_id
	 * @return 
	 */
	public String synckey(String session_id) {
		return synckey(session_id, Long.toString(this.actionCounter++));
	}
}
