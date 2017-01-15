package hakan_akkurt_matthias_will.de.to_do_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import hakan_akkurt_matthias_will.de.to_do_list.database.TodoDatabase;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

public class ToDoDetailActivity extends AppCompatActivity {
    public static final String TODO_ID_KEY = "TODO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);

        long id = getIntent().getLongExtra(TODO_ID_KEY,0);

        ToDo todo = TodoDatabase.getInstance(this).readToDo(id);

        Log.e("todo id", String.valueOf(todo.getId()));
        Log.e("todo name", todo.getName());

    }
}
