package hakan_akkurt_matthias_will.de.to_do_list.adapter.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import hakan_akkurt_matthias_will.de.to_do_list.R;
import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

/**
 * Created by Hakan Akkurt on 13.01.2017.
 */

public class ToDoOverviewListAdapter extends ArrayAdapter<ToDo> {
    public ToDoOverviewListAdapter(Context context, List<ToDo> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDo currentToDo = getItem(position);

        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.todo_overwiew_listitem, parent, false);
        }

        ((TextView) view.findViewById(R.id.name)).setText(currentToDo.getName());

        TextView dueDate = (TextView) view.findViewById(R.id.dueDate);

        if(currentToDo.getDueDate()== null){
            dueDate.setVisibility(View.GONE);
        }else{
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(String.valueOf(currentToDo.getDueDate().get(Calendar.YEAR)));
        }

        return view;
    }
}
