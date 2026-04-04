package org.davidparada.controlador;

import org.davidparada.controlador.interfaceControlador.IResenaControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.EstadisticasResenasJuegoDto;
import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.modelo.dto.ResenaDto;
import org.davidparada.modelo.entidad.JuegoEntidad;
import org.davidparada.modelo.entidad.ResenaEntidad;
import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.enums.EstadoPublicacionEnum;
import org.davidparada.modelo.enums.OrdenarResenaEnum;
import org.davidparada.modelo.enums.TipoErrorEnum;
import org.davidparada.modelo.formulario.ResenaForm;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.modelo.formulario.validacion.ResenaFormValidador;
import org.davidparada.modelo.mapper.JuegoEntidadADtoMapper;
import org.davidparada.modelo.mapper.ResenaEntidadADtoMapper;
import org.davidparada.modelo.mapper.UsuarioEntidadADtoMapper;
import org.davidparada.repositorio.interfaceRepositorio.IResenaRepo;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.davidparada.controlador.util.ComprobarErrores.comprobarListaErrores;

public class ResenaControlador implements IResenaControlador {

    public static final double CANTIDAD_HORAS_JUGADAS_POR_DEFECTO = 0.0;
    public static final int VALOR_MIN_POS_EN_ESTADISTICA = 60;
    public static final int VALOR_MAX_NEG_EN_ESTADISTICA = 40;
    public static final double CALCULO_PORCENTAJE = 100.0;
    public static final int INICIO_VARIABLE_A_CERO = 0;
    private final IResenaRepo resenaRepo;
    private final ObtenerEntidadesOptional obtenerEntidades;
    private final IGestorTransacciones gestorTransacciones;

    public ResenaControlador(IResenaRepo reseniaRepo,
                             ObtenerEntidadesOptional obtenerEntidades,
                             IGestorTransacciones gestorTransacciones
    ) {
        this.resenaRepo = reseniaRepo;
        this.obtenerEntidades = obtenerEntidades;
        this.gestorTransacciones = gestorTransacciones;
    }

