package com.iapp.chess.standart_ui;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.iapp.chess.util.Settings;

public class QuestionDialog extends Dialog {

    private Label title;
    private TextButton positiveButton;
    private TextButton negativeButton;
    private Label question;
    private ImageButton cancelButton;


    public QuestionDialog(Skin skin) {
        super("", skin);
    }

    public QuestionDialog setTitle(String titleText) {
        title = new Label(titleText, Settings.gdxGame.getUIKit());
        title.setFontScale(0.3f);
        title.setColor(Color.BLACK);

        return this;
    }

    public QuestionDialog setPositive(String text, ChangeListener action) {
        positiveButton = new TextButton(text, Settings.gdxGame.getUIKit(), "button");
        positiveButton.getLabel().setFontScale(0.5f);
        positiveButton.addListener(action);

        return this;
    }

    public QuestionDialog setNegative(String text, ChangeListener action) {
        negativeButton = new TextButton(text, Settings.gdxGame.getUIKit(), "button");
        negativeButton.getLabel().setFontScale(0.5f);
        negativeButton.addListener(action);

        return this;
    }

    public QuestionDialog setQuestion(String questionText) {
        question = new Label(questionText, Settings.gdxGame.getLabelSkin());
        question.setWrap(true);
        question.setFontScale(0.5f);

        return this;
    }

    public QuestionDialog setOnCancel(ChangeListener action) {
        cancelButton = new ImageButton(Settings.gdxGame.getUIKit(), "cancel");
        cancelButton.addListener(action);

        return this;
    }

    public void build() {
        getTitleTable().add(title).padRight(330).padTop(20);
        getTitleTable().add(cancelButton).size(18, 17).padTop(26).padRight(13);

        getContentTable().add(question).width(380).padBottom(90);

        getButtonTable().add(negativeButton).size(180, 33).center().padBottom(13.65f);
        getButtonTable().add(positiveButton).size(180, 33).center().padBottom(13.65f);
    }
}
