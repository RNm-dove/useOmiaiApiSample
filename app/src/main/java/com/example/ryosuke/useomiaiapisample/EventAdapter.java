package com.example.ryosuke.useomiaiapisample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

/**
 * Created by ryosuke on 2018/02/19.
 */

class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private OnEventItemClickListener onEventItemClickListener;
    private List<OmiaiService.Result> items;

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView eventName;
        private final TextView eventDetail;
        private final ImageView eventImage;


        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventDetail = (TextView) itemView.findViewById(R.id.event_detail);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);
        }
    }

    public EventAdapter(Context context, OnEventItemClickListener onEventItemClickListener){

        this.context = context;
        this.onEventItemClickListener = onEventItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OmiaiService.Result item = getItemAt(position);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onEventItemClickListener.onEventItemClick(item);
            }
        });

        holder.eventName.setText(item.title);
        holder.eventDetail.setText(item.article);

        Glide.with(context)
                .load(item.image)
                .asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.eventImage){
                    @Override
            protected void setResource(Bitmap resource){
                        // 画像を丸く切り抜く
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.eventImage.setImageDrawable(circularBitmapDrawable);
                    }
        });

    }

    private OmiaiService.Result getItemAt(int position) {
        return items.get(position);
    }

    public void setItemAndRefresh(List<OmiaiService.Result> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    interface OnEventItemClickListener {

        void onEventItemClick(OmiaiService.Result item);
    }
}
