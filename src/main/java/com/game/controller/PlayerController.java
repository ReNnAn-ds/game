package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest")
public class PlayerController {
    @Autowired
    private PlayerServiceImp playerServiceImp;

    @GetMapping(path = "/players")
    public @ResponseBody
    ResponseEntity<List<Player>> getAllPlayers(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "title", required = false) String title,
                                               @RequestParam(value = "race", required = false) Race race,
                                               @RequestParam(value = "profession", required = false) Profession profession,
                                               @RequestParam(value = "after", required = false) Long after,
                                               @RequestParam(value = "before", required = false) Long before,
                                               @RequestParam(value = "banned", required = false) Boolean banned,
                                               @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                               @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                               @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                               @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                               @RequestParam(value = "order", required = false) PlayerOrder order,
                                               @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<Player> playerList = playerServiceImp.getPlayersList(name, title, race, profession,
                after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel);
        return new ResponseEntity<List<Player>>(playerServiceImp.getPlayersListPagedAndOrdered(playerList, order, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping(path = "/players/count")
    public @ResponseBody
    ResponseEntity<Integer> getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "title", required = false) String title,
                                            @RequestParam(value = "race", required = false) Race race,
                                            @RequestParam(value = "profession", required = false) Profession profession,
                                            @RequestParam(value = "after", required = false) Long after,
                                            @RequestParam(value = "before", required = false) Long before,
                                            @RequestParam(value = "banned", required = false) Boolean banned,
                                            @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                            @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        return new ResponseEntity<Integer>(playerServiceImp.getPlayersCount(name, title, race, profession,
                after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel), HttpStatus.OK);
    }

    @GetMapping(path = "/players/{id}")
    public @ResponseBody
    ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        Player player;
        try {
            player = playerServiceImp.getPlayerById(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @PostMapping(path = "/players")
    public @ResponseBody
    ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player newPlayer;
        try {
            newPlayer = playerServiceImp.createPlayer(player);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Player>(newPlayer, HttpStatus.OK);
    }

    @DeleteMapping(path = "/players/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deletePlayer(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }
        try {
            playerServiceImp.deletePlayer(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @PostMapping(path = "/players/{id}")
    public @ResponseBody
    ResponseEntity<Player> updatePlayer(@PathVariable Long id,
                                        @RequestBody Player player) {
        if (id < 1) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        Player updatedPlayer;
        try {
            updatedPlayer = playerServiceImp.updatePlayer(id, player);
        } catch (NullPointerException ex){
            return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Player>(updatedPlayer, HttpStatus.OK);
    }
}
