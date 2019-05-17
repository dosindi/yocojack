/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nyakundid.controller;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author nyakundid
 *
 * @Description sort deckA and deckB array in descending order
 *
 */
public class Rule2 {

    private Logger log = Logger.getLogger(Rule2.class.getName());
    public Rule2() {
    }

    public int[] scoreRank(String deck) {

        String[] rack = deck.split(" ");
        int[] desc = new int[rack.length];
        List<String> list = Arrays.asList(new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "K", "Q", "A"});
        int i = 0;
        for (String x : rack) {
            for (String y : list) {
                if (x.contains(y)) {
                    switch (y) {
                        case "2":
                            desc[i] = 2;
                            break;
                        case "3":
                            desc[i] = 3;
                            break;
                        case "4":
                            desc[i] = 4;
                            break;
                        case "5":
                            desc[i] = 5;
                            break;
                        case "6":
                            desc[i] = 6;
                            break;
                        case "7":
                            desc[i] = 7;
                            break;
                        case "8":
                            desc[i] = 8;
                            break;
                        case "9":
                            desc[i] = 9;
                            break;
                        case "10":
                            desc[i] = 10;
                            break;
                        case "J":
                            desc[i] = 10;
                            break;
                        case "K":
                            desc[i] = 10;
                            break;
                        case "Q":
                            desc[i] = 10;
                            break;
                        case "A":
                            desc[i] = 11;
                            break;
                    }
                    System.out.println("size"+desc.length+"|"+i+" | "+desc[i]);
                    i++;
                }
            }
        }
        
        return desc;
    }
}
