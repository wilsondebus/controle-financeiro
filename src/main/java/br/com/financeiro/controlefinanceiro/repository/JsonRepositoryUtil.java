package br.com.financeiro.controlefinanceiro.repository;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonRepositoryUtil {

    private final ObjectMapper objectMapper;

    public JsonRepositoryUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public synchronized <T> List<T> lerLista(
            String caminho,
            Class<T> classe
    ) {

        File arquivo = new File(caminho);

        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }

        try {

            CollectionType tipoLista =
                    objectMapper
                            .getTypeFactory()
                            .constructCollectionType(
                                    List.class,
                                    classe
                            );

            return objectMapper.readValue(
                    arquivo,
                    tipoLista
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao ler o arquivo " + caminho,
                    e
            );
        }
    }

    public synchronized <T> void salvarLista(
            String caminho,
            List<T> dados
    ) {

        File arquivo = new File(caminho);

        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        try {

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(
                            arquivo,
                            dados
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao salvar o arquivo " + caminho,
                    e
            );
        }
    }

    public long proximoId(List<?> lista) {

        long maiorId = 0;

        for (Object objeto : lista) {

            try {

                Long id = (Long) objeto
                        .getClass()
                        .getMethod("getId")
                        .invoke(objeto);

                if (id != null && id > maiorId) {
                    maiorId = id;
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        "Erro ao gerar próximo ID",
                        e
                );
            }
        }

        return maiorId + 1;
    }
}