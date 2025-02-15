package com.cmyk.ego.speaktoyouspring.api.test;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String testParam1;

    @Column(unique = true, nullable = false)
    private String testParam2;

    @Column(nullable = false)
    private String testParam3;
}
