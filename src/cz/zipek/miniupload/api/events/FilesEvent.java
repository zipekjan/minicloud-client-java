/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.miniupload.api.events;

import cz.zipek.miniupload.api.External;
import cz.zipek.miniupload.api.File;
import cz.zipek.miniupload.api.Folder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kamen
 */
public class FilesEvent extends cz.zipek.miniupload.api.Event {
	///@var root folder of received files
	private Folder root;
	
	///@var current time difference from server (Local time - server time)
	private int offset;
	
	public FilesEvent(External sender, JSONObject data, int code) {
		super(sender, data, code);
		
		this.parseData(data);
	}
	
	private void parseData(JSONObject data) {
		if (data == null) {
			return;
		}
			
		try {
			JSONObject body = data.getJSONObject("body");
			JSONArray files = body.getJSONArray("files");

			root = new Folder("/");
			for(int i = 0; i < files.length(); i++)
			{
				JSONObject obj = files.getJSONObject(i);
				File file = new File(sender, obj);

				String[] path = file.getFolderPath().split("/");
				if (path.length > 1 || path[0].length() > 0)
				{
					Folder child = new Folder(path[path.length - 1]);
					child.add(file);
					for(int p = path.length - 2; p >= 0; p--)
					{
						Folder c = new Folder(path[p]);
						c.add(child);
						child = c;
					}
					getRoot().add(child);
				} else {
					getRoot().add(file);
				}
			}
			
			offset = (int)(new Date().getTime() / 1000) - body.getInt("time");
		} catch(JSONException excentric) {
			Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, excentric);
		}
	}

	/**
	 * @return the root
	 */
	public Folder getRoot() {
		return root;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
}
