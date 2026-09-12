package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.exception.SocioTorcedorException;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SocioTorcedorService {

    public void tornarseSocio(Categoria categoria, Usuario usuario){
        Associado associado = usuario.getAssociado();

        if(associado.getCategoria() != Categoria.NAO_ASSOCIADO){
            throw new SocioTorcedorException("Sócio torcedor já está associado");
        }

        associado.setCategoria(categoria);
        InjectionProvider.getAssociadoRepository().save(associado);

        Authentication currAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                usuario,
                currAuth.getCredentials(),
                currAuth.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
