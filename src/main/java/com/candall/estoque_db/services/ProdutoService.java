package com.candall.estoque_db.services;

import com.candall.estoque_db.models.Caixa;
import com.candall.estoque_db.models.Produto;
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
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CaixaRepository caixaRepository;

    @Transactional
    public ProdutoResponseDto criar(ProdutoResponseDto dto, MultipartFile imagem){
        Caixa caixa = caixaRepository.findById(dto.caixaId())
                .orElseThrow(()-> new RuntimeException("Caixa não encontrada"));

        String urlImagem = salvarImagemLocal(imagem);

        Produto produto = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .validade(dto.validade())
                .preco(dto.preco())
                .quantidade(dto.quantidade())
                .imagemUrl(urlImagem)
                .caixa(caixa)
                .build();

        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaResponse(produtoSalvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarPorValidadeCrescente(){
        return produtoRepository.findByOrderByValidadeAsc().stream().map(this::converterParaResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarPorQuantidadeDecrescente() {
        return produtoRepository.findAllByOrderByQuantidadeDesc().stream().map(this::converterParaResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDto> listarPorNomeCrescente() {
        return produtoRepository.findAllByOrderByNomeAsc().stream().map(this::converterParaResponse).toList();
    }

    private String salvarImagemLocal(MultipartFile arquivo) {
        // 1. Validação: Se nenhum arquivo foi enviado, retorna nulo
        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        try {
            String diretorioDestino = "uploads/imagens/";
            File pasta = new File(diretorioDestino);

            if (!pasta.exists()) {
                pasta.mkdirs();
            }

            String nomeUnicoArquivo = System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();

            Path caminhoCompleto = Paths.get(diretorioDestino + nomeUnicoArquivo);

            Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

            return caminhoCompleto.toString();

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar a imagem localmente", e); // Substituir exceção
        }
    }

    private ProdutoResponseDto converterParaResponse(Produto produto) {
        String urlacessivel = null;
        if(produto.getImagemUrl() != null){
            String localhost = "http://localhost:8080/";
            String caminhoFormatado = produto.getImagemUrl().replace("\\", "/");
            urlacessivel = localhost + caminhoFormatado;
        }
        return new ProdutoResponseDto(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getValidade(),
                urlacessivel,
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getPrecoTotal(),
                produto.getCaixa().getId()
        );
    }

}
