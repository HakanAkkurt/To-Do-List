package hakan_akkurt_matthias_will.de.to_do_list.model;

import com.google.android.gms.maps.model.LatLng;

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
    private String   description;
    private LatLng location;

    public ToDo() {
        this(null, null, false, null, null);
    }

    public ToDo(final String name) {
        this(name, null, false, null, null);
    }

    public ToDo(final String name, final Calendar dueDate, final boolean favorite, final String description, final LatLng location) {
        this.name = name;
        this.dueDate = dueDate;
        this.favorite = favorite;
        this.description = description;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(final Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(final boolean favorite) {
        this.favorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(final LatLng location) {
        this.location = location;
    }
}

