/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nyakundid.model;

import com.nyakundid.controller.Rule1;
import com.nyakundid.controller.Rule2;
import com.nyakundid.controller.Rule3;
import java.util.Arrays;
import java.util.Collections;
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
        Rule1 rule = new Rule1(getPlayerA());
        Rule1 rule1 = new Rule1(getPlayerB());

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
            //sort deck in descending and compare deck A and B
            Rule2 rule2 = new Rule2();
            int[] deckA = rule2.scoreRank(getPlayerA());
            int[] deckB = rule2.scoreRank(getPlayerB());
            int scoreA = findMax(deckA);
            int scoreB = findMax(deckB);
            if (scoreA > scoreB) {
                r2append = " beats " + scoreB + ")";
            } else if (scoreB > scoreA) {
                r2append = " beats " + scoreA + ")";
            }

            //TODO rank according to suit, in the following order: S, H, C, D; spades beats hearts, hearts beats clubs, clubs beats diamonds. 
            if (Arrays.equals(deckA, deckB)) {
                log.log(Level.INFO, "**EQUAL {0}", game);
                
                boolean winner = false;
                String[] list = new String[]{"S", "H", "C", " D"};
                for(int x=0;x<list.length&&!winner;x++ ){
                   boolean a = getPlayerA().contains(list[x]);
                   boolean b = getPlayerB().contains(list[x]);
                 log.log(Level.INFO, "**EQUAL {0} || {1} || {2} || {3} || {4} || {5}", new Object[]{game,a,b,getPlayerA(),getPlayerB(),list[x]});  
                   if(a&&a!=b){
                       winner = true;   
                       r2append = " beats " +  list[x] + ")";
                   }
                    if(b&&b!=a){
                       winner = true;
                       r2append = " beats " + list[x] + ")";
                   }
                   
                }
           
 

            }

        }

        return r2score + r2append;
    }

    public int findMax(int[] deck) {

        int max = 0;
        for (int x : deck) {
            if (x > max) {
                max = x;
            }
        }
        return max;
    }

}
