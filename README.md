# Star Wars GraphQL Demo

A comprehensive GraphQL API demo showcasing Viaduct's custom directives, batch resolvers, and performance optimizations using Star Wars data.

## Requirements

- Java JDK 21 is installed
- JAVA_HOME environment variable is set correctly or `java` is in classpath

## Quick Start

For more information, check out the [Viaduct Getting Started](https://airbnb.io/viaduct/docs/getting_started/) docs

### 1. Start the demo app

```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`

### 2. Access GraphiQL

Open your browser and go to [http://localhost:8080/graphiql]()

### 3. Try Example Queries

#### Basic Characters Query
```graphql
query {
  allCharacters(limit: 5) {
    id
    name
    homeworld { name }
  }
}
```

#### Complex Query with Batch Resolution
```graphql
query {
  allCharacters(limit: 3) {
    name
    homeworld { name }
    species { name }
    filmCount
    richSummary
  }
}
```

#### Film Query with Characters
```graphql
query {
  allFilms {
    title
    director
    mainCharacters {
      name
      homeworld { name }
    }
  }
}
```

### 4. Nodes and Node IDs

A central concept in Viaduct is that of a `Node`: an entity that can be fetched by an "identifier" (i.e., a primary key).  In GraphQL these identifiers have the type `ID`, and `Node` types always have an `id` field containing the `Node`'s identifier.

Internally, an `ID` consists of two components: a `Node` type name, and a "local" identifier of the particular `Node` instance.  For example, in the StarWars application, the `Character` with "local id" 5 happens to be Obi-Wan Kenobi.

To prevent developers from "hardwiring" any particular representation of identifiers into their code, Viaduct uses an encoding of `ID`s that obscures a bit their content.  However, in GraphiQL, you do need to provide encoded `ID`s to fields like `Query.node`.  To help you navigate the data set, we've incorporated an encoder (and decoder) for ids.  Along the left-hand side of GraphiQL you'll see a "key" icon (🔑).  If you press that icon, a panel will come up for encoding IDs.  Type into the top text box a string of the form "*TypeName:LocalId*".  So, for example, if you enter "Character:5" into that box, the string "Q2hhcmFjdGVyOjU=" will be displayed.  In the query panel of GraphiQL you can use this id in a query like:

```graphql
query {
  node(id: "Q2hhcmFjdGVyOjU=") {
    ... on Character {
      name
      homeworld {
        id
      }
    }
  }
}
```

in your result you should see:

```JSON
{
  "data": {
    "node": {
      "name": "Obi-Wan Kenobi",
      "homeworld": {
        "id": "UGxhbmV0OjQ="
      }
    }
  }
}
```

you can put this "UGxhbmV0OjQ=" id into the decoder tool, which will tell you this is the id for "Planet:4".  You can also drop this identifier into a `node` query to retrieve the name of this planet:


```graphql
query {
  node(id: "UGxhbmV0OjQ=") {
    ... on Planet {
      name
    }
  }
}
```

in your result you should see:

```JSON
{
  "data": {
    "node": {
      "name": "Stewjon"
    }
  }
}
```


## Technical Deep Dive

For comprehensive technical details - especially if you want to write your own application, see [DEEPDIVE.md](DEEPDIVE.md).
