package ua.projects.discordbot.repository;

import java.util.List;

public interface CommonRepository <T> {
    List<T> findAll();
    T find(Integer id);
    void delete(Integer id);
}
