package com.sanvalero.entrenoexamenmayo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Creado por @ author: Pedro Orós
 * el 11/05/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwResults {

    private int count;
    private String next;
    private String previous;
    private List<SwPeople> results;
}
