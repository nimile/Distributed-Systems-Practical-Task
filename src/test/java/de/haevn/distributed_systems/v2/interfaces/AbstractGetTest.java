package de.haevn.distributed_systems.v2.interfaces;

public abstract class AbstractGetTest<T> extends AbstractTestData<T> {
    public abstract void get();
    public abstract void getById();
    public abstract void getByIdObjectDoesNotExists();

}
