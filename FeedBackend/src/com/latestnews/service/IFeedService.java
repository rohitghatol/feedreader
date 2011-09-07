package com.latestnews.service;

import java.util.List;

import com.latestnews.model.FeedItem;

/**
 * POJO Service Interface to fetch fifa feeds synchronously.
 * @author rohit
 *
 */
public interface IFeedService{

	public List<FeedItem> fetchLatestFeeds();
}
