package com.michaelfotiadis.locationmanagerviewer.data.containers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
/**
 * 
 * @author Alexandros Schillings
 *
 */
public final class MyFragmentInfo {
    private final Class<?> clss;
    private final Bundle args;
    
    // OR
    
    private final Fragment frag;
    
    public MyFragmentInfo(Class<?> _class, Bundle _args) {
        clss = _class;
        args = _args;
        frag = null;
    }
    
    public MyFragmentInfo(Fragment _frag) {
        clss = null;
        args = null;
        frag = _frag;
    }
    
    public Bundle getArgs(){
     return args;
    }
    
    public Fragment getFrag(){
     return frag;
    }
    
    public Class<?> getClss(){
     return clss;
    }
}