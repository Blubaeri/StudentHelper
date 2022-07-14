package main.java;

/** An item on a todo list. */
public final class Task {

    private final long id;
    private final String name;
    private final String lastNames;
    private final String username;
    private final long timestamp;

    public Task(long id, String names, String lastNames, String username,
    long timestamp) {
    
        this.id = id;
        this.names = names;
        this.lastNames = lastNames;
        this.username = username;
        this.timestamp = timestamp;

    }

    public String getUsername() {

        return this.username;

    }
    
}
