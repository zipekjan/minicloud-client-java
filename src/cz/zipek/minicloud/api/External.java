/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api;

import cz.zipek.minicloud.api.events.ErrorEvent;
import cz.zipek.minicloud.api.events.FileEvent;
import cz.zipek.minicloud.api.events.PathEvent;
import cz.zipek.minicloud.api.events.ServerInfoEvent;
import cz.zipek.minicloud.api.events.SuccessEvent;
import cz.zipek.minicloud.api.events.UserEvent;
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

	class codes {
		public static final String PATH = "path";
		public static final String FILE = "file";
		public static final String FILES = "files";
		public static final String USER = "user";
		public static final String SERVER_INFO = "server";
		public static final String ERROR = "error";
	}
	
	private final Map<String, Class> events = new HashMap<>();

	private String server = "http://minicloud.zipek.cz";

	private String auth;
	
	private long actionCounter;
	
	/**
	 * Used for parametrized thread
	 */
	class ParamThread extends Thread {

		public final String params;
		public final String auth;

		ParamThread(String params, String auth) {
			this.params = params;
			this.auth = auth;
		}
	}

	public External() {
		this.actionCounter = 0;
		
		events.put("", SuccessEvent.class);
		events.put(codes.FILE, FileEvent.class);
		events.put(codes.ERROR, ErrorEvent.class);
		events.put(codes.PATH, PathEvent.class);
		events.put(codes.USER, UserEvent.class);
		events.put(codes.SERVER_INFO, ServerInfoEvent.class);
		
	}

	public External(String server) {
		this();
		
		this.setServer(server);
	}

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

	
	private String md5(String what) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return md5(what.getBytes("UTF-8"));
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
		return getHexString(MessageDigest.getInstance("MD5").digest(what));
		
	}

	private String sha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return sha256(input.getBytes("UTF-8"));
	}
	
	private String sha256(char[] input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return sha256(toBytes(input));
	}
	
	private String sha256(byte[] input) throws NoSuchAlgorithmException {
		return getHexString(MessageDigest.getInstance("SHA-256").digest(input));
	}
	
	private String getHexString(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if ((0xff & bytes[i]) < 0x10) {
				result.append("0").append(Integer.toHexString((0xFF & bytes[i])));
			} else {
				result.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return result.toString();
	}

	public Thread request(String params) {
		return request(params, getAuth());
	}
	
	public Thread request(String params, String auth) {		
		Thread request = new ParamThread(params, auth) {
			@Override
			public void run() {
				try {
					JSONObject res = loadResponse(this.params, this.auth);
					dispatchResponse(res);
				} catch (JSONException | IOException excalibur) {
					dispatchResponse(null);
					Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, excalibur);
				}
			}
		};
		request.start();
		return request;
	}

	private void dispatchResponse(JSONObject response) {
		
		if (response == null) {
			fireEvent(new ErrorEvent(this, null, null));
			return;
		}
		
		String type = response.optString("type", "");
		String action_id = response.optString("action_id", null);
		JSONObject data = response.optJSONObject("data");
		
		Class handler = events.get(type);
		
		if (handler != null) {
					
			System.out.println("Event type " + handler.getName());
			
			try {
				fireEvent((Event)(
					handler
						.getDeclaredConstructor(External.class, JSONObject.class, String.class)
						.newInstance(this, data, action_id)
				));
			} catch (NoSuchMethodException |
					SecurityException |
					InstantiationException |
					IllegalAccessException |
					IllegalArgumentException |
					InvocationTargetException ex) {
				Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
				
				//We need to fire something
				fireEvent(new Event(this, data, action_id));
			}
		} else {
			fireEvent(new Event(this, data, action_id));
		}
	}

	private JSONObject loadResponse(String params, String auth) throws IOException, JSONException {
		URL urlsort = new URL(getApiUrl());
		HttpURLConnection conn = (HttpURLConnection) urlsort.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("X-Auth", auth);
		//conn.setRequestProperty("Content-Length", "" + Integer.toString(request.getBytes().length));

		System.out.println(params);
		
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
				response += line + "\n";
			}
		} catch (Exception e) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
				response = "";
				String line;
				while ((line = in.readLine()) != null) {
					response += line + "\n";
				}
			} catch (Exception e2) {
				Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, e2);
				return null;
			}
		}
		
		System.out.print(response);
		
		return new JSONObject(response);
	}
	
	private String createUrl(String action, Map<String, String> params) {
		try {
			StringBuilder result = new StringBuilder();
			result.append("action=");
			result.append(URLEncoder.encode(action, "UTF-8"));
			
			for(Map.Entry<String, String> item : params.entrySet()) {
				result.append("&");
				result.append(item.getKey());
				result.append("=");
				result.append(URLEncoder.encode(item.getValue(), "UTF-8"));
			}
			
			return result.toString();
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return "";
	}
	
	public String getServerInfo() {
		return getServerInfo(false);
	}
	
	public String getServerInfo(boolean wait) {
		return getServerInfo(wait, Long.toString(this.actionCounter++));
	}
	
	public String getServerInfo(boolean wait, String action_id) {
		Map<String, String> params = new HashMap<>();
		params.put("action_id", action_id);
		
		Thread request = request(createUrl("get_server_info", params));

		if (wait) {
			try {
				request.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		return action_id;
	}
	
	public String getUser() {
		return getUser(false);
	}
	
	public String getUser(boolean wait) {
		return getUser(wait, Long.toString(this.actionCounter++));
	}
	
	public String getUser(boolean wait, String action_id) {
		Map<String, String> params = new HashMap<>();
		params.put("action_id", action_id);
		
		Thread request = request(createUrl("get_user", params));

		if (wait) {
			try {
				request.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		return action_id;
	}
	
	public String getPath() {
		return getPath(null);
	}
	
	public String getPath(String path) {
		return getPath(path, false);
	}
	
	public String getPath(String path, boolean wait) {
		return getPath(path, wait, Long.toString(this.actionCounter++));
	}
	
	public String getPath(String path, boolean wait, String action_id) {
		Map<String, String> params = new HashMap<>();
		params.put("action_id", action_id);
		
		if (path != null) {
			params.put("path", path);
		}
		
		Thread request = request(createUrl("get_path", params));

		if (wait) {
			try {
				request.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		return action_id;
	}
	
	public String delete(List<File> files, String action_id) {
		try {
			StringBuilder params = new StringBuilder();
			params.append(String.format(
					"action=delete&action_id=%s",
					URLEncoder.encode(action_id, "UTF-8")
			));
			for (File file : files) {
				params.append("&files[]=");
				params.append(file.getId());
			}
			request(params.toString(), getAuth());
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		return action_id;
	}
	
	public String delete(List<File> files) {
		return delete(files, Long.toString(this.actionCounter++));
	}
	
	/**
	 *
	 * @param file
	 * @param folder
	 * @param action_id
	 * @return 
	 */
	public String move(File file, String folder, String action_id) {
		try {
			request(
					String.format(
							"action=update&action_id=%s&id=%s&folder=%s",
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
	 * @param file
	 * @param folder
	 * @return 
	 */
	public String move(File file, String folder) {
		return move(file, folder, Long.toString(actionCounter++));
	}
	
	/**
	 *
	 * @param file
	 * @param name
	 * @param note
	 * @param action_id
	 * @return 
	 */
	public String update(File file, String name, String note, String action_id) {
		try {
			request(
					String.format(
							"action=update&action_id=%s&id=%s&name=%s&note=%s",
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
	 * @param file
	 * @param name
	 * @param note
	 * @return 
	 */
	public String update(File file, String name, String note) {
		return update(file, name, note, Long.toString(actionCounter++));
	}
	
	public void setAuth(String aAuth) {
		auth = aAuth;
	}
	
	public void setAuth(String login, char[] password) {
		
		try {
			setAuth(sha256(login + sha256(password)));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(External.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public String getAuth() {
		return auth;
	}
}
