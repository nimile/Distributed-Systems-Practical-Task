package de.haevn.distributed_systems.v2.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPostTest<T> extends AbstractTestData<T>{

    public abstract void post();
    public abstract void postThrowsArgumentMismatchException();
    public abstract void postExistsException();
    public abstract void postById();

}
