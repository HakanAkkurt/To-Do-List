package hakan_akkurt_matthias_will.de.to_do_list;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Locale;

import hakan_akkurt_matthias_will.de.to_do_list.R;
import hakan_akkurt_matthias_will.de.to_do_list.database.TodoDatabase;
import hakan_akkurt_matthias_will.de.to_do_list.dialogs.DatePickerFragment;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

public class CreateNewToDoActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, DatePickerDialog.OnDateSetListener {

    public Button currentPosition;
    public GoogleMap map;
    private Marker marker;
    private LocationManager locationManager;

    private ToDo todo;

    private TextView dueDate;
    private EditText name;
    private EditText description;
    private CheckBox favorite;

    private Button      save;
    private ImageButton removeDueDate;
    private ImageButton removePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_to_do);

        this.currentPosition = (Button) findViewById(R.id.currentPosition);

        this.todo = new ToDo();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.name = (EditText) findViewById(R.id.name);
        this.description = (EditText) findViewById(R.id.description);
        this.favorite = (CheckBox) findViewById(R.id.favorite);

        this.save = (Button) findViewById(R.id.save);
        this.removeDueDate = (ImageButton) findViewById(R.id.deleteDueDate);
        this.removePosition = (ImageButton) findViewById(R.id.deletePosition);

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (this.currentPosition != null) {
            this.currentPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    searchPosition();
                }
            });
        }

        this.dueDate = (TextView) findViewById(R.id.dueDateText);

        this.dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        //Ursuppe, Grundschleim eines jeden Dialoges

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("SEHR WICHTIG!!!");
        builder.setMessage("Sie sind im Begriff ein neues ToDo zu erstellen. Herzlichen glückwunsch und viel Erfolg.");
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();*/

        this.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                todo.setName(editable.toString().length() == 0 ? null : editable.toString());
            }
        });

        this.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                todo.setDescription(editable.toString().length() == 0 ? null : editable.toString());
            }
        });

        this.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                todo.setFavorite(b);
            }
        });

        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (todo.getName() == null) {
                    Toast.makeText(CreateNewToDoActivity.this, "Fehler beim Speichern, bitte noch einen Namen eingeben.", Toast.LENGTH_SHORT).show();
                    return;
                }

                TodoDatabase.getInstance(CreateNewToDoActivity.this).createToDo(todo);
                finish();
            }
        });

        this.removePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                removeLocation();
            }
        });

        this.removeDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                removeDueDate();
            }
        });

    }

    private void searchPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermission(5);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.map = googleMap;
    }

    @Override
    public void onLocationChanged(final Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

        todo.setLocation(position);

        if (this.map != null) {

            if (this.marker != null) {
                this.marker.remove();
            }

            this.marker = map.addMarker(new MarkerOptions().position(position));

            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        }

        removeListener();
    }

    private void removeListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermission(4);
        }
        this.locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(final String s, final int i, final Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(final String s) {

    }

    @Override
    public void onProviderDisabled(final String s) {

    }

    private void requestPermission(final int resultCode) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, resultCode);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    searchPosition();
                }
                break;
            case 4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    removeListener();
                }
                break;
        }
    }

    @Override
    public void onDateSet(final DatePicker datePicker, final int i, final int i1, final int i2) {
        this.dueDate.setText(String.format(Locale.GERMANY, "%02d. %02d. %d", i2, i1 + 1, i));

        Calendar c = Calendar.getInstance();
        c.set(i, i1, i2);

        todo.setDueDate(c);
    }

    private void removeLocation() {
        if (this.marker != null) {
            this.marker.remove();
        }

        todo.setLocation(null);
    }

    private void removeDueDate() {
        this.dueDate.setText("");
        this.todo.setDueDate(null);
    }
}