package com.zfang.graphicdemo.refact.price;

import com.zfang.graphicdemo.refact.Movie;


public class RegularPrice extends Price {
    @Override
    public int getPriceCode() {
        return Movie.REGUALAR;
    }
}
