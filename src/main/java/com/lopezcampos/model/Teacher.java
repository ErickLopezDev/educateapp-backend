package com.lopezcampos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teacher")
    private Long idTeacher;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "surname", nullable = false, length = 100)
    private String surname;
    
    @Column(name = "dni", unique = true, nullable = false, length = 20)
    private String dni;
    
    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "specialty", length = 100)
    private String specialty;
    
    @Column(name = "status", length = 20)
    private String status;
    
    // Relationship with Courses
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;
    
}