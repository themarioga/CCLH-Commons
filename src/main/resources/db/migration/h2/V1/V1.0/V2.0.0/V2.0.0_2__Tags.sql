-- v2.0.0_2 - Tags

create Game t_tag
(
    tag varchar(255) not null,
    text longtext not null,
    lang_id char(2) not null,
    constraint t_tag_pk
        primary key (tag, lang_id),
    constraint t_tag_t_lang_id_fk
        foreign key (lang_id) references t_lang (id)
);

INSERT INTO t_tag (tag, lang_id, text) VALUES
      ('PLAYER_WELCOME', 'es', '¡Bienvenido! Acabo de añadirte a mi base de datos.\nSiempre que necesites ayuda puedes escribir /help\n¡Gracias por unirte!'),
      ('ALL_MESSAGES_SENT', 'es', 'Se han enviado todos los mensajes.'),
      ('GO_BACK', 'es', '← Volver'),
      ('ACCEPT', 'es', 'Aceptar'),
      ('CANCEL', 'es', 'Cancelar'),
      ('DICTIONARIES_HELP', 'es', 'Bienvenido a la ayuda de {0} versión {1}\n\nPuedes consultar la ayuda en el siguiente enlace: {2}\n\n¡¡Comparte tus diccionarios!!\n\n\nCreado por {3}.'),
      ('DICTIONARIES_MAIN_MENU', 'es', '¡Hola! Soy el bot de diccionarios de Cartas Contra la Humanidad.\nEstas son las acciones que puedes realizar:'),
      ('DICTIONARIES_LIST_BUTTON', 'es', 'Listar mis diccionarios'),
      ('DICTIONARIES_CREATE_BUTTON', 'es', 'Crear diccionario'),
      ('DICTIONARIES_RENAME_BUTTON', 'es', 'Renombrar diccionario'),
      ('DICTIONARIES_DELETE_BUTTON', 'es', 'Borrar diccionario'),
      ('DICTIONARIES_TOGGLE_PUBLISH_BUTTON', 'es', 'Publicar/despublicar diccionario'),
      ('DICTIONARIES_TOGGLE_SHARE_BUTTON', 'es', 'Compartir/descompartir diccionario'),
      ('DICTIONARIES_MANAGE_CARDS_BUTTON', 'es', 'Gestionar cartas'),
      ('DICTIONARIES_MANAGE_COLLABS_BUTTON', 'es', 'Gestionar colaboradores'),
      ('DICTIONARIES_LIST', 'es', 'Estos son los diccionarios que has creado o eres colaborador:\n\n{0}'),
      ('DICTIONARIES_RENAME_LIST', 'es', 'Estos son los diccionarios que has creado:\n\n{0}\n\nAhora respondeme con el número de diccionario que deseas renombrar.'),
      ('DICTIONARIES_DELETE_LIST', 'es', 'Estos son los diccionarios que has creado:\n\n{0}\n\nAhora respondeme con el número de diccionario que deseas borrar.'),
      ('DICTIONARIES_TOGGLE_LIST', 'es', 'Estos son los diccionarios que has creado:\n\n{0}\n\nAhora respondeme con el número de diccionario que deseas publicar/despublicar.'),
      ('DICTIONARIES_SHARE_LIST', 'es', 'Estos son los diccionarios que has creado:\n\n{0}\n\nAhora respondeme con el número de diccionario que deseas compartir/descompartir.'),
      ('DICTIONARIES_MANAGE_CARDS_LIST', 'es', 'Estos son los diccionarios que has creado:\n\n{0}\n\nAhora respondeme con el número de diccionario del que deseas editar las cartas.'),
      ('DICTIONARIES_MANAGE_COLLABORATORS_LIST', 'es', 'Estos son los diccionarios que has creado:\n\n{0}\n\nAhora respondeme con el número de diccionario del que deseas editar los colaboradores.'),
      ('DICTIONARY_CREATE', 'es', '¡Vamos a ello! Dime el nombre del diccionario que quieres crear.'),
      ('DICTIONARY_CREATED', 'es', 'Tu diccionario se ha creado correctamente, ahora ve al menú para añadir las cartas.'),
      ('DICTIONARY_RENAME', 'es', '¡Vamos a ello! Dime el nuevo nombre del diccionario que quieres renombrar.'),
      ('DICTIONARY_RENAMED', 'es', 'Tu diccionario se ha renombrado correctamente.'),
      ('DICTIONARY_TOGGLED_ON', 'es', 'Se ha publicado el diccionario {0} correctamente.'),
      ('DICTIONARY_TOGGLED_OFF', 'es', 'Se ha despublicado el diccionario {0} correctamente.'),
      ('DICTIONARY_SHARED_ON_REQUEST', 'es', 'Se ha solicitado compartir el diccionario {0}.'),
      ('DICTIONARY_SHARED_OFF_REQUEST', 'es', 'Se ha solicitado descompartir el diccionario {0}.'),
      ('DICTIONARY_SHARED_ON', 'es', 'Se ha compartido el diccionario {0} correctamente.'),
      ('DICTIONARY_SHARED_OFF', 'es', 'Se ha descompartido el diccionario {0} correctamente.'),
      ('DICTIONARY_SHARED_REJECTED', 'es', 'Se ha rechazado compartir el diccionario {0}.'),
      ('DICTIONARY_DELETE', 'es', '<b>¡CUIDADO! Este diccionario está PUBLICADO</b>. Se borrará el diccionario y también las <b>cartas</b> asociadas.\n\n¿Estás completamente seguro de que quieres borrarlo?\n· Responde SI para confirmar.\n· Responde cualquier otra cosa o ignora el mensaje para cancelar.'),
      ('DICTIONARY_DELETED', 'es', 'Tu diccionario se ha borrado correctamente.'),
      ('DICTIONARY_DELETE_CANCELLED', 'es', 'Se ha cancelado el borrado del diccionario.'),
      ('CARDS_MENU', 'es', 'Estas gestionando las cartas del diccionario: {0}'),
      ('CARDS_WHITE_LIST_BUTTON', 'es', 'Listar cartas blancas'),
      ('CARDS_BLACK_LIST_BUTTON', 'es', 'Listar cartas negras'),
      ('CARDS_WHITE_ADD_BUTTON', 'es', 'Añadir cartas blancas'),
      ('CARDS_BLACK_ADD_BUTTON', 'es', 'Añadir cartas negras'),
      ('CARDS_WHITE_EDIT_BUTTON', 'es', 'Editar carta blanca'),
      ('CARDS_BLACK_EDIT_BUTTON', 'es', 'Editar carta negra'),
      ('CARDS_WHITE_REMOVE_BUTTON', 'es', 'Borrar carta blanca'),
      ('CARDS_BLACK_REMOVE_BUTTON', 'es', 'Borrar carta negra'),
      ('CARDS_WHITE_LIST', 'es', '¡Marchando! Aqui tienes las cartas blancas. (Es posible que se envien en varios mensajes)'),
      ('CARDS_WHITE_LIST_END', 'es', 'Esas son todas las cartas blancas.'),
      ('CARDS_WHITE_CARD_ADD', 'es', 'Respondeme con el texto de la carta blanca que deseas añadir.'),
      ('CARDS_WHITE_CARD_ADD_ANOTHER', 'es', 'Respondeme con el texto de la carta blanca que deseas añadir o escribe :cancel: para volver al menú.'),
      ('CARDS_WHITE_CARD_ADDED', 'es', 'Carta blanca añadida correctamente. Llevas {0} de {1}.'),
      ('CARDS_WHITE_CARD_EDIT', 'es', 'Respondeme con el número de la carta blanca que deseas editar.'),
      ('CARDS_WHITE_CARD_EDIT_NEW_TEXT', 'es', 'Respondeme con el nuevo texto que quieres para esa carta.'),
      ('CARDS_WHITE_CARD_EDITED', 'es', 'Carta blanca editada correctamente.'),
      ('CARDS_WHITE_CARD_DELETE', 'es', 'Respondeme con el número de la carta blanca que deseas borrar.'),
      ('CARDS_WHITE_CARD_DELETED', 'es', 'Carta blanca borrada correctamente.'),
      ('CARDS_BLACK_LIST', 'es', '¡Marchando! Aqui tienes las cartas negras. (Es posible que se envien en varios mensajes)'),
      ('CARDS_BLACK_LIST_END', 'es', 'Esas son todas las cartas negras.'),
      ('CARDS_BLACK_CARD_ADD', 'es', 'Respondeme con el texto de la carta negra que deseas añadir.'),
      ('CARDS_BLACK_CARD_ADD_ANOTHER', 'es', 'Respondeme con el texto de la carta negra que deseas añadir o escribe :cancel: para volver al menú.'),
      ('CARDS_BLACK_CARD_ADDED', 'es', 'Carta negra añadida correctamente. Llevas {0} de {1}.'),
      ('CARDS_BLACK_CARD_EDIT', 'es', 'Respondeme con el número de la carta blanca que deseas editar.'),
      ('CARDS_BLACK_CARD_EDIT_NEW_TEXT', 'es', 'Respondeme con el nuevo texto que quieres para esa carta.'),
      ('CARDS_BLACK_CARD_EDITED', 'es', 'Carta blanca editada correctamente.'),
      ('CARDS_BLACK_CARD_DELETE', 'es', 'Respondeme con el número de la carta blanca que deseas borrar.'),
      ('CARDS_BLACK_CARD_DELETED', 'es', 'Carta blanca borrada correctamente.'),
      ('COLLABORATORS_MENU', 'es', 'Estas gestionando los colaboradores del diccionario: {0}'),
      ('COLLABORATORS_LIST_BUTTON', 'es', 'Listar colaboradores'),
      ('COLLABORATORS_ADD_BUTTON', 'es', 'Añadir colaborador'),
      ('COLLABORATORS_DELETE_BUTTON', 'es', 'Quitar colaborador'),
      ('COLLABORATORS_TOGGLE_BUTTON', 'es', 'Activar/Desactivar colaborador'),
      ('COLLABORATORS_LIST', 'es', 'Estos son los colaboradores de este diccionario:\n\n{0}'),
      ('COLLABORATORS_ADD', 'es', '¡Vamos a ello! Dime el nombre (p.ej. @cclhbot) o el id (puedes obtenerlo con el comando /getmyid) del usuario que deseas añadir como colaborador.\n\n<b>OJO: El colaborador que desees añadir debe haber iniciado este bot de diccionarios primero (escribiendole /start por privado)</b>'),
      ('COLLABORATORS_ADDED', 'es', 'Se ha enviado una invitación de colaborador a {0} correctamente. Ahora deberá aceptar la invitación.'),
      ('COLLABORATORS_ACCEPT_MESSAGE', 'es', '{0} te ha invitado a colaborar en su diccionario {1}.'),
      ('COLLABORATORS_ACCEPTED_CREATOR', 'es', '{0} ha aceptado ser colaborador.'),
      ('COLLABORATORS_REJECTED_CREATOR', 'es', '{0} ha rechazado ser colaborador.'),
      ('COLLABORATORS_ACCEPTED_MESSAGE', 'es', '¡Perfecto! Ya eres colaborador, ahora pide al creador del bot que te active.'),
      ('COLLABORATORS_REJECTED_MESSAGE', 'es', 'Has rechazado ser colaborador.'),
      ('COLLABORATORS_DELETE', 'es', '¡Vamos a ello! Dime el nombre (p.ej. @cclhbot) o el id (puedes obtenerlo con el comando /getmyid) del usuario que deseas eliminar como colaborador.'),
      ('COLLABORATORS_DELETED', 'es', 'Se ha eliminado el colaborador correctamente.'),
      ('COLLABORATORS_DELETED_MESSAGE', 'es', 'Te han eliminado del diccionario {0}'),
      ('COLLABORATORS_TOGGLE', 'es', '¡Vamos a ello! Dime el nombre (p.ej. @cclhbot) o el id (puedes obtenerlo con el comando /getmyid) del usuario que deseas activar/desactivar como colaborador.'),
      ('COLLABORATORS_TOGGLED_ON', 'es', 'Se ha activado al colaborador {0} correctamente.'),
      ('COLLABORATORS_TOGGLED_OFF', 'es', 'Se ha desactivado al colaborador {0} correctamente.'),
      ('COLLABORATORS_TOGGLED_ON_MESSAGE', 'es', 'Has recibido permisos de edición en el diccionario {0}.'),
      ('COLLABORATORS_TOGGLED_OFF_MESSAGE', 'es', 'Has perdido permisos de edición en el diccionario {0}.'),
      ('GAME_HELP', 'es', '	Bienvenido a la ayuda de {0} versión {1}\n\nPuedes consultar la ayuda en el siguiente enlace: {2}\n\nDisfrutad del bot y... ¡A jugar!\n\nCreado por {3}.'),
      ('GAME_CREATING', 'es', 'Un momentito, estoy creando la partida...'),
      ('GAME_CREATED_GROUP', 'es', '¡Ya he creado la partida!'),
      ('GAME_SELECT_MODE', 'es', '\nSelecciona modo de juego:'),
      ('GAME_PUNCTUATION_MODE', 'es', '\nSelecciona modo de puntuación:'),
      ('GAME_TYPE_ROUNDS_SELECT', 'es', '\nSelecciona rondas de la partida:'),
      ('GAME_TYPE_POINTS_SELECT', 'es', '\nSelecciona nº de puntos para ganar:'),
      ('GAME_SELECTED_MODE', 'es', 'El modo de juego es <b>{0}</b>.'),
      ('GAME_MODE_DEMOCRACY', 'es', 'Democracia'),
      ('GAME_MODE_CLASSIC', 'es', 'Clásico'),
      ('GAME_MODE_DICTATORSHIP', 'es', 'Dictadura'),
      ('GAME_DICTIONARY_SELECT', 'es', '\nSelecciona el mazo:'),
      ('GAME_SELECTED_DICTIONARY', 'es', 'El mazo de cartas seleccionado es <b>{0}</b>.'),
      ('GAME_TYPE_POINTS', 'es', 'Puntos'),
      ('GAME_TYPE_ROUNDS', 'es', 'Rondas'),
      ('GAME_SELECTED_POINTS_TO_WIN', 'es', 'El número de puntos para ganar es <b>{0}</b>.'),
      ('GAME_SELECTED_ROUNDS_TO_END', 'es', 'El número de rondas es <b>{0}</b>.'),
      ('GAME_SELECT_MAX_PLAYERS', 'es', '\nSelecciona nº máximo de jugadores:'),
      ('GAME_SELECTED_MAX_PLAYER_NUMBER', 'es', 'El número máximo de jugadores es <b>{0}</b>.'),
      ('GAME_CREATED_CURRENT_PLAYER_NUMBER', 'es', 'Se han unido estos <b>{0}</b> jugadores:'),
      ('GAME_CREATED_CURRENT_VOTE_DELETION_NUMBER', 'es', '<b>{0}</b> jugador/es han votado borrar la partida.'),
      ('GAME_SELECT_CARD', 'es', '<b>Ronda {0}</b>\n\nLa carta negra de esta ronda es <b>{1}</b>\n\nAhora los jugadores jugaran sus cartas blancas por privado.'),
      ('GAME_VOTE_CARD', 'es', '<b>Ronda {0}</b>\n\nLa carta negra de esta ronda es <b>{1}</b>\n\nLos jugadores eligieron las siguientes cartas blancas:\n\n<b>{2}</b>\n\nAhora los jugadores votaran por privado.'),
      ('GAME_END_ROUND', 'es', '¡Enhorabuena <b>{0}</b>! Tu carta <b>{1}</b> ha ganado la ronda <b>{2}</b>.\n\nLa carta negra de esta ronda era <b>{3}</b>\n\nLos jugadores eligieron las siguientes cartas blancas:\n<b>{4}</b>\n\nLas puntuaciones son las siguientes:\n<b>{5}</b>'),
      ('GAME_END_GAME', 'es', '¡Fin de la partida! El ganador del juego es... <b>{0}</b>. ¡¡¡Enhorabuena!!!'),
      ('GAME_DELETED', 'es', 'Se ha borrado la partida.'),
      ('GAME_DELETION_USER', 'es', 'Se ha borrado la partida de {0}.'),
      ('GAME_DELETION_ALL', 'es', 'Se han borrado todas las partidas.'),
      ('GAME_DELETION_FORCED', 'es', 'Lo sentimos. Su partida ha sido borrada por la administración. Puede crear una partida nueva.'),
      ('GAME_LEAVE', 'es', 'Dejar la partida'),
      ('GAME_LEFT', 'es', 'Has dejado la partida'),
      ('GAME_JOIN_BUTTON', 'es', 'Unirse a la partida'),
      ('GAME_CONFIGURE_BUTTON', 'es', 'Configurar la partida'),
      ('GAME_START_BUTTON', 'es', 'Iniciar la partida'),
      ('GAME_DELETE_BUTTON', 'es', 'Borrar partida'),
      ('GAME_CHANGE_GAME_MODE', 'es', 'Cambiar modo de juego'),
      ('GAME_CHANGE_PUNCTUATION_MODE', 'es', 'Cambiar modo de puntuacion'),
      ('GAME_CHANGE_DICTIONARY', 'es', 'Cambiar mazo de cartas'),
      ('GAME_CHANGE_MAX_N_PLAYERS', 'es', 'Cambiar nº máximo de jugadores'),

      ('PLAYER_CREATED_GAME', 'es', 'He creado la partida en el grupo, puedes configurarla allí'),
      ('PLAYER_JOINING', 'es', 'Un momentito, estoy intentando unirte a la partida...'),
      ('PLAYER_JOINED', 'es', 'Te has unido a la partida.'),
      ('PLAYER_SELECT_CARD', 'es', '	<b>Ronda {0}</b>\n\nLa carta negra de esta ronda es:\n\n<b>{1}</b>\n\nDebes elegir una carta blanca de las siguientes:'),
      ('PLAYER_SELECTED_CARD', 'es', '	<b>Ronda {0}</b>\n\nLa carta negra de esta ronda es:\n\n<b>{1}</b>\n\nHas elegido la carta blanca:\n\n<b>{2}</b>'),
      ('PLAYER_VOTE_CARD', 'es', '	<b>Ronda {0}</b>\n\nLa carta negra de esta ronda es:\n\n<b>{1}</b>\n\nHas elegido la carta blanca:\n\n<b>{2}</b>\n\nDebes votar una carta blanca de las siguientes:'),
      ('PLAYER_VOTED_CARD', 'es', '	<b>Ronda {0}</b>\n\nLa carta negra de esta ronda es:\n\n<b>{1}</b>\n\nHas elegido la carta blanca:\n\n<b>{2}</b>\n\nHas votado la carta blanca:\n\n<b>{3}</b>'),
      ('PLAYER_VOTED_DELETION', 'es', 'Has votado borrar la partida'),

      ('ERROR_COMMAND_SHOULD_BE_ON_PRIVATE', 'es', 'Este comando debe ser enviado por privado al bot.'),
      ('ERROR_COMMAND_SHOULD_BE_ON_GROUP', 'es', 'Este comando debe ser enviado en un grupo en el que esté el bot.'),
      ('ERROR_UNKNOWN', 'es', 'Error desconocido'),
      ('ERROR_USER_ALREADY_REGISTERED', 'es', 'Ya estas registrado. Consulta /help para mas información.'),
      ('ERROR_DICTIONARY_ALREADY_EXISTS', 'es', 'Ya existe un diccionario con este nombre.'),
      ('ERROR_DICTIONARY_NOT_FOUND', 'es', 'Ese diccionario no existe.'),
      ('ERROR_DICTIONARY_NOT_YOURS', 'es', 'Ese diccionario no te pertenece.'),
      ('ERROR_DICTIONARY_NOT_FILLED', 'es', 'No puedes publicar el diccionario porque faltan cartas de algun tipo.'),
      ('ERROR_DICTIONARY_NOT_PUBLISHED', 'es', 'No puedes compartir un diccionario que no está publicado. Publícalo para poder compartirlo.'),
      ('ERROR_DICTIONARY_ALREADY_FILLED', 'es', 'No puedes añadir mas cartas de este tipo a este diccionario.'),
      ('ERROR_DICTIONARY_ALREADY_PUBLISHED', 'es', 'No puedes editar un diccionario que ya está publicado. Despublicalo para poder editarlo.'),
      ('ERROR_DICTIONARY_ALREADY_SHARED', 'es', 'No puedes modificar un diccionario que está compartido. Contacta con {0} para despublicarlo.'),
      ('ERROR_DICTIONARY_SHARED', 'es', 'El diccionario está compartido, contacta a {0} para borrarlo.'),
      ('ERROR_COLLABORATOR_DOESNT_EXISTS', 'es', 'El usuario es incorrecto o no está registrado en el bot.'),
      ('ERROR_COLLABORATOR_ADD_USER_DOESNT_EXISTS', 'es', 'El usuario que intentas añadir como colaborador es incorrecto o no está registrado en el bot.'),
      ('ERROR_COLLABORATOR_REMOVE_USER_DOESNT_EXISTS', 'es', 'El usuario que intentas eliminar como colaborador es incorrecto o no es colaborador en este diccionario.'),
      ('ERROR_COLLABORATOR_ADD_ALREADY_EXISTS', 'es', 'El usuario que intentas añadir ya es colaborador en este diccionario.'),
      ('ERROR_COLLABORATOR_ADD_MAX_REACHED', 'es', 'Has alcanzado el número máximo de colaboradores y no puedes añadir más.'),
      ('ERROR_CARD_ALREADY_EXISTS', 'es', 'Ya existe otra carta con el mismo texto en este diccionario.'),
      ('ERROR_CARD_EXCEEDED_LENGTH', 'es', 'La carta supera el máximo de {0} caracteres.'),
      ('ERROR_CARD_NOT_YOURS', 'es', 'Esa carta no te pertenece.'),
      ('ERROR_CARD_DOESNT_EXISTS', 'es', 'La carta que intentas editar no existe.'),
      ('ERROR_GAME_ALREADY_CREATED', 'es', 'Ya existe un juego activo en este grupo o el creador tiene un juego activo en otro grupo.'),
      ('ERROR_GAME_ALREADY_FILLED', 'es', 'Ya se ha superado el número máximo de jugadores.'),
      ('ERROR_GAME_ALREADY_STARTED', 'es', 'La partida ya está iniciada.'),
      ('ERROR_GAME_ONLY_CREATOR_CAN_CONFIGURE', 'es', 'Solo el creador de la partida puede configurarla.'),
      ('ERROR_GAME_ONLY_CREATOR_CAN_DELETE', 'es', 'Solo el creador de la partida puede borrarla.'),
      ('ERROR_GAME_ONLY_CREATOR_CAN_START', 'es', 'Solo el creador de la partida puede iniciarla.'),
      ('ERROR_GAME_DOESNT_EXISTS', 'es', 'No hay ninguna partida iniciada en este grupo.'),
      ('ERROR_GAME_USER_DOESNT_EXISTS', 'es', 'No estas registrado. Escribeme /start por privado.'),
      ('ERROR_GAME_NOT_FILLED', 'es', 'La partida aun no ha llegado al numero minimo de jugadores.'),
      ('ERROR_GAME_NOT_STARTED', 'es', 'La partida aun no se ha iniciado.'),
      ('ERROR_PLAYER_NO_GAMES', 'es', 'No tienes ninguna partida activa.'),
      ('ERROR_PLAYER_ALREADY_PLAYING', 'es', 'Ya estas participando en una partida.'),
      ('ERROR_PLAYER_ALREADY_JOINED', 'es', 'Ya estas participando en esta partida.'),
      ('ERROR_PLAYER_DOES_NOT_EXISTS', 'es', 'No estas unido a ninguna partida.'),
      ('ERROR_PLAYER_ALREADY_PLAYED_CARD', 'es', 'Ya has jugado una carta.'),
      ('ERROR_PLAYER_ALREADY_VOTED_CARD', 'es', 'Ya has votado una carta.'),
      ('ERROR_PLAYER_ALREADY_VOTED_DELETION', 'es', 'Ya has votado una carta.'),
      ('ERROR_CANT_FIND_WINNER_CARD', 'es', 'No se ha localizado ninguna carta ganadora');
