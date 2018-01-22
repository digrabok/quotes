## Requirements
- Java 1.8

## Configuration
File ```quoter/src/main/resources/application.yml.sample``` should be copped to ```quoter/src/main/resources/application.yml```.   
File ```quotes.txt``` (contains quotes) should be copped to ```quoter/src/main/resources/```.

Search algorithm have additional configuration properties:   
- ```quotes-search.transformations``` - additional text transformations for search. 
Available transformations:   
ALPHANUMERIC_ONLY - only letters and numbers will be compared during search (any whitespace symbols e.t.c. will be skipped).   
CASE_INSENSITIVE - search will be case insensitive.   
This properties could be deleted if it will be necessary.

## Usage
1. Build:   
```./gradlew build```   
2. Run:   
```java -jar build/libs/quoter-1.0-SNAPSHOT.jar```
3. Main URL:   
```http://127.0.0.1:8080/api/search?text=```   
_text_ property should contain text for analyse.