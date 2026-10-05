# Entregas — AuraMed

Integrantes: Samuel Oliveira Matos, Christiano Gonçalves, Nicolas Kiffer e Larissa Finelli.

## Cartões CRC

- [Documento editável](Cartoes_CRC_AuraMed.docx), preenchido a partir do modelo fornecido.
- [Versão para leitura e entrega](Cartoes_CRC_AuraMed.pdf).

São 12 cartões: seis classes de domínio e seis serviços. As responsabilidades refletem o código Java existente, incluindo validações, controle de horários, capacidade dos quartos e histórico derivado dos atendimentos. As colaborações incluem os participantes de cada caso de uso; algumas referências são feitas por classes que utilizam o modelo.

## Diagrama de classes

- [PDF](Diagrama_Classes_AuraMed.pdf).
- [SVG vetorial](Diagrama_Classes_AuraMed.svg), que pode ser aberto no navegador.
- [Fonte Mermaid](Diagrama_Classes_AuraMed.mmd).

O diagrama representa Paciente, ProfissionalSaude, Consulta, Internacao, Quarto, HistoricoMedico e as duas enumerações. Inclui atributos, associações navegáveis e multiplicidades. No SVG e no PDF, alguns métodos de acesso são apresentados como exemplos; os demais getters, setters e construtores foram omitidos para facilitar a leitura. A fonte Mermaid concentra-se nos atributos e associações.

HistoricoMedico é um record montado pelo serviço, sem tabela própria. As consultas do histórico têm status REALIZADA; as internações incluem registros com e sem alta. Serviços e repositórios são descritos nos cartões CRC. A classe vazia User e os DTOs auxiliares não fazem parte deste diagrama de domínio.

## Páginas em HTML e CSS

- [Profissionais](../auramed/src/main/resources/templates/profissionais.html) e [CSS](../auramed/src/main/resources/static/css/profissionais.css).
- [Consultas](../auramed/src/main/resources/templates/consultas.html) e [CSS](../auramed/src/main/resources/static/css/consultas.css).

Abra os arquivos HTML diretamente no navegador para visualizar. As duas páginas reutilizam `static/css/style.css`, com caminhos relativos para permitir a visualização local. Os links das páginas já existentes mantêm os atributos Thymeleaf do projeto.

As telas apresentam dados fictícios, resumos, detalhes expansíveis e formulários demonstrativos. Os elementos `details` e `summary` permitem abrir os formulários e os dados sem JavaScript. Os botões de envio estão desabilitados porque não há gravação de dados neste protótipo.

Não foram criadas rotas Java para as novas páginas nem alterados serviços, repositórios ou banco de dados. Para uma futura integração ao Spring, será necessário fornecer as rotas, as coleções do modelo e substituir os dados demonstrativos.
