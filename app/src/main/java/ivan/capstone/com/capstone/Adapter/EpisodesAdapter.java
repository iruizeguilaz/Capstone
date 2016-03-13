package ivan.capstone.com.capstone.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Episode;
import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.MyApplication;
import ivan.capstone.com.capstone.R;

/**
 * Created by Ivan on 19/02/2016.
 */
public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private int selectedItem;
    private List<Episode> episodes;
    private int layout;

    public EpisodesAdapter(List<Episode> items, int layoutData)
    {
        episodes = items;

        this.layout = layoutData;
        selectedItem = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = episodes.get(position);
        String name = episodes.get(position).getEpisode_number() + ". " + episodes.get(position).getName();
        holder.viewTitle.setText(name);
        holder.viewReleaseDate.setText(episodes.get(position).getDate());

        String rating;
        if (episodes.get(position).getVotes().equals(("0"))) {
            rating = episodes.get(position).getVotes()+ " "
                    + MyApplication.getContext().getResources().getString(R.string.votes_serie);
        } else {
            rating = episodes.get(position).getRating()
                    + MyApplication.getContext().getResources().getString(R.string.rating_serie)
                    + "  " + episodes.get(position).getVotes()+ " "
                    + MyApplication.getContext().getResources().getString(R.string.votes_serie);
        }
        holder.viewRating.setText(rating);
        holder.viewOverview.setText(episodes.get(position).getOverview());
        if (!holder.item.getImage_url().equals("")) {
            Picasso.with(holder.itemView.getContext())
                    .load(holder.item.getImage_url())
                    .fit().centerCrop()
                    .into(holder.viewMiniatura);
        }else {
            Picasso.with(holder.itemView.getContext())
                    .load(R.drawable.no_image)
                    .fit().centerCrop()
                    .into(holder.viewMiniatura);
        }

    }

    @Override
    public int getItemCount() {
        if (episodes != null) {
            return episodes.size() > 0 ? episodes.size() : 0;
        } else {
            return 0;
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder
             {
            public final TextView viewTitle;
            public final TextView viewReleaseDate;
            public final ImageView viewMiniatura;
            public final TextView viewRating;
            public final TextView viewOverview;

        public Episode item;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            viewTitle = (TextView) view.findViewById(R.id.name_episode);
            viewReleaseDate = (TextView) view.findViewById(R.id.date_episode);
            viewMiniatura = (ImageView) view.findViewById(R.id.image_episode);
            viewRating = (TextView) view.findViewById(R.id.rating_episode);
            viewOverview = (TextView) view.findViewById(R.id.name_overview_episode);
        }


    }







}
