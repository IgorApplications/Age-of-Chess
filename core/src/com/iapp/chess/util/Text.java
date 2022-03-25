package com.iapp.chess.util;

import com.sun.org.apache.bcel.internal.generic.PUSH;

import javax.swing.plaf.PanelUI;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.Objects;

public class Text {

    /**
     * MainView
     * */
    public static final String TITLE;
    public static final String PLAY;
    public static final String NOVICE_LEVEL;
    public static final String EASY_LEVEL;
    public static final String CLOSE;
    public static final String TWO_PLAYERS_LEVEL;
    public static final String SETTINGS;
    public static final String LEVEL_SELECTION;

    /**
     * GameView
     * */
    public static final String CANCEL;
    public static final String QUESTION_EXIT_MENU;
    public static final String YES;
    public static final String MENU;
    public static final String ON_THE_MENU;
    public static final String VICTORY;
    public static final String LOSE;
    public static final String DRAW;
    public static final String BLACK_VICTORY;
    public static final String WHITE_VICTORY;
    public static final String NEW_GAME;
    public static final String ROOK;
    public static final String KNIGHT;
    public static final String BISHOP;
    public static final String QUEEN;
    public static final String TRANSFORM_PAWN;
    public static final String MOVE_BLACK;
    public static final String MOVE_WHITE;
    public static final String NOVICE_LEVEL_CAPS;
    public static final String EASY_LEVEL_CAPS;
    public static final String NEXT;
    public static final String REPLAY;
    public static final String TWO_PLAYERS_LEVEL_CAPS;
    public static final String MIDDLE_LEVEL;
    public static final String MIDDLE_LEVEL_CAPS;
    public static final String HARD_LEVEL;
    public static final String HARD_LEVEL_CAPS;
    public static final String MASTER_LEVEL;
    public static final String MASTER_LEVEL_CAPS;
    public static final String SAVE;
    public static final String BACK;

    /**
     * SettingsView
     **/
    public static final String GAME_MODE;
    public static final String ORIENTATION_SCREEN;
    public static final String SAVE_WINDOW_SIZE;

    public static final String AI_COLOR;
    public static final String GRAPHICS_EFFECTS;
    public static final String OUTLINE_FIGURES;
    public static final String OUTLINE_FELLED_FIGURE;
    public static final String MOVE_HINTS;
    public static final String CASTLE_HINTS;
    public static final String OUTLINE_CHECK;
    public static final String GREEN_CROSS;
    public static final String SELECTION_FIGURES;

    public static final String SOUND_EFFECTS;
    public static final String SOUND_PRESSING;
    public static final String SOUND_MOVE;
    public static final String SOUND_VICTORY;
    public static final String LATEST_SOUND_VICTORY;
    public static final String SOUND_CASTLE;
    public static final String SOUND_CHECK;
    public static final String SOUND_LOSE;

    public static final String HORIZONTAL;
    public static final String VERTICAL;
    public static final String BLACK;
    public static final String WHITE;
    public static final String RANDOM;

    public static final String USER_NOVICE;
    public static final String USER_PLAYER;
    public static final String USER_ADVANCED;
    public static final String USER_EXPERIENCED;
    public static final String USER_MASTER;

    /**
     * DialogView
     * */
    public static final String GAME_MENU;
    public static final String REPLAY_TEXT;
    public static final String MENU_TEXT;
    public static final String QUESTION_ABOUT_BUYING;

    public static final String FINISHED_GAME_IN;
    public static final String MOVES;
    public static final String REWARD;
    public static final String DEFEATED_AI;
    public static final String DEFEATED_OPPONENT;
    public static final String BEST_SCORE;
    public static final String BEST_SCORE_AT_LEVEL;
    public static final String LOSS_TEXT;
    public static final String DRAW_TEXT;
    public static final String NEVER_VICTORY;

    private static String sysLang;

