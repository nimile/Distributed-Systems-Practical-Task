package de.haevn.distributed_systems.v2.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPutTest<T>{
    protected final List<T> data = new ArrayList<>();
    protected Optional<T> optionalObject;
    protected Optional<T> emptyObject;
    protected Optional<ArrayList<T>> emptyObjectList;
    protected Optional<ArrayList<T>> optionalList;

    public abstract void put();
    public abstract void putThrowsArgumentMismatchException();
    public abstract void putByIdApiException();
    public abstract void putByIdConflictException();
    public abstract void putByIdArgumentMismatchException();
    public abstract void putById();

}
