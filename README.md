# Investments tracker application

_(A brief summary of what your project does in 1-2 lines.)_


## Table of Contents

<details>
<summary><h3>About the Project</h3></summary>

This project is an Investments tracker application, that helps you track your investments.
Deposits and Withdrawals can be only in 1 currency - EUR

</details>


<details>
<summary><h3>Technologies Used</h3></summary>

| Layer           | Technology         |
|-----------------|--------------------|
| Backend         | Java & Spring Boot |
| Database        | MySQL              |
| ORM             | Spring Data JPA    |
| Authentication  | JWT?               |
| Build tools     | Maven              |
| Frontend        | React              |
| Version control | Git                |

</details>


<details>
<summary><h3>Architecture Overview</h3></summary>

_Diagram how the app works_

</details>


<details>
<summary><h3>Features</h3></summary>

User Registration & Login
JWT Authentication
CRUD Operations
Role-based Authorization
Pagination & Sorting
Swagger API Documentation
Error Handling

</details>


<details>
<summary><h3>Installation Guide</h3></summary>

What is needed to run the project

</details>


<details>
<summary><h3>How to Run the Project</h3></summary>

Backend installation
Frontend installation

</details>


<details>
<summary><h3>API Endpoints</h3></summary>

http://localhost:8080/swagger-ui/index.html

Create an openapi.json file and automatically generate API docs using Swagger.

| Method | Endpoint                                                                                      | Description                                                   | Auth required |
|--------|-----------------------------------------------------------------------------------------------|---------------------------------------------------------------|---------------|
| `POST` | `api/v1/deposits/in`                                                                          | Insert a deposit                                              | N/A           |
| `GET`  | `/api/v1/cashtransactions/get?CashTransactionType={Type}&fromDate={fromDate}&toDate={toDate}` | Get deposits / withdrawals / dividends / fees in range        | N/A           |
| `GET`  | `/api/v1/cashtransactions/get/total/amount?CashTransactionType={type}`                        | Get total amount of deposits / withdrawals / dividends / fees | N/A           |
| `POST` | `/withdrawal/out`                                                                             | Make a withdrawal                                             |               |
| `GET`  | `/withdrawal/get/from/{fromDate}/to/{toDate}`                                                 | Get withdrawals in range                                      |               |
| `GET`  | `/withdrawal/get/all`                                                                         | Get all withdrawals                                           |               |
| `GET`  | `/withdrawal/get/total/amount`                                                                | Get total amount of withdrawals                               |               |
| `POST` | `/dividend/in`                                                                                | Insert a dividend                                             |               |
| `GET`  | `/dividend/get/from/{fromDate}/to/{toDate}`                                                   | Get dividends in range                                        |               |
| `GET`  | `/dividend/get/all`                                                                           | Get all dividends                                             |               |
| `GET`  | `/dividend/get/total/amount`                                                                  | Get total amount of dividends                                 |               |
| `POST` | `/api/transaction/in`                                                                         | Insert a transaction                                          |               |

</details> 


<details>
<summary><h3>Database Structure</h3></summary>

DB schema

</details>


<details>
<summary><h3>Folder Structure</h3></summary>

Screenshot from IntelliJ

</details>


<details>
<summary><h3>Screenshots</h3></summary>

Home Page
Login Page

</details>


<details>
<summary><h3>Future Improvements</h3></summary>

- Add Swagger-ui
- Add email notification for weekly portfolio view
- Docker

</details>


<details>
<summary><h3>Author</h3></summary>

#### Your Name: Denis Buserski 
#### [LinkedIn](https://www.linkedin.com/in/denis-buserski/) 
#### [GitHub](https://github.com/DenisBuserski) 

</details>


<details>
<summary><h3>License</h3></summary>

This project is licensed under the MIT License. (ADD MIT)

</details>