    static {
        sysLang = Locale.getDefault().getLanguage();

        if (Objects.equals(sysLang, "ru")) {
            TITLE = "Шахматы";
            PLAY = "Играть";
            NOVICE_LEVEL = "уровень\nновичка";
            EASY_LEVEL = "лёгкий\nуровень";
            CLOSE = "закрыт";
            TWO_PLAYERS_LEVEL = "два\nигрока";
            SETTINGS = "Настройки";
            LEVEL_SELECTION = "Выбор уровней";

            QUESTION_EXIT_MENU = "Вы хотите выйти в меню?";
            CANCEL = "Отмена";
            MENU = "Меню";
            ON_THE_MENU = "В Меню";
            VICTORY = "ВЫ ПОБЕДИЛИ!";
            LOSE = "ВЫ ПРОИГРАЛИ!";
            DRAW = "НИЧЬЯ";
            BLACK_VICTORY = "ЧЁРНЫЕ ПОБЕДИЛИ!";
            WHITE_VICTORY = "БЕЛЫЕ ПОБЕДИЛИ!";
            NEW_GAME = "Новая игра";
            ROOK = "Ладья";
            KNIGHT = "Конь";
            BISHOP = "Слон";
            QUEEN = "Королева";
            TRANSFORM_PAWN = "Превратить пешку в ...?";
            MOVE_BLACK = "Ход чёрных";
            MOVE_WHITE = "Ход белых";
            NOVICE_LEVEL_CAPS = "УРОВЕНЬ НОВИЧКА";
            EASY_LEVEL_CAPS = "ЛЁГКИЙ УРОВЕНЬ";
            NEXT = "Дальше";
            REPLAY = "Заново";
            TWO_PLAYERS_LEVEL_CAPS = "ДВА ИГРОКА";
            MIDDLE_LEVEL = "средний\nуровень";
            MIDDLE_LEVEL_CAPS = "СРЕДНИЙ УРОВЕНЬ";
            HARD_LEVEL = "сложный\nуроень";
            HARD_LEVEL_CAPS = "СЛОЖНЫЙ УРОВЕНЬ";
            MASTER_LEVEL = "уровень\nмастера";
            MASTER_LEVEL_CAPS = "УРОВЕНЬ МАСТЕРА";
            SAVE = "Сохранить";
            YES = "Да";
            BACK = "Назад";

            GAME_MODE = "Режим игры";
            ORIENTATION_SCREEN = "Ориентация экрана";
            SAVE_WINDOW_SIZE = "Сохранять размеры окна";

            AI_COLOR = "Цвет фигур ИИ";
            GRAPHICS_EFFECTS = "Графические эффекты";
            OUTLINE_FIGURES = "Обводка фигур";
            OUTLINE_FELLED_FIGURE = "Обводка срубленных фигур";
            MOVE_HINTS = "Посказки ходов";
            CASTLE_HINTS = "Посказки ракировки";
            OUTLINE_CHECK = "Обводка при шахе";
            GREEN_CROSS = "Значок зелёного креста";
            SELECTION_FIGURES = "Выбор набора фигура";

            SOUND_EFFECTS = "Звуковые эффекты";
            SOUND_PRESSING = "Звук нажатия";
            SOUND_MOVE = "Звук хода";
            SOUND_VICTORY = "Звук победы";
            LATEST_SOUND_VICTORY = "Звук победы на последнем уровне";
            SOUND_CASTLE = "Звук ракировки";
            SOUND_CHECK = "Звук шаха";
            SOUND_LOSE = "Звук поражения";

            HORIZONTAL = "Горизонтально";
            VERTICAL = "Вертикально";
            BLACK = "Чёрные";
            WHITE = "Белые";
            RANDOM = "Случайно";

            USER_NOVICE = "Новичок";
            USER_PLAYER = "Игрок";
            USER_ADVANCED = "Продвинутый";
            USER_EXPERIENCED = "Опытный";
            USER_MASTER = "Мастер";

            GAME_MENU = "Игровое меню";
            REPLAY_TEXT = "Игра прервана. Вы хотите начать текущую игру заново?";
            MENU_TEXT = "Игра прервана. Вы хотите покинуть игру? Выберите режим выхода в меню.";
            QUESTION_ABOUT_BUYING = "Вы действительно хотите купить данный набор шахмат за 1000 монет?";

            FINISHED_GAME_IN = "Вы закончили игру за ";
            MOVES = " хода/ов";
            REWARD = "Награда за данную игру:";
            DEFEATED_AI = "Вы победили искуственный интеллект:";
            DEFEATED_OPPONENT = "Вы победили своего соперника:";
            BEST_SCORE = "Это ваш лучший результат на данном уровне!";
            BEST_SCORE_AT_LEVEL = "Ваш лучший результат на данном уровне: ";
            DRAW_TEXT = "К сожалению вы несмогли победить своего соперника. Игра завершена, так как больше нет возможных ходов.";
            LOSS_TEXT = "К сожалению, вы проиграли. В следующий раз возможно вам повезет.";
            NEVER_VICTORY = "ы ещё не разу не смогли пройти этот уровень.";
        } else {
            TITLE = "Chess";
            PLAY = "Play";
            NOVICE_LEVEL = "level\nnovice";
            EASY_LEVEL = "level\neasy";
            CLOSE = "close";
            TWO_PLAYERS_LEVEL = "two\nplayers";
            SETTINGS = "Settings";
            LEVEL_SELECTION = "Level selection";

            QUESTION_EXIT_MENU = "Do you want to go to the\n" + "             " + "menu?";
            CANCEL = "Cancel";
            MENU = "Menu";
            ON_THE_MENU = "Menu";
            VICTORY = "YOU WON!";
            LOSE = "YOU LOST!";
            DRAW = "DRAW";
            BLACK_VICTORY = "BLACK WON!";
            WHITE_VICTORY = "WHITE WON!";
            NEW_GAME = "New game";
            ROOK = "Rook";
            KNIGHT = "Knight";
            BISHOP = "Bishop";
            QUEEN = "Queen";
            TRANSFORM_PAWN = "turn a pawn into...?";
            MOVE_BLACK = "Move black";
            MOVE_WHITE = "Move white";
            NOVICE_LEVEL_CAPS = "LEVEL NOVICE";
            EASY_LEVEL_CAPS = "LEVEL EASY";
            NEXT = "Next";
            REPLAY = "Replay";
            TWO_PLAYERS_LEVEL_CAPS = "TWO PLAYERS";
            MIDDLE_LEVEL = "middle\nlevel";
            MIDDLE_LEVEL_CAPS = "MIDDLE LEVEL";
            HARD_LEVEL = "hard\nlevel";
            HARD_LEVEL_CAPS = "HARD LEVEL";
            MASTER_LEVEL = "level\nmaster";
            MASTER_LEVEL_CAPS = "LEVEL MASTER";
            SAVE = "Save";
            YES = "Yes";
            BACK = "Back";

            GAME_MODE = "Game mode";
            ORIENTATION_SCREEN = "Orientation screen";
            SAVE_WINDOW_SIZE = "Save window size";

            AI_COLOR = "AI figure color";
            GRAPHICS_EFFECTS = "Graphics effects";
            OUTLINE_FIGURES = "Outline figures";
            OUTLINE_FELLED_FIGURE = "Outline felled figures";
            MOVE_HINTS = "Move hints";
            CASTLE_HINTS = "Castle hints";
            OUTLINE_CHECK = "Outline at check";
            GREEN_CROSS = "Green cross icon";
            SELECTION_FIGURES = "Selecting a set of figures";

            SOUND_EFFECTS = "Sound effects";
            SOUND_PRESSING = "Sound pressing";
            SOUND_MOVE = "Sound move";
            SOUND_VICTORY = "Sound victory";
            LATEST_SOUND_VICTORY = "Sound victory at the last level";
            SOUND_CASTLE = "Sound castle";
            SOUND_CHECK = "Sound check";
            SOUND_LOSE = "Sound lose";

            HORIZONTAL = "Horizontal";
            VERTICAL = "Vertical";
            BLACK = "Black";
            WHITE = "White";
            RANDOM = "Random";

            USER_NOVICE = "Novice";
            USER_PLAYER = "Player";
            USER_ADVANCED = "Advanced";
            USER_EXPERIENCED = "Experienced";
            USER_MASTER = "Master";

            GAME_MENU = "Game menu";
            REPLAY_TEXT = "The game has been interrupted. Do you want to restart the current game?";
            MENU_TEXT = "The game has been interrupted. Do you want to leave the game? Select the exit mode in the menu.";
            QUESTION_ABOUT_BUYING = "Are you sure you want to buy this chess set for 1000 coins?";

            FINISHED_GAME_IN = "You finished the game in ";
            MOVES = " moves";
            REWARD = "Reward for this game:";
            DEFEATED_AI = "You have defeated artificial intelligence:";
            DEFEATED_OPPONENT = "You defeated your opponent:";
            BEST_SCORE = "This is your best score at this level!";
            BEST_SCORE_AT_LEVEL = "Your best score at this level: ";
            DRAW_TEXT = "Unfortunately, you could not defeat your opponent. The game is over because there are no more possible moves.";
            LOSS_TEXT = "Unfortunately, you lost. You might be lucky next time.";
            NEVER_VICTORY = "So far, you have never been able to pass this level.";
        }

    }

    private Text() {
        throw new AssertionError();
    }
}
