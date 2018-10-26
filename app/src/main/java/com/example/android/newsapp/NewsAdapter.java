package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News>{


    public NewsAdapter(Context context, ArrayList<News> newsArticles){
        super(context, 0, newsArticles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if (convertView == null){
            listItemView = LayoutInflater.from( getContext()).inflate( R.layout.news_list_item,parent,false );
        }

        News currentArticle = getItem( position );

        assert currentArticle != null;
        String newsSection = currentArticle.getSection();
        TextView sectionNameView;
        sectionNameView = listItemView.findViewById( R.id.section );
        sectionNameView.setText( newsSection );

        String newsTitle = currentArticle.getTitle();
        TextView titleView = listItemView.findViewById( R.id.title );
        titleView.setText( newsTitle );

        String formattedDate = formatDate(currentArticle.getDate());
        TextView dateView = listItemView.findViewById( R.id.date );
        dateView.setText( formattedDate );

        String newsAuthor = currentArticle.getAuthor() + " ";
        TextView authorView = listItemView.findViewById( R.id.author );
        authorView.setText( newsAuthor );

        return listItemView;

    }

    private String formatDate(String date){
        final SimpleDateFormat inputParser = new SimpleDateFormat( "yyyy-MM-dd'HH:mm:ss'Z'", Locale.getDefault() );
        Date date_out = null;
        try{
            date_out = inputParser.parse( date );
        } catch (final ParseException e){
            e.printStackTrace();
        }
        final SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM dd ''yy", Locale.US  );
        return outputFormatter.format( date_out );
    }

}
