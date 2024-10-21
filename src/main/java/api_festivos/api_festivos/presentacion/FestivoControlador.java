package api_festivos.api_festivos.presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api_festivos.api_festivos.core.dominio.Festivo;
import api_festivos.api_festivos.core.interfaces.servicios.IFestivoServicio;

@RestController
@RequestMapping("/api/festivos")
public class FestivoControlador {

    private final IFestivoServicio servicio;

    @Autowired
    public FestivoControlador(IFestivoServicio servicio) {
        this.servicio = servicio;
    }

    @RequestMapping(value = "/verificar/{anio}/{mes}/{dia}", method = RequestMethod.GET)
    public String verificarFestivo(@PathVariable int anio, @PathVariable int mes, @PathVariable int dia) {
        try {
            boolean esFestivo = servicio.verificarFestivo(anio, mes, dia);
            if (esFestivo) {
                return "La fecha " + dia + "/" + mes + "/" + anio + " es un festivo.";
            } else {
                return "La fecha " + dia + "/" + mes + "/" + anio + " no es un festivo.";
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage(); // Devolver el mensaje de error si la fecha no es válida
        }
    }

    @GetMapping("/listar")
    public List<Festivo> listarFestivos() {
        return servicio.listarFestivos();
    }
}
