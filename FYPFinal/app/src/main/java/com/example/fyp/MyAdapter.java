package com.example.fyp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    public List<AllEventsModel> eventList;
    public List<AllEventsModel> eventListFull = new ArrayList<>();
    private ClickListener clickListener;

    public MyAdapterListener onClickListener;


    public interface MyAdapterListener {

        void iconRatingBarOnClick( int position, int rating);
    }

//    Context c;
//    public MyAdapter(Context c, ArrayList<AllEventsModel> listData) {
//        this.c = c;
//        this.eventList = listData;
//        this.eventListFull = new ArrayList<>(this.eventList);
//    }
//    public MyAdapter(ArrayList<AllEventsModel> listData) {
//        this.eventList = listData;
//        this.eventListFull = new ArrayList<>(this.eventList);
//    }

    public MyAdapter(List<AllEventsModel> eventList, ClickListener clickListener, MyAdapterListener listener)
    {
//        this.eventList = new ArrayList<>();
        this.eventList = eventList;
        this.clickListener = clickListener;
        onClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_holder,parent,false);
        return new MyViewHolder(myView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(eventList.get(position).getTitle());
        holder.time.setText(eventList.get(position).getTime());
        if(eventList.get(position).getRating()!=-1){
            holder.like_bt.setRating(eventList.get(position).getRating());
        }

        if(!eventList.get(position).getPicture().isEmpty())
            Picasso.get().load(eventList.get(position).getPicture()).into(holder.pic);
    }

    @Override
    public int getItemCount() {

        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ClickListener clickListener;
        TextView title, time;
        ImageView pic;
        public RatingBar like_bt;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(@NonNull View view, final ClickListener clickListener) {
            super(view);
            title = view.findViewById(R.id.title_view);
            pic = view.findViewById(R.id.pic);
            time = view.findViewById(R.id.time_view);
            like_bt = view.findViewById(R.id.like_bt);
            this.clickListener = clickListener;
            listenerRef = new WeakReference<>(clickListener);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            //Log.d("These are: ", "My Events: " +  eventList.get(1));


            like_bt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    onClickListener.iconRatingBarOnClick(getAdapterPosition(), (int) rating);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == like_bt.getId()) {
                Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
           //clickListener.onTouch(getAdapterPosition());
           listenerRef.get().onTouch(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onLongTouch(getAdapterPosition());
            return true;
        }
    }

    @Override
    public Filter getFilter() {
        return eventsFilter;
    }

    private Filter eventsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AllEventsModel> filteredList = new ArrayList<>();

            for(int i = 0; i < eventListFull.size(); i++)
            {
                Log.e("Event Title-FULL-1: ", eventListFull.get(i).getTitle());
                Log.e("Event Category-FULL-1: ", eventListFull.get(i).getType());
            }

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eventListFull);

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                Log.e("In AD-1", "HEYYYYYYYYYYY");
                for(AllEventsModel item : eventListFull) {

                    if(item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                Log.e("In AD-2", "HEYYYYYYYYYYY");
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.e("In AD-3", "HEYYYYYYYYYYY");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            for(int i = 0; i < eventListFull.size(); i++)
            {
                Log.e("Event Title-FULL-2: ", eventListFull.get(i).getTitle());
                Log.e("Event Category-FULL-2: ", eventListFull.get(i).getType());
            }

            for(int i = 0; i < eventList.size(); i++)
            {
                Log.e("Event Title: ", eventList.get(i).getTitle());
                Log.e("Event Category: ", eventList.get(i).getType());
            }
            eventList.clear();
            eventList.addAll((List) results.values);
            Log.e("In AD-4", "HEYYYYYYYYYYY");
            notifyDataSetChanged();
            Log.e("In AD-5", "HEYYYYYYYYYYY");
        }
    };


    public interface ClickListener
    {
        void onTouch(int position);
        void onLongTouch(int position);
    }

}
