package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.AssociadoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.exception.SocioTorcedorException;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SocioTorcedorService {

    @Autowired
    private AssociadoRepository associadoRepository;
    
    public void tornarseSocio(Categoria categoria, Usuario usuario){
        Associado associado = usuario.getAssociado();

        if(associado.getCategoria() != Categoria.NAO_ASSOCIADO){
            throw new SocioTorcedorException("Sócio torcedor já está associado");
        }

        associado.setCategoria(categoria);
        associadoRepository.save(associado);

        atualizaContextoSeguranca(usuario);
    }
    
    public void revogarAssociacao(Usuario usuario){
        Associado associado = usuario.getAssociado();
        
        if(associado.getCategoria() == Categoria.NAO_ASSOCIADO){
            throw new SocioTorcedorException("Sócio já não está acossido a nenhuma categoria");
        }
        
        associado.setCategoria(Categoria.NAO_ASSOCIADO);
        associadoRepository.save(associado);

        atualizaContextoSeguranca(usuario);
    }

    private void atualizaContextoSeguranca(Usuario usuario){
        try {
            Authentication currAuth = SecurityContextHolder.getContext().getAuthentication();
            assert currAuth != null;

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    usuario,
                    currAuth.getCredentials(),
                    currAuth.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } catch (Exception e){
            log.warn("Não foi possível atualizar contexto de segurança do usuário");
        }
    }
}
