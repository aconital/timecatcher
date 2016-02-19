package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpsc.timecatcher.R;
import com.cpsc.timecatcher.model.Task;

import java.util.Date;
import java.util.List;


/**
 * Created by hroshandel on 2016-02-09.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, end, start;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            end = (TextView) view.findViewById(R.id.end);
            start = (TextView) view.findViewById(R.id.start);
        }
    }


    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = taskList.get(position);

        Date endtime= task.getEndTime();
        Date starttime=task.getStartTime();
        String title=   task.getTitle();
        if(title !=null)
            holder.title.setText(title);
        else
            holder.end.setText("No Title");

        if(endtime !=null)
            holder.end.setText(endtime.toString());
        else
            holder.end.setText("No EndTime");

        if(starttime!=null)
            holder.start.setText(starttime.toString());
        else
            holder.start.setText("No StartTime");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}