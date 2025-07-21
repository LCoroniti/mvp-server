package com.tus.traunreut;

import com.tus.traunreut.scraper.DataFetchException;

public interface IScraper<T> {
    T fetchData() throws DataFetchException;
}
