package com.candall.estoque_db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String categoria;
    private LocalDateTime validade;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_total", precision = 10, scale = 2)
    private BigDecimal precoTotal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "caixa_id", nullable = false)
    private Caixa caixa;

}
