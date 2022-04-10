package domain;

import java.io.Serializable;

public class Entity<T> implements Serializable {

    private T Id;

    public Entity(T id) {
        Id = id;
    }

    public T getId() {
        return Id;
    }

    public void setId(T id) {
        Id = id;
    }

    @Override
    public String toString() {
        return Id.toString();

    }
}
