/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dm.system;

import com.dm.service.NodeManager;
import javax.enterprise.inject.Model;

/**
 *
 * @author Administrator
 */
@Model
public class CheckTreeBean {
    
    public String getTreeData(){
        String result = NodeManager.getInstance().parseCheckTree(null);
        //return result;
        return "123123123";
    }
    
}
