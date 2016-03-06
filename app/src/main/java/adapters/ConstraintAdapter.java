package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cpsc.timecatcher.helper.Constants;
import com.cpsc.timecatcher.R;
import com.cpsc.timecatcher.model.Constraint;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by yutongluo on 2/18/16.
 */
public class ConstraintAdapter extends ArrayAdapter<Constraint> {

    private List<Constraint> constraintList;

    public ConstraintAdapter(Context context, int resource, List<Constraint> constraints) {
        super(context, resource, constraints);
        constraintList = constraints;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.constraint_list_row, null);
        }

        final Constraint constraint = getItem(position);
        if ( constraint != null ) {
            TextView operatorTextView = (TextView) v.findViewById(R.id.constraint_list_operator);
            TextView otherTaskTextView = (TextView) v.findViewById(R.id.constraint_list_other);
            Button constraintDelete = (Button) v.findViewById(R.id.constraint_delete);
            String operatorText = "";
            switch(constraint.getOperator()) {
                case BEFORE:
                    operatorText = "Before";
                    break;
                case AFTER:
                    operatorText = "After";
                    break;
            }
            operatorTextView.setText(operatorText);
            try {
                otherTaskTextView.setText(constraint.getOther().getTitle());
            } catch (ParseException e) {
                Log.e(Constants.NEW_CONSTRAINT_TAG, "Invalid state: Other cannot be found! " );
                Log.e(Constants.NEW_CONSTRAINT_TAG, e.getLocalizedMessage());
            }

            constraintDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    constraintList.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            Log.w(Constants.NEW_EDIT_TASK_TAG, "Constraint was null");
        }
        return v;
    }
}
