# SPEC de Tests (versión esencial)

## Convenciones
- JUnit 5, carpeta `src/test/java` espejando los paquetes. Nomenclatura `<Clase>Test`.
- `GeneradorAleatorio.setSeed(SEED_POR_DEFECTO)` y `setSeedFija(true)` en `@BeforeEach`.
- El tiempo se pasa como `ahora : long` (sin `Thread.sleep`).
- Sin sockets ni UI reales.

---

## Spec Server

| Paquete | Clase principal | Test |
|---|---|---|
| utilidades | Json | JsonTest |
| mapa | Mapa | MapaTest |
| logica | Partida | PartidaTest |
| controlador | Controlador | ControladorTest |

### JsonTest
+ mensajeAJson_createMatch_generaUnaSolaLinea() : void
+ roundTrip_todosLosTiposDeMensaje_conservanCampos() : void
+ jsonAMensaje_tipoDesconocido_lanzaIllegalArgumentException() : void
+ jsonAMensaje_camposExtra_losIgnora() : void
+ roundTrip_gameStartedConEntidadPolimorfica_resuelveAnimalCafeBandera() : void

// El round trip se ejecuta una vez por cada valor de `TipoMensaje` (`@EnumSource`).

### MapaTest
+ getInstance_siempreLaMismaInstancia() : void
+ getZonas_devuelveTOTAL_ZONES() : void
+ vecinos_sonSimetricos() : void
+ getRuta_nodosEnDistintaZona_rutaConexa() : void
+ getRuta_esLaMasCorta_verificadaConDijkstraManual() : void
+ posicion_seguirRuta_avanzaConVelocidadConstante() : void
+ posicion_seguirRuta_conDeltaTimeGrande_noSobrepasaElDestino() : void

// Valida la regla de movimiento: solo por caminos, velocidad constante y ruta entre zonas.

### PartidaTest
Configuración:
+ agregarJugador_tercerJugador_devuelveFalse() : void
+ seleccionarEspecie_mismaEspecieDeLosDosJugadores_seRechaza() : void
+ iniciarPartida_creaSietePacks_dosJugadoresYCincoBots() : void
+ iniciarPartida_packsEnSieteZonasDistintas() : void
+ iniciarPartida_diezCafesColocados() : void

Combate:
+ ataque_restaDamageDelPack_alObjetivo() : void
+ ataque_energiaLlegaACero_animalMuere() : void
+ ataque_antesDelCooldown_noSeEjecuta_devuelveMenosUno() : void
+ ejemploEnunciado_animal20EnergiaVsBot4x10_muereEn5s_yMataUnBot() : void

Bonificaciones:
+ cafe_incrementaDamageDeTodoElPack_yseConsumeUnaSolaVez() : void
+ botsEliminados_cadaDos_suman1Energia_sinSuperarMAX_PLAYER_ENERGY() : void

Victoria y visibilidad:
+ bandera_tocadaPorRival_terminaPartidaYDeclaraGanador() : void
+ verZona_sinAnimalesEnLaZona_seRechaza() : void
+ construirDatos_noIncluyeEntidadesDeZonasNoVisibles() : void

// Se usa seed fija y se avanza el tiempo con `ahora` explícito. Si los hilos de `Partida` dificultan la prueba, se ejercita `decidir(ahora)` directamente.

### ControladorTest
+ crearJuego_devuelveCodigoDeLongitudCorrecta() : void
+ crearJuego_codigosUnicosEntrePartidasActivas() : void
+ unirsePartida_codigoInexistente_devuelveFalse() : void
+ unirsePartida_codigoEnMinusculas_funcionaCaseInsensitive() : void
+ unirsePartida_partidaLlena_devuelveFalse() : void
+ jugadorDesconectado_elOtroJugadorGana() : void
+ finalizarPartida_laRemueveDelMapa() : void

// `Servidor` falso (stub/Mockito) que guarda los mensajes enviados.
// Decisión documentada: si un jugador se desconecta, el rival gana.

---

## Spec Cliente Test

| Paquete | Clase principal | Test |
|---|---|---|
| util | Json | JsonTest |
| red | MessageDispatcher | MessageDispatcherTest |
| modelo | GameModel | GameModelTest |
| controlador | SelectionManager | SelectionManagerTest |

### JsonTest
+ mensajeAJson_envuelveEnTypeYPayload_enUnaSolaLinea() : void
+ jsonAMensaje_typeDesconocido_lanzaIllegalArgumentException() : void
+ jsonAMensaje_payloadNoEncaja_lanzaIllegalArgumentException() : void
+ roundTrip_todosLosTiposDeMensaje() : void
+ deserializar_entidadAnimalCafeBandera_resuelveSubtipoPorTipo() : void
+ deserializar_posicionConNodoNull_noFalla() : void

// Contrato cliente-servidor: guardar en `src/test/resources` un JSON de `DatosEnviar` generado por el servidor y comprobar que el cliente lo deserializa.

### MessageDispatcherTest
+ despachar_matchCreated_invocaOnMatchCreated() : void
+ despachar_gameStarted_invocaOnGameStarted() : void
+ despachar_stateUpdate_invocaActualizarEstado() : void
+ despachar_gameOver_invocaOnGameOver() : void
+ despachar_jsonInvalido_noLanzaExcepcion_ni_tocaElModelo() : void
+ despachar_mensajeClienteAServidor_seDescarta() : void
+ conexionPerdida_invocaOnDesconexion() : void

// Se espera el `invokeLater` con `SwingUtilities.invokeAndWait` vacío, o se inyecta un ejecutor. El modelo es un spy de `GameModel` que registra las llamadas.

### GameModelTest
+ onPrimerMensaje_exitoTrue_nombreConfirmado() : void
+ onPrimerMensaje_exitoFalse_guardaRazonComoUltimoError() : void
+ onMatchJoined_guardaCodigoYJugadores() : void
+ onGameStarted_guardaDatos_yZonaVisibleInicial() : void
+ actualizarEstado_recuerdaAnimalesConocidos() : void
+ getJugadorLocal_identificaPorNickname() : void
+ reiniciarPartida_limpiaElEstado_peroConservaNickname() : void
+ cadaCambio_notificaObservadoresConElTipoMensajeCorrecto() : void

### SelectionManagerTest
+ seleccionar_animalVivoLocal_seAgrega() : void
+ seleccionar_animalMuerto_seRechaza() : void
+ seleccionar_animalDeOtroJugador_seRechaza() : void
+ seleccionar_dosVeces_noDuplica() : void
+ getIdsSeleccionados_devuelveIdPackEIdAnimal() : void
+ sincronizar_quitaAnimalesQueYaNoEstanVivosONiVigentes() : void

---

## Automatización (ambos repositorios)
Workflow `.github/workflows/tests.yml` que ejecuta `mvn test` con Java 21 en cada push y pull request, con `-Djava.awt.headless=true`.

---

