# User Management System

User Management System is a system which is responsible for performing operations for the users like add users, display user details, update user details, delete users.

## Types of Users
* Customer
* Admin

## User Fields
* Username
* User type
* Password
* Age
* Email id
* Mobile No.
* Address - Optional field

## CRUD Operations defined
* add
* getById
* getAll
* update
* deleteById
* deleteAll

## Commands

### Compile code

```bash
sbt compile
```
### Execute main class
Firstly, go to the root directory of the module where src and target folder is present and then run the below command:-

```bash
sbt run
```

### Delete all generated files

```bash
sbt clean
```

### Generate scalastyle config file

```bash
sbt scalastyleGenerateConfig
```

### Execute scalastyle plugin

```bash
sbt scalastyle
```