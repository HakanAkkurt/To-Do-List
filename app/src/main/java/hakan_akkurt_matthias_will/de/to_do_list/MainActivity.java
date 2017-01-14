package hakan_akkurt_matthias_will.de.to_do_list;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import hakan_akkurt_matthias_will.de.to_do_list.adapter.listview.ToDoOverviewListAdapter;
import hakan_akkurt_matthias_will.de.to_do_list.database.TodoDatabase;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

import static hakan_akkurt_matthias_will.de.to_do_list.R.id.todos;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ToDoOverviewListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //eigene Codes


        this.listView = (ListView) findViewById(R.id.todos);

        Button create = (Button) findViewById(R.id.create);
        Button clearAll = (Button) findViewById(R.id.clearAll);
        Button clearFirst = (Button) findViewById(R.id.clearFirst);
        Button updateFirst = (Button) findViewById(R.id.updateFirst);



        this.adapter = new ToDoOverviewListAdapter(this, TodoDatabase.getInstance(this).getAllTodosAsCursor());
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
                Object element = adapterView.getAdapter().getItem(position);

                Log.e("ClickOnList", element.toString());

            }
        });

        if(create != null){
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);

                    database.createToDO(new ToDo("Einkaufen"));
                    database.createToDO(new ToDo("was geht?", Calendar.getInstance()));

                    refreshListView();

                }
            });
        }

        if(clearAll != null){
            clearAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);
                    database.deleteAllToDos();
                    refreshListView();
                }
            });
        }

        if(clearFirst != null){
            clearFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);
                    ToDo first = database.getFirstTodo();
                    if(first != null){
                        database.deleteToDo(first);
                        refreshListView();
                    }

                }
            });
        }

        if(updateFirst != null){
            updateFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);
                    ToDo first = database.getFirstTodo();
                    if(first != null){
                        Random r = new Random();
                        first.setName(String.valueOf(r.nextInt()));
                        database.updateToDo(first);
                        refreshListView();


                    }

                }
            });
        }





        //bis hier



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void refreshListView(){
        adapter.changeCursor(TodoDatabase.getInstance(this).getAllTodosAsCursor());

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
