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
        StringBuilder result = new StringBuilder("Rental Record for " + getName() + "\n");
        int size = _rentals.size();
        /**
         * 临时变量容易引发问题
         * 1 大量参数传递
         * 2 混淆或者跟丢他们
         */
        for (int i = 0; i < size; i++) {
            Rental each = _rentals.get(i);
            // show figures for this rental
            result.append("\t").append(each.getMovie().getTitle()).append("\t").append(each.getCharge()).append("\n");
        }

        // add footer lines
        result.append("Amount owed is ").append(getTotalCharge()).append("\n");
        result.append("Your earned ").append(getTotalFrequentRenterPoints()).append("frequent renter points");

        return result.toString();
    }

    private double getTotalCharge() {
        double result = 0;
        for (Rental each : _rentals) {
            result += each.getCharge();
        }

        return result;
    }

    private int getTotalFrequentRenterPoints() {
        int result = 0;
        for (Rental each: _rentals) {
            result += each.getFrequentRenterPoints();
        }

        return result;
    }
}
