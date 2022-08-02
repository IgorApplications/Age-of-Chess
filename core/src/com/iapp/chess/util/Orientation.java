package com.iapp.chess.util;

import com.iapp.chess.view.BoardMatrix;

public class Orientation {

    public static float cameraWidth;
    public static float cameraHeight;

    /**
     * SplashScreen*
     */
    public static float grayLineHeight;
    public static float grayWhiteLineHeight;

    public static float logoX;
    public static float logoY;
    public static float logoWidth;
    public static float logoHeight;

    public static float bootTextX;
    public static float bootTextY;

    /**
     * MainView
     * */
    public static float mainUIDialogWidth;
    public static float mainUIDialogHeight;
    public static float mainUIDialogX;
    public static float mainUIDialogY;

    /**
     * GameView
     */
    public static float boardX;
    public static float boardY;
    public static float boardWidth;
    public static float boardHeight;

    public static float buttonMenuX;
    public static float buttonMenuY;
    public static float buttonMenuWidth;
    public static float buttonMenuHeight;

    public static float buttonDescriptionX;
    public static float buttonDescriptionY;
    public static float buttonDescriptionWidth;
    public static float buttonDescriptionHeight;

    public static float buttonReplayX;
    public static float buttonReplayY;
    public static float buttonReplayWidth;
    public static float buttonReplayHeight;

    public static float buttonBackX;
    public static float buttonBackY;
    public static float buttonBackWidth;
    public static float buttonBackHeight;

    public static float buttonHintX;
    public static float buttonHintY;
    public static float buttonHintWidth;
    public static float buttonHintHeight;

    public static float labelLevelFontScale;

    public static float labelCountMovesX;
    public static float labelCountMovesY;
    public static float labelCountMovesFontScale;

    public static float headerY;

    public static float figureSize;
    public static float boardEdge;
    public static float figureSpriteSize;

    public static float figureMarginX;
    public static float figureMarginY;
    public static float flippedMarginY;

    public static float moveMarginX;
    public static float moveMarginY;

    public static float blackLineY;
    public static float blackLineHeight;

    /**
     * DialogView
     * */
    public static float finishDialogX;
    public static float finishDialogY;

    public static float finishDialogWidth;
    public static float finishDialogHeight;
    public static float choiceDialogX;
    public static float choiceDialogY;

    public static float finishDialogWidthLine;
    public static float finishDialogMarginCancel;

    public static float replayDialogX;
    public static float replayDialogY;

    public static float menuDialogX;
    public static float menuDialogY;

    public static float descriptionDialogX;
    public static float descriptionDialogY;

    public static float questionAboutBuyingX;
    public static float questionAboutBuyingY;

    /**
     * SettingsView
     * */
    public static float uiDialogX;
    public static float uiDialogY;
    public static float uiDialogWidth;
    public static float uiDialogHeight;

    public void init(Type type) {
        initWindowOrientation(type);
        initCamera(type);
        initSplashScreen(type);
        initMainView(type);
        initGameView(type);
        initDialogView(type);
        initSettingsView(type);
    }

    public void initWindowOrientation(Type type) {
        if (type == Type.HORIZONTAL) Settings.launcher.setRequestedHorizontally();
        else Settings.launcher.setRequestedVertically();
    }

    public void initCamera(Type type) {
        cameraWidth = Position.CAMERA_WIDTH.getValue(type);
        cameraHeight = Position.CAMERA_HEIGHT.getValue(type);
    }

    public void initSplashScreen(Type type) {
        grayLineHeight = Position.GRAY_LINE_HEIGHT.getValue(type);
        grayWhiteLineHeight = Position.GRAY_WHITE_LINE_HEIGHT.getValue(type);

        logoX = Position.LOGO_X.getValue(type);
        logoY = Position.LOGO_Y.getValue(type);
        logoWidth = Position.LOGO_WIDTH.getValue(type);
        logoHeight = Position.LOGO_HEIGHT.getValue(type);

        bootTextX = Position.BOOT_TEXT_X.getValue(type);
        bootTextY = Position.BOOT_TEXT_Y.getValue(type);
    }

