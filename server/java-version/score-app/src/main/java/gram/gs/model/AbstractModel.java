package gram.gs.model;

import java.io.Serializable;

public abstract class AbstractModel<T extends AbstractModel<T>> implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T id(String id) {
        this.id = id;
        return (T) this;
    }

}
