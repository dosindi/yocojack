/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nyakundid.model;

import com.nyakundid.controller.Rule1;
import com.nyakundid.controller.Rule2;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author nyakundid Bean for displaying score
 */
public class ScoreTable {

    @Getter
    @Setter
    private int game;
    @Getter
    @Setter
    private String playerA;
    @Getter
    @Setter
    private String playerB;
    @Getter
    @Setter
    private boolean playerAWins;

    private Logger log = Logger.getLogger(ScoreTable.class.getName());

    public ScoreTable(int game, String playerA, String playerB, boolean playerAWins) {

        this.game = game;
        this.playerA = playerA;
        this.playerB = playerB;
        this.playerAWins = playerAWins;
    }

    public ScoreTable() {
    }

    //TODO add rules here
    public String winnerMsg() {
        String r2score = "";
        String r2append = "";
        Rule1 rule = new Rule1(playerA);
        Rule1 rule1 = new Rule1(playerB);

        if (rule.results() < 22 && rule1.results() < 22 && rule.results() != rule1.results()) {
            r2score = "(Higher value  hand)";
        }

        if (rule.results() > 21) {
            r2score = "(Player A  over 21)";
        }

        if (rule1.results() > 21) {
            r2score = "(Player B  over 21)";
        }

        if (rule.results() == rule1.results()) {
            if (playerAWins) {
                r2score = "(High card A ";
            } else {
                r2score = "(High card B ";
            }

            //TODO  logic for draws 
            // r2score = "beats 10)";
            //sort deck in descending and compare deck A and B
            Rule2 rule2 = new Rule2();
            int[] deckA = rule2.scoreDesc(playerA);
            int[] deckB = rule2.scoreDesc(playerB);
            Arrays.sort(deckA,0,deckA.length - 1);
            Arrays.sort(deckB,0,deckB.length - 1);
            log.info("#"+game+": "+deckA[deckA.length - 1] + " || " + deckB[deckB.length - 1]);
            if (deckA[deckA.length - 1] > deckB[deckB.length - 1]) {
                r2append = " beats " + deckA[deckA.length - 1] + ")";
            } else if (deckB[deckB.length - 1] > deckA[deckA.length - 1]) {
                r2append = " beats " + deckA[deckA.length - 1] + ")";
            }

            log.log(Level.INFO, "{0}==={1}", new Object[]{deckA, deckB});
            // ranked according to suit, in the following order: S, H, C, D; spades beats hearts, hearts beats clubs, clubs beats diamonds. 
            if (Arrays.equals(deckA, deckB)) {
                log.log(Level.INFO, "**EQUAL" + game);
            }

        }

        return r2score + r2append;
    }

}
