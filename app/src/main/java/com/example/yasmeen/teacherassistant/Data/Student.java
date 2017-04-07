package com.example.yasmeen.teacherassistant.Data;

import java.io.Serializable;

/**
 * Created by yasmeen on 3/17/2017.
 */

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name ;
    private boolean isSelected ;

    public Student(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
