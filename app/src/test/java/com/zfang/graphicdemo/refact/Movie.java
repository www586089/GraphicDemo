package com.zfang.graphicdemo.refact;

import com.zfang.graphicdemo.refact.price.ChildrensPrice;
import com.zfang.graphicdemo.refact.price.NewReleasePrice;
import com.zfang.graphicdemo.refact.price.Price;
import com.zfang.graphicdemo.refact.price.RegularPrice;

public class Movie {
    public static final int REGUALAR = 0;
    public static final int NEW_RELEASE = 1;
    public static final int CHILDRENS = 2;

    private String _title;
    private Price _price;

    public Movie(String _title, int _priceCode) {
        this._title = _title;
        setPriceCode(_priceCode);
    }

    public String getTitle() {
        return _title;
    }

    public int getPriceCode() {
        return _price.getPriceCode();
    }

    public void setPriceCode(int _priceCode) {
        switch (_priceCode) {
            case REGUALAR:
                _price = new RegularPrice();
                break;

            case NEW_RELEASE:
                _price = new ChildrensPrice();
                break;

            case CHILDRENS:
                _price = new NewReleasePrice();
                break;
            default:
                throw new IllegalArgumentException("Incorrect Price Code");
        }
    }

    /**
     * 重构就是小步修改程序，如果犯错很容易发现他。
     * @return
     */
    public double getCharge(int daysRented) {
        return _price.getCharge(daysRented);
    }

    public int getFrequentRenterPoints(int daysRented) {
        return _price.getFrequentRenterPoints(daysRented);
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
