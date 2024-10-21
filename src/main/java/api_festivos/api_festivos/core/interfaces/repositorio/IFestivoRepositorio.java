package api_festivos.api_festivos.core.interfaces.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import api_festivos.api_festivos.core.dominio.Festivo;

public interface IFestivoRepositorio extends JpaRepository<Festivo, Long> {
    
}
