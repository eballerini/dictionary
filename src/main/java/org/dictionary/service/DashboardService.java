package org.dictionary.service;

import java.time.LocalDate;

import org.dictionary.api.DashboardAPI;
import org.dictionary.api.DashboardStatAPI;

public interface DashboardService {

    DashboardAPI getDashboard();

    DashboardStatAPI getStats(String login, LocalDate startDate, LocalDate endDate);

}