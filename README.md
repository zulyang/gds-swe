# GDS SWE Mini Project

User Web Application with HTTP endpoints, built with Spring Boot. 
 
## Requirements

For building and running the application you need:
- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 4](https://maven.apache.org)

## Database

The web app uses a H2 in-memory database. It can be changed easily in the `application.properties`file. 
## Running the application locally

You'll need Java 8 installed.

     ./mvnw spring-boot:run

To test that it works, open a browser tab at http://localhost:8080/users.  
Alternatively, you can run

    curl 'http://localhost:8080/users'


## Acceptance Requirement 1
#### 1. Return all users
```
curl 'https://localhost:8080/users'
```
Expected Response Body: 
```json
{
    "results": [
        {
            "name": "John",
            "salary": 2500.05
        },
        {
            "name": "Mary Posa",
            "salary": 4000.0
        },
        {
            "name": "Sally Tan",
            "salary": 1000.0
        }
    ]
}
```

#### 2. End Point Parameters (Valid)

```
curl 'http://localhost:8080/users?min=1000&max=3000&sort=salary'
```
Expected Response Body: 
```json
{
    "results": [
        {
            "name": "Sally Tan",
            "salary": 1000.0
        },
        {
            "name": "John",
            "salary": 2500.05
        }
    ]
}
```

#### 3. End Point Parameters (Invalid)
Run:

```
curl 'http://localhost:8080/users?sort=birthdate'
```
Expected Response: 
```json
{
    "error": "Illegal Sorting Parameter"
}
```
Run:
```
curl 'http://localhost:8080/users?min=onehundred'
```
Expected Response: 
```json
{
    "error": "For input string: \"onehundred\""
}
```

## Acceptance Requirement 2
CSV files can be found in the userapp/csv folder. 

#### 1. Upload valid CSV

csv file: `acceptance-criteria-2.csv`
```csv
NAME,SALARY
Peter Parker,0
Tony Stark,1000000
Steve Rogers,2000
Thor,3000.1
Bruce Banner,2100
Tchalla,3999
Natasha Romanoff,2100.83
Thanos,-100
John,1200.05
Mary Posa,1500.15
```
Run:
```
curl -F file=@<file_location> http://localhost:8080/upload/
```
Expected Response:
```json
{
    "Success": 1
}
```
Validate Response:

Run:
```
curl 'https://localhost:8080/users/'
```
Expected Response:
```json
{
    "results": [
        {
            "name": "John",
            "salary": 1200.05
        },
        {
            "name": "Mary Posa",
            "salary": 1500.15
        },
        {
            "name": "Sally Tan",
            "salary": 1000.0
        },
        {
            "name": "Peter Parker",
            "salary": 0.0
        },
        {
            "name": "Steve Rogers",
            "salary": 2000.0
        },
        {
            "name": "Thor",
            "salary": 3000.1
        },
        {
            "name": "Bruce Banner",
            "salary": 2100.0
        },
        {
            "name": "Tchalla",
            "salary": 3999.0
        },
        {
            "name": "Natasha Romanoff",
            "salary": 2100.83
        }
    ]
}
```
## Acceptance Requirement 3

#### 1. Upload invalid CSV File (Salary column has invalid valid type)
csv file: `acceptance-criteria-3a.csv`
```csv
NAME,SALARY
Peter Parker,20
Tony Stark,2000000
Steve Rogers,2000
Thor,3000.1
Bruce Banner,HULK
Tchalla,3999
Natasha Romanoff,2100.83
Thanos,-100
John,1200.05
Mary Posa,1500.15
```
Run:
```
curl -F file=@<file_location> http://localhost:8080/upload/
```
Expected Response: 
```json
{
    "Success": 0
}
```
Validate Response:

Run:
```
curl 'https://localhost:8080/users/'
```
Expected Response: 
```json
{
    "results": [
        {
            "name": "John",
            "salary": 1200.05
        },
        {
            "name": "Mary Posa",
            "salary": 1500.15
        },
        {
            "name": "Sally Tan",
            "salary": 1000.0
        },
        {
            "name": "Peter Parker",
            "salary": 0.0
        },
        {
            "name": "Steve Rogers",
            "salary": 2000.0
        },
        {
            "name": "Thor",
            "salary": 3000.1
        },
        {
            "name": "Bruce Banner",
            "salary": 2100.0
        },
        {
            "name": "Tchalla",
            "salary": 3999.0
        },
        {
            "name": "Natasha Romanoff",
            "salary": 2100.83
        }
    ]
}
```
#### 2. Upload invalid CSV File (Extra column)

csv file: `acceptance-criteria-3b.csv`
```csv
NAME,SALARY,ALIAS
Peter Parker,2,Spiderman
Tony Stark,1000000,Ironman
Steve Rogers,3000,Captain America
Thor,3000.1,Thor
Bruce Banner,2100,Hulk
Tchalla,3999,Black Panther
Natasha Romanoff,2100.83,Black Widow
Thanos,-100,Thanos
John,1200.05,Hancock
Mary Posa,1500.15,Poppins
```
Expected Response: 
```json
{
    "Success": 0
}
```
Validate Response: Same as above
