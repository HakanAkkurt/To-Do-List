package hakan_akkurt_matthias_will.de.to_do_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private EditText name;
    private EditText dueDate;
    private EditText description;
    private CheckBox favorite;
    private Button update;


    private ToDo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);

        long id = getIntent().getLongExtra(TODO_ID_KEY, 0);
        this.todo = TodoDatabase.getInstance(this).readToDo(id);

        name = (EditText) findViewById(R.id.name);
        dueDate = (EditText) findViewById(R.id.dueDateText);
        description = (EditText) findViewById(R.id.description);
        favorite = (CheckBox) findViewById(R.id.favorite);
        update = (Button) findViewById(R.id.update);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name.setText(todo.getName());
        dueDate.setText(todo.getDueDate() == null ? "-" : getDateInString(todo.getDueDate()));
        description.setText(todo.getDescription() == null ? "no description" : todo.getDescription());
        favorite.setChecked(todo.isFavorite());

        Log.e("todo", todo.toString());
        Log.e("todo id", String.valueOf(todo.getId()));
        Log.e("todo name", todo.getName());



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                TodoDatabase.getInstance(ToDoDetailActivity.this).updateToDo(todo);

            }
        }); //Tut sich irgendwie noch nichts....


    }

    private String getDateInString(Calendar calendar) {
        return String.format(Locale.GERMANY, "%02d. %02d. %d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        //return calendar.get(Calendar.DAY_OF_MONTH) + ". " + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //LatLng position = new LatLng(51.505636, -0.075315);

        if (this.todo != null && this.todo.getLocation() != null) {
            googleMap.addMarker(new MarkerOptions().position(todo.getLocation()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(todo.getLocation(), 15));
        }

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}