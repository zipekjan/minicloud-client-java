/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.download;

import cz.zipek.minicloud.Manager;
import cz.zipek.minicloud.api.Eventor;
import cz.zipek.minicloud.api.File;
import cz.zipek.minicloud.api.Listener;
import cz.zipek.minicloud.api.download.events.DownloadAllDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileDoneEvent;
import cz.zipek.minicloud.api.download.events.DownloadFileStartedEvent;
import java.nio.file.FileSystems;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kamen
 */
public class Downloader extends Eventor<DownloadEvent> implements Listener {

	private final List<DownloadItem> items = new LinkedList<>();
	private DownloadThread thread;
	private String targetFolder;

	public void add(File file) {
		add(file, null);
	}

	public void add(File file, String target) {
		getItems().add(new DownloadItem(file, target));
	}
	
	public void remove(File file) {
		DownloadItem found = null;
		for(DownloadItem item : getItems()) {
			if (item.getFile() == file) {
				found = item;
				break;
			}
		}
		if (found != null) {
			getItems().remove(found);
		}
	}

	public void start(String target_folder) {
		if (getItems().size() > 0 && thread == null) {
			targetFolder = target_folder;
			nextFile();
		}
	}

	private synchronized void nextFile() {
		DownloadItem file = getItems().get(0);
		getItems().remove(0);

		String target = file.getTarget();
		if (target == null) {
			target = targetFolder
				+ FileSystems.getDefault().getSeparator()
				+ file.getFile().getName();
		}
		
		fireEvent(new DownloadFileStartedEvent(file.getFile(), target));
		
		thread = new DownloadThread(file.getFile(), target, Manager.external.getAuth());
		thread.addListener(this);
		thread.start();
	}

	@Override
	public synchronized void handleEvent(Object event, Object sender) {
		if (event instanceof DownloadFileDoneEvent) {
			if (getItems().size() > 0) {
				nextFile();
			} else {
				fireEvent(new DownloadAllDoneEvent());
			}
		}
		fireEvent((DownloadEvent) event);
	}

	public void stop() {
		if (thread != null) {
			thread.setStopDownload(true);
			thread = null;
		}
	}

	/**
	 * @return the items
	 */
	public List<DownloadItem> getItems() {
		return items;
	}
}
