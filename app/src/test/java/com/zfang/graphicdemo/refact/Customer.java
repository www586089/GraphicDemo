package com.zfang.graphicdemo.refact;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String _name;
    private List<Rental> _rentals = new ArrayList<>();

    public Customer(String _name) {
        this._name = _name;
    }

    public void addRental(Rental rental) {
        _rentals.add(rental);
    }

    public String getName() {
        return _name;
    }

    public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;
        StringBuilder result = new StringBuilder("Rental Record for " + getName() + "\n");
        int size = _rentals.size();
        /**
         * 临时变量容易引发问题
         * 1 大量参数传递
         * 2 混淆或者跟丢他们
         */
        for (int i = 0; i < size; i++) {
            Rental each = _rentals.get(i);

            // add frequent renter points
            frequentRenterPoints++;
            // add bonus for a two day new release rental
            if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) && each.getDaysRented() > 1) {
                frequentRenterPoints++;
            }

            // show figures for this rental
            result.append("\t").append(each.getMovie().getTitle()).append("\t").append(each.getCharge()).append("\n");

            totalAmount += each.getCharge();
        }

        // add footer lines
        result.append("Amount owed is ").append(totalAmount).append("\n");
        result.append("Your earned ").append(frequentRenterPoints).append("frequent renter points");

        return result.toString();
    }
}
