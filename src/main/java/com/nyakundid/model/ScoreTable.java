/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nyakundid.model;

import com.nyakundid.controller.Rule1;
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
        }

        return r2score;
    }

}
