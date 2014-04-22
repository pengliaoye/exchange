/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dm.exchange;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author cmp
 */
public class OtherTest {
    
    @Test
    public void test1(){
        List<String> names = new ArrayList<>();
        Collections.sort(names, (String o1, String o2) -> o2.compareTo(o2));        
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
    }
    
}
