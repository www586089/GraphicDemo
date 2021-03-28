package com.zfang.graphicdemo.refact;

public class Rental {
    private Movie _movie;
    private int _daysRented;


    public Rental(Movie _movie, int _daysRented) {
        this._movie = _movie;
        this._daysRented = _daysRented;
    }

    public Movie getMovie() {
        return _movie;
    }

    public int getDaysRented() {
        return _daysRented;
    }

    /**
     * 重构就是小步修改程序，如果犯错很容易发现他。
     * @return
     */
    public double getCharge() {
        return _movie.getCharge(_daysRented);
    }

    public int getFrequentRenterPoints() {
        return _movie.getFrequentRenterPoints(_daysRented);
    }
}
