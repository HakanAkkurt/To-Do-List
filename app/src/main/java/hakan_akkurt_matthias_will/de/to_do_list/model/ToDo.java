package hakan_akkurt_matthias_will.de.to_do_list.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Hakan Akkurt on 13.01.2017.
 */

public class ToDo implements Serializable {
    private long id;
    private String name;
    private Calendar dueDate;
    private boolean favorite;
    private String description;

    public ToDo(final String name) {
        this(name, null, false, null);
    }

    public ToDo(final String name, final Calendar dueDate, final boolean favorite, final String description) {
        this.name = name;
        this.dueDate = dueDate;
        this.favorite = favorite;
        this.description = description;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
