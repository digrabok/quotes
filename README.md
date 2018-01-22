## Requirements
- Java 1.8

## Configuration
File ```quoter/src/main/resources/application.yml.sample``` should be copped to ```quoter/src/main/resources/application.yml```.   
File ```quotes.txt``` (contains quotes) should be copped to ```quoter/src/main/resources/```.

## Usage
1. Build:   
```./gradlew build```   
2. Run:   
```java -jar build/libs/quoter-1.0-SNAPSHOT.jar```
3. Main URL:   
```http://127.0.0.1:8080/api/search?text=```   
_text_ property should contain text for analyse.