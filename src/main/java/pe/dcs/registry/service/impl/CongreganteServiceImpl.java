package pe.dcs.registry.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.dcs.registry.entity.Congregante;
import pe.dcs.registry.repository.ICongreganteDAO;
import pe.dcs.registry.service.ICongreganteService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class CongreganteServiceImpl implements ICongreganteService {

    final ICongreganteDAO data;

    public CongreganteServiceImpl(ICongreganteDAO data) {
        this.data = data;
    }

    @Override
    public Set<Congregante> findCongreganteToValidate(String apellido, String nombre) {
        return data.findCongreganteToValidate(apellido, nombre);
    }

    @Override
    public int eliminarCongregantesNombreApellidoVacios() {
        return data.deleteCongregantesWithEmptyNombreOrApellido();
    }

    @Override
    public Page<Congregante> findCongregantes(Pageable pageable) {
        return data.findCongregantes(pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByApellido(String apellido, Pageable pageable) {
        return data.findCongregantesByApellido(apellido, pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByNombre(String nombre, Pageable pageable) {
        return data.findCongregantesByNombre(nombre, pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByFechaNacimiento(String fechaNacimiento, Pageable pageable) {
        return data.findCongregantesByFechaNacimiento(fechaNacimiento, pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByBautizado(String bautizado, Pageable pageable) {
        return data.findCongregantesByBautizado(bautizado, pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByNumeroGPC(String numeroGPC, Pageable pageable) {
        return data.findCongregantesByNumeroGPC(numeroGPC, pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByMinisterio(String ministerio, Pageable pageable) {
        return data.findCongregantesByMinisterio(ministerio, pageable);
    }

    @Override
    public Page<Congregante> findCongregantesByCurso(String curso, Pageable pageable) {
        return data.findCongregantesByCurso(curso, pageable);
    }

    @Override
    public Set<Congregante> findCongregantes(Sort sort) {
        return data.findCongregantes(sort);
    }

    @Override
    public Set<Congregante> findCongregantesByApellido(String apellido, Sort sort) {
        return data.findCongregantesByApellido(apellido, sort);
    }

    @Override
    public Set<Congregante> findCongregantesByNombre(String nombre, Sort sort) {
        return data.findCongregantesByNombre(nombre, sort);
    }

    @Override
    public Set<Congregante> findCongregantesByFechaNacimiento(String fechaNacimiento, Sort sort) {
        return data.findCongregantesByFechaNacimiento(fechaNacimiento, sort);
    }

    @Override
    public Set<Congregante> findCongregantesByBautizado(String bautizado, Sort sort) {
        return data.findCongregantesByBautizado(bautizado, sort);
    }

    @Override
    public Set<Congregante> findCongregantesByNumeroGPC(String numeroGPC, Sort sort) {
        return data.findCongregantesByNumeroGPC(numeroGPC, sort);
    }

    @Override
    public Set<Congregante> findCongregantesByMinisterio(String ministerio, Sort sort) {
        return data.findCongregantesByMinisterio(ministerio, sort);
    }

    @Override
    public Set<Congregante> findCongregantesByCurso(String curso, Sort sort) {
        return data.findCongregantesByCurso(curso, sort);
    }

    @Override
    public Optional<Congregante> findCongreganteById(UUID id) {
        return data.findCongreganteById(id);
    }

    @Override
    public void saveCongregante(Congregante congregante) {
        data.save(congregante);
    }

    @Override
    public void updateCongregante(UUID id, String apellido, String nombre, String sexo, String telefono, String direccion,
                                  String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo,
                                  String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                                  String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio,
                                  String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio,
                                  String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                                  String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, LocalDateTime fechaModificacion, Boolean habilitado) {
        data.updateCongregante(id, apellido, nombre, sexo, telefono, direccion, mesCumpleanios, fechaNacimiento, edad, estadoCivil,
                cantidadHijo, tiempoIBET, iglesiaAnterior, bautizado, iglesiaBautizo, tiempoBautizo, estudiando, curso, cursoUltimo,
                tiempoSinEstudio, motivoSinEstudio, participandoGPC, motivoNoGPC, numeroGPC, enMinisterio, ministerio, cargo,
                estudiandoEscuelaCiervo, cursoEscuelaCiervo, cursoUltimoEscuelaCiervo, tiempoSinEstudioEscuelaCiervo, motivoSinEstudioEscuelaCiervo,
                fechaModificacion, habilitado);
    }
}
