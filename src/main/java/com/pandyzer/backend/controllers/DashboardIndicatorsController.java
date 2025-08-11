package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.dto.DashboardIndicators;
import com.pandyzer.backend.services.DashboardIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/indicators")
public class DashboardIndicatorsController {

    @Autowired
    private DashboardIndicatorsService service;

    @GetMapping(value = "/{userId}")
    public DashboardIndicators getIndicatorsByUserId(@PathVariable Long userId){
        return service.getIndicatorsByUserId(userId);
    }

}
