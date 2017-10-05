package fr.wcs.battlegeek.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import fr.wcs.battlegeek.MainMenuActivity;
import fr.wcs.battlegeek.R;

/**
 * Created by Germain on 05/10/17.
 */

public class EndGameDefeatFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.smiley_defeat);
        builder.setTitle(R.string.end_game_fragment_title);
        builder.setMessage(R.string.end_game_defeat);
        builder.setPositiveButton(R.string.ok_defeat, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent victory = new Intent(getActivity(), MainMenuActivity.class);
                startActivity(victory);
            }
        });

        AlertDialog dialog = builder.show();
        TextView endGameMessage = dialog.findViewById(android.R.id.message);
        endGameMessage.setGravity(Gravity.CENTER);

        return dialog;
    }
}