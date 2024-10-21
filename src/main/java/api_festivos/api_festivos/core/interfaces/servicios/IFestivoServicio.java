package api_festivos.api_festivos.core.interfaces.servicios;

import java.util.List; // Asegúrate de importar esta clase

import api_festivos.api_festivos.core.dominio.Festivo;

public interface IFestivoServicio {
    boolean verificarFestivo(int anio, int mes, int dia);
    List<Festivo> listarFestivos();
}
