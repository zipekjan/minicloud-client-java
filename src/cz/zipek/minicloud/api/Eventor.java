/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kamen
 * @param <E> expected event class
 */
public class Eventor<E> {
	protected final List<Listener> listeners = new ArrayList<>();
	protected final List<Listener> toRemove = new ArrayList<>();
	protected final List<Listener> toAdd = new ArrayList<>();
	
	public synchronized void addListener(Listener<E> listener) {
		listeners.add(listener);
	}
	
	public synchronized void addListenerLater(Listener<E> listener) {
		toAdd.add(listener);
	}


	/**
	 * Don't call this method from handler!
	 * 
	 * @param listener 
	 */
	public synchronized void removeListener(Listener<E> listener) {
		listeners.remove(listener);
	}
	
	/**
	 * This method should be only called inside handler method
	 * 
	 * @param listener 
	 */
	public void removeListenerLater(Listener<E> listener) {
		toRemove.add(listener);
	}
	
	protected synchronized void fireEvent(E event) {
		if (toRemove.size() > 0) {
			for(Listener l : toRemove) {
				listeners.remove(l);
			}
			toRemove.clear();
		}
		
		if (toAdd.size() > 0) {
			for(Listener l : toAdd) {
				listeners.add(l);
			}
			toAdd.clear();
		}

		for(Listener listener : listeners) {
			listener.handleEvent(event, this);
		}
		
		if (toRemove.size() > 0) {
			for(Listener l : toRemove) {
				listeners.remove(l);
			}
			toRemove.clear();
		}
		
		if (toAdd.size() > 0) {
			for(Listener l : toAdd) {
				listeners.add(l);
			}
			toAdd.clear();
		}
	}
}