package com.oddlyspaced.np.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlyspaced.np.Interface.OnTouchColorLevel;
import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Utils.ColorLevel;

import java.util.ArrayList;

// RecyclerView Adapter for the color level list
public class BatteryColorAdapter extends RecyclerView.Adapter<BatteryColorAdapter.ViewHolder> {

    // Required items
    // ui context
    private Context context;
    // array list of items for colors
    private ArrayList<ColorLevel> list;
    // the onClick listener for the color items/levels
    private OnTouchColorLevel listener;

    // parametrized constructor
    public BatteryColorAdapter(Context context, ArrayList<ColorLevel> list) {
        this.context = context;
        this.list = list;
    }

    // Called when RecyclerView in drawn in UI
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // checking if listener overridden
        try {
            listener = (OnTouchColorLevel) parent.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement on touch listener!");
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_color_list, parent, false);
        return new ViewHolder(v);
    }

    // Called when data is bound to the items
    // --when the list items are being loaded--
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

    // constructor / defining class for the list items
    class ViewHolder extends RecyclerView.ViewHolder {

        // defining the modifiable items of the item view
        TextView start, end;
        View color, touch;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // giving context
            start = itemView.findViewById(R.id.txRecyclerViewStartPercentage);
            end = itemView.findViewById(R.id.txRecyclerViewEndPercentage);
            color = itemView.findViewById(R.id.viewRecyclerViewColor);
            touch = itemView.findViewById(R.id.viewRecyclerViewTouch);
        }
    }

}
