/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.functional;

/**
 *
 * @author bsorrentino
 */
public class Tuple2<V1,V2> {
    
    public final V1 value1;
    public final V2 value2;

    public Tuple2(V1 value1, V2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
    
    public static <V1,V2>  Tuple2<V1,V2> of(V1 value1, V2 value2) {
        return new Tuple2<>( value1, value2 );
    }
    
    
    
}
