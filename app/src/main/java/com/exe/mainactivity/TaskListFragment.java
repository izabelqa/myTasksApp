package com.exe.mainactivity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private boolean subtitleVisible;
    public static final String KEY_EXTRA_TASK_ID = "com.exe.mainactivity.id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Powiąż widok fragmentu z plikiem fragment_task_list.xml
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.fragment_task_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
            if(itemId == R.id.new_task) {
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;
            } else if (itemId == R.id.show_subtitle) {
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();//wymuszenie rekonstrukcji przycisków akcji menu
                updateSubtitle();
                return true;
            }
                return super.onOptionsItemSelected(item);
        }

    public void updateSubtitle() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            List<Task> tasks = TaskStorage.getInstance().getTasks();
            int todoTasksCount = 0;
            for (Task task : tasks) {
                if (!task.isDone()) {
                    todoTasksCount++;
                }
            }
            String subtitle = getString(R.string.subtitle_format, todoTasksCount);
            if (!subtitleVisible) {
                subtitle = null;
            }
            actionBar.setSubtitle(subtitle);
        }
    }

    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if(adapter == null){
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setTasks(tasks);
            adapter.notifyDataSetChanged();
        }
          updateSubtitle();
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private TextView nameTextView;
       private TextView dateTextView;
       private Task task;
       private ImageView iconImageView;
       private CheckBox checkBox;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent,false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            iconImageView = itemView.findViewById(R.id.task_item_category_icon);
            checkBox = itemView.findViewById(R.id.task_item_checkbox);
        }

        public void bind (Task task){
            this.task = task;
            nameTextView.setText(task.getName());

            if(task.isDone()){
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);//skreślam wykonane zadanie
            } else{
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);//usuwam skreślenie
            }

            dateTextView.setText(task.getDate().toString());

            if(task.getCategory().equals(Category.HOME)) {
                iconImageView.setImageResource(R.drawable.ic_house);
            } else {
                iconImageView.setImageResource(R.drawable.ic_university);
            }
            checkBox.setChecked(task.isDone());

        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        @Override
        public void onClick (View view){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }

    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;
        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }
        public void setTasks(List<Task> tasks){
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder (@NonNull TaskHolder holder, int position){
            Task task = tasks.get(position);
            holder.bind(task);

            CheckBox checkBox = holder.getCheckBox();
            checkBox.setChecked(tasks.get(position).isDone());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked));
        }

        @Override
        public int getItemCount(){
            return tasks.size();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateView();
        updateSubtitle();
    }

}
