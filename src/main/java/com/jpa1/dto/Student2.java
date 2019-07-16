package com.jpa1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//一个dto类

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student2 {


    private String studentId;

    private String studentName;


    private String studentSex;


    private String studentAge;


    private String studentClass;

}
