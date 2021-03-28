package com.zfang.graphicdemo.refact.price;

import com.zfang.graphicdemo.refact.Movie;

public class NewReleasePrice extends Price {
    @Override
    int getPriceCode() {
        return Movie.NEW_RELEASE;
    }
}
