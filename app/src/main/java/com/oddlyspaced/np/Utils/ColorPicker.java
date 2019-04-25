package com.oddlyspaced.np.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.oddlyspaced.np.R;


/**
 * https://www.youtube.com/watch?v=ARezg1D9Zd0
 */

public class ColorPicker extends AppCompatDialogFragment {

    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;
    private ColorPickerListener listener;
    private View dColor;
    private EditText editTextColor;
    public String color;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ColorPickerListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement editTextColor picker listener!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color_picker, null);

        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onColorSet(color);
            }
        });

        dColor = view.findViewById(R.id.viewDialogColor);
        red = view.findViewById(R.id.sbRed);
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorString();
                updateColorView(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        green = view.findViewById(R.id.sbGreen);
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorString();
                updateColorView(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blue = view.findViewById(R.id.sbBlue);
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorString();
                updateColorView(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editTextColor = view.findViewById(R.id.editTextColor);
        editTextColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    dColor.setBackgroundColor(Color.parseColor(s.toString()));
                    color = s.toString();
                    updateColorSeekbar(color);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid Color!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updateColorSeekbar(color);


        return builder.create();
    }

    public interface ColorPickerListener {
        void onColorSet(String color);
    }

    private void updateColorString () {
        String r = Integer.toHexString(red.getProgress());
        if (r.length() == 1)
            r = "0" + r;
        String g = Integer.toHexString(green.getProgress());
        if (g.length() == 1)
            g = "0" + g;
        String b = Integer.toHexString(blue.getProgress());
        if (b.length() == 1)
            b = "0" + b;
        color =  "#" + r + g + b;
    }

    private void updateColorView(String c) {
        dColor.setBackgroundColor(Color.parseColor(c));
        editTextColor.setText(c);
    }

    private void updateColorSeekbar(String c){
        red.setProgress(Integer.parseInt(c.substring(1, 3), 16));
        green.setProgress(Integer.parseInt(c.substring(3, 5), 16));
        blue.setProgress(Integer.parseInt(c.substring(5), 16));
    }
}
