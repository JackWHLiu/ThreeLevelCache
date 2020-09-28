package com.example.threelevelcache;

import java.util.List;

public interface PersonView {

    void showPerson(List<Person> persons);
    void reducePerson(Person person);
    void appendPerson(Person person);
}
