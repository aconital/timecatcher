package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpsc.timecatcher.NewEditTaskFragment;
import com.cpsc.timecatcher.helper.ItemTouchHelperViewHolder;
import com.cpsc.timecatcher.R;
import com.cpsc.timecatcher.helper.ItemTouchHelperAdapter;
import com.cpsc.timecatcher.model.Category;
import com.cpsc.timecatcher.model.Task;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by hroshandel on 2016-02-09.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private List<Task> taskList;
    private FragmentActivity mContext;

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
        public LinearLayout tagsLayout;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            end = (TextView) view.findViewById(R.id.end);
            start = (TextView) view.findViewById(R.id.start);
            tagsLayout=(LinearLayout) view.findViewById(R.id.tags);
            logo=(ImageView) view.findViewById(R.id.logo);

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
    public TaskAdapter(List<Task> taskList, FragmentActivity c) {
        this.taskList = taskList;
        mContext=c;
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
        Category category;
        List<Category> categories;
        try {
            category = task.getCategory();
        } catch (ParseException | NullPointerException e) {
            category = null;
        }
        holder.tagsLayout.removeAllViews();
        String firstCat = category != null ? category.getTitle() : "";
        if (category != null) {
            TextView cat =new TextView(mContext);
            cat.setText(category.getTitle());
            cat.setPadding(25,5,25,5);
            cat.setGravity(Gravity.CENTER);
            cat.setBackgroundResource(R.drawable.bg_round);
            cat.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            holder.tagsLayout.addView(cat);
        }

        switch (firstCat) {
            case "Gym":
                holder.logo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.gym));
                break;
            case "School":
                holder.logo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.school));
                break;
            case "Housework":
                holder.logo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.housework));
                break;
            case "Family":
                holder.logo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.family));
                break;
            case "Work":
                holder.logo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.work));
                break;
            default:
                holder.logo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.none));
                break;
        }


        int duration=  task.getTotalTime();
        int dur_hr= duration/60;
        int dur_min = duration %60;
        if(dur_hr<1)
            holder.end.setText(dur_min+" min");
        else
            if(dur_min ==0)
            holder.end.setText(dur_hr+" h ");
            else
            holder.end.setText(dur_hr+" h "+dur_min+" min");

        Date starttime=task.getStartTime();


        String title=   task.getTitle();
        if(title !=null)
            holder.title.setText(title);


       Calendar c= Calendar.getInstance();
        c.setTime(starttime);
        if(!(c.get(Calendar.HOUR_OF_DAY) == 0 && c.get(Calendar.MINUTE) == 0))
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
            holder.start.setText("Unscheduled");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}