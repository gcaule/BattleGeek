package fr.wcs.battlegeek.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.utils.Utils;

public class CustomListAdapter extends BaseAdapter {

    private  Context mContext;
    private LayoutInflater  inflater;
    private List<PlayerModel> mPlayerModelItems;

    public CustomListAdapter(Context context, List<PlayerModel> playerModelItems) {
        this.mContext = context;
        this.mPlayerModelItems = playerModelItems;
    }

    @Override
    public int getCount() {
        return mPlayerModelItems.size();
    }

    @Override
    public Object getItem(int location) {
        return mPlayerModelItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View scoreView, ViewGroup parent) {
        ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (scoreView == null) {

            scoreView = inflater.inflate(R.layout.list_row, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) scoreView.findViewById(R.id.textViewName);
            holder.name.setTextColor(Color.parseColor("#FFEE00"));
            holder.ratio = (TextView) scoreView.findViewById(R.id.textViewRatio);
            holder.ratio.setTextColor(Color.parseColor("#FFEE00"));
            holder.bestTime = (TextView) scoreView.findViewById(R.id.textViewBestTime);
            holder.bestTime.setTextColor(Color.parseColor("#FFEE00"));
            holder.shotsCount = (TextView) scoreView.findViewById(R.id.textViewShotsCount);
            holder.shotsCount.setTextColor(Color.parseColor("#FFEE00"));
            holder.levelGames = (TextView) scoreView.findViewById(R.id.textViewLevelGames);
            holder.levelGames.setTextColor(Color.parseColor("#FFEE00"));

            scoreView.setTag(holder);

        } else {
            holder = (ViewHolder) scoreView.getTag();
        }

        try {
            final PlayerModel m = mPlayerModelItems.get(position);
            String level = PlayerModel.getComparatorLevel().toString();
            holder.name.setText(m.getName());
            holder.ratio.setText(m.getRatio().get(level).toString() + "%");
            long bestTime = m.getBestTime().get(level.toString());
            holder.bestTime.setText(bestTime != 2_147_483_647L ? Utils.timeFormat(bestTime) : "-");
            int shotsCount = m.getBestShotsCount().get(level.toString());
            holder.shotsCount.setText(shotsCount != 2_147_483_647 ? String.valueOf(shotsCount) : "-");
            holder.levelGames.setText(m.getGameParts().get(level).toString());
        }
        catch (Exception e){}

        return scoreView;
    }

    static class ViewHolder {

        TextView name;
        TextView ratio;
        TextView bestTime;
        TextView shotsCount;
        TextView levelGames;
    }

}