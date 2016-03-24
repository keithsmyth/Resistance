package com.keithsmyth.resistance.data;

import com.keithsmyth.resistance.data.model.CharacterDataModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterProvider {

    private static final CharacterDataModel MERLIN = new CharacterDataModel("Merlin", false) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != MORDRED;
        }

        @Override
        public String revealedDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel PERCIVAL = new CharacterDataModel("Percival", false) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return characterDataModel == MERLIN || characterDataModel == MORGANA;
        }

        @Override
        public String revealedDescription() {
            return "Merlin / Morgana are";
        }
    };

    private static final CharacterDataModel MORDRED = new CharacterDataModel("Mordred", true) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String revealedDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel ASSASSIN = new CharacterDataModel("Assassin", true) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String revealedDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel MORGANA = new CharacterDataModel("Morgana", true) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String revealedDescription() {
            return "Evil players are";
        }
    };

    private static final CharacterDataModel OBERON = new CharacterDataModel("Oberon", true) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return false;
        }

        @Override
        public String revealedDescription() {
            return "";
        }
    };

    private static final CharacterDataModel SERVANT = new CharacterDataModel("Loyal servant of Arthur", false) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return false;
        }

        @Override
        public String revealedDescription() {
            return "";
        }
    };

    private static final CharacterDataModel MINION = new CharacterDataModel("Minion of Mordred", true) {
        @Override
        public boolean isRevealed(CharacterDataModel characterDataModel) {
            return characterDataModel.isBad && characterDataModel != OBERON;
        }

        @Override
        public String revealedDescription() {
            return "Evil players are";
        }
    };
    
    private final Map<String, CharacterDataModel> mapCharacterNameToDataModel;

    public CharacterProvider() {
        mapCharacterNameToDataModel = new HashMap<>(8);
        mapCharacterNameToDataModel.put(MERLIN.name, MERLIN);
        mapCharacterNameToDataModel.put(PERCIVAL.name, PERCIVAL);
        mapCharacterNameToDataModel.put(MORDRED.name, MORDRED);
        mapCharacterNameToDataModel.put(ASSASSIN.name, ASSASSIN);
        mapCharacterNameToDataModel.put(MORGANA.name, MORGANA);
        mapCharacterNameToDataModel.put(OBERON.name, OBERON);
        mapCharacterNameToDataModel.put(SERVANT.name, SERVANT);
        mapCharacterNameToDataModel.put(MINION.name, MINION);
    }

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

    public CharacterDataModel getCharacter(String name) {
        return mapCharacterNameToDataModel.get(name);
    }
}
