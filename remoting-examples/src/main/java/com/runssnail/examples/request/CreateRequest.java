package com.runssnail.examples.request;

import java.io.Serializable;

/**
 * Created by zhengwei on 2017/11/6.
 */
public class CreateRequest implements Serializable {

    private String name;

    private int age;

    public CreateRequest(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
