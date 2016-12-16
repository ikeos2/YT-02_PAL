package edu.iastate.pal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by evanl on 10/27/2016.
 */

public class NewCourseDialogFragment extends DialogFragment {
    private static final String DAYS_KEY = "days";
    private ArrayList<Integer> mSelectedItems;
    private SelectDaysDialogListener mListener;
    private boolean[] checkItems;


    static NewCourseDialogFragment newInstance(ArrayList<Integer> arrayList) {
        NewCourseDialogFragment dialogFragment = new NewCourseDialogFragment();

        Bundle args = new Bundle();
        args.putIntegerArrayList(DAYS_KEY, arrayList);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    public interface SelectDaysDialogListener {
        void onDialogPositiveClick(ArrayList<Integer> arrayList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save the state of the checkboxes
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(DAYS_KEY, mSelectedItems);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkItems = new boolean[5];
    }

    /**
     * Used to create a listener for dialog
     * @param activity
     *      Activity used to attach listener
     */
    @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the SelectDialogListener so we can send events to the host
                mListener = (SelectDaysDialogListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement SelectDaysDialogListener");
            }
        }

    /**
     * Creates the checkbox dialog and saves the current state of the checkboxes
     * @return
     *      Returns a Dialog which the calling activity will use to show
     *
     */
    @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            if((mSelectedItems = getArguments().getIntegerArrayList(DAYS_KEY)) == null) {
                mSelectedItems = new ArrayList<>();
            }

            for(int i = 0; i < mSelectedItems.size(); i++){
                    checkItems[mSelectedItems.get(i)] = true;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Select Days")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(R.array.s_activity_newCourse_daysOfTheWeek, checkItems,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {

                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                    // Set the action buttons
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            Collections.sort(mSelectedItems);
                            mListener.onDialogPositiveClick(mSelectedItems);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }
}

