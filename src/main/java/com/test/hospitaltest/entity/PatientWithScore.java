package com.test.hospitaltest.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PatientWithScore {
    private Patient patient;
    private int score;
}
