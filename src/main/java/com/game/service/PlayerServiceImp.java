package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class PlayerServiceImp implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public List<Player> getPlayersList(String name, String title, Race race, Profession profession, Long after,
                                       Long before, Boolean banned, Integer minExperience, Integer maxExperience,
                                       Integer minLevel, Integer maxLevel) {
        Iterable<Player> playerIterable = playerRepository.findAll();
        List<Player> playerList = new ArrayList<>();
        for (Player player : playerIterable) {
            playerList.add(player);
        }
        if (name != null) {
            playerList = playerList.stream().filter(player -> player.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        if (title != null) {
            playerList = playerList.stream().filter(player -> player.getTitle().toLowerCase().contains(title.toLowerCase())).collect(Collectors.toList());
        }
        if (race != null) {
            playerList = playerList.stream().filter(player -> player.getRace().equals(race)).collect(Collectors.toList());
        }
        if (profession != null) {
            playerList = playerList.stream().filter(player -> player.getProfession().equals(profession)).collect(Collectors.toList());
        }
        if (after != null) {
            playerList = playerList.stream().filter(player -> player.getBirthday().getTime() > after).collect(Collectors.toList());
        }
        if (before != null) {
            playerList = playerList.stream().filter(player -> player.getBirthday().getTime() < before).collect(Collectors.toList());
        }
        if (banned != null) {
            playerList = playerList.stream().filter(player -> player.getBanned() == banned).collect(Collectors.toList());
        }
        if (minExperience != null) {
            playerList = playerList.stream().filter(player -> player.getExperience() >= minExperience).collect(Collectors.toList());
        }
        if (maxExperience != null) {
            playerList = playerList.stream().filter(player -> player.getExperience() <= maxExperience).collect(Collectors.toList());
        }
        if (minLevel != null) {
            playerList = playerList.stream().filter(player -> player.getLevel() >= minLevel).collect(Collectors.toList());
        }
        if (maxLevel != null) {
            playerList = playerList.stream().filter(player -> player.getLevel() <= maxLevel).collect(Collectors.toList());
        }

        return playerList;
    }

    @Override
    public List<Player> getPlayersListPagedAndOrdered(List<Player> playerList, PlayerOrder order, Integer pageNumber, Integer pageSize) {
        if (order == null) {
            order = PlayerOrder.ID;
        }
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }
        MutableSortDefinition sortDefinition = new MutableSortDefinition(order.getFieldName(), true, true);
        PagedListHolder<Player> page = new PagedListHolder<>(playerList, sortDefinition);
        page.resort();
        page.setPage(pageNumber);
        page.setPageSize(pageSize);
        return page.getPageList();
    }

    @Override
    public Integer getPlayersCount(String name, String title, Race race, Profession profession,
                                   Long after, Long before, Boolean banned, Integer minExperience,
                                   Integer maxExperience, Integer minLevel, Integer maxLevel) {
        return getPlayersList(name, title, race, profession,
                after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel).size();
    }

    @Override
    public Player createPlayer(Player player) {
        String name = player.getName();
        if (name == null || !(name.length() > 0 && name.length() <= 12)) {
            throw new IllegalArgumentException();
        }
        String title = player.getTitle();
        if (title == null || !(title.length() > 0 && title.length() <= 30)) {
            throw new IllegalArgumentException();
        }
        Race race = player.getRace();
        if (race == null) {
            throw new IllegalArgumentException();
        }
        Profession profession = player.getProfession();
        if (profession == null) {
            throw new IllegalArgumentException();
        }
        Date birthday = player.getBirthday();
        if (birthday == null || birthday.getTime() < 0 || !(((birthday.getYear() + 1900) >= 2000) && ((birthday.getYear() + 1900) <= 3000))) {
            throw new IllegalArgumentException();
        }
        Boolean banned = player.getBanned();
        if (banned == null) {
            player.setBanned(false);
        }
        Integer experience = player.getExperience();
        if (experience == null || !(experience >= 0 && experience <= 10000000)) {
            throw new IllegalArgumentException();
        }
        player.setLevel(ExpLvlCalculator.lvl(player.getExperience()));
        player.setUntilNextLevel(ExpLvlCalculator.untilNextLvlExp(player.getExperience(), player.getLevel()));

        return playerRepository.save(player);

    }

    @Override
    public Player getPlayerById(Long id) {
        Player player = null;
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
        }
        if (player == null){
            throw new IllegalArgumentException();
        }
        return player;
    }

    @Override
    public Player updatePlayer(Long id, Player player) throws IllegalArgumentException {
        Player existingPlayer;
        try {
            existingPlayer = getPlayerById(id);
        } catch (IllegalArgumentException e){
            throw new NullPointerException();
        }
        if (player.getName() != null){
            existingPlayer.setName(player.getName());
        }
        if (player.getTitle() != null){
            existingPlayer.setTitle(player.getTitle());
        }
        if (player.getRace() !=null){
            existingPlayer.setRace(player.getRace());
        }
        if (player.getProfession() !=null){
            existingPlayer.setProfession(player.getProfession());
        }
        if (player.getBirthday() !=null){
            existingPlayer.setBirthday(player.getBirthday());
        }
        if (player.getBanned() !=null){
            existingPlayer.setBanned(player.getBanned());
        }
        if (player.getExperience() !=null){
            existingPlayer.setExperience(player.getExperience());
        }
        return createPlayer(existingPlayer);

    }

    @Override
    public void deletePlayer(Long id) throws IllegalArgumentException {
        Player player = getPlayerById(id);
        playerRepository.delete(player);
    }
}
