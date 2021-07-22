package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.projects.discordbot.persistence.Attribute;

import java.util.Set;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    Set<Attribute> findAttributesByDescriptionIn(String[] array);

    boolean existsAttributeByDescriptionIs(String description);
}
