package org.davidparada.controlador;

import org.davidparada.controlador.interfaceControlador.IBibliotecaControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.BibliotecaDto;
import org.davidparada.modelo.dto.EstadisticasBibliotecaDto;
import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.modelo.entidad.BibliotecaEntidad;
import org.davidparada.modelo.entidad.JuegoEntidad;
import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.enums.OrdenarJuegosBibliotecaEnum;
import org.davidparada.modelo.enums.TipoErrorEnum;
import org.davidparada.modelo.formulario.BibliotecaForm;
import org.davidparada.modelo.formulario.validacion.BibliotecaFormValidador;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.modelo.mapper.BibliotecaEntidadADtoMapper;
import org.davidparada.modelo.mapper.JuegoEntidadADtoMapper;
import org.davidparada.modelo.mapper.UsuarioEntidadADtoMapper;
import org.davidparada.repositorio.interfaceRepositorio.IBibliotecaRepo;
import org.davidparada.repositorio.interfaceRepositorio.IJuegoRepo;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.davidparada.controlador.util.ComprobarErrores.comprobarListaErrores;

public class BibliotecaControlador implements IBibliotecaControlador {

    public static final double HORAS_DE_JUEGO_POR_DEFECTO = 0.0;
    public static final double INICIO_VARIABLE_DOUBLE = 0.0;
    public static final int INICIO_VARIABLE_NEG = -1;
    public static final int CERO = 0;
    private final IBibliotecaRepo bibliotecaRepo;
    private final IJuegoRepo juegoRepo;
    private final ObtenerEntidadesOptional obtenerEntidades;
    private final IGestorTransacciones gestorTransacciones;

