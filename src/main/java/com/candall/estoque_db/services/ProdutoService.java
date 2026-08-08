package com.candall.estoque_db.services;

import com.candall.estoque_db.models.Caixa;
import com.candall.estoque_db.models.Produto;
import com.candall.estoque_db.models.dto.ProdutoRequestDto;
import com.candall.estoque_db.models.dto.ProdutoResponseDto;
import com.candall.estoque_db.repositories.CaixaRepository;
import com.candall.estoque_db.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CaixaRepository caixaRepository;

    @Transactional
    public ProdutoResponseDto criar(ProdutoRequestDto dto, MultipartFile imagem) throws IOException {

        String uploadDir = "uploads/";

        Caixa caixa = caixaRepository.findById(dto.caixaId())
                                     .orElseThrow(() -> new RuntimeException("Caixa não encontrada e/ou não existe"));

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String imageName = imagem.getOriginalFilename();
        Path filePath = Paths.get(uploadDir+imageName);
        Files.write(filePath, imagem.getBytes());

        Produto produto = Produto.builder()
                                 .nome(dto.nome())
                                 .categoria(dto.categoria())
                                 .validade(dto.validade())
                                 .preco(dto.preco())
                                 .quantidade(dto.quantidade())
                                 .imagemUrl("http://localhost:8080/uploads/"+imageName.replace(" ", "-"))
                                 .caixa(caixa)
                                 .build();

        Produto produtoSalvo = produtoRepository.save(produto);

        return new ProdutoResponseDto(
                produtoSalvo.getNome(),
                produtoSalvo.getCategoria(),
                produtoSalvo.getValidade(),
                produtoSalvo.getImagemUrl(),
                produtoSalvo.getPreco(),
                produtoSalvo.getQuantidade(),
                produtoSalvo.getPrecoTotal(),
                produtoSalvo.getCaixa().getId()
        );
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarPorValidadeCrescente() {

        return produtoRepository.findByOrderByValidadeAsc()
                                .stream()
                                .map(p -> new ProdutoResponseDto(
                                             p.getNome(),
                                             p.getCategoria(),
                                             p.getValidade(),
                                             p.getImagemUrl(),
                                             p.getPreco(),
                                             p.getQuantidade(),
                                             p.getPrecoTotal(),
                                             p.getCaixa().getId())).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarPorQuantidadeDecrescente() {

        return produtoRepository.findAllByOrderByQuantidadeDesc()
                .stream()
                .map(p -> new ProdutoResponseDto(
                        p.getNome(),
                        p.getCategoria(),
                        p.getValidade(),
                        p.getImagemUrl(),
                        p.getPreco(),
                        p.getQuantidade(),
                        p.getPrecoTotal(),
                        p.getCaixa().getId()
                )).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarPorNomeCrescente() {

        return produtoRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(p -> new ProdutoResponseDto(
                        p.getNome(),
                        p.getCategoria(),
                        p.getValidade(),
                        p.getImagemUrl(),
                        p.getPreco(),
                        p.getQuantidade(),
                        p.getPrecoTotal(),
                        p.getCaixa().getId()
                )).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDto buscarPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return new ProdutoResponseDto(
                produto.getNome(),
                produto.getCategoria(),
                produto.getValidade(),
                produto.getImagemUrl(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getPrecoTotal(),
                produto.getCaixa().getId()
        );
    }

    @Transactional
    public ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto, MultipartFile novaImagem) throws IOException {

        Produto produtoExistente = produtoRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produtoExistente.getCaixa().getId().equals(dto.caixaId())) {

            Caixa novaCaixa = caixaRepository.findById(dto.caixaId())
                                             .orElseThrow(() -> new RuntimeException("Nova Caixa não encontrada"));

            produtoExistente.setCaixa(novaCaixa);
        }

        produtoExistente.setNome(dto.nome());
        produtoExistente.setCategoria(dto.categoria());
        produtoExistente.setValidade(dto.validade());
        produtoExistente.setPreco(dto.preco());
        produtoExistente.setQuantidade(dto.quantidade());

        if (novaImagem != null && !novaImagem.isEmpty()) {
            deletarImagemLocal(produtoExistente.getImagemUrl());
            //String novaUrl = salvarImagemLocal(novaImagem);
            produtoExistente.setImagemUrl(null);
        }

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return new ProdutoResponseDto(
                produtoAtualizado.getNome(),
                produtoAtualizado.getCategoria(),
                produtoAtualizado.getValidade(),
                produtoAtualizado.getImagemUrl(),
                produtoAtualizado.getPreco(),
                produtoAtualizado.getQuantidade(),
                produtoAtualizado.getPrecoTotal(),
                produtoAtualizado.getCaixa().getId()
        );
    }

    @Transactional
    public void deletar(Long id) {

        Produto produto = produtoRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        deletarImagemLocal(produto.getImagemUrl());

        produtoRepository.delete(produto);
    }

    private void deletarImagemLocal(String caminhoImagem) {
        if (caminhoImagem != null && !caminhoImagem.isBlank()) {
            try {
                Path caminhoCompleto = Paths.get(caminhoImagem);
                Files.deleteIfExists(caminhoCompleto);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao deletar a imagem do disco local", e);
            }
        }
    }
}
