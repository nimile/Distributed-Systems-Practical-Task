package de.haevn.distributed_systems.v2.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPostTest<T>{
    protected List<T> data = new ArrayList<>();
    protected Optional<T> optionalObject;

    public abstract void post();
    public abstract void postThrowsArgumentMismatchException();
    public abstract void postExistsException();
    public abstract void postById();

}
