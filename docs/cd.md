# Dokumentacja techniczna do Continuous Deployment Github Actions

Plik `continuous-deployment.yaml` służy do automatycznego wdrażania kodu na środowiska: developerskie (`Dev`) i produkcyjne (`Prod`). Wykorzystuje Github Actions wraz z usługami Microsoft Azure do budowy obrazów Docker i wdrażania ich do usługi Azure App Service. 

## Zdarzenia wyzwalające

```yaml
on:
  pull_request:
    branches:
      - main
  workflow_dispatch:
```

Workflow uruchamia się, gdy pull request zostanie wystawiony na gałąź `main`. Dodatkowo, workflow można ręcznie uruchomić za pomocą `workflow_dispatch:`.

## Joby (Zadania)

### Definicja środowiska developerskiego:

```yaml
build-deployment-dev:
    runs-on: ubuntu-latest
    environment:
      name: 'Dev'
      url: https://rescuehubapp-dev.azurewebsites.net
```

Job `build-deployment-dev` jest uruchamiany na najnowszej wersji Ubuntu. Definiuje środowisko `Dev` z adresem URL, gdzie uruchomiona zostanie aplikacja.

### Kroki dla środowiska developerskiego:

Pierwsze cztery kroki są identyczne jak w przypadku workflow Continuous Integration. Oznacza to, że kod jest klonowany, instalowane jest JDK 17, nadawane są uprawnienia dla `gradlew` a następnie budowany jest projekt.

Dodatkowe kroki obejmują:

```yaml
- name: Log in to Azure Container Registry
        uses: azure/docker-login@v1
        with:
          login-server: rescuehubdev.azurecr.io
          username: ${{ secrets.ACR_DEV_USERNAME }}
          password: ${{ secrets.ACR_DEV_PASSWORD }}
```

Zalogowanie się do Azure Container Registry (ACR), usługi, która zapewnia Dockerowi prywatne repozytorium. Wykorzystuje dane przechowywane w Github Secrets do wyszukania `username` i `password` zasobu.

```yaml
- name: Build and push Docker image
        run: |
          cd RescueHubProject
          docker build -t rescuehubdev.azurecr.io/rescuehub:${{ github.sha }} .
          docker push rescuehubdev.azurecr.io/rescuehub:${{ github.sha }}
```

Budowanie Dockerowel obrazu dla aplikacji i pushowanie go do repozytorium w ACR. Obraz jest tagowany za pomocą SHA aktualnego commita.

```yaml
- name: Deploy to Azure App Service
        uses: azure/webapps-deploy@v2
        with:
          app-name: RescueHubApp-dev
          slot-name: production
          publish-profile: ${{ secrets.PUBLISH_PROFILE_SERVER_DEV }}
          images: rescuehubdev.azurecr.io/rescuehub:${{ github.sha }}
```

Uruchomienie aplikacji na Azure App Service. Dane dotyczące nazwy aplikacji, slotu, profilu publikacji i dołączania obrazów Docker są konfigurowane.

### Definicja środowiska produkcyjnego:

```yaml
build-deployment:
    needs: build-deployment-dev
    environment:
      name: 'Prod'
      url: https://rescuehubapp.azurewebsites.net
    runs-on: ubuntu-latest
```

Job `build-deployment` odpowiada za wdrożenie na środowisko produkcyjne. Zależy on od poprzedniego joba, `build-deployment-dev`, co oznacza, że uruchamia się tylko wtedy, gdy job Dev zakończy się sukcesem. Jest zdefiniowane jako środowisko `Prod` z odpowiednim adresem URL. 

### Kroki dla środowiska produkcyjnego:

Kroki są analogiczne do kroków dla środowiska developerskiego. Różnice dotyczyć będą nazw serwisów ACR, nazwy aplikacji oraz sekretów Githuba użytych do zalogowania i wdrożenia na serwer (używamy teraz sekretów dla serwisów produkcyjnych zamiast developerskich).

**Azure**

Azure, czyli Microsoft Azure, to zestaw usług chmurowych, które rozwijają aplikacje i usługi za pomocą narzędzi Microsoft dla zarówno publicznej, jak i prywatnej chmury. Pomaga to firmom zagwarantować bezproblemową integrację chmur, a także zapewnia narzędzia umożliwiające zarządzanie chmurą. Azure jest szeroko wykorzystywany dla budowy, wdrażania oraz zarządzania aplikacjami przez globalną sieć datacenter.

**Azure Web Service**

Azure Web Services, znany również jako Azure App Service, to platforma do hostowania aplikacji webowych, REST API i backendów mobilnych. Umożliwia budowanie, wdrażanie i skalowanie aplikacji internetowych i API w różnuch językach programowania na dowolnym systemie operacyjnym. W Azure App Service można zintegrować Azure DevOps, GitHub, BitBucket, Docker Hub czy Azure Container Registry by umożliwić automatyczne działania CI/CD.

**Azure Container Registry**

Azure Container Registry (ACR) to usługa zarządzania prywatnymi rejestratorami kontenerów Dokera, która pozwala na budowanie, przechowywanie i zarządzanie obrazami kontenerów dla wszelkiego oprogramowania kontenerowego przechowywanego w Azure. ACR jest zintegrowany z usługami wdrażania kontenerów Azure, co umożliwia przenoszenie aplikacji na różne środowiska, takie jak Azure Kubernetes Service (AKS), Azure App Service itp. Dzięki ACR, obrazy są przechowywane w prywatnym rejestrze, zabezpieczając dostęp do nich i umożliwiając łatwe wdrożenie na dowolnym serwerze hostującym kontenery.
