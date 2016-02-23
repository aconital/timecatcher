package adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpsc.timecatcher.helper.ItemTouchHelperViewHolder;
import com.cpsc.timecatcher.R;
import com.cpsc.timecatcher.helper.ItemTouchHelperAdapter;
import com.cpsc.timecatcher.model.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by hroshandel on 2016-02-09.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private List<Task> taskList;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Task prev = taskList.remove(fromPosition);
        taskList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //remove task
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements
            ItemTouchHelperViewHolder {
        public TextView title, end, start;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            end = (TextView) view.findViewById(R.id.end);
            start = (TextView) view.findViewById(R.id.start);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
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

        boolean isFixed = task.getFixed();
        //different color for fixed tasks
        if(isFixed)
        {
            holder.start.setBackgroundResource(R.color.deep);
            holder.end.setBackgroundResource(R.color.deep);
        }

        Date endtime= task.getEndTime();
        Date starttime=task.getStartTime();

        String title=   task.getTitle();
        if(title !=null)
            holder.title.setText(title);
        else
            holder.end.setText("No Title");

        if(endtime !=null) {
            Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
            calendar.setTime(endtime);   // assigns calendar to given date
            String minute= Integer.toString(calendar.get(Calendar.MINUTE));
            String hour=Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
            //changing 0 to 00
            if(calendar.get(Calendar.MINUTE) < 10)
            {
                minute= "0"+calendar.get(Calendar.MINUTE);
            }
            if(calendar.get(Calendar.HOUR_OF_DAY) < 10)
            {
                hour= "0"+calendar.get(Calendar.HOUR_OF_DAY);
            }

            holder.end.setText(hour+":"+ minute);
        }
        else
            holder.end.setText("No EndTime");

        if(starttime!=null)
        {
            Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
            calendar.setTime(starttime);   // assigns calendar to given date
            String minute= Integer.toString(calendar.get(Calendar.MINUTE));
            String hour=Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));

            if(calendar.get(Calendar.MINUTE) < 10)
            {
                minute= "0"+calendar.get(Calendar.MINUTE);
            }
            if(calendar.get(Calendar.HOUR_OF_DAY) < 10)
            {
                hour= "0"+calendar.get(Calendar.HOUR_OF_DAY);
            }

            holder.start.setText(hour+":"+minute);
        }
        else
            holder.start.setText("No StartTime");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}