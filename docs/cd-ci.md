# Dokumentacja Techniczna dla pliku continuous-integration.yml

Plik `continuous-integration.yml` jest plikiem konfiguracyjnym dla GitHub Actions, który zarządza procesem Continuous Integration (CI) dla repozytorium projektu. CI pozwala automatycznie kompilować, testować oraz weryfikować kod, kiedy zostaną wprowadzone zmiany. Dzięki temu procesowi, jakiekolwiek problemy związane z kompilacją, testami jednostkowymi czy integracyjnymi będą natychmiast zauważone, co przyczyni się do utrzymania jakości kodu na wysokim poziomie.

Poniżej znajduje się opis poszczególnych części pliku `continuous-integration.yml`.

## Wydarzenia wyzwalające

CI będzie uruchamiany w następujących przypadkach:

1. Kiedy do repozytorium zostanie przesłany pull request, którego gałąź bazowa to `develop`. Nie obejmuje to gałęzi `main`.

yaml
on:
  pull_request:
    branches:
      - develop
      - "!main"
```

2. Kiedy do gałęzi `develop` zostanie wypchnięty nowy commit. Nie obejmuje to gałęzi `main`.

```yaml
  push:
    branches:
      - develop
      - "!main"
```

3. Kiedy CI zostanie uruchomiony ręcznie za pomocą opcji "workflow_dispatch".

```yaml
  workflow_dispatch:
```

## Praca: build-integration

W pliku `continuous-integration.yml` zdefiniowane jest jedno zadanie (job) o nazwie "build-integration", które zostanie wykonane w środowisku Ubuntu.

```yaml
jobs:
  build-integration:
    runs-on: ubuntu-latest
```

### Krok 1: Pobierz kod źródłowy z repozytorium

Pobierz kod źródłowy z repozytorium GitHub za pomocą akcji actions/checkout@v2.

```yaml
    steps:
      - uses: actions/checkout@v2
```

### Krok 2: Ustaw JDK 17

Zainstaluj i skonfiguruj JDK 17, używając dystrybucji temurin, za pomocą akcji actions/setup-java@v2.

```yaml
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
```

### Krok 3: Nadaj uprawnienia wykonania dla Gradle Wrapper

Nadaj uprawnienia wykonania dla pliku `gradlew`, który jest Gradle Wrapperem umożliwiającym uruchomienie Gradle bez instalowania go systemowo.

```yaml
      - name: Grant execute permission for gradlew
        run: | 
          cd RescueHubProject
          chmod +x gradlew
```

### Krok 4: Kompiluj i buduj projekt za pomocą Gradle

Wykonaj kompilację i budowanie projektu, używając Gradle.

```yaml
      - name: Build with Gradle
        run: |
          cd RescueHubProject
          ./gradlew build
```

### Krok 5: Testuj projekt za pomocą Gradle

Wykonaj testy jednostkowe i integracyjne dla projektu, używając Gradle.

```yaml
      - name: Test with Gradle
        run: |
          cd RescueHubProject
          ./gradlew test
```

Po zakończeniu wszystkich kroków, CI poinformuje, czy budowa i testy zostały zakończone pomyślnie czy też wystąpiły błędy. Pull request będzie można zmergować tylko, jeśli CI zakończy się pomyślnie.
