package org.dictionary.api;

import java.util.List;

public class DashboardAPI {

    private List<StatAPI> stats;

    public DashboardAPI() {
        super();
    }

    public List<StatAPI> getStats() {
        return stats;
    }

    public void setStats(List<StatAPI> stats) {
        this.stats = stats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stats == null) ? 0 : stats.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DashboardAPI other = (DashboardAPI) obj;
        if (stats == null) {
            if (other.stats != null)
                return false;
        } else if (!stats.equals(other.stats))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DashboardAPI [stats=" + stats + "]";
    }

}
