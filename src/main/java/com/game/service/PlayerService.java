package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {


    void deletePlayerById(long id);

    Boolean checkIDExist(Long id);

    Player getPlayerById (long id);

    int getCount();

    void savePlayer(Player player);

   List<Player> findAll();


    Player updatePlayer(Player oldPlayer, Player newPlayer);

    boolean isValidPlayer(Player player);
}
