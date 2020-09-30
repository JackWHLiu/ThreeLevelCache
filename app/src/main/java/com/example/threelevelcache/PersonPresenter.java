package com.example.threelevelcache;

import java.util.ArrayList;
import java.util.List;

public class PersonPresenter {

    public void fetchPersons(PersonView personView) {
        PersonCache cache = new PersonCache();
        List<Person> people = cache.fetchAll();
        personView.showPerson(people);
    }

    public void addPersons(PersonView personView) {
        List<Person> persons = new ArrayList<>();
        Person person = new Person("张三", 16);
        Person person2 = new Person("李四", 20);
        persons.add(person);
        persons.add(person2);
        personView.appendPerson(person);
        personView.appendPerson(person2);
        PersonCache cache = new PersonCache();
        cache.addAll(persons);
    }

    public void removePersons(PersonView personView) {
        List<Person> persons = new ArrayList<>();
        Person person = new Person("张三", 16);
        Person person2 = new Person("李四", 20);
        persons.add(person);
        persons.add(person2);
        personView.reducePerson(person);
        personView.reducePerson(person2);
        PersonCache cache = new PersonCache();
        cache.removeAll(persons);
    }

    public void savePersons(PersonView personView) {
        List<Person> persons = new ArrayList<>();
        Person person = new Person("张三", 16);
        Person person2 = new Person("李四", 20);
        persons.add(person);
        persons.add(person2);
        PersonCache cache = new PersonCache();
        cache.saveAll(persons);
    }
}
