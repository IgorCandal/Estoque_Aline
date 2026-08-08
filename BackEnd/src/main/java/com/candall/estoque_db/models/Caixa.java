package com.candall.estoque_db.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "caixa_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoCaixa;

    @JsonManagedReference
    @OneToMany(mappedBy = "caixa", cascade = CascadeType.ALL)
    private List<Produto> produtos;

}
