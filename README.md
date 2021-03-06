# Code Quality:
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4ea36f851d0a4175939b86f869cae720)](https://www.codacy.com/app/sabarnath/graphql-java-spring-boot-example?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sabarnath/graphql-java-spring-boot-example&amp;utm_campaign=Badge_Grade)

# graphql-java-spring-boot-example
Sample app for my tutorial [Building a GraphQL Server with Spring Boot](https://www.pluralsight.com/guides/java-and-j2ee/building-a-graphql-server-with-spring-boot). 

You'll need [Java 9](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html).

Clone this repo and execute `mvnw spring-boot:run`. Or inside an IDE, execute the class `com.example.DemoGraphQL.DemoGraphQlApplication`.

You can go to [http://localhost:8080/h2-console/login.jsp](http://localhost:8080/h2-console/login.jsp) and enter the following information:
- JDBC URL: jdbc:h2:mem:testdb
- User Name: sa
- Password: <blank>

To check the database or to [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to start executing queries. For example:
```
{
  findAllBooks {
    id
    isbn
    title
    pageCount
    author {
      firstName
      lastName
    }
  }
}
```

Or:
```
mutation {
  newBook(
    title: "Java: The Complete Reference, Tenth Edition", 
    isbn: "1259589331", 
    author: 1) {
      id title
  }
}
```

# License
MIT
