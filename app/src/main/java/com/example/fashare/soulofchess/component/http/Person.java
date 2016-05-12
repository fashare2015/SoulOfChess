package com.example.fashare.soulofchess.component.http;

/**
 * Created by apple on 2016/4/19.
 */
public class Person {
    private String name;
    private String age;

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Person[name = %s, age = %s]", name, age);
    }
}
