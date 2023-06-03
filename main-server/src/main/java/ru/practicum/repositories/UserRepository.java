package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from Users u where u.id in :ids /*:pageable*/")
    Page<User> findByIds(Long[] ids, Pageable pageable);
}