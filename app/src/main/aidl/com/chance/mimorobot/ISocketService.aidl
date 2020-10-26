// ISocketService.aidl
package com.chance.mimorobot;

// Declare any non-default types here with import statements

interface ISocketService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   boolean sendMessage(String message,String name);
}
