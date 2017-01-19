package hakan_akkurt_matthias_will.de.to_do_list.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import hakan_akkurt_matthias_will.de.to_do_list.R;
import hakan_akkurt_matthias_will.de.to_do_list.dialogs.listener.OnNumberPicketListener;

/**
 * Created by Hakan Akkurt on 19.01.2017.
 */

public class NumberPickerDialogFragment extends DialogFragment {

    private static final String MAX_KEY = "max";
    private static final String MIN_KEY = "min";

    private NumberPicker picker;

    private OnNumberPicketListener listener;

    public static NumberPickerDialogFragment newInstance(final int max, final int min) {
        Bundle args = new Bundle();

        args.putInt(MAX_KEY, max);
        args.putInt(MIN_KEY, min);
        NumberPickerDialogFragment fragment = new NumberPickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(final OnNumberPicketListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //TODO kann ausgelagert werde und in newInstance übergeben werden
        builder.setTitle("NumberPicker");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                if(listener != null){
                    listener.onNumberPicket(true, picker.getValue(), NumberPickerDialogFragment.this);
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                if(listener != null){
                    listener.onNumberPicket(false, picker.getValue(), NumberPickerDialogFragment.this);
                }
                dialogInterface.dismiss();
            }
        });

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_number_picker, null);

        this.picker = (NumberPicker) v.findViewById(R.id.numberPicker);

        this.picker.setMaxValue(getArguments().getInt(MAX_KEY));
        this.picker.setMinValue(getArguments().getInt(MIN_KEY));

        //TODO fill number picker with min and max values

        builder.setView(v);

        return builder.create();
    }
}
