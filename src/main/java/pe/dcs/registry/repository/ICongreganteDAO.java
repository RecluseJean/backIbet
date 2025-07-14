package pe.dcs.registry.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.dcs.registry.entity.Congregante;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ICongreganteDAO extends JpaRepository<Congregante, UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Congregante c WHERE c.nombre IS NULL OR c.nombre = '' OR c.apellido IS NULL OR c.apellido = ''")
    int deleteCongregantesWithEmptyNombreOrApellido();

    @Query("SELECT NEW Congregante(c.apellido, c.nombre) " +
            "FROM Congregante c " +
            "WHERE c.apellido LIKE ?1 " +
            "AND c.nombre LIKE ?2")
    Set<Congregante> findCongreganteToValidate(String apellido, String nombre);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c")
    Page<Congregante> findCongregantes(Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.apellido) LIKE %?1%")
    Page<Congregante> findCongregantesByApellido(String apellido, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.nombre) LIKE %?1%")
    Page<Congregante> findCongregantesByNombre(String nombre, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE c.fechaNacimiento LIKE %?1%")
    Page<Congregante> findCongregantesByFechaNacimiento(String fechaNacimiento, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.bautizado) LIKE %?1%")
    Page<Congregante> findCongregantesByBautizado(String bautizado, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.numeroGPC) LIKE %?1%")
    Page<Congregante> findCongregantesByNumeroGPC(String numeroGPC, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.ministerio) LIKE %?1%")
    Page<Congregante> findCongregantesByMinisterio(String ministerio, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.curso) LIKE %?1%")
    Page<Congregante> findCongregantesByCurso(String curso, Pageable pageable);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c")
    Set<Congregante> findCongregantes(Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.apellido) LIKE %?1%")
    Set<Congregante> findCongregantesByApellido(String apellido, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.nombre) LIKE %?1%")
    Set<Congregante> findCongregantesByNombre(String nombre, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE c.fechaNacimiento LIKE %?1%")
    Set<Congregante> findCongregantesByFechaNacimiento(String fechaNacimiento, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.bautizado) LIKE %?1%")
    Set<Congregante> findCongregantesByBautizado(String bautizado, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.numeroGPC) LIKE %?1%")
    Set<Congregante> findCongregantesByNumeroGPC(String numeroGPC, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.ministerio) LIKE %?1%")
    Set<Congregante> findCongregantesByMinisterio(String ministerio, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE LOWER(c.curso) LIKE %?1%")
    Set<Congregante> findCongregantesByCurso(String curso, Sort sort);

    @Query("SELECT NEW Congregante(c.id, c.apellido, c.nombre, c.sexo, c.telefono, c.direccion, c.mesCumpleanios, c.fechaNacimiento, " +
            "c.edad, c.estadoCivil, c.cantidadHijo, c.tiempoIBET, c.iglesiaAnterior, c.bautizado, c.iglesiaBautizo, c.tiempoBautizo, " +
            "c.estudiando, c.curso, c.cursoUltimo, c.tiempoSinEstudio, c.motivoSinEstudio, c.participandoGPC, c.motivoNoGPC, c.numeroGPC, " +
            "c.enMinisterio, c.ministerio, c.cargo, c.estudiandoEscuelaCiervo, c.cursoEscuelaCiervo, c.cursoUltimoEscuelaCiervo, " +
            "c.tiempoSinEstudioEscuelaCiervo, c.motivoSinEstudioEscuelaCiervo, c.fechaRegistro, c.fechaModificacion, c.habilitado) " +
            "FROM Congregante c " +
            "WHERE c.id = ?1")
    Optional<Congregante> findCongreganteById(UUID id);

    @Modifying
    @Query("UPDATE Congregante c " +
            "SET c.apellido = ?2, " +
            "c.nombre = ?3, " +
            "c.sexo = ?4, " +
            "c.telefono = ?5, " +
            "c.direccion = ?6, " +
            "c.mesCumpleanios = ?7, " +
            "c.fechaNacimiento = ?8, " +
            "c.edad = ?9, " +
            "c.estadoCivil = ?10, " +
            "c.cantidadHijo = ?11, " +
            "c.tiempoIBET = ?12, " +
            "c.iglesiaAnterior = ?13, " +
            "c.bautizado = ?14, " +
            "c.iglesiaBautizo = ?15, " +
            "c.tiempoBautizo = ?16, " +
            "c.estudiando = ?17, " +
            "c.curso = ?18, " +
            "c.cursoUltimo = ?19, " +
            "c.tiempoSinEstudio = ?20, " +
            "c.motivoSinEstudio = ?21, " +
            "c.participandoGPC = ?22, " +
            "c.motivoNoGPC = ?23, " +
            "c.numeroGPC = ?24, " +
            "c.enMinisterio = ?25, " +
            "c.ministerio = ?26, " +
            "c.cargo = ?27, " +
            "c.estudiandoEscuelaCiervo = ?28, " +
            "c.cursoEscuelaCiervo = ?29, " +
            "c.cursoUltimoEscuelaCiervo = ?30, " +
            "c.tiempoSinEstudioEscuelaCiervo = ?31, " +
            "c.motivoSinEstudioEscuelaCiervo = ?32, " +
            "c.fechaModificacion = ?33, " +
            "c.habilitado = ?34 " +
            "WHERE c.id = ?1")
    void updateCongregante(UUID id, String apellido, String nombre, String sexo, String telefono, String direccion,
                           String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo,
                           String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                           String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio,
                           String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio,
                           String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                           String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, LocalDateTime fechaModificacion,
                           Boolean habilitado);
}
