package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public void deletePlayerById(long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Boolean checkIDExist(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public Player getPlayerById(long id) {
        Player player = null;
        Optional<Player> pl = playerRepository.findById(id);
        if (pl.isPresent()) {
            player = pl.get();
        }
        return player;
    }

    @Override
    public int getCount() {
        return (int) playerRepository.count();
    }

    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }


    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }


    @Override
    public Player updatePlayer(Player oldPlayer, Player newPlayer) {
        if (newPlayer.getName() != null) {
            oldPlayer.setName(newPlayer.getName());
        }
//        else throw new IllegalArgumentException();
        if (newPlayer.getTitle() != null) {
            oldPlayer.setTitle(newPlayer.getTitle());
        }
//        else throw new IllegalArgumentException();
        if (newPlayer.getRace() != null) {
            oldPlayer.setRace(newPlayer.getRace());
        }
//        else throw new IllegalArgumentException();
        if (newPlayer.getProfession() != null) {
            oldPlayer.setProfession(newPlayer.getProfession());
        }
//        else throw new IllegalArgumentException();
        if (newPlayer.getBirthday() != null) {
            oldPlayer.setBirthday(newPlayer.getBirthday());
        }
//        else throw new IllegalArgumentException();
        if (newPlayer.getBanned() != null) {
            oldPlayer.setBanned(newPlayer.getBanned());
        }
//        else throw new IllegalArgumentException();
        if (newPlayer.getExperience() != null) {
            oldPlayer.setExperience(newPlayer.getExperience());
        }
//        else throw new IllegalArgumentException();

        playerRepository.save(oldPlayer);
        return oldPlayer;
    }

    @Override
    public boolean isValidPlayer(Player player) {
        return ((player.getTitle() == null)||(player.getTitle() != null && player.getTitle().length() <30))
                && ((player.getName() == null)|| (player.getName() != null && player.getName().length()<12))
                && (player.getBirthday()==null||isDateValid(player.getBirthday()))
                && (player.getExperience() == null ||(player.getExperience() != null &&
                player.getExperience() > 0 &&
                player.getExperience() < 10_000_000));
    }

    public boolean isDateValid(Date birthdate) {
        if (birthdate.getTime() < 0) {
            return false;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(birthdate);
        return date.get(Calendar.YEAR) >= 2_000
                && date.get(Calendar.YEAR) <= 3_000;
    }
}
