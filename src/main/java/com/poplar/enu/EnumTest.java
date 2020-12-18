package com.poplar.enu;

import java.util.SplittableRandom;

/**
 * Create BY poplar ON 2020/11/26
 */

public class EnumTest{

    public static void main(String[] args) {
        //System.out.println(ThreadLocalRandom.current().nextInt(2));
        SplittableRandom random=new SplittableRandom();
        System.out.println(random.nextInt(5));
    }
}

enum CoffeeType {
    ESPRESSO, POUR_OVER, FRENCH_PRESS, LATTE, FLAT_WHITE
}