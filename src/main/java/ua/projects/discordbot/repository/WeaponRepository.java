package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.projects.discordbot.persistence.Weapon;

public interface WeaponRepository extends JpaRepository<Weapon, Integer> {

    Weapon findWeaponByTypeIs(String type);

    boolean existsWeaponByTypeIs(String type);

}
