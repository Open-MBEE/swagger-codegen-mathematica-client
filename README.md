# Swagger Codegen for Mathematica

## Overview
This is a project to generate your own Mathematica client library with Swagger.

## What's Swagger?
The goal of Swagger™ is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via Swagger, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, Swagger removes the guesswork in calling the service.


Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the Swagger project, including additional libraries with support for other languages and more. 

## How do I use this?
The client includes something along these lines:

```
.
|- README.md    // this file
|- pom.xml      // build script
|-- src
|--- main
|---- java
|----- org.openmbee.swagger.codegen.MathematicaClientGenerator.java // generator file
|---- resources
|----- mathematica-client // template files
|----- META-INF
|------ services
|------- io.swagger.codegen.CodegenConfig
```

You can run this:

```
mvn package
```

In your generator project.  A single jar file will be produced in `target`.  You can now use that with codegen:

```
java -cp /path/to/swagger-codegen-cli.jar:/path/to/your.jar io.swagger.codegen.Codegen -l mathematica-client -i /path/to/swagger.yaml -o ./test
```

Now your templates are available to the client generator and you can write output values

```
# The following additional debug options are available for all codegen targets:
# -DdebugSwagger prints the OpenAPI Specification as interpreted by the codegen
# -DdebugModels prints models passed to the template engine
# -DdebugOperations prints operations passed to the template engine
# -DdebugSupportingFiles prints additional data passed to the template engine

java -DdebugOperations -cp /path/to/swagger-codegen-cli.jar:/path/to/your.jar io.swagger.codegen.Codegen -l mathematica-client -i /path/to/swagger.yaml -o ./test
```

Will, for example, output the debug info for operations.

It is also highly recommended to specify -DinvokerPackage=API_NAME to prevent namespace conflicts when generating multiple client libraries.