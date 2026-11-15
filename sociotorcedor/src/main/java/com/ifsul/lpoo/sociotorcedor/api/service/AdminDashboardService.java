package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    /// TODO implementar service dashboard
    public DashboardInfoDTO getInformacoesDashboard(){
        return new DashboardInfoDTO();
    }

}
