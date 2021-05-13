package com.sanvalero.entrenoexamenmayo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Creado por @ author: Pedro Orós
 * el 11/05/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwPeople {

    private String name;
    private String gender;
    private String height;
    private String mass;
    private String birth_year;
}
