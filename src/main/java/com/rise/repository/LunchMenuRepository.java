package com.rise.repository;

import com.rise.entity.LunchMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LunchMenuRepository extends JpaRepository<LunchMenu,Long> {
}
