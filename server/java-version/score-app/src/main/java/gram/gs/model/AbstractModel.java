package gram.gs.model;

import java.io.Serializable;

public abstract class AbstractModel implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
