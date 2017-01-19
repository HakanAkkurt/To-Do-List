package hakan_akkurt_matthias_will.de.to_do_list.dialogs.listener;

import android.app.DialogFragment;

import hakan_akkurt_matthias_will.de.to_do_list.dialogs.NumberPickerDialogFragment;

/**
 * Created by Hakan Akkurt on 19.01.2017.
 */

public interface OnNumberPicketListener {
    void onNumberPicket(final boolean numberPicked, final int number, final NumberPickerDialogFragment dialogFragment);
}