    public void initGameView(Type type) {
        boardX = Position.BOARD_X.getValue(type);
        boardY = Position.BOARD_Y.getValue(type);
        boardWidth = Position.BOARD_WIDTH.getValue(type);
        boardHeight = Position.BOARD_HEIGHT.getValue(type);

        buttonMenuX = Position.BUTTON_MENU_X.getValue(type);
        buttonMenuY = Position.BUTTON_MENU_Y.getValue(type);
        buttonMenuWidth = Position.BUTTON_MENU_WIDTH.getValue(type);
        buttonMenuHeight = Position.BUTTON_MENU_HEIGHT.getValue(type);

        buttonDescriptionX = Position.BUTTON_DESCRIPTION_X.getValue(type);
        buttonDescriptionY = Position.BUTTON_DESCRIPTION_Y.getValue(type);
        buttonDescriptionWidth = Position.BUTTON_DESCRIPTION_WIDTH.getValue(type);
        buttonDescriptionHeight = Position.BUTTON_DESCRIPTION_HEIGHT.getValue(type);

        buttonReplayX = Position.BUTTON_REPLAY_X.getValue(type);
        buttonReplayY = Position.BUTTON_REPLAY_Y.getValue(type);
        buttonReplayWidth = Position.BUTTON_REPLAY_WIDTH.getValue(type);
        buttonReplayHeight = Position.BUTTON_REPLAY_HEIGHT.getValue(type);

        buttonBackX = Position.BUTTON_BACK_X.getValue(type);
        buttonBackY = Position.BUTTON_BACK_Y.getValue(type);
        buttonBackWidth = Position.BUTTON_BACK_WIDTH.getValue(type);
        buttonBackHeight = Position.BUTTON_BACK_HEIGHT.getValue(type);

        buttonHintX = Position.BUTTON_HINT_X.getValue(type);
        buttonHintY = Position.BUTTON_HINT_Y.getValue(type);
        buttonHintWidth = Position.BUTTON_HINT_WIDTH.getValue(type);
        buttonHintHeight = Position.BUTTON_HINT_HEIGHT.getValue(type);

        labelCountMovesFontScale = Position.LABEL_LEVEL_FONT_SCALE.getValue(type);
        headerY = Position.HEADER_Y.getValue(type);

        figureSize = Position.FIGURE_SIZE.getValue(type);
        boardEdge = Position.BOARD_EDGE.getValue(type);
        figureSpriteSize = Position.FIGURE_SPRITE_SIZE.getValue(type);

        figureMarginX = Position.FIGURE_MARGIN_X.getValue(type);
        figureMarginY = Position.FIGURE_MARGIN_Y.getValue(type);
        flippedMarginY = Position.FLIPPED_MARGIN_Y.getValue(type);

        moveMarginX = Position.MOVE_MARGIN_X.getValue(type);
        moveMarginY = Position.MOVE_MARGIN_Y.getValue(type);

        blackLineY = Position.BLACK_LINE_Y.getValue(type);
        blackLineHeight = Position.BLACK_LINE_HEIGHT.getValue(type);

        BoardMatrix.reInit();
    }

    public void initMainView(Type type) {
        mainUIDialogWidth = Position.MAIN_UI_DIALOG_WIDTH.getValue(type);
        mainUIDialogHeight = Position.MAIN_UI_DIALOG_HEIGHT.getValue(type);
        mainUIDialogX = Position.MAIN_UI_DIALOG_X.getValue(type);
        mainUIDialogY = Position.MAIN_UI_DIALOG_Y.getValue(type);
    }

    public void initDialogView(Type type) {
        finishDialogX = Position.FINISH_DIALOG_X.getValue(type);
        finishDialogY = Position.FINISH_DIALOG_Y.getValue(type);

        finishDialogWidth = Position.FINISH_DIALOG_WIDTH.getValue(type);
        finishDialogHeight = Position.FINISH_DIALOG_HEIGHT.getValue(type);

        finishDialogWidthLine = Position.FINISH_WIDTH_DIALOG_LINE.getValue(type);
        finishDialogMarginCancel = Position.FINISH_MARGIN_CANCEL.getValue(type);

        choiceDialogX = Position.CHOICE_DIALOG_X.getValue(type);
        choiceDialogY = Position.CHOICE_DIALOG_Y.getValue(type);

        replayDialogX = Position.REPLAY_DIALOG_X.getValue(type);
        replayDialogY = Position.REPLAY_DIALOG_Y.getValue(type);

        menuDialogX = Position.MENU_DIALOG_X.getValue(type);
        menuDialogY = Position.MENU_DIALOG_Y.getValue(type);

        descriptionDialogX = Position.DESCRIPTION_DIALOG_X.getValue(type);
        descriptionDialogY = Position.DESCRIPTION_DIALOG_Y.getValue(type);

        questionAboutBuyingX = Position.QUESTION_ABOUT_BUYING_X.getValue(type);
        questionAboutBuyingY = Position.QUESTION_ABOUT_BUYING_Y.getValue(type);

        labelCountMovesX = Position.LABEL_COUNT_MOVES_X.getValue(type);
        labelCountMovesY = Position.LABEL_COUNT_MOVES_Y.getValue(type);
        labelLevelFontScale = Position.LABEL_COUNT_MOVES_FONT_SCALE.getValue(type);
    }

    public void initSettingsView(Type type) {
        uiDialogX = Position.UI_DIALOG_X.getValue(type);
        uiDialogY = Position.UI_DIALOG_Y.getValue(type);
        uiDialogWidth = Position.UI_DIALOG_WIDTH.getValue(type);
        uiDialogHeight = Position.UI_DIALOG_HEIGHT.getValue(type);
    }

