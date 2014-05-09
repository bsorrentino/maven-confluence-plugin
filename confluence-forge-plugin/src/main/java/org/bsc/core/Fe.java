/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.core;

public interface Fe<P,R> {
    
    R f( P param ) throws Exception;
};

