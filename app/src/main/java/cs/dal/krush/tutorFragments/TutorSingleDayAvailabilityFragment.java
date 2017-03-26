package cs.dal.krush.tutorFragments;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cs.dal.krush.R;
import cs.dal.krush.adapters.CustomTutorDayTimeAdapter;
import cs.dal.krush.adapters.TutorDayTimeRowitem;
import cs.dal.krush.models.DBHelper;

/**
 * Created by greg on 19/03/17.
 */

public class TutorSingleDayAvailabilityFragment extends Fragment {

    private DBHelper db;
    private ListView lvTutorDaySchedule;
    private String year,month,day; // ex: 2017 03 20
    static int USER_ID;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutor_single_day_availability, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        db = new DBHelper(getActivity().getBaseContext());
        lvTutorDaySchedule=(ListView)getView().findViewById(R.id.lvTutorDaySchedule);

        String date = getArguments().getString("DATE");
        USER_ID = getArguments().getInt("USER_ID");

        //fetch custom app font
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"fonts/FredokaOne-Regular.ttf");

        loadSchedule(date);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Loads the ListView of the tutor's availability
     */
    public void loadSchedule(String date){
        ArrayList<TutorDayTimeRowitem> items = new ArrayList<TutorDayTimeRowitem>();
        TutorDayTimeRowitem newItem;

        Cursor rs;
        String time;

        String[] splitDate = date.split("[-]");

        year = splitDate[0];
        month = splitDate[1];
        day = splitDate[2];

        rs = db.availableTime.getByDay(year, month, day);
        try {
            while (rs.moveToNext()) {

                time = rs.getString(rs.getColumnIndex("start"));
                time += " - ";
                time += rs.getString(rs.getColumnIndex("end"));

                newItem = new TutorDayTimeRowitem(time);

                items.add(newItem);

                time = "";
            }
        } finally {
            rs.close();
        }

        CustomTutorDayTimeAdapter adapter = new CustomTutorDayTimeAdapter(
                getActivity(),
                R.layout.tutor_single_day_availability_row_layout,
                items,
                date,
                USER_ID);

        lvTutorDaySchedule.setAdapter(adapter);
    }

}