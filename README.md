# Testovací úloha: Import dat z RÚIAN (XML do PostgreSQL)

Tento projekt slouží ke stažení, zpracování a uložení prostorových dat (RÚIAN/VFR) z rozsáhlého XML archivu do relační databáze. Řešení využívá paměťově efektivní StAX parser a je plně kontejnerizováno pomocí Dockeru.

## 🚀 Návod ke spuštění

### 1. Sestavení projektu (Build)
Před spuštěním kontejnerů je nutné aplikaci zkompilovat do `.jar` souboru. V kořenovém adresáři projektu spusťte:
`bash
mvn clean package -DskipTests
`
*(Případně pomocí wrapperu: `.\mvnw clean package -DskipTests`)*

### 2. Spuštění prostředí
Aplikaci a databázi spustíte synchronně jediným příkazem pomocí Docker Compose:
`bash
docker-compose up --build
`

### 3. Spuštění importu (Testování)
Samotný proces stažení a parsování XML souboru je vystaven přes REST API. Pro spuštění procesu odešlete **GET požadavek** (přes Postman, cURL nebo běžný prohlížeč) na adresu:
`text
http://localhost:8080/getObce
`
*Aplikace následně stáhne ZIP archiv, rozparsuje data (s ošetřením vnořených referencí) a uloží entity do PostgreSQL. Výsledkem volání bude JSON odpověď s uloženými daty.*
