package com.mantey.newsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mantey.newsapp.R;
import com.mantey.newsapp.model.Story;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<Story> {

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<Story> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Story currentStory = getItem(position);

        TextView sectionName = (TextView) convertView.findViewById(R.id.news_section);
        TextView title = (TextView) convertView.findViewById(R.id.news_title);
        TextView date = (TextView) convertView.findViewById(R.id.news_published_date);
        TextView time = (TextView) convertView.findViewById(R.id.news_published_time);
        TextView author = (TextView) convertView.findViewById(R.id.news_author);

        if (currentStory != null) {

            sectionName.setText(currentStory.getSection());
            title.setText(currentStory.getTitle());

            //We read the date format form the json response as "yyyy-MM-dd'T'HH:mm:ss'Z'"
            //and convert it to a simple readable format
            SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date newsDate = readFormat.parse(currentStory.getDatePublished());
                String formattedDate = formatDate(newsDate);
                date.setText(formattedDate);

                String formattedTime = formatTime(newsDate);
                time.setText(formattedTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //We check to see if there's an author for the story
            if (TextUtils.isEmpty(currentStory.getAuthor())) {
                author.setVisibility(View.GONE);
            } else {
                String author_name = getContext().getResources().getString(R.string.author_name, currentStory.getAuthor());
                author.setText(author_name);
            }

        }


        return convertView;
    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
