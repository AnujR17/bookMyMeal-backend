package com.rise.service;

import com.rise.entity.LunchMenu;
import com.rise.repository.LunchMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LunchMenuService {

    @Autowired
    private LunchMenuRepository lunchMenuRepository;

    public List<LunchMenu> getAllMenus() {
        return lunchMenuRepository.findAll();
    }
}

