/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.model;

/**
 *
 * @author bsorrentino
 */
public class Storage {
    
    public enum Representation {
        VIEW,
        PAGE,
        STORAGE,
        WIKI;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

}
