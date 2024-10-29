package org.charcoalwhite.classicmana.api;

public interface ServerPlayerEntityApi {
    default void tickMana() {

    };

    default void regenMana() {

    }

    default void updateMana() {

    }

    default void updateManaKeepAlive() {
        
    }

    default void setMana(Integer playerMana) {
        
    }

    default void setMaxMana(Integer playerMaxMana) {

    }

    default void setManabarLife(Integer playerManabarLife) {

    }
}