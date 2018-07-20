package app.adria.com.fbphotosexporting.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.adria.com.fbphotosexporting.R;
import app.adria.com.fbphotosexporting.entities.Photo;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MViewHolder> {

    private ArrayList<Photo> data;
    private ItemClickedListener itemClickedListener;

    public MAdapter() {
    }

    public MAdapter(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        MViewHolder holder = new MViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MViewHolder holder, int position) {
        final Photo photo = data.get(position);

        //Loading the photo by link using Picasso library
        Picasso.get()
                .load(photo.getUrl())
                .into(holder.ivPhoto);

        holder.tvName.setText(photo.getName());
        holder.tvName.setVisibility(photo.getName().isEmpty() ? View.GONE : View.VISIBLE);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickedListener != null) {
                    itemClickedListener.clicked(holder, photo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    // Set new data and refresh the recycler
    public void setData(ArrayList<Photo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    public class MViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;
        TextView tvName;

        public MViewHolder(View v) {
            super(v);

            ivPhoto = v.findViewById(R.id.iv_photo);
            tvName = v.findViewById(R.id.tv_name);
        }
    }

    public interface ItemClickedListener {
        void clicked(@NonNull MViewHolder holder, @NonNull Photo photo);
    }
}
