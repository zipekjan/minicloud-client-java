/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zipek.minicloud.api;

/**
 *
 * @author Kamen
 * @param <E>
 */
public interface Listener<E> {
	public void handleEvent(E event, Object sender);
}
