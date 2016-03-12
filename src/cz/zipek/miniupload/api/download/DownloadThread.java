/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.miniupload.api.download;

import cz.zipek.miniupload.api.File;
import cz.zipek.miniupload.api.Listener;
import cz.zipek.miniupload.api.download.events.DownloadFailedEvent;
import cz.zipek.miniupload.api.download.events.DownloadStoppedEvent;
import cz.zipek.miniupload.api.download.events.DownloadFileDoneEvent;
import cz.zipek.miniupload.api.download.events.DownloadProgressEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class DownloadThread extends Thread
{
	private final File source;
	private final String target;
	private boolean stopDownload;

	public DownloadThread(File source, String target) {
		super("File download");
		this.source = source;
		this.target = target;
	}
	
	protected final List<Listener> listeners = new ArrayList<>();
	
	public synchronized void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	protected synchronized void fireEvent(DownloadEvent event) {
		for(Listener listener : listeners) {
			listener.handleEvent(event, this);
		}
	}

	@Override
	public void run() {
		try {
			URL url = new URL(this.getSource().getDownloadLink());
			HttpURLConnection httpConn;
			
			try {
				httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setDoOutput(true);
				httpConn.setDoInput(true);
				httpConn.setChunkedStreamingMode(4096);
				int status = httpConn.getResponseCode();
				if (status == HttpURLConnection.HTTP_OK) {
					FileOutputStream outputStream;
					long total;
					long downloaded;
					int bytesRead;
					byte[] buffer;
					try (InputStream inputStream = httpConn.getInputStream()) {
						outputStream = new FileOutputStream(this.getTarget());

						buffer = new byte[4096];
						total = httpConn.getContentLengthLong();
						downloaded = 0;

						while ((bytesRead = inputStream.read(buffer)) != -1 && !stopDownload) {
							outputStream.write(buffer, 0, bytesRead);

							downloaded += bytesRead;
							this.fireEvent(new DownloadProgressEvent(source, target, downloaded, total));
						}

						outputStream.close();
						inputStream.close();

						if (stopDownload) {
							fireEvent(new DownloadStoppedEvent(source));
						} else {
							fireEvent(new DownloadFileDoneEvent(source, target));
						}
					} catch (IOException ex) {
						fireEvent(new DownloadFailedEvent(source, ex));
					}
				} else {
					fireEvent(new DownloadFailedEvent(source, null));
				}
			} catch (IOException ex) {
				fireEvent(new DownloadFailedEvent(source, ex));
				Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (MalformedURLException ex) {
			fireEvent(new DownloadFailedEvent(source, ex));
			Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @return the source
	 */
	public File getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param stopDownload the stopDownload to set
	 */
	public void setStopDownload(boolean stopDownload) {
		this.stopDownload = stopDownload;
	}
}
