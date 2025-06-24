package com.bonrix.common.test;

import java.util.HashSet;

public class SetHashcodeDuplicationExample {

	
	public static void main(String a[]){
        
        HashSet<Price> lhm = new HashSet<Price>();
        lhm.add(new Price("Banana", 20));
        lhm.add(new Price("Apple", 20));
        lhm.add(new Price("Orange", 30));
        lhm.add(new Price("xyz", 30));
        for(Price pr:lhm){
            System.out.println(pr);
        }
        Price duplicate = new Price("Banana", 20);
        System.out.println("inserting duplicate object...");
        lhm.add(duplicate);
        System.out.println("After insertion:");
        for(Price pr:lhm){
            System.out.println(pr);
        }
    }
}
