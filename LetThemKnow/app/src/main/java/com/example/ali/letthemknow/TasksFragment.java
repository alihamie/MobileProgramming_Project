package com.example.ali.letthemknow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ibrahim
 */

public class TasksFragment extends Fragment {

    public FloatingActionButton tasksButton;
    public static final String ARG_PAGE = "ARG_PAGE";
    public RecyclerView mRecycleView;

    public static TasksFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent addTaskToList(Context context){}

    public ArrayList<String> getTasks(){
        Context c = getActivity().getApplicationContext();
        Gson gson = new Gson();
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(c);
        String json  = pr.getString("TASKS",null);;
        Type t = new TypeToken<ArrayList<Contact>>(){}.getType();
        ArrayList<String> tasks;
        if(json != null)
            tasks = gson.fromJson(json,t);
        else{
            tasks = new ArrayList<String>();
        }
        return tasks;

    }

    public ArrayList<String> getSteps(){
        Context c = getActivity().getApplicationContext();
        Gson gson = new Gson();
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(c);
        String json  = pr.getString("STEPS",null);;
        Type t = new TypeToken<ArrayList<Contact>>(){}.getType();
        ArrayList<String> steps;
        if(json != null)
            steps = gson.fromJson(json,t);
        else{
            steps = new ArrayList<String>();
        }
        return steps;

    }

    public void addTasks(ArrayList<String> tasks){
        Context c = getActivity().getApplicationContext();
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pr.edit();

        String savedTasks = pr.getString("TASKS",null);
        Gson gson = new Gson();
        String json;
        if(savedTasks == null) {
            json = gson.toJson(tasks);

        }else{
            Type t = new TypeToken<ArrayList<Contact>>(){}.getType();
            ArrayList<String> ts;
            ts = gson.fromJson(savedTasks,t);
            Set<String> s = new HashSet<>();
            s.addAll(ts);
            s.addAll(tasks);
            ts.clear();
            ts.addAll(s);
            json = gson.toJson(ts);
        }
        editor.putString("TASKS", json);
        editor.commit();

    }
    public void addSteps(ArrayList<String> steps){
        Context c = getActivity().getApplicationContext();
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pr.edit();

        String savedTasks = pr.getString("STEPS",null);
        Gson gson = new Gson();
        String json;
        if(savedTasks == null) {
            json = gson.toJson(steps);

        }else{
            Type t = new TypeToken<ArrayList<Contact>>(){}.getType();
            ArrayList<String> ts;
            ts = gson.fromJson(savedTasks,t);
            Set<String> s = new HashSet<>();
            s.addAll(ts);
            s.addAll(steps);
            ts.clear();
            ts.addAll(s);
            json = gson.toJson(ts);
        }
        editor.putString("STEPS", json);
        editor.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setContentView(R.layout.fragment_tasks);
        View taskView = inflater.inflate(R.layout.fragment_tasks, container, false);
        ListView taskListView = taskView.findViewById(R.id.task_list);
        ListView stepListView = taskView.findViewById(R.id.step_list);
        final ArrayList<String> taskList = getTasks();
        final ArrayList<String> stepList = getSteps();


        ArrayAdapter<String> stepAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stepList);
        taskListView.setAdapter(stepAdapter);

        ArrayAdapter<String> taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList);
        taskListView.setAdapter(taskAdapter);

        final EditText taskEditText, numStepsEditText;
        taskEditText = (EditText) taskView.findViewById(R.id.new_task_description);
        numStepsEditText = (EditText) taskView.findViewById(R.id.num_steps);
        FloatingActionButton add = (FloatingActionButton) taskView.findViewById(R.id.floating_add);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Task currentTask = new Task();
                currentTask.taskDescription = taskEditText.getText().toString();
                currentTask.numSteps = Integer.parseInt(numStepsEditText.getText().toString());
                taskList.add(currentTask.taskDescription);
                stepList.add(currentTask.numSteps.toString());
                addTasks(taskList);
                addSteps(stepList);
            }
        });
        return taskView;
    }


}
