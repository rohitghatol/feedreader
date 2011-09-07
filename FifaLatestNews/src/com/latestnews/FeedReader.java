package com.latestnews;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.latestnews.adapter.FeedListAdapter;
import com.latestnews.cache.ImageLoader;
import com.latestnews.cache.InMemoryUnlimitedImageCache;
import com.latestnews.cache.QueuedImageLoader;
import com.latestnews.model.FeedItem;
import com.latestnews.parser.FeedParser;
import com.latestnews.service.ApacheHttpService;
import com.latestnews.service.IFeedService;
import com.latestnews.service.ServerFeedServiceImpl;

public class FeedReader extends ListActivity {
	
	/**
	 * Image Loader used by this activity 
	 */
	private ImageLoader imageLoader = null;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imageLoader = new QueuedImageLoader();
		imageLoader.setImageCache(new InMemoryUnlimitedImageCache());

		// TODO replace with ServerFeedServiceImpl
		final IFeedService feedService = new ServerFeedServiceImpl();
		feedService.setHttpService(new ApacheHttpService());
		feedService.setFeedParser(new FeedParser());

		AsyncTask<String, Void, List<FeedItem>> asyncTask = new AsyncTask<String, Void, List<FeedItem>>() {
			private ProgressDialog progressDialog = new ProgressDialog(
					FeedReader.this);

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setTitle("Loading Fifa Feeds");
				if (!progressDialog.isShowing()) {
					progressDialog.show();
				}
			}

			@Override
			protected List<FeedItem> doInBackground(String... params) {

				return feedService.fetchLatestFeeds();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
			 */
			@Override
			protected void onPostExecute(List<FeedItem> feeds) {
				super.onPostExecute(feeds);
				if (progressDialog.isShowing()) {
					progressDialog.hide();
				}
				
				setListAdapter(new FeedListAdapter(getApplicationContext(),
						feeds, imageLoader));

			}

		};

		asyncTask.execute("http://www.fifa.com/newscentre/photo/rss.xml");

	}
}