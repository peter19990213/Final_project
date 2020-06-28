package com.example.final_project;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project.data.FinalContract;

public class MyCursorAdapter extends RecyclerView.Adapter<MyCursorAdapter.MyViewHolder> {

    private Cursor myCursor;
    private Context myContext;


    public MyCursorAdapter(Context myContext) {
        this.myContext = myContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(myContext)
                .inflate(R.layout.position_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int idIndex = myCursor.getColumnIndex(FinalContract.FinalEntry._ID);
        int longitudeIndex = myCursor.getColumnIndex(FinalContract.FinalEntry.COLUMN_longitude);
        int latitudeIndex = myCursor.getColumnIndex(FinalContract.FinalEntry.COLUMN_latitude);
        int nameIndex = myCursor.getColumnIndex(FinalContract.FinalEntry.COLUMN_name);

        myCursor.moveToPosition(position);

        final int id = myCursor.getInt(idIndex);
        float longitude = myCursor.getFloat(longitudeIndex);
        float latitude = myCursor.getFloat(latitudeIndex);
        String name = myCursor.getString(nameIndex);

        holder.idView.setText(Integer.toString(id));
        holder.itemView.setTag(id);
        holder.infoView.setText("          \t          " + Float.toString(longitude) + "\t          " + Float.toString(latitude) + "\n" + name);
    }

    @Override
    public int getItemCount() {
        if (myCursor == null) {
            return 0;
        }
        return myCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (myCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = myCursor;
        this.myCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView idView;
        TextView infoView;

        public MyViewHolder(View itemView) {
            super(itemView);

            idView = (TextView) itemView.findViewById(R.id.id_textview);
            infoView = (TextView) itemView.findViewById(R.id.info_textview);
        }
    }

}