    public enum Type {
        VERTICAL, HORIZONTAL
    }

    /**
     * Chess game position
     */
    private enum Position {
        CAMERA_WIDTH(900, 500),
        CAMERA_HEIGHT(500, 900),

        /**
         * SplashScreen
         */
        GRAY_LINE_HEIGHT(60, 60),
        GRAY_WHITE_LINE_HEIGHT(60, 60),

        LOGO_X(150, 0),
        LOGO_Y(130, 350),
        LOGO_WIDTH(600, 500),
        LOGO_HEIGHT(300, 280),

        BOOT_TEXT_X(300, 110),
        BOOT_TEXT_Y(45, 45),

        /**
         * MainView
         * */
        MAIN_UI_DIALOG_WIDTH(490, 490),
        MAIN_UI_DIALOG_HEIGHT(490, 650),
        MAIN_UI_DIALOG_X(200, 5),
        MAIN_UI_DIALOG_Y(5, 115),

        /**
         * GameView
         **/
        BOARD_EDGE(8.4_939_759f, 9.036_144_578f),
        FIGURE_SIZE(56.62_650_602f, 60.24_096_386f),
        FIGURE_SPRITE_SIZE(50, 53.19f),

        FIGURE_MARGIN_X(3.3f, 3.51f),
        FIGURE_MARGIN_Y(0.4f, 0.426f),
        FLIPPED_MARGIN_Y(5.9f, 5.7f),

        MOVE_MARGIN_X(28.25f, 30),
        MOVE_MARGIN_Y(27.25f, 28.5f),

        BOARD_WIDTH(470, 500),
        BOARD_HEIGHT(470, 500),
        BOARD_X(240, 0),
        BOARD_Y(15, 215),

        BUTTON_REPLAY_X(10, 25),
        BUTTON_REPLAY_Y(360, 100),
        BUTTON_REPLAY_WIDTH(60, 60),
        BUTTON_REPLAY_HEIGHT(60, 60),

        BUTTON_BACK_X(830, 115),
        BUTTON_BACK_Y(430, 100),
        BUTTON_BACK_WIDTH(60, 60),
        BUTTON_BACK_HEIGHT(60, 60),

        BUTTON_DESCRIPTION_X(830, 215),
        BUTTON_DESCRIPTION_Y(360, 100),
        BUTTON_DESCRIPTION_WIDTH(60, 60),
        BUTTON_DESCRIPTION_HEIGHT(60, 60),

        BUTTON_HINT_X(830, 315),
        BUTTON_HINT_Y(290, 100),
        BUTTON_HINT_WIDTH(60, 60),
        BUTTON_HINT_HEIGHT(60, 60),

        BUTTON_MENU_X(10, 415),
        BUTTON_MENU_Y(430, 100),
        BUTTON_MENU_WIDTH(60, 60),
        BUTTON_MENU_HEIGHT(60, 60),

        LABEL_LEVEL_FONT_SCALE(0.3f, 0.5f),

        LABEL_COUNT_MOVES_X(630, 200),
        LABEL_COUNT_MOVES_Y(492, 20),
        LABEL_COUNT_MOVES_FONT_SCALE(0.3f, 0.5f),

        HEADER_Y(492, 850),

        BLACK_LINE_HEIGHT(80, 80),
        BLACK_LINE_Y(410, 810),

        /**
         * DialogView
         * */
        FINISH_DIALOG_X(290, 50),
        FINISH_DIALOG_Y(20, 220),
        FINISH_DIALOG_WIDTH(370, 400),
        FINISH_DIALOG_HEIGHT(460, 490),
        FINISH_WIDTH_DIALOG_LINE(320, 347),
        FINISH_MARGIN_CANCEL(8, 11),

        CHOICE_DIALOG_X(270, 45),
        CHOICE_DIALOG_Y(90, 305),

        REPLAY_DIALOG_X(246, 25),
        REPLAY_DIALOG_Y(100, 310),

        MENU_DIALOG_X(245, 25),
        MENU_DIALOG_Y(100, 310),

        DESCRIPTION_DIALOG_X(225, 0),
        DESCRIPTION_DIALOG_Y(370, 600),

        QUESTION_ABOUT_BUYING_X(210, 12.5f),
        QUESTION_ABOUT_BUYING_Y(100, 280),

        /**
         * SettingsView
         * */
        UI_DIALOG_X(200, 5),
        UI_DIALOG_Y(5, 90),
        UI_DIALOG_WIDTH(490, 490),
        UI_DIALOG_HEIGHT(490, 690);

        final float horizontal;
        final float vertical;

        float getValue(Type type) {
            if (type == Type.HORIZONTAL) return horizontal;
            return vertical;
        }

        Position(float horizontal, float vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }
    }
}
