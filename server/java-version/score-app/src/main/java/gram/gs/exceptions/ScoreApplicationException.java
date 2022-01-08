package gram.gs.exceptions;

public class ScoreApplicationException extends Exception {

    private final String id;

    public ScoreApplicationException(String id, String e) {
        super(e);
        this.id = id;
    }
}
