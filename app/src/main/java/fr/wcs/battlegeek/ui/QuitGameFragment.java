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
 * Created by apprenti on 12/10/17.
 */

public class QuitGameFragment extends DialogFragment {

    private OnCancelListener listener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.smiley_neutral1);
        builder.setTitle(R.string.quit_game_alert_title);
        builder.setMessage(R.string.quit_game_alert_message);
        builder.setPositiveButton(R.string.ok_exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
        builder.setNegativeButton(R.string.no_exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(listener != null) {
                    listener.onCancel();
                }
            }
        });
        builder.create();
        AlertDialog dialog = builder.show();

        TextView quitGameMessage = dialog.findViewById(android.R.id.message);
        quitGameMessage.setGravity(Gravity.CENTER);

        return dialog;
    }

    public interface OnCancelListener {
        public void onCancel();
    }

    public void setOnCancelListener(OnCancelListener listener){
        this.listener = listener;
    }
}
