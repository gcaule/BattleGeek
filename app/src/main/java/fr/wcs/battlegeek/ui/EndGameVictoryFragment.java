package fr.wcs.battlegeek.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import fr.wcs.battlegeek.R;

/**
 * Created by Germain on 05/10/17.
 */

public class EndGameVictoryFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.smiley_happy);
        builder.setTitle(R.string.end_game_fragment_title);
        builder.setMessage(R.string.end_game_victory);
        builder.setPositiveButton(R.string.ok_victory, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        builder.create();
        AlertDialog dialog = builder.show();

        TextView endGameMessage = dialog.findViewById(android.R.id.message);
        endGameMessage.setGravity(Gravity.CENTER);

        return dialog;
    }
}