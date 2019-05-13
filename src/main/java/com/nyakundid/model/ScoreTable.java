/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nyakundid.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author nyakundid
 * Bean for displaying score
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

}
