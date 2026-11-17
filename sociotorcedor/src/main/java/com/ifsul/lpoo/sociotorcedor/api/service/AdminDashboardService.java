package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoCategoriaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    /// TODO implementar service dashboard
    public DashboardInfoDTO getInformacoesDashboard(){
        return new DashboardInfoDTO();
    }

    /// TODO implementar métricas por categoria
    public List<DashboardInfoCategoriaDTO> getInformacoesCategorias(){
        return List.of();
    }

}
