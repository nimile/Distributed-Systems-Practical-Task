package de.haevn.distributed_systems.v2.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGetTest<T>{
    protected final List<T> data = new ArrayList<>();
    protected Optional<T> optionalObject;
    protected Optional<T> emptyObject;
    protected Optional<ArrayList<T>> emptyObjectList;

    public abstract void get();
    public abstract void getById();
    public abstract void getByIdObjectDoesNotExists();

}
