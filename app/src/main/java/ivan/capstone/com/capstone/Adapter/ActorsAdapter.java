package ivan.capstone.com.capstone.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Actor;
import ivan.capstone.com.capstone.R;

/**
 * Created by Ivan on 19/02/2016.
 */
public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ViewHolder> {

    private int selectedItem;
    private List<Actor> actors;
    private int layout;

    public ActorsAdapter(List<Actor> items, int layoutData)
    {
        actors = items;

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
        holder.item = actors.get(position);
        String name = actors.get(position).getName();
        holder.viewName.setText(name);
        holder.viewRole.setText(actors.get(position).getRole());

        if (!holder.item.getImage_url().equals("")) {
            Picasso.with(holder.itemView.getContext())
                    .load(holder.item.getImage_url())
                    .fit().centerCrop()
                    .into(holder.viewMiniatura);
        }

    }

    @Override
    public int getItemCount() {
        if (actors != null) {
            return actors.size() > 0 ? actors.size() : 0;
        } else {
            return 0;
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder
             {
            public final TextView viewName;
            public final TextView viewRole;
            public final ImageView viewMiniatura;


        public Actor item;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            viewName = (TextView) view.findViewById(R.id.name_actor);
            viewRole = (TextView) view.findViewById(R.id.role_actor);
            viewMiniatura = (ImageView) view.findViewById(R.id.image_actor);

        }


    }







}
