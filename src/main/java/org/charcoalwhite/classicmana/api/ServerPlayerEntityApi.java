package org.charcoalwhite.classicmana.api;

public interface ServerPlayerEntityApi {
    default void tickMana() {

    };

    default void regenMana() {

    }

    default void updateMana() {

    }

    default int getManaRegen() {
        return 0;
    }

    default void setManaRegen(int playerMana) {
        
    }

    default int incrementManaRegen(int playerMana) {
        return 0;
    }

    default int incrementManaRegen() {
        return 0;
    }

    default void resetManaRegen() {

    }

    default int getMana() {
        return 0;
    }

    default void setMana(int playerMana) {
        
    }

    default int incrementMana(int playerMana) {
        return 0;
    }

    default int incrementMana() {
        return 0;
    }

    default void resetMana() {

    }

    default int getMaxMana() {
        return 0;
    }

    default void setMaxMana(int playerMaxMana) {

    }

    default int incrementMaxMana(int playerMana) {
        return 0;
    }

    default int incrementMaxMana() {
        return 0;
    }

    default void resetMaxMana() {
        
    }

    default int getManabarLife() {
        return 0;
    }

    default void setManabarLife(int playerManabarLife) {

    }

    default int incrementManabarLife(int playerMana) {
        return 0;
    }

    default int incrementManabarLife() {
        return 0;
    }

    default void resetManabarLife() {
        
    }
}