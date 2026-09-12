package com.ifsul.lpoo.sociotorcedor.config;

import com.ifsul.lpoo.sociotorcedor.core.validation.lote.IntervaloLoteValidator;
import com.ifsul.lpoo.sociotorcedor.core.validation.lote.LoteValidationHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoteValidationConfig {

    @Bean
    public LoteValidationHandle loteValidationChain(
            IntervaloLoteValidator intervaloValidator
    ){
        return intervaloValidator;
    }
}
