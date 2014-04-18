/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dm.exchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author cmp
 */
public class OtherTest {
    
    public void test1(){
        List<String> names = new ArrayList<>();
        Collections.sort(names, (String o1, String o2) -> o2.compareTo(o2));
    }    
    
}
