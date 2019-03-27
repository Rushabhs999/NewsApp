package com.example.newsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.Activity.WebviewFullscreenActivity;
import com.example.newsapp.Model.NewsListModel;
import com.example.newsapp.R;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NewsListModel> newsListModelArrayList = new ArrayList<>();

    public NewsListAdapter(Context context, ArrayList<NewsListModel> newsListModelArrayList) {
        this.context = context;
        this.newsListModelArrayList = newsListModelArrayList;
    }

    @NonNull
    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_news_list_item1, viewGroup, false);
        return new NewsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListAdapter.ViewHolder viewHolder, int position) {
        final NewsListModel model = newsListModelArrayList.get(position);
        viewHolder.tv_title.setText(model.getTitle());
        viewHolder.tv_authorName.setText(model.getAuthorName());
        viewHolder.tv_sourceName.setText(model.getSourceName());
        viewHolder.tv_description.setText(model.getDescription());
        RequestOptions requestOptions = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.loader)).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(model.getThumbnailURL()).apply(requestOptions).into(viewHolder.img_banner);

        viewHolder.ll_mainNewsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebviewFullscreenActivity.class);
                intent.putExtra("contentURL", model.getClickURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_mainNewsItem;
        private ImageView img_banner;
        private TextView tv_title, tv_sourceName, tv_authorName, tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_mainNewsItem = itemView.findViewById(R.id.ll_mainNewsItem);
            img_banner = itemView.findViewById(R.id.img_banner);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_sourceName = itemView.findViewById(R.id.tv_sourceName);
            tv_authorName = itemView.findViewById(R.id.tv_authorName);
            tv_description = itemView.findViewById(R.id.tv_description);

            tv_title.setSingleLine(true);
            tv_description.setMaxLines(2);
            tv_authorName.setSingleLine(true);
        }
    }
}
