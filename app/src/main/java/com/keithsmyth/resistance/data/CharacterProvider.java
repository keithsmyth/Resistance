package com.keithsmyth.resistance.data;

import com.keithsmyth.resistance.data.model.characters.CharacterDataModel;

import java.util.Arrays;
import java.util.List;

public class CharacterProvider {

    private static final CharacterDataModel MERLIN = new CharacterDataModel("Merlin", false) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != MORDRED;
        }

        @Override
        public String canSeeDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel PERCIVAL = new CharacterDataModel("Percival", false) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return characterDataModel == MERLIN || characterDataModel == MORGANA;
        }

        @Override
        public String canSeeDescription() {
            return "Merlin / Morgana are";
        }
    };

    private static final CharacterDataModel MORDRED = new CharacterDataModel("Mordred", true) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String canSeeDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel ASSASSIN = new CharacterDataModel("Assassin", true) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String canSeeDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel MORGANA = new CharacterDataModel("Morgana", true) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String canSeeDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel OBERON = new CharacterDataModel("Oberon", true) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return false;
        }

        @Override
        public String canSeeDescription() {
            return null;
        }
    };

    private static final CharacterDataModel SERVANT = new CharacterDataModel("Loyal servant of Arthur", false) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return false;
        }

        @Override
        public String canSeeDescription() {
            return null;
        }
    };

    private static final CharacterDataModel MINION = new CharacterDataModel("Minion of Mordred", true) {
        @Override
        public boolean canSee(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String canSeeDescription() {
            return "Evil players are";
        }
    };

    public List<CharacterDataModel> getSelectableCharacters() {
        return Arrays.asList(
            MERLIN,
            PERCIVAL,
            MORDRED,
            ASSASSIN,
            MORGANA,
            OBERON
        );
    }

    public CharacterDataModel getServant() {
        return SERVANT;
    }

    public CharacterDataModel getMinion() {
        return MINION;
    }
}
