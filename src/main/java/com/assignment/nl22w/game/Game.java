package com.assignment.nl22w.game;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface Game {

	int escapeFromTheWoods(Resource resource) throws IOException;
}
