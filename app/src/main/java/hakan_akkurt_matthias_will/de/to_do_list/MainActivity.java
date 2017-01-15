package hakan_akkurt_matthias_will.de.to_do_list;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import hakan_akkurt_matthias_will.de.to_do_list.adapter.listview.ToDoOverviewListAdapter;
import hakan_akkurt_matthias_will.de.to_do_list.database.TodoDatabase;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ToDoOverviewListAdapter adapter;
    private List<ToDo> dataSource;


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
        Button sort = (Button) findViewById(R.id.sort);

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

        if(create != null){
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);

                    ToDo todo1 = new ToDo("X");
                    Calendar c1 = Calendar.getInstance();
                    c1.set(2016, 8, 30);
                    todo1.setDueDate(c1);

                    ToDo todo2 = new ToDo("C");
                    ToDo todo3 = new ToDo("A");

                    Calendar c2 = Calendar.getInstance();
                    c2.set(2016, 8, 14);
                    todo3.setDueDate(c2);

                    ToDo todo4 = new ToDo("B");

                    ToDo todo5 = new ToDo("H");
                    Calendar c3 = Calendar.getInstance();
                    c3.set(2016, 8, 30);
                    todo5.setDueDate(c3);

                    database.createToDO(todo1);
                    database.createToDO(todo2);
                    database.createToDO(todo3);
                    database.createToDO(todo4);
                    database.createToDO(todo5);


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

                    if(dataSource.size() > 0){
                        TodoDatabase database = TodoDatabase.getInstance(MainActivity.this);
                        database.deleteToDo(dataSource.get(0));
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
                    if(dataSource.size() > 0){
                        Random r = new Random();
                        dataSource.get(0).setName(String.valueOf(r.nextInt()));
                        database.updateToDo(dataSource.get(0));
                        refreshListView();


                    }

                }
            });
        }

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
        /*adapter.changeCursor(TodoDatabase.getInstance(this).getAllTodosAsCursor());*/
        dataSource.clear();
        dataSource.addAll(TodoDatabase.getInstance(this).readAllToDos());
        adapter.notifyDataSetChanged();

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
