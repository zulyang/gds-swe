package com.example.userapp.repository;
import com.example.userapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value="SELECT * FROM User WHERE salary >= ?1 AND salary <= ?2 LIMIT ?3 OFFSET ?4", nativeQuery = true)
    List<User> returnAll(float min, float max, int limit, int offset);

    @Query(value="SELECT * FROM User WHERE salary >= ?1 AND salary <= ?2 ORDER BY ?3 LIMIT ?4 OFFSET ?5", nativeQuery = true)
    List<User> returnAllSorted(float min, float max, int sort, int limit, int offset);
}
