<img src="brzuchboliodpizzy.png" alt="Getting Started" width="300" height="auto">

# Dokumentacja Techniczna dla pliku continuous-integration.yml

Plik `continuous-integration.yml` jest plikiem konfiguracyjnym dla GitHub Actions, który zarządza procesem Continuous Integration (CI) dla repozytorium projektu. CI pozwala automatycznie kompilować, testować oraz weryfikować kod, kiedy zostaną wprowadzone zmiany. Dzięki temu procesowi, jakiekolwiek problemy związane z kompilacją, testami jednostkowymi czy integracyjnymi będą natychmiast zauważone, co przyczyni się do utrzymania jakości kodu na wysokim poziomie.

Poniżej znajduje się opis poszczególnych części pliku `continuous-integration.yml`.

## Wydarzenia wyzwalające

CI będzie uruchamiany w następujących przypadkach:

1. Kiedy do repozytorium zostanie przesłany pull request, którego gałąź bazowa to `develop`. Nie obejmuje to gałęzi `main`.

```
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

### Krok 1: Pobranie kodu źródłowego z repozytorium

Pobranie kodu źródłowego z repozytorium GitHub za pomocą akcji actions/checkout@v2.

```yaml
    steps:
      - uses: actions/checkout@v2
```

### Krok 2: Ustawienie JDK 17

Instalacja i konfiguracja JDK 17, używając dystrybucji temurin, za pomocą akcji actions/setup-java@v2.

```yaml
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
```

### Krok 3: Nadanie uprawnieia wykonania dla Gradle Wrapper

Nadanie uprawnienia wykonania dla pliku `gradlew`, który umożliwia uruchomienie Gradle bez instalowania go systemowo.

```yaml
      - name: Grant execute permission for gradlew
        run: | 
          cd RescueHubProject
          chmod +x gradlew
```

### Krok 4: Kompilacja i budowanie projektu za pomocą Gradle

Wykonanie kompilacji i budowania projektu, używając Gradle.

```yaml
      - name: Build with Gradle
        run: |
          cd RescueHubProject
          ./gradlew build
```

### Krok 5: Testuowanie  za pomocą Gradle

Wykonuje testy jednostkowe i integracyjne dla projektu, używając Gradle.

```yaml
      - name: Test with Gradle
        run: |
          cd RescueHubProject
          ./gradlew test
```

Po zakończeniu wszystkich kroków, CI poinformuje, czy budowa i testy zostały zakończone pomyślnie czy też wystąpiły błędy. Pull request będzie można zmergować tylko, jeśli CI zakończy się pomyślnie.

# Dokumentacja Techniczna dla pliku continuous-deployment.yml

Plik `continuous-deployment.yml` jest plikiem konfiguracyjnym dla GitHub Actions, który zarządza procesem Continuous Deployment (CD) dla repozytorium projektu. CD umożliwia automatyczne wdrażanie nowych wersji oprogramowania do środowiska produkcyjnego po pomyślnym przejściu procesu CI. W ramach tego konkretnego workflow, aplikacja zostanie opublikowana na usługach Azure, korzystając z Azure Container Registry (ACR) oraz Azure App Service.

Azure to chmurowa platforma firmy Microsoft oferująca różne usługi dla tworzenia, wdrażania i zarządzania aplikacjami w chmurze. Azure Container Registry (ACR) to usługa przechowywania obrazów Docker, która umożliwia budowanie, przechowywanie i obsługę prywatnych obrazów Docker w bezpiecznym środowisku. Azure App Service to usługa hostowania dla aplikacji webowych, zapewniająca zarządzanie, skalowanie i automatyzację.

Poniżej znajduje się opis poszczególnych części pliku `continuous-deployment.yml`.

## Wydarzenia wyzwalające

CD będzie uruchamiany w następujących przypadkach:

1. Kiedy do repozytorium zostanie przesłany pull request, którego gałąź bazowa to `main`.

```yaml
on:
  pull_request:
    branches:
      - main
```

2. Kiedy CD zostanie uruchomiony ręcznie za pomocą opcji "workflow_dispatch".

```yaml
  workflow_dispatch:
```

## Job: build-deployment

W pliku `continuous-deployment.yml` zdefiniowane jest jedno zadanie (job) o nazwie "build-deployment", które zostanie wykonane w środowisku Ubuntu.

```yaml
jobs:
  build-deployment:
    runs-on: ubuntu-latest
```

### Krok 1-4: Pobranie kodu, Ustawienie JDK 17, Nadanie uprawnienia Gradle, budowa projektu

Te kroki są identyczne jak w przypadku workflow Continuous Integration. Pobierają kod z repozytorium, ustawiają JDK 17, nadają uprawnienia wykonania dla Gradle Wrappera i budują projekt.

### Krok 5: Logowanie do Azure Container Registry

Zalogowanie do Azure Container Registry (ACR) za pomocą akcji azure/docker-login@v1.

```yaml
      - name: Log in to Azure Container Registry
        uses: azure/docker-login@v1
        with:
          login-server: rescuehub.azurecr.io
          username: ${{ secrets.ACR_USERNAME }}
          password: ${{ secrets.ACR_PASSWORD }}
```

### Krok 6: Budowa i wyśłanie obrazu Docker

Zbudowanie obrazu Docker dla aplikacji, nadając mu tag oparty na identyfikatorze commitu (github.sha). Następnie wysłanie go do Azure Container Registry.

```yaml
      - name: Build and push Docker image
        run: |
          cd RescueHubProject
          docker build -t rescuehub.azurecr.io/rescuehub:${{ github.sha }} .
          docker push rescuehub.azurecr.io/rescuehub:${{ github.sha }}
```

### Krok 7: Wdrażanie do Azure App Service

Akcja `azure/webapps-deploy@v2` umożliwia wdrożenie obrazu Docker z ACR do aplikacji utworzonej w Azure App Service.

```yaml
      - name: Deploy to Azure App Service
        uses: azure/webapps-deploy@v2
        with:
          app-name: RescueHubApp
          slot-name: production
          publish-profile: ${{ secrets.PUBLISH_PROFILE_SERVER_PROD }}
          images: rescuehub.azurecr.io/rescuehub:${{ github.sha }}
```

Po zakończeniu wszystkich kroków, Continuous Deployment poinformuje, czy wdrożenie zakończyło się pomyślnie czy też wystąpiły błędy. Pomyślne zakończenie CD oznacza, że nowa wersja aplikacji została wdrożona w środowisku produkcyjnym na Azure.
