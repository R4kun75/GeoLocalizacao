# App Multiplataforma - Geolocalização em KMP

## Objetivo
[cite_start]Aplicativo desenvolvido em Kotlin Multiplatform (KMP) para recuperar e exibir a geolocalização do usuário em tempo real no Android e iOS[cite: 6, 7].

## [cite_start]Decisões Técnicas
* [cite_start]**UI Compartilhada:** Optou-se por utilizar o Compose Multiplatform para garantir 100% de compartilhamento de código da interface[cite: 9], otimizando o tempo de desenvolvimento para ambas as plataformas.
* [cite_start]**Geolocalização:** Utilização da biblioteca MOKO Geo para manipulação das coordenadas[cite: 7].
* [cite_start]**Arquitetura:** Aplicação estruturada seguindo Clean Code, com separação estrita entre `ui`, `domain` e `data`.

## [cite_start]Dificuldades Encontradas
* **Compatibilidade do Gradle:** Houve conflito entre a versão do Android Gradle Plugin (AGP) gerada pelo Wizard e a suportada pela IDE, exigindo um downgrade manual.
* **Atualizações do MOKO Geo:** As versões mais recentes da biblioteca alteraram a assinatura das funções *factory* e removeram alguns parâmetros padrão do `LocationTrackerAccuracy`