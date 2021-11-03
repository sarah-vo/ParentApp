package ca.cmpt276.parentapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import ca.cmpt276.parentapp.model.FlipCoin;

public class CoinSideSelectMessageFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Pick your side")
                .setMessage("It's (child name)'s turn! Please pick a side.")
                .setPositiveButton("heads", listener)
                .setNeutralButton("tails", listener)
                .create();
    }
}
