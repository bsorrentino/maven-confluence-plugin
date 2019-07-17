/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.functional;

import lombok.Value;

/**
 *
 * @author bsorrentino
 */
@Value(staticConstructor="of")
public class Tuple2<V1,V2> {
    V1 value1;
    V2 value2;
}

