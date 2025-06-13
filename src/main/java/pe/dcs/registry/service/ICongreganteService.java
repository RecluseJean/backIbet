package pe.dcs.registry.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pe.dcs.registry.entity.Congregante;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ICongreganteService {

    Set<Congregante> findCongreganteToValidate(String apellido, String nombre);

    Page<Congregante> findCongregantes(Pageable pageable);

    Page<Congregante> findCongregantesByApellido(String apellido, Pageable pageable);

    Page<Congregante> findCongregantesByNombre(String nombre, Pageable pageable);

    Page<Congregante> findCongregantesByFechaNacimiento(String fechaNacimiento, Pageable pageable);

    Page<Congregante> findCongregantesByBautizado(String bautizado, Pageable pageable);

    Page<Congregante> findCongregantesByNumeroGPC(String numeroGPC, Pageable pageable);

    Page<Congregante> findCongregantesByMinisterio(String ministerio, Pageable pageable);

    Page<Congregante> findCongregantesByCurso(String curso, Pageable pageable);

    Set<Congregante> findCongregantes(Sort sort);

    Set<Congregante> findCongregantesByApellido(String apellido, Sort sort);

    Set<Congregante> findCongregantesByNombre(String nombre, Sort sort);

    Set<Congregante> findCongregantesByFechaNacimiento(String fechaNacimiento, Sort sort);

    Set<Congregante> findCongregantesByBautizado(String bautizado, Sort sort);

    Set<Congregante> findCongregantesByNumeroGPC(String numeroGPC, Sort sort);

    Set<Congregante> findCongregantesByMinisterio(String ministerio, Sort sort);

    Set<Congregante> findCongregantesByCurso(String curso, Sort sort);

    Optional<Congregante> findCongreganteById(UUID id);

    void saveCongregante(Congregante congregante);

    void updateCongregante(UUID id, String apellido, String nombre, String sexo, String telefono, String direccion,
                           String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo,
                           String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                           String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio,
                           String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio,
                           String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                           String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, LocalDateTime fechaModificacion,
                           Boolean habilitado);
}
