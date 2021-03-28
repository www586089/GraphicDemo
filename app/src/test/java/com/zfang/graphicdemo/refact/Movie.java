package com.zfang.graphicdemo.refact;

public class Movie {
    public static final int REGUALAR = 0;
    public static final int NEW_RELEASE = 1;
    public static final int CHILDRENS = 2;

    private String _title;
    private int _priceCode;

    public Movie(String _title, int _priceCode) {
        this._title = _title;
        this._priceCode = _priceCode;
    }

    public String getTitle() {
        return _title;
    }

    public int getPriceCode() {
        return _priceCode;
    }

    public void setPriceCode(int _priceCode) {
        this._priceCode = _priceCode;
    }

    /**
     * 重构就是小步修改程序，如果犯错很容易发现他。
     * @return
     */
    public double getCharge(int daysRented) {
        double thisAmount = 0;
        //determine amounts for each line
        switch (getPriceCode()) {
            case Movie.REGUALAR:
                thisAmount += 2;
                if (daysRented > 2) {
                    thisAmount += (daysRented - 2) * 1.5;
                }
                break;

            case Movie.NEW_RELEASE:
                thisAmount += daysRented * 3;
                break;

            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if (daysRented > 3) {
                    thisAmount += (daysRented - 3) * 1.5;
                }
                break;
        }
        return thisAmount;
    }

    public int getFrequentRenterPoints(int daysRented) {
        if ((getPriceCode() == Movie.NEW_RELEASE) && daysRented > 1) {
            return 2;
        } else {
            return 1;
        }
    }
}
