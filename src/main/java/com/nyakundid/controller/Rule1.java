package com.nyakundid.controller;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nyakundid
 * @description: Each player is first dealt two cards, after which players can
 * request as many cards as they like. If the value of their hand is more than
 * 21 then they will lose the round. If neither player has a score above 21 then
 * the player with the highest score wins the round.
 */
public class Rule1 {

    private String deck = "";

    private Logger log = Logger.getLogger(Rule1.class.getName());

    public Rule1(String deck) {
        this.deck = deck;
    }

    public int results() {

        String[] rack = deck.split(" ");
        int score = 0;
        List<String> list = Arrays.asList(new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "K", "Q", "A"});
        for (String x : rack) {
            for (String y : list) {
//                log.log(Level.INFO, "=={0} == {1}=={2}", new Object[]{x, y, score});
                if (x.contains(y)) {
                    switch (y) {
                        case "2":
                            score = score + 2;
                            break;
                        case "3":
                            score = score + 3;
                            break;
                        case "4":
                            score = score + 4;
                            break;
                        case "5":
                            score = score + 5;
                            break;
                        case "6":
                            score = score + 6;
                            break;
                        case "7":
                            score = score + 7;
                            break;
                        case "8":
                            score = score + 8;
                            break;
                        case "9":
                            score = score + 9;
                            break;
                        case "10":
                            score = score + 10;
                            break;
                        case "J":
                            score = score + 10;
                            break;
                        case "K":
                            score = score + 10;
                            break;
                        case "Q":
                            score = score + 10;
                            break;
                        case "A":
                            score = score + 11;
                            break;
                    }
                }
            }

        }

        return score;
    }

    public String resultLabel(int s) {

        String res = "";

        if (s > 21) {
            res = "(Over 21)";
        } else {
            res = "(Score " + s + ")";
        }

        return res;

    }

}
