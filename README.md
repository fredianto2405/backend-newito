# BACKEND NEWITO

## How to Run Project

```bash
# environment development
mvn spring-boot:run -Pdevelopment
```

```bash
# environment staging
mvn spring-boot:run -Pstaging
```

```bash
# environment production
mvn spring-boot:run -Pproduction
```

## How to Build Project

```bash
# environment development
mvn clean install -DskipTests -Pdevelopment
```

```bash
# environment staging
mvn clean install -DskipTests -Pstaging
```

```bash
# environment production
mvn clean install -DskipTests -Pproduction
```