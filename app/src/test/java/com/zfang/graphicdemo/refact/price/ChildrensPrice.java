package com.zfang.graphicdemo.refact.price;

import com.zfang.graphicdemo.refact.Movie;

public class ChildrensPrice extends Price {

    @Override
    int getPriceCode() {
        return Movie.CHILDRENS;
    }
}
