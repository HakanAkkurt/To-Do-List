package hakan_akkurt_matthias_will.de.to_do_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Locale;

import hakan_akkurt_matthias_will.de.to_do_list.database.TodoDatabase;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

public class ToDoDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TODO_ID_KEY = "TODO";

    private TextView name;
    private TextView dueDate;
    private TextView description;
    private CheckBox favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);

        long id = getIntent().getLongExtra(TODO_ID_KEY,0);

        name = (TextView) findViewById(R.id.name);
        dueDate = (TextView) findViewById(R.id.dueDate);
        description = (TextView) findViewById(R.id.description);
        favorite = (CheckBox) findViewById(R.id.favorite);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ToDo todo = TodoDatabase.getInstance(this).readToDo(id);

        name.setText(todo.getName());
        dueDate.setText(todo.getDueDate() == null ? "-" : getDateInString(todo.getDueDate()) );
        description.setText(todo.getDescription() == null ? "no description" : todo.getDescription());

        favorite.setChecked(todo.isFavorite());

        Log.e("todo", todo.toString());
        Log.e("todo id", String.valueOf(todo.getId()));
        Log.e("todo name", todo.getName());

    }

    private String getDateInString(Calendar calendar){
       //return String.format(Locale.GERMANY, "%02d. %02d. %d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + 1 + "." + calendar.get(Calendar.YEAR)
                 + "   " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(51.505636, -0.075315);
        googleMap.addMarker(new MarkerOptions().position(position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
}
