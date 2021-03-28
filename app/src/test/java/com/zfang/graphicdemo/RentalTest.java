package com.zfang.graphicdemo;

import com.zfang.graphicdemo.refact.Customer;
import com.zfang.graphicdemo.refact.Movie;
import com.zfang.graphicdemo.refact.Rental;

public class RentalTest {

    private static RentalTest instance = new RentalTest();

    public static void main(String[] args) {
        instance.test();
    }

    private void test() {
        Customer customer = new Customer("Jack");
        customer.addRental(new Rental(new Movie("<童话大王>", Movie.CHILDRENS), 1));
        customer.addRental(new Rental(new Movie("<天外飞仙>", Movie.REGUALAR), 1));
        customer.addRental(new Rental(new Movie("<爸爸去哪儿>", Movie.NEW_RELEASE), 1));

        System.out.println(customer.statement());
    }
}
