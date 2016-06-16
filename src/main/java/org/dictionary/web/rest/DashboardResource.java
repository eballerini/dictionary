package org.dictionary.web.rest;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;

import org.dictionary.api.DashboardAPI;
import org.dictionary.api.DashboardStatAPI;
import org.dictionary.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    @Inject
    private DashboardService dashboardService;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<DashboardAPI> getDashboard() {

        DashboardAPI dashboard = dashboardService.getDashboard();

        return ResponseEntity.ok().body(dashboard);
    }

    @RequestMapping(value = "/dashboardstats", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<DashboardStatAPI> getDashboardStats(Principal principal,
            @RequestParam("startDate") String startDateAsStr, 
            @RequestParam("endDate") String endDateAsStr) {

        LocalDate startDate = LocalDate.parse(startDateAsStr, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate endDate = LocalDate.parse(endDateAsStr, DateTimeFormatter.BASIC_ISO_DATE);

        DashboardStatAPI dashboardStats = dashboardService.getStats(principal.getName(), startDate, endDate);

        return ResponseEntity.ok().body(dashboardStats);
    }
}
