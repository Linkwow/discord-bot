package ua.projects.discordbot.repository;

import java.util.List;

//todo : remove interface. unnecessary.
public interface CommonRepository <T> {
    List<T> findAll();
    T find(Integer id);
    void delete(Integer id);
}
