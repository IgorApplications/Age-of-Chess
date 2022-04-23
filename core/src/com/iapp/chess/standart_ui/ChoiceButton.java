package com.iapp.chess.standart_ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.iapp.chess.util.Settings;

public class ChoiceButton extends Group {

    private ButtonGroup<TextButton> group = new ButtonGroup<>();

    public ChoiceButton(String... textArr) {
        if (textArr.length < 2) throw new IllegalArgumentException();

        TextButton choiceStart = new TextButton(textArr[0], Settings.gdxGame.getUIKit(), "choice1");
        addChoice(choiceStart);

        for (int i = 1; i < textArr.length - 1; i++) {
            TextButton choice = new TextButton(textArr[i], Settings.gdxGame.getUIKit(), "choice2");
            addChoice(choice);
        }
        TextButton choiceEnd = new TextButton(textArr[textArr.length - 1], Settings.gdxGame.getUIKit(), "choice3");
        addChoice(choiceEnd);
    }

    public void setFontScale(float scale) {
        for (TextButton textButton : group.getButtons()) {
            textButton.getLabel().setFontScale(scale);
        }
    }

    @Override
    public void setSize(float width, float height) {
        Array<TextButton> array = group.getButtons();
        float btnWidth = Math.round(width / array.size);

        for (int i = 0; i < array.size; i++) {
            TextButton textButton = array.get(i);
            textButton.setX(btnWidth * i);
            textButton.setSize(btnWidth, height);
            textButton.getLabel().setSize(btnWidth, height);
        }

        super.setSize(width, height);
    }

    public TextButton getChecked() {
        return group.getChecked();
    }

    public int getCheckedIndex() {
        return group.getCheckedIndex();
    }

    public void setChecked(String text) {
        group.setChecked(text);
    }

    public void setChecked(int index) {
        group.getButtons().get(index).setChecked(true);
    }

    private void addChoice(TextButton textButton) {
        group.add(textButton);
        addActor(textButton);
    }
}
