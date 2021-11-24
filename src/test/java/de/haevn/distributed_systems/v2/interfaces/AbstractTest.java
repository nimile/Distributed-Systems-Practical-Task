package de.haevn.distributed_systems.v2.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractTest <T>{
    protected List<T> data = new ArrayList<>();
    protected Optional<T> optionalObject;

    public abstract void post();
    public abstract void postById();

    public abstract void put();
    public abstract void putById();

    public abstract void get();
    public abstract void getById();

    public abstract void delete();
    public abstract void deleteById();
}
