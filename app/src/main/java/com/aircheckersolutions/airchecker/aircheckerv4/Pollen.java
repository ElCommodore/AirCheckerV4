package com.aircheckersolutions.airchecker.aircheckerv4;

public class Pollen {

    String name;
    String currentValue;
    int status;
    boolean activated;
    int resID;

    public Pollen(String _name, String _currentValue, int _status, boolean _activated, int _resID){
        status = _status;
        name = _name;
        currentValue = _currentValue;
        activated = _activated;
        resID = _resID;
    }
}
