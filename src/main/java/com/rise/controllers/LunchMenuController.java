package com.rise.controllers;

import com.rise.entity.LunchMenu;
import com.rise.service.LunchMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lunchmenus")
public class LunchMenuController {
    @Autowired
    private LunchMenuService lunchMenuService;

    @GetMapping
    public List<LunchMenu> getAllMenus() {
        return lunchMenuService.getAllMenus();
    }
}
