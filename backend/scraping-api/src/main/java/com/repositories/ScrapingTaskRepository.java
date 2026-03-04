package com.repositories;

import com.entities.ScrapingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface intéragissant avec la base de donnée.
 * La classe est vide, car elle étend JpaRepository qui s'occupe de la gestion CRUD.
 */
@Repository
public interface ScrapingTaskRepository extends JpaRepository<ScrapingTask, String> {
}
