# AutoDoc Workflow

Plik konfiguracji dla GitHub Actions.

## Opis ogólny
Wykorzystuje GitHuba do automatycznego generowania dokumentacji API za pomocą narzędzi Swagger i Asciidoctor. Po generacji dokumentacji w formacie PDF umieszcza go na gałęzi "dokumentacja-update".

## Warunki startu
Workflow jest uruchamiany manualnie za pomocą API GitHuba.

## Job
Workflow składa się z kilku etapów, które są wykonywane po kolei:

- **Checkout**: Krok służący do pobrania kodu źródłowego z repozytorium.

- **Set up JDK 17**: W tym etapie jest instalowana odpowiednia wersja JDK.

- **Grant execute permission for gradlew**: Nadaje prawa do wykonywania dla skryptu gradlew.

- **Build with Gradle**: Buduje projekt za pomocą Gradle.

- **Start Application**: Uruchamia zbudowaną aplikację w tle.

- **Sleep for 30 seconds**: Dokonuje krótkiego opóźnienia, aby dać czas na uruchomienie aplikacji.

- **Fetch Swagger JSON**: Pobiera dokumentację API w formacie Swagger JSON.

- **Create required directories**: Tworzy katalogi potrzebne do generowania i przechowywania dokumentacji.

- **Convert Swagger to AsciiDoc**: Konwertuje dokumentację z formatu Swagger JSON do AsciiDoc.

- **Install Asciidoctor and Asciidoctor PDF**: Instaluje narzędzia potrzebne do generowania dokumentacji w formacie PDF z plików AsciiDoc.

- **Combine AsciiDoc files**: Łączy generowane pliki AsciiDoc w jedno.

- **Convert AsciiDoc to PDF**: Konwertuje dokumentację do formatu PDF.

- **Copy PDF to docs directory**: Kopiuje wygenerowany PDF do katalogu docs.

- **Archive AsciiDoc and PDF files**: Archiwizuje wygenerowane pliki do pobrania.

- **Commit PDF file**: Tworzy commita z wygenerowanym PDFem.

- **Push changes**: Pushuje zmiany na odpowiednią gałąź.

## Wymagania
Plik musi mieć dostęp do tokenu PAT właściciela repozytorium (`secrets.MY_TOKEN`), aby umożliwić push zmian na odpowiednią gałąź.
