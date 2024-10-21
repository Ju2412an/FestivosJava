package api_festivos.api_festivos.Aplicacion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api_festivos.api_festivos.core.dominio.Festivo;
import api_festivos.api_festivos.core.interfaces.repositorio.IFestivoRepositorio;
import api_festivos.api_festivos.core.interfaces.servicios.IFestivoServicio;

@Service // Agregar esta anotación para indicar que es un componente de servicio administrado por Spring
public class FestivoServicio implements IFestivoServicio {
    @Autowired
    private IFestivoRepositorio repositorio;
    
    public boolean verificarFestivo(int anio, int mes, int dia) {
        // Validar si la fecha es válida
        if (!isValidDate(anio, mes, dia)) {
            throw new IllegalArgumentException("La fecha no existe.");
        }

        Iterable<Festivo> festivos = repositorio.findAll();
        for (Festivo festivo : festivos) {
            LocalDate fechaFestivo = null;
            // Fijo
            if (festivo.getIdTipo() == 1) {
                fechaFestivo = LocalDate.of(anio, festivo.getMes(), festivo.getDia());
            }
            // Ley Puente Festivo
            else if (festivo.getIdTipo() == 2) {
                fechaFestivo = LocalDate.of(anio, festivo.getMes(), festivo.getDia());
                if (fechaFestivo.getDayOfWeek().getValue() != 1) {
                    fechaFestivo = fechaFestivo.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); // Siguiente lunes
                }
            }
            // Basado en Pascua
            else if (festivo.getIdTipo() == 3) {
                LocalDate domingoPascua = domingoPascua(anio);
                fechaFestivo = domingoPascua.plusDays(festivo.getDiasPascua());
            }
            // Basado en Pascua y Ley Puente Festivo
            else if (festivo.getIdTipo() == 4) {
                LocalDate domingoPascua = domingoPascua(anio);
                fechaFestivo = domingoPascua.plusDays(festivo.getDiasPascua());
                if (fechaFestivo.getDayOfMonth() != festivo.getDia()) {
                    fechaFestivo = fechaFestivo.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); // Siguiente lunes
                }
            }

            if (fechaFestivo != null && fechaFestivo.isEqual(LocalDate.of(anio, mes, dia))) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidDate(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private LocalDate domingoPascua(int anio) // Especificar el tipo de dato del parámetro
    {
        int a = anio % 19;
        int b = anio % 4;
        int c = anio % 7;
        int d = (19 * a + 24) % 30;
        int dias = d + (2 * b + 4 * c + 6 * d + 5) % 7;
        int diaDomingoPascua = 15 + dias + 7;
        if (diaDomingoPascua > 31) {
            diaDomingoPascua -= 31;
            return LocalDate.of(anio, Month.APRIL, diaDomingoPascua); // Utilizar Month.APRIL
        } else {
            return LocalDate.of(anio, Month.MARCH, diaDomingoPascua); // Utilizar Month.APRIL
        }
    }

    public List<Festivo> listarFestivos() {
        return repositorio.findAll(); // Suponiendo que tienes un repositorio JPA
    }
}
