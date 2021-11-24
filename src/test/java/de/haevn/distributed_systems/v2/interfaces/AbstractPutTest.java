package de.haevn.distributed_systems.v2.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPutTest<T>{
    protected List<T> data = new ArrayList<>();
    protected Optional<T> optionalObject;

    public abstract void put();
    public abstract void putThrowsArgumentMismatchException();
    public abstract void putApiException();
    public abstract void putById();

}
