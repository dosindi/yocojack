/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nyakundid.controller;

/**
 *
 * @author nyakundid
 */
public class Rule3 {

    public Rule3() {

    }


    public int[] suitRank(String deck) {
        String[] val = deck.split(" ");
        int[] suitScore = new int[val.length];

        //S, H, C, D; spades beats hearts, hearts beats clubs, clubs beats diamonds.
        String[] list = new String[]{"S", "H", "C", " D"};
        int i = 0;
        for (String x : val) {
            for (String y : list) {
                if (x.contains(y)) {
                    switch (y) {

                        case "S":
                            suitScore[i] = 4;
                            break;
                        case "H":
                            suitScore[i] = 3;
                            break;
                        case "C":
                            suitScore[i] = 2;
                            break;
                        case "D":
                            suitScore[i] = 1;
                            break;
                    }
                    i++;
                }

            }
        }
        return suitScore;
    }

}