    public BibliotecaControlador(IBibliotecaRepo bibliotecaRepo,
                                 IJuegoRepo juegoRepo,
                                 ObtenerEntidadesOptional obtenerEntidades,
                                 IGestorTransacciones gestorTransacciones
    ) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.juegoRepo = juegoRepo;
        this.obtenerEntidades = obtenerEntidades;
        this.gestorTransacciones = gestorTransacciones;
    }

    // Añadir juego a biblioteca

    @Override
    public BibliotecaDto anadirJuego(Long idUsuario, Long idJuego) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        comprobarIdUsuario(idUsuario, errores);
        comprobarIdJuego(idJuego, errores);

        return gestorTransacciones.inTransaction(() -> {

            Optional<BibliotecaEntidad> bibliotecaExistente =
                    bibliotecaRepo.buscarPorUsuarioYJuego(idUsuario, idJuego);

            if (bibliotecaExistente.isPresent()) {
                errores.add(new ErrorModel("juego", TipoErrorEnum.DUPLICADO));
                throw new IllegalStateException();
            }

            UsuarioEntidad usuario;
            JuegoEntidad juego;
            try {
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                juego = obtenerEntidades.obtenerJuego(idJuego, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }

            BibliotecaForm nuevaBiblioteca = new BibliotecaForm(
                    idUsuario,
                    idJuego,
                    Instant.now(),
                    HORAS_DE_JUEGO_POR_DEFECTO,
                    null,
                    false
            );
            try {
                BibliotecaFormValidador.validarBiblioteca(nuevaBiblioteca);
            } catch (ValidationException e) {
                throw new IllegalStateException(e);
            }
            BibliotecaEntidad biblioteca = bibliotecaRepo.crear(nuevaBiblioteca);

            return BibliotecaEntidadADtoMapper.bibliotecaEntidadADto(biblioteca, usuario, juego);
        });
    }

    // Ver Biblioteca personal
    @Override
    public List<BibliotecaDto> verBiblioteca(Long idUsuario, OrdenarJuegosBibliotecaEnum orden) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        comprobarIdUsuario(idUsuario, errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario;
            try {
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException(e);
            }

            List<BibliotecaDto> juegos = construirBiblioteca(idUsuario, usuario);
            return ordenarBiblioteca(juegos, orden);
        });
    }

    private List<BibliotecaDto> construirBiblioteca(Long idUsuario, UsuarioEntidad usuario) {
        List<BibliotecaEntidad> juegosEntidad = bibliotecaRepo.buscarPorUsuario(idUsuario);
        return juegosEntidad.stream()
                .map(b -> {

                    JuegoEntidad juego = juegoRepo.buscarPorId(b.getIdJuego()).orElseThrow();

                    return new BibliotecaDto(
                            b.getIdBiblioteca(),
                            b.getIdUsuario(),
                            UsuarioEntidadADtoMapper.usuarioEntidadADto(usuario),
                            b.getIdJuego(),
                            JuegoEntidadADtoMapper.juegoEntidadADto(juego),
                            b.getFechaAdquisicion(),
                            b.getHorasDeJuego(),
                            b.getUltimaFechaDeJuego(),
                            b.isEstadoInstalacion()
                    );
                })
                .toList();
    }

    private List<BibliotecaDto> ordenarBiblioteca(List<BibliotecaDto> juegos, OrdenarJuegosBibliotecaEnum orden) {
        if (orden == null) {
            return juegos;
        }

        return switch (orden) {
            case ALFABETICO -> juegos.stream()
                    .sorted(Comparator.comparing(b -> b.juegoDto().titulo()))
                    .toList();
            case TIEMPO_DE_JUEGO -> juegos.stream()
                    .sorted(Comparator.comparing((BibliotecaDto b) -> b.horasDeJuego()).reversed())
                    .toList();
            case ULTIMA_SESION -> juegos.stream()
                    .sorted(Comparator.comparing((BibliotecaDto b) -> b.ultimaFechaDeJuego()).reversed())
                    .toList();
            case FECHA_DE_ADQUISICION -> juegos.stream()
                    .sorted(Comparator.comparing((BibliotecaDto b) -> b.fechaAdquisicion()).reversed())
                    .toList();
            default -> throw new IllegalArgumentException("No se encontró el orden");
        };
    }


    // Eliminar juego de biblioteca
    @Override
    public BibliotecaDto eliminarJuego(Long idUsuario, Long idJuego) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        comprobarIdUsuario(idUsuario, errores);
        comprobarIdJuego(idJuego, errores);

        return gestorTransacciones.inTransaction(() -> {
            BibliotecaEntidad biblioteca;
            UsuarioEntidad usuario;
            JuegoEntidad juego;
            try {
                biblioteca = obtenerEntidades.obtenerBiblioteca(idUsuario, idJuego, errores);
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                juego = obtenerEntidades.obtenerJuego(idJuego, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException(e);
            }
            bibliotecaRepo.eliminar(biblioteca.getIdBiblioteca());

            return BibliotecaEntidadADtoMapper.bibliotecaEntidadADto(
                    biblioteca,
                    usuario,
                    juego
            );
        });
    }

    // Actualizar tiempo de juego
    @Override
    public BibliotecaDto actualizarTiempoDeJuego(Long idUsuario, Long idJuego, double horas) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        comprobarIdUsuario(idUsuario, errores);
        comprobarIdJuego(idJuego, errores);
        if (horas < CERO) {
            errores.add(new ErrorModel("horas", TipoErrorEnum.RANGO_INVALIDO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            try {
                BibliotecaEntidad bibliotecaEntidad = obtenerEntidades.obtenerBiblioteca(idUsuario, idJuego, errores);
                UsuarioEntidad usuarioEntidad = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                JuegoEntidad juegoEntidad = obtenerEntidades.obtenerJuego(idJuego, errores);

                double horasTotales = bibliotecaEntidad.getHorasDeJuego() + horas;
                BibliotecaForm bibliotecaFormActualizada = new BibliotecaForm(
                        idUsuario,
                        idJuego,
                        bibliotecaEntidad.getFechaAdquisicion(),
                        horasTotales,
                        Instant.now(),
                        bibliotecaEntidad.isEstadoInstalacion()
                );

                BibliotecaEntidad actualizado = bibliotecaRepo.actualizar(
                        bibliotecaEntidad.getIdBiblioteca(),
                        bibliotecaFormActualizada
                ).orElseThrow(() -> new IllegalStateException());

                return BibliotecaEntidadADtoMapper.bibliotecaEntidadADto(
                        actualizado,
                        usuarioEntidad,
                        juegoEntidad
                );

            } catch (ValidationException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    // Consultar última sesión
    @Override
    public BibliotecaDto consultarUltimaSesion(Long idUsuario, Long idJuego) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        comprobarIdUsuario(idUsuario, errores);
        comprobarIdJuego(idJuego, errores);

        return gestorTransacciones.inTransaction(() -> {
            try {
                BibliotecaEntidad bibliotecaEntidad = obtenerEntidades.obtenerBiblioteca(idUsuario, idJuego, errores);
                UsuarioEntidad usuarioEntidad = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                JuegoEntidad juegoEntidad = obtenerEntidades.obtenerJuego(idJuego, errores);

                if (bibliotecaEntidad.getUltimaFechaDeJuego() == null) {
                    return BibliotecaEntidadADtoMapper.bibliotecaEntidadADto(
                            bibliotecaEntidad, usuarioEntidad, juegoEntidad
                    );
                }
                Instant ultimaFechaHoraDeJuego = bibliotecaEntidad.getUltimaFechaDeJuego();
                Instant horaActual = Instant.now();

                // Diferencia real
                Duration duracion = Duration.between(ultimaFechaHoraDeJuego, horaActual);

                long horasEnTotal = duracion.toHours();

                // Convertimos para mostrar
                ZonedDateTime fechaLocal = ultimaFechaHoraDeJuego
                        .atZone(ZoneId.systemDefault());

                DateTimeFormatter formato =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                return BibliotecaEntidadADtoMapper.bibliotecaEntidadADto(bibliotecaEntidad, usuarioEntidad, juegoEntidad);
            } catch (ValidationException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    // Filtrar biblioteca
    @Override
    public List<BibliotecaDto> buscarSegunCriterios(Long idUsuario, String texto, Boolean estadoInstalacion) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        comprobarIdUsuario(idUsuario, errores);

        return gestorTransacciones.inTransaction(() -> {
            try {
                UsuarioEntidad usuarioEntidad = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                List<BibliotecaEntidad> bibliotecaEntidad = bibliotecaRepo.buscarPorUsuario(idUsuario);
                return bibliotecaEntidad.stream()
                        .filter(b ->
                                estadoInstalacion == null ||
                                        b.isEstadoInstalacion() == estadoInstalacion
                        )
                        .map(b -> {
                            JuegoEntidad juego = juegoRepo.buscarPorId(b.getIdJuego()).orElseThrow();
                            return new BibliotecaDto(b.getIdBiblioteca(),
                                    b.getIdUsuario(),
                                    UsuarioEntidadADtoMapper.usuarioEntidadADto(usuarioEntidad),
                                    b.getIdJuego(),
                                    JuegoEntidadADtoMapper.juegoEntidadADto(juego),
                                    b.getFechaAdquisicion(),
                                    b.getHorasDeJuego(),
                                    b.getUltimaFechaDeJuego(),
                                    b.isEstadoInstalacion()
                            );
                        })
                        .filter(dto -> texto == null || dto.juegoDto().titulo().toLowerCase().contains(texto.toLowerCase()))
                        .toList();
            } catch (ValidationException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    // Ver estadísticas de biblioteca

    @Override
    public EstadisticasBibliotecaDto estadisticasBiblioteca(Long idUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        comprobarIdUsuario(idUsuario, errores);

        return gestorTransacciones.inTransaction(() -> {
            List<BibliotecaEntidad> biblioteca = bibliotecaRepo.buscarPorUsuario(idUsuario);
            int totalJuegos = biblioteca.size();
            double horasTotales = INICIO_VARIABLE_DOUBLE;
            double valorTotal = INICIO_VARIABLE_DOUBLE;
            Optional<JuegoEntidad> juegoMasJugado = Optional.empty();
            double maxHoras = INICIO_VARIABLE_NEG;
            List<JuegoEntidad> juegosInstalados = new ArrayList<>();
            List<JuegoEntidad> juegosNuncaJugados = new ArrayList<>();
            for (BibliotecaEntidad b : biblioteca) {
                JuegoEntidad juego = juegoRepo.buscarPorId(b.getIdJuego()).orElse(null);
                if (juego == null) {
                    continue;
                }
                horasTotales += b.getHorasDeJuego();
                valorTotal += juego.getPrecioBase();
                if (b.isEstadoInstalacion()) {
                    juegosInstalados.add(juego);
                }
                if (b.getHorasDeJuego() == HORAS_DE_JUEGO_POR_DEFECTO) {
                    juegosNuncaJugados.add(juego);
                }
                if (b.getHorasDeJuego() > maxHoras) {
                    maxHoras = b.getHorasDeJuego();
                    juegoMasJugado = Optional.of(juego);
                }
            }
            List<JuegoDto> juegosInstaladosDto = juegosInstalados.stream()
                    .map(j -> JuegoEntidadADtoMapper.juegoEntidadADto(j))
                    .toList();

            List<JuegoDto> juegosNuncaJugadosDto = juegosNuncaJugados.stream()
                    .map(j -> JuegoEntidadADtoMapper.juegoEntidadADto(j))
                    .toList();
            return new EstadisticasBibliotecaDto(
                    totalJuegos,
                    horasTotales,
                    juegosInstaladosDto,
                    juegoMasJugado.map(j -> JuegoEntidadADtoMapper.juegoEntidadADto(j)),
                    valorTotal,
                    juegosNuncaJugadosDto
            );
        });
    }

    // Comprobaciones
    private void comprobarIdUsuario(Long idUsuario, List<ErrorModel> errores) throws ValidationException {
        // Compruebo que idUsuario no sea nulo
        if (idUsuario == null) {
            errores.add(new ErrorModel("usuario", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);
    }

    private void comprobarIdJuego(Long idJuego, List<ErrorModel> errores) throws ValidationException {
        // Compruebo que idJuego no sea nulo
        if (idJuego == null) {
            errores.add(new ErrorModel("juego", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);
    }
}