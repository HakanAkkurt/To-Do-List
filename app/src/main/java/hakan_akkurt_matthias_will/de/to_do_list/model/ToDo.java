package hakan_akkurt_matthias_will.de.to_do_list.model;

import java.util.Calendar;

/**
 * Created by Hakan Akkurt on 13.01.2017.
 */

public class ToDo {
    private long id;
    private String name;
    private Calendar dueDate;

    public ToDo(final String name) {
        this(name, null);
    }

    public ToDo(final String name, final Calendar dueDate) {
        this.name = name;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
