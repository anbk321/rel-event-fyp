package com.example.fyptrial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public List<AllEventsModel> eventList;
    private ClickListener clickListener;

    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {

        void iconRatingBarOnClick( int position, int rating);
    }

    Context c;
    public MyAdapter(Context c, ArrayList<AllEventsModel> listData) {
        this.c = c;
        this.eventList = listData;
    }

    public MyAdapter(List<AllEventsModel> eventList, ClickListener clickListener, MyAdapterListener listener)
    {
        this.eventList = new ArrayList<>();
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

    public interface ClickListener
    {
        void onTouch(int position);
        void onLongTouch(int position);
    }

}
