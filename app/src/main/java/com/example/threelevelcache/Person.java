package com.example.threelevelcache;

import com.example.cache_generator.annotation.ThreeLevelCache;

import dora.db.OrmTable;
import dora.db.PrimaryKeyEntity;
import dora.db.constraint.AssignType;
import dora.db.constraint.PrimaryKey;
import dora.db.table.Column;
import dora.db.table.Table;

@ThreeLevelCache
@Table("person")
public class Person implements OrmTable {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    private Long id;
    @Column("name")
    private String name;
    @Column("age")
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public PrimaryKeyEntity getPrimaryKey() {
        return new PrimaryKeyEntity("_id", id);
    }

    @Override
    public boolean isUpgradeRecreated() {
        return false;
    }
}
