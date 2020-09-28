package com.example.threelevelcache;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import dora.db.Orm;

/**
 * 模拟MVP中的V层和P层，不要太在意写法。
 */
public class MainActivity extends AppCompatActivity implements PersonView {

    PersonPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Orm.init(this, "test");
        mPresenter = new PersonPresenter();
        mPresenter.fetchPersons(this);
    }

    @Override
    public void showPerson(List<Person> persons) {
    }

    @Override
    public void reducePerson(Person person) {
    }

    @Override
    public void appendPerson(Person person) {
    }
}