package hakan_akkurt_matthias_will.de.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuInflater;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import hakan_akkurt_matthias_will.de.to_do_list.adapter.listview.ToDoOverviewListAdapter;
import hakan_akkurt_matthias_will.de.to_do_list.database.TodoDatabase;
import hakan_akkurt_matthias_will.de.to_do_list.dialogs.NumberPickerDialogFragment;
import hakan_akkurt_matthias_will.de.to_do_list.dialogs.listener.OnNumberPicketListener;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnNumberPicketListener {

    private ListView listView;
    private ToDoOverviewListAdapter adapter;
    private List<ToDo> dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //eigene Codes


        this.listView = (ListView) findViewById(R.id.todos);



        this.dataSource = TodoDatabase.getInstance(this).readAllToDos();

        this.adapter = new ToDoOverviewListAdapter(this, dataSource);
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int position, long id) {
                Object element = adapterView.getAdapter().getItem(position);

                if(element instanceof ToDo){
                    ToDo todo = (ToDo) element;

                    Intent intent = new Intent(MainActivity.this, ToDoDetailActivity.class);
                    intent.putExtra(ToDoDetailActivity.TODO_ID_KEY, todo.getId());

                    startActivity(intent);
                }
                Log.e("ClickOnList", element.toString());

            }
        });

        /*
        if(sort != null){
            sort.setOnClickListener(new View.OnClickListener() {
              @Override
                public void onClick(final View view){
                  adapter.sort(new Comparator<ToDo>() {
                      @Override
                      public int compare(final ToDo toDo1, final ToDo todo2) {

                          if(toDo1.getDueDate() != null && todo2.getDueDate() !=null){
                              long date = (toDo1.getDueDate().getTimeInMillis() / 1000) - (todo2.getDueDate().getTimeInMillis() /1000);
                              if(date != 0){
                                  return (int)date;
                              }


                          }else if(toDo1.getDueDate() != null){
                              return -1;
                          }else if(todo2.getDueDate() != null){
                              return 1;
                          }
                          return toDo1.getName().compareToIgnoreCase(todo2.getName());
                      }
                  });

              }
            });
        }*/

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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


    @Override
    public void onNumberPicket(final boolean numberPicked, final int number, final NumberPickerDialogFragment dialogFragment) {
        if (numberPicked) {
            Toast.makeText(this, "neue nummer: " + number, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "abbruch", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        dataSource.clear();
        dataSource.addAll(TodoDatabase.getInstance(this).readAllToDos());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_clear_all:
                this.clearAll();
                return true;
            case R.id.menu_new_todo:
                this.newTodo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearAll() {
        TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);
        database.deleteAllToDos();
        refreshListView();
    }

    public void newTodo(){
        Intent i = new Intent(MainActivity.this, CreateNewToDoActivity.class);
        startActivity(i);
    }
}
