package com.oddlyspaced.np.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Utils.ColorLevel;

import java.util.ArrayList;

public class BatteryColorAdapter extends RecyclerView.Adapter<BatteryColorAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ColorLevel> list;
    private onTouchColorLevel listener;

    public BatteryColorAdapter(Context context, ArrayList<ColorLevel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            listener = (onTouchColorLevel) parent.getContext();
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement on touch listener!");
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_color_list, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ColorLevel item = list.get(position);
        holder.color.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.start.setText(item.getStartLevel() + "%");
        holder.end.setText(item.getEndLevel() + "%");
        holder.touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTouchItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView start, end;
        public View color, touch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            start = itemView.findViewById(R.id.txRecyclerViewStartPercentage);
            end = itemView.findViewById(R.id.txRecyclerViewEndPercentage);
            color = itemView.findViewById(R.id.viewRecyclerViewColor);
            touch = itemView.findViewById(R.id.viewRecyclerViewTouch);
        }
    }

    public interface onTouchColorLevel{
        void onTouchItem(int position);
    }

}
