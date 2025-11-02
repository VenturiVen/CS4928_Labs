package com.cafepos.command;

public interface Command {

    void execute();

    void undo();
}