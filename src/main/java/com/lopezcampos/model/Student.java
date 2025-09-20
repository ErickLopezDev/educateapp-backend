package com.lopezcampos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student")
    private Long idStudent;
    
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
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Matriculation> matriculations;
    
}