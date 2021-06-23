package com.gafur.packtask.service;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.BackpackSolution;

public interface PackService {

    BackpackSolution packItemsByMaxCost(BackpackSample pack);
}
