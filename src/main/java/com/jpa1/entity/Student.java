package com.jpa1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")

public class Student {

    @Id
    @Column(name = "student_id")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    private String studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_sex")
    private String studentSex;

    @Column(name = "student_age")
    private String studentAge;

    @Column(name = "student_class")
    private String studentClass;

}
