#!/usr/bin/env python3
"""Gera o diagrama de classes lendo os arquivos Java da pasta model.

Uso (na raiz do projeto):
    python3 scripts/gerar_diagrama_classes.py          # atualiza o .md
    python3 scripts/gerar_diagrama_classes.py --check  # só confere (sai com erro se estiver velho)
"""
import re
import sys
from pathlib import Path

RAIZ = Path(__file__).resolve().parent.parent
MODEL = RAIZ / "auramed/src/main/java/com/auramed/auramed/model"
ALVO = RAIZ / "docs/Diagrama_Classes_AuraMed.md"
INICIO, FIM = "<!-- diagrama:inicio -->", "<!-- diagrama:fim -->"
# Classes de apoio que não fazem parte do diagrama de domínio.
IGNORAR = {"User", "QuartoVagas", "InternacaoResumo"}

PRIVADO = re.compile(r"^\s*private\s+([\w<>]+)\s+(\w+)\s*(?:=[^;]*)?;", re.M)


def ler_classes():
    """Devolve (entidades, records, enums) em ordem alfabética."""
    entidades, records, enums = {}, {}, {}
    for arq in sorted(MODEL.glob("*.java")):
        nome, src = arq.stem, arq.read_text(encoding="utf-8")
        if nome in IGNORAR:
            continue
        if re.search(r"\benum\s+" + nome, src):
            corpo = re.search(r"enum\s+\w+\s*\{(.*?)(?:;|\})", src, re.S).group(1)
            corpo = re.sub(r"@\w+(\([^)]*\))?", "", corpo)  # tira anotações como @Deprecated
            enums[nome] = [v.strip() for v in corpo.split(",") if v.strip()]
        elif re.search(r"\brecord\s+" + nome, src):
            comp = re.search(r"record\s+\w+\s*\((.*?)\)\s*\{", src, re.S).group(1)
            records[nome] = [tuple(c.split()[:2]) for c in comp.split(",") if c.strip()]
        elif "@Entity" in src:
            entidades[nome] = PRIVADO.findall(src)
    return entidades, records, enums


def gerar():
    entidades, records, enums = ler_classes()
    conhecidas = set(entidades) | set(records) | set(enums)
    linhas = ["classDiagram", "direction TB"]
    relacoes = []

    for nome, campos in entidades.items():
        linhas.append(f"class {nome} {{")
        for tipo, campo in campos:
            if tipo in entidades:
                relacoes.append(f'{nome} "*" --> "1" {tipo}')
            elif tipo in enums:
                relacoes.append(f"{nome} ..> {tipo}")
                linhas.append(f"    {tipo} {campo}")
            else:
                linhas.append(f"    {tipo} {campo}")
        linhas.append("}")

    for nome in records:
        linhas += [f"class {nome} {{", "    <<record>>", "}"]
        for tipo, campo in records[nome]:
            lista = re.fullmatch(r"List<(\w+)>", tipo)
            alvo = lista.group(1) if lista else tipo
            if alvo in conhecidas:
                relacoes.append(f'{nome} --> "{"*" if lista else "1"}" {alvo}')

    for nome, valores in enums.items():
        linhas += [f"class {nome} {{", "    <<enum>>"] + [f"    {v}" for v in valores] + ["}"]

    linhas += relacoes
    for nome in records:
        linhas.append(f'note for {nome} "Não tem tabela própria. É montado na hora."')
    linhas += [
        "",
        "classDef entidade fill:#d5f5d5,stroke:#2e8b2e,color:#000",
        "classDef montado fill:#cfe8ff,stroke:#1d6fb8,color:#000",
        "classDef opcoes fill:#e6d9f5,stroke:#7a4cb0,color:#000",
    ]
    linhas += [f"class {n}:::entidade" for n in entidades]
    linhas += [f"class {n}:::montado" for n in records]
    linhas += [f"class {n}:::opcoes" for n in enums]
    return "```mermaid\n" + "\n".join(linhas) + "\n```"


def main():
    texto = ALVO.read_text(encoding="utf-8")
    if INICIO not in texto or FIM not in texto:
        sys.exit(f"Faltam os marcadores {INICIO} e {FIM} em {ALVO.name}")
    antes, resto = texto.split(INICIO, 1)
    _, depois = resto.split(FIM, 1)
    novo = f"{antes}{INICIO}\n{gerar()}\n{FIM}{depois}"
    if "--check" in sys.argv:
        if novo != texto:
            sys.exit("Diagrama desatualizado. Rode: python3 scripts/gerar_diagrama_classes.py")
        print("Diagrama em dia.")
    else:
        ALVO.write_text(novo, encoding="utf-8")
        print(f"Atualizado: {ALVO.relative_to(RAIZ)}")


if __name__ == "__main__":
    main()
