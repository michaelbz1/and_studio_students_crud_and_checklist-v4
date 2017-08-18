package com.journaldev.sqlite;

public class Student {

    // Table columns
    String _ID = null;
    String STUDENTID = null;
    String STUDENTNAME  = null;
    String STUDENTPER = null;

    public String get_ID() {
        return _ID;
    }
    public void set_ID(String code) {
        this._ID = _ID;
    }
    public String getSTUDENTID() {
        return STUDENTID;
    }
    public void setSTUDENTID(String STUDENTID) {
        this.STUDENTID = STUDENTID;
    }
    public String getSTUDENTNAME() {
        return STUDENTNAME;
    }
    public void setSTUDENTNAME(String STUDENTNAME) {
        this.STUDENTNAME = STUDENTNAME;
    }
    public String getSTUDENTPER() {
        return STUDENTPER;
    }
    public void setSTUDENTPER(String STUDENTPER) {
        this.STUDENTPER = STUDENTPER;
    }
}
