package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerController {

    public boolean checkId(Long id) {
        if (id == null || id < 0L || id == 0L) {
            System.out.println("id == null or id = 0 or id < 0");
            return false;
        }
        if (id % 1 != 0L) {
            System.out.println("id is not Long");
            return false;
        }
        if (!String.valueOf(id).matches("^-?\\d+$")) {
            System.out.println("id is not digit");
            return false;
        }
        return true;
    }

    @Autowired
    private PlayerService playerService;

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable("id") Long id) {
        if (!checkId(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.checkIDExist(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        playerService.deletePlayerById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/rest/players/{id}")
    public ResponseEntity getPlayerById(@PathVariable("id") Long id) {
        if (!checkId(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.checkIDExist(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Player player = playerService.getPlayerById(id);
        return new ResponseEntity(player, HttpStatus.OK);
    }

    @GetMapping("/rest/players/count")
    public ResponseEntity getCount(String name, String title, Race race, Profession profession, Long after, Long before,
                                  Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                                  Integer maxLevel, PlayerOrder order) {
        if (order == null) order = PlayerOrder.ID;
        {
            PlayerOrder finalOrder = order;
            List<Player> players = playerService.findAll().stream()
                    .sorted(((player1, player2) -> {
                        if (PlayerOrder.LEVEL.equals(finalOrder)) {
                            return player1.getLevel().compareTo(player2.getLevel());
                        }
                        if (PlayerOrder.BIRTHDAY.equals(finalOrder)) {
                            return player1.getBirthday().compareTo(player2.getBirthday());
                        }

                        if (PlayerOrder.EXPERIENCE.equals(finalOrder)) {
                            return player1.getExperience().compareTo(player2.getExperience());
                        }
                        if (PlayerOrder.NAME.equals(finalOrder)) {
                            return player1.getName().compareTo(player2.getName());
                        }
                        return player1.getId().compareTo(player2.getId());
                    }))
                    .filter(player -> name == null || player.getName().contains(name))
                    .filter(player -> title == null || player.getTitle().contains(title))
                    .filter(player -> race == null || player.getRace().equals(race))
                    .filter(player -> profession == null || player.getProfession().equals(profession))
                    .filter(player -> after == null || player.getBirthday().getTime() > after)
                    .filter(player -> before == null || player.getBirthday().getTime() < before)
                    .filter(player -> banned == null || player.getBanned().equals(banned))
                    .filter(player -> minExperience == null || player.getExperience() >= minExperience)
                    .filter(player -> maxExperience == null || player.getExperience() <= maxExperience)
                    .filter(player -> minLevel == null || player.getLevel() >= minLevel)
                    .filter(player -> maxLevel == null || player.getLevel() <= maxLevel)
                    .collect(Collectors.toList());
            return new ResponseEntity(players.size(), HttpStatus.OK);
        }
    }

    @GetMapping("/rest/players")
    public ResponseEntity findAll(String name, String title, Race race, Profession profession, Long after, Long before,
                                  Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                                  Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize) {

        if (pageSize == null) pageSize = 3;
        if (pageNumber == null) pageNumber = 0;
        if (order == null) order = PlayerOrder.ID;
        {
            PlayerOrder finalOrder = order;
            List<Player> players = playerService.findAll().stream()
                    .sorted(((player1, player2) -> {
                        if (PlayerOrder.LEVEL.equals(finalOrder)) {
                            return player1.getLevel().compareTo(player2.getLevel());
                        }
                        if (PlayerOrder.BIRTHDAY.equals(finalOrder)) {
                            return player1.getBirthday().compareTo(player2.getBirthday());
                        }

                        if (PlayerOrder.EXPERIENCE.equals(finalOrder)) {
                            return player1.getExperience().compareTo(player2.getExperience());
                        }
                        if (PlayerOrder.NAME.equals(finalOrder)) {
                            return player1.getName().compareTo(player2.getName());
                        }
                        return player1.getId().compareTo(player2.getId());
                    }))
                    .filter(player -> name == null || player.getName().contains(name))
                    .filter(player -> title == null || player.getTitle().contains(title))
                    .filter(player -> race == null || player.getRace().equals(race))
                    .filter(player -> profession == null || player.getProfession().equals(profession))
                    .filter(player -> after == null || player.getBirthday().getTime() > after)
                    .filter(player -> before == null || player.getBirthday().getTime() < before)
                    .filter(player -> banned == null || player.getBanned().equals(banned))
                    .filter(player -> minExperience == null || player.getExperience() >= minExperience)
                    .filter(player -> maxExperience == null || player.getExperience() <= maxExperience)
                    .filter(player -> minLevel == null || player.getLevel() >= minLevel)
                    .filter(player -> maxLevel == null || player.getLevel() <= maxLevel)
                    .skip(pageSize * pageNumber)
                    .limit(pageSize)
                    .collect(Collectors.toList());
            //playerService.findAllByNameAndTitleAndRaceAndProfessionAndBirthdayBetweenAndBannedAndExperienceBetweenAndLevelBetween
            // (name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);;
            return new ResponseEntity(players, HttpStatus.OK);
        }
    }


    @PostMapping("/rest/players")
    public ResponseEntity createPlayer(@RequestBody Player player ) {

        String name = player.getName();
        String title = player.getTitle();
        Race race = player.getRace();
        Profession profession = player.getProfession();
        Date birthday = player.getBirthday();
        Boolean banned = player.getBanned();
        Integer experience = player.getExperience();


        if (name == null || name.equals("") || name.length() > 12 || title == null || title.length() > 30
                || race == null || profession == null || birthday == null
                || birthday.getTime() < new Date(0L).getTime()
                || experience == null || experience < 0 || experience > 10_000_000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Integer level = (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);

        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - experience;

        Boolean ban;

        if (banned == null) ban = false;
        else ban = banned;

        Player createdPlayer = new Player(name, title, race, profession, birthday, ban,
                experience, level, untilNextLevel);

        playerService.savePlayer(createdPlayer);

        return new ResponseEntity<>( createdPlayer, HttpStatus.OK);
    }


    @PostMapping("/rest/players/{id}")
    public ResponseEntity UpdatePlayer(@PathVariable("id") Long id, @RequestBody Player player) {
        if (!checkId(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.checkIDExist(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Player savedPlayer;
        savedPlayer = playerService.getPlayerById(id);
        if (player == null) {
            return new ResponseEntity(savedPlayer,HttpStatus.OK);
        }

        if (!playerService.isValidPlayer(player))
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player result;
        try {
            result = playerService.updatePlayer(savedPlayer, player);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getExperience()!=null){
            Integer level = (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
            Integer untilExt = 50 * (level + 1) * (level + 2) - player.getExperience();
            result.setLevel(level);
            result.setUntilNextLevel(untilExt);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
