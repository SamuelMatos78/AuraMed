# Documentos do AuraMed

Grupo: Samuel Oliveira Matos, Christiano Gonçalves, Nicolas Kiffer e Larissa Finelli.

Os `.md` são para ler direto no GitHub. PDF, SVG e DOCX para entrega ficam na pasta [`entrega`](entrega/).

## Para ler

- [Diagramas e fluxogramas](Diagramas_AuraMed.md): como o sistema funciona.
- [Diagrama de classes](Diagrama_Classes_AuraMed.md): as classes e as ligações entre elas. É gerado pelo código com `python3 scripts/gerar_diagrama_classes.py`.
- [Cartões CRC](Cartoes_CRC_AuraMed.md): o que cada classe faz e com quem trabalha.

## Para entregar

- Cartões CRC: [DOCX editável](entrega/Cartoes_CRC_AuraMed.docx) e [PDF](entrega/Cartoes_CRC_AuraMed.pdf).
- Diagrama de classes: [PDF](entrega/Diagrama_Classes_AuraMed.pdf) e [SVG](entrega/Diagrama_Classes_AuraMed.svg).

## Telas de exemplo

São só protótipos em HTML e CSS. Abra no navegador para ver.

- [Profissionais](../auramed/src/main/resources/templates/profissionais.html)
- [Consultas](../auramed/src/main/resources/templates/consultas.html)

Elas usam dados de mentira e os botões de enviar estão desligados. Ainda não têm rotas em Java nem ligação com o banco.
