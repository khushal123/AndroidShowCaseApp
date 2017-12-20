package com.purpletealabs.sephora.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.purpletealabs.sephora.R;
import com.purpletealabs.sephora.dtos.Book;

public class BookViewModel extends BaseObservable {
    private String thumbnail;

    private String bookTitle;

    private String publisher;

    private String authors;

    private float rating;

    BookViewModel(Book book) {
        this.thumbnail = book.getVolumeInfo().getImageLinks() == null ? null : book.getVolumeInfo().getImageLinks().getThumbnail();
        this.bookTitle = book.getVolumeInfo().getTitle();
        this.publisher = book.getVolumeInfo().getPublisher();
        this.authors = book.getVolumeInfo().getAuthors();
        this.rating = book.getVolumeInfo().getAverageRating();
    }

    @BindingAdapter({"app:thumbnail"})
    public static void loadImage(SimpleDraweeView view, String imageUrl) {
        Uri uri;
        if (TextUtils.isEmpty(imageUrl)) {
            uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.no_image)).build();
        } else {
            uri = Uri.parse(imageUrl);
        }
        view.setImageURI(uri);
    }

    @Bindable
    public String getThumbnail() {
        return thumbnail;
    }

    @Bindable
    public String getBookTitle() {
        return bookTitle;
    }

    @Bindable
    public String getPublisher() {
        return publisher;
    }

    @Bindable
    public String getAuthors() {
        return authors;
    }

    @Bindable
    public float getRating() {
        return rating;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        notifyChange();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        notifyChange();
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        notifyChange();
    }

    public void setAuthors(String authors) {
        this.authors = authors;
        notifyChange();
    }

    public void setRating(float rating) {
        this.rating = rating;
        notifyChange();
    }
}