    @Override
    public ResenaDto escribirResena(
            Long idUsuario,
            Long idJuego,
            boolean recomendado,
            String texto
    ) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        if (idUsuario == null) {
            errores.add(new ErrorModel("idUsuario", TipoErrorEnum.OBLIGATORIO));
        }
        if (idJuego == null) {
            errores.add(new ErrorModel("idJuego", TipoErrorEnum.OBLIGATORIO));
        }
        if (texto == null || texto.isBlank()) {
            errores.add(new ErrorModel("textoResena", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario;
            JuegoEntidad juego;
            try {
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                juego = obtenerEntidades.obtenerJuego(idJuego, errores);
                obtenerEntidades.obtenerBiblioteca(idUsuario, idJuego, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }

            List<ResenaEntidad> resenasEntidad = resenaRepo.buscarPorJuego(idJuego);

            boolean yaExiste = resenasEntidad.stream()
                    .anyMatch(r -> r.getIdUsuario().equals(idUsuario));
            if (yaExiste) {
                errores.add(new ErrorModel("resena", TipoErrorEnum.DUPLICADO));
            }
            try {
                comprobarListaErrores(errores);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            ResenaForm nuevaResena = new ResenaForm(
                    idUsuario,
                    idJuego,
                    recomendado,
                    texto,
                    CANTIDAD_HORAS_JUGADAS_POR_DEFECTO,
                    Instant.now(),
                    null,
                    EstadoPublicacionEnum.PUBLICADA
            );
            try {
                ResenaFormValidador.validarResena(nuevaResena);
            } catch (ValidationException e) {
                throw new RuntimeException();
            }

            ResenaEntidad resena = resenaRepo.crear(nuevaResena);

            return ResenaEntidadADtoMapper.resenaEntidadADto(resena, usuario, juego);
        });
    }

    @Override
    public ResenaDto eliminarResena(Long idResena, Long idUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idResena == null) {
            errores.add(new ErrorModel("idResena", TipoErrorEnum.OBLIGATORIO));
        }
        if (idUsuario == null) {
            errores.add(new ErrorModel("idUsuario", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);
        return gestorTransacciones.inTransaction(() -> {
            ResenaEntidad resena;
            UsuarioEntidad usuario;
            JuegoEntidad juego;
            try {
                resena = obtenerEntidades.obtenerResenaPorIdYUsuario(idResena, idUsuario, errores);
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                juego = obtenerEntidades.obtenerJuego(resena.getIdJuego(), errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }
            resenaRepo.eliminar(resena.getIdResena());

            return ResenaEntidadADtoMapper.resenaEntidadADto(resena, usuario, juego);
        });
    }

    @Override
    public List<ResenaDto> obtenerResenas(Long idJuego,
                                          boolean recomendado,
                                          OrdenarResenaEnum orden
    ) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idJuego == null) {
            errores.add(new ErrorModel("idJuego", TipoErrorEnum.OBLIGATORIO));
        }
        if (orden == null) {
            errores.add(new ErrorModel("orden", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            JuegoEntidad juego;
            try {
                juego = obtenerEntidades.obtenerJuego(idJuego, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }

            JuegoDto juegoDto = JuegoEntidadADtoMapper.juegoEntidadADto(juego);

            List<ResenaEntidad> resenasFiltradas =
                    filtrarYOrdenarResenas(idJuego, recomendado, orden);

            try {
                return convertirADto(resenasFiltradas, juegoDto, errores);
            } catch (ValidationException e) {
                throw new RuntimeException();
            }
        });
    }

    private List<ResenaEntidad> filtrarYOrdenarResenas(Long idJuego,
                                                       boolean recomendado,
                                                       OrdenarResenaEnum orden) {

        List<ResenaEntidad> resenasEntidad = resenaRepo.buscarPorJuego(idJuego);

        Comparator<ResenaEntidad> comparador;

        switch (orden) {
            case RECIENTES:
                comparador = (r1, r2) ->
                        r2.getFechaPublicacion().compareTo(r1.getFechaPublicacion());
                break;

            case HORAS_JUGADAS:
                comparador = (r1, r2) ->
                        r2.getCantidadHorasJugadas().compareTo(r1.getCantidadHorasJugadas());
                break;

            case ACTUALIZADAS:
                comparador = (r1, r2) ->
                        r2.getFechaUltimaEdicion().compareTo(r1.getFechaUltimaEdicion());
                break;

            default:
                comparador = Comparator.comparing(ResenaEntidad::getIdResena);
        }

        return resenasEntidad.stream()
                .filter(r -> r.getEstadoPublicacion() == EstadoPublicacionEnum.PUBLICADA)
                .filter(r -> r.isRecomendado() == recomendado)
                .sorted(comparador)
                .toList();
    }

    private List<ResenaDto> convertirADto(List<ResenaEntidad> resenas,
                                          JuegoDto juegoDto,
                                          List<ErrorModel> errores) throws ValidationException {

        List<ResenaDto> resultado = new ArrayList<>();

        for (ResenaEntidad r : resenas) {

            UsuarioEntidad usuarioEntidad = obtenerEntidades.obtenerUsuario(r.getIdUsuario(), errores);

            resultado.add(new ResenaDto(
                    r.getIdResena(),
                    r.getIdUsuario(),
                    UsuarioEntidadADtoMapper.usuarioEntidadADto(usuarioEntidad),
                    r.getIdJuego(),
                    juegoDto,
                    r.isRecomendado(),
                    r.getTextoResena(),
                    r.getCantidadHorasJugadas(),
                    r.getFechaPublicacion(),
                    r.getFechaUltimaEdicion(),
                    r.getEstadoPublicacion()
            ));
        }

        return resultado;
    }

    @Override
    public ResenaDto ocultarResena(Long idResena, Long idUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idResena == null) {
            errores.add(new ErrorModel("idResena", TipoErrorEnum.OBLIGATORIO));
        }
        if (idUsuario == null) {
            errores.add(new ErrorModel("idUsuario", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {

            ResenaEntidad resena;
            try {
                resena = obtenerEntidades.obtenerResenaPorIdYUsuario(idResena, idUsuario, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }

            ResenaForm nuevaResena = new ResenaForm(
                    resena.getIdUsuario(),
                    resena.getIdJuego(),
                    resena.isRecomendado(),
                    resena.getTextoResena(),
                    resena.getCantidadHorasJugadas(),
                    resena.getFechaPublicacion(),
                    Instant.now(),
                    EstadoPublicacionEnum.OCULTA);

            Optional<ResenaEntidad> resenaActualizada = resenaRepo.actualizar(idResena, nuevaResena);
            ResenaEntidad resenaGuardada = resenaActualizada.orElseThrow();
            UsuarioEntidad usuario;
            JuegoEntidad juego;
            try {
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
                juego = obtenerEntidades.obtenerJuego(resena.getIdJuego(), errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }
            return ResenaEntidadADtoMapper.resenaEntidadADto(
                    resenaGuardada,
                    usuario,
                    juego
            );
        });
    }

    @Override
    public EstadisticasResenasJuegoDto consultarEstadisticasResenaPorJuego(Long idJuego) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idJuego == null) {
            errores.add(new ErrorModel("idJuego", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            try {
                obtenerEntidades.obtenerJuego(idJuego, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }

            List<ResenaEntidad> resenas = resenaRepo.buscarPorJuego(idJuego);
            int total = resenas.size();

            if (total == INICIO_VARIABLE_A_CERO) {
                return new EstadisticasResenasJuegoDto(INICIO_VARIABLE_A_CERO, INICIO_VARIABLE_A_CERO, INICIO_VARIABLE_A_CERO, INICIO_VARIABLE_A_CERO, "NEUTRA");
            }
            int positivas = INICIO_VARIABLE_A_CERO;
            double sumaHoras = INICIO_VARIABLE_A_CERO;
            for (ResenaEntidad r : resenas) {
                if (r.isRecomendado()) {
                    positivas++;
                }
                sumaHoras += r.getCantidadHorasJugadas();
            }
            int negativas = total - positivas;
            double porcentajePositivas = (positivas * CALCULO_PORCENTAJE) / total;
            double porcentajeNegativas = (negativas * CALCULO_PORCENTAJE) / total;
            double promedioHoras = sumaHoras / total;

            String tendencia;
            if (porcentajePositivas > VALOR_MIN_POS_EN_ESTADISTICA) {
                tendencia = "POSITIVA";
            } else if (porcentajePositivas < VALOR_MAX_NEG_EN_ESTADISTICA) {
                tendencia = "NEGATIVA";
            } else {
                tendencia = "NEUTRA";
            }

            return new EstadisticasResenasJuegoDto(
                    total,
                    porcentajePositivas,
                    porcentajeNegativas,
                    promedioHoras,
                    tendencia
            );
        });
    }

    @Override
    public List<ResenaDto> obtenerResenasUsuario(Long idUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idUsuario == null) {
            errores.add(new ErrorModel("idUsuario", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario;
            try {
                usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
            } catch (ValidationException e) {
                throw new IllegalStateException();
            }

            List<ResenaEntidad> resenasEntidad = resenaRepo.buscarPorUsuario(idUsuario);
            List<ResenaDto> resenasDto = new ArrayList<>();

            for (ResenaEntidad r : resenasEntidad) {
                JuegoEntidad juegoEntidad;
                try {
                    juegoEntidad = obtenerEntidades.obtenerJuego(r.getIdJuego(), errores);
                } catch (ValidationException e) {
                    throw new IllegalStateException();
                }
                resenasDto.add(new ResenaDto(
                        r.getIdResena(),
                        r.getIdUsuario(),
                        UsuarioEntidadADtoMapper.usuarioEntidadADto(usuario),
                        r.getIdJuego(),
                        JuegoEntidadADtoMapper.juegoEntidadADto(juegoEntidad),
                        r.isRecomendado(),
                        r.getTextoResena(),
                        r.getCantidadHorasJugadas(),
                        r.getFechaPublicacion(),
                        r.getFechaUltimaEdicion(),
                        r.getEstadoPublicacion()
                ));
            }
            return resenasDto;
        });
    }
}