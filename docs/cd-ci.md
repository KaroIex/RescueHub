# Dokumentacja techniczna do Continuous Integration Github Actions

Plik `continuous-integration.yaml` jest wykorzystywany przez Github Actions do automatycznego testowania i budowania kodu dostarczonego do repozytorium. Poniżej przedstawiamy opis tego pliku oraz poszczególnych czynności przez niego wykonywanych.

## Zdarzenia wyzwalające

```yaml
on:
  pull_request:
    branches:
      - develop
      - "!main"
  push:
    branches:
      - develop
      - "!main"
  workflow_dispatch:
```

Workflow uruchomi się, gdy zostanie wystawiony nowy pull request lub push do gałęzi `develop`. Zaznaczono tutaj wyjątek dla gałęzi `main` - żadne zmiany na tej gałęzi nie uruchomią tego workflow.

`workflow_dispatch:` pozwala na ręczne uruchomienie workflow.

## Joby

```yaml
jobs:
  build-integration:
    runs-on: ubuntu-latest
    ...
```

W tym workflow występuje jeden job o nazwie `build-integration`. Przeprowadzane są na najnowszej wersji systemu Ubuntu.

## Kroki 

```yaml
steps:
      - uses: actions/checkout@v2
      ...
```

Pierwszym krokiem jest wyszukiwanie kodu z repozytorium na Githubie. Wykorzystuje to Githubową akcję `actions/checkout@v2`.

```yaml
- name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
```

Następnie występuje instalacja JDK 17. Do tego celu używana jest inne Githubowej akcje `actions/setup-java@v2`. Kod jest budowany za pomocą dystrybucji `temurin`.

```yaml
      - name: Grant execute permission for gradlew
        run: | 
          cd RescueHubProject
          chmod +x gradlew
```

Następnie nadawane są uprawnienia do uruchamiania skryptu `gradlew`. Skrypt ten jest wykorzystywany przez system budowy Gradle.

```yaml
- name: Build with Gradle
        run: |
          cd RescueHubProject
          ./gradlew build
```

Pierwszym zadań Gradle jest budowanie projektu. Gradle automatycznie pobierze wszystkie wymagane zależności do budowy projektu.

```yaml
- name: Test with Gradle
        run: |
          cd RescueHubProject
          ./gradlew test
```

Ostatnim krokiem jest uruchomienie testów. Projekty Gradle często zawierają zestawy testów jednostkowych, które mogą być automatycznie przeprowadzone w trakcie procesu budowy.
