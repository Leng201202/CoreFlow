# CoreFlow --- Project Proposal

## 1. Project Overview

**Project Name:** CoreFlow\
**Project Type:** Core Banking Management System\
**Current Scope:** Individual Customer and Account Management\
**Primary Goal:** Build a backend-focused banking system that models
realistic customer onboarding and bank account management before
expanding into transactions, ledger accounting, payments, loans, and
other banking capabilities.

CoreFlow is designed as a learning and portfolio project using modern
Java and Spring technologies. The system will initially focus on
**individual customers only**. Organization and corporate banking
features are outside the current scope and may be introduced in a later
version.

------------------------------------------------------------------------

## 2. Problem Statement

Banking systems must maintain accurate customer information, identity
verification records, account ownership, banking products, currencies,
and balances while preserving clear relationships between these records.

A simple CRUD design is not sufficient for a realistic banking
application. CoreFlow therefore separates customer information, KYC
information, account ownership, account products, currencies, and
balances into dedicated domain entities with explicit relationships and
business rules.

The first version establishes this foundation before implementing
financial transactions.

------------------------------------------------------------------------

## 3. Project Objectives

The current objectives are to:

-   Manage individual banking customers.
-   Store customer profile, contact, address, identity document, and KYC
    information.
-   Support customer verification and customer lifecycle statuses.
-   Allow one customer to own multiple bank accounts.
-   Support individual and joint account ownership.
-   Support multiple banking products such as Savings and Current
    accounts.
-   Support accounts in different currencies.
-   Maintain a current posted balance for each account.
-   Create a domain structure that can later support transactions,
    double-entry ledger accounting, FX, holds, loans, security, and
    auditing.

------------------------------------------------------------------------

## 4. Proposed Technology Stack

### Backend

-   Java 21
-   Spring Boot
-   Spring Data JPA
-   Spring Security
-   PostgreSQL
-   Flyway
-   Bean Validation
-   JUnit and Mockito
-   Testcontainers
-   OpenAPI / Swagger

### Frontend

-   React
-   TypeScript
-   Vite
-   Tailwind CSS
-   shadcn/ui
-   TanStack Query
-   React Hook Form
-   Zod

### Development and Deployment

-   Git and GitHub
-   Docker
-   GitHub Actions

Additional technologies such as Redis and Spring Batch may be introduced
when later banking domains require them.

------------------------------------------------------------------------

# 5. Current Functional Scope

The current development scope contains two primary domains:

1.  **Customer Domain**
2.  **Account Domain**

Financial transactions are intentionally excluded from the current
phase.

------------------------------------------------------------------------

# 6. Customer Domain

## 6.1 Purpose

The Customer Domain represents an individual who has a banking
relationship with CoreFlow.

The domain separates the main customer identity from profile, contact,
address, identity-document, and KYC information so that each type of
information can evolve independently.

## 6.2 Customer Domain Entities

### CUSTOMER

The root record for an individual banking customer.

  -----------------------------------------------------------------------
  Field             PostgreSQL Type   Java Type         Description
  ----------------- ----------------- ----------------- -----------------
  id                BIGSERIAL         Long              Internal primary
                                                        key

  customer_uuid     UUID              UUID              External/API
                                                        identifier

  customer_number   VARCHAR(30)       String            Unique banking
                                                        customer number

  status            VARCHAR(30)       Enum              Customer
                                                        lifecycle status

  created_at        TIMESTAMPTZ       OffsetDateTime    Creation
                                                        timestamp

  updated_at        TIMESTAMPTZ       OffsetDateTime    Last update
                                                        timestamp
  -----------------------------------------------------------------------

Possible statuses:

-   `PENDING_VERIFICATION`
-   `ACTIVE`
-   `SUSPENDED`
-   `BLOCKED`
-   `CLOSED`

The current version supports **individual customers only**, so a
customer-type discriminator is not required for V1.

### CUSTOMER_PROFILE

Stores personal information about the customer.

Main fields:

-   first_name
-   middle_name
-   last_name
-   date_of_birth
-   gender
-   nationality
-   occupation

Relationship:

`CUSTOMER 1 : 1 CUSTOMER_PROFILE`

### CUSTOMER_CONTACT

Stores customer communication methods.

Examples:

-   Email
-   Phone

Important fields include:

-   contact_type
-   contact_value
-   is_primary
-   verification_status
-   verified_at

Relationship:

`CUSTOMER 1 : N CUSTOMER_CONTACT`

### CUSTOMER_ADDRESS

Stores one or more addresses belonging to the customer.

Examples:

-   Residential address
-   Mailing address

Main fields include:

-   address_type
-   country
-   state
-   city
-   postal_code
-   address_line
-   is_primary

Relationship:

`CUSTOMER 1 : N CUSTOMER_ADDRESS`

### IDENTITY_DOCUMENT

Stores documents used to establish customer identity.

Possible document types include:

-   Passport
-   National ID
-   Driver License
-   Residence Permit

Main fields include:

-   document_type
-   document_number
-   issuing_country
-   issue_date
-   expiry_date
-   verification_status

Relationship:

`CUSTOMER 1 : N IDENTITY_DOCUMENT`

### KYC_RECORD

Stores Know Your Customer verification information.

Main fields include:

-   verification_level
-   status
-   verified_by_user_id
-   verified_at
-   expiry_date

Possible statuses:

-   `PENDING`
-   `APPROVED`
-   `REJECTED`
-   `EXPIRED`

Relationship:

`CUSTOMER 1 : N KYC_RECORD`

The `verified_by_user_id` field is intended to reference the system user
responsible for the verification. The complete User Account / Identity
and Access domain will be designed separately.

------------------------------------------------------------------------

## 6.3 Customer Domain Relationship Model

``` text
CUSTOMER
   |
   +-- 1:1 --> CUSTOMER_PROFILE
   |
   +-- 1:N --> CUSTOMER_CONTACT
   |
   +-- 1:N --> CUSTOMER_ADDRESS
   |
   +-- 1:N --> IDENTITY_DOCUMENT
   |
   +-- 1:N --> KYC_RECORD
```

------------------------------------------------------------------------

# 7. Account Domain

## 7.1 Purpose

The Account Domain represents bank accounts owned by individual
customers.

A customer can own multiple accounts, including accounts in different
currencies. CoreFlow also supports joint ownership through an Account
Holder entity.

Account configuration is separated from the actual customer account
through the Account Product entity.

------------------------------------------------------------------------

## 7.2 Account Domain Entities

### ACCOUNT

Represents one actual bank account.

Main fields:

  -----------------------------------------------------------------------
  Field             PostgreSQL Type   Java Type         Description
  ----------------- ----------------- ----------------- -----------------
  id                BIGSERIAL         Long              Internal primary
                                                        key

  uuid              UUID              UUID              External/API
                                                        identifier

  account_number    VARCHAR(34)       String            Unique account
                                                        number

  product_id        BIGINT            AccountProduct    Banking product

  currency_code     CHAR(3)           Currency/String   Account currency

  ownership_type    VARCHAR(20)       Enum              Individual or
                                                        joint

  status            VARCHAR(20)       Enum              Account lifecycle
                                                        status

  opened_at         TIMESTAMPTZ       OffsetDateTime    Opening timestamp

  closed_at         TIMESTAMPTZ       OffsetDateTime    Closing
                                                        timestamp, if
                                                        closed

  overdraft_limit   NUMERIC(19,4)     BigDecimal        Approved
                                                        overdraft limit
                                                        when applicable
  -----------------------------------------------------------------------

Possible account statuses:

-   `PENDING`
-   `ACTIVE`
-   `FROZEN`
-   `DORMANT`
-   `CLOSED`

Possible ownership types:

-   `INDIVIDUAL`
-   `JOINT`

An account does **not** store `account_type` directly. Its type is
determined by its associated Account Product.

------------------------------------------------------------------------

### ACCOUNT_HOLDER

Connects customers to accounts and allows CoreFlow to support both
individual and joint accounts.

Main fields:

-   id
-   account_id
-   customer_id
-   holder_role
-   ownership_percentage
-   added_at

Possible holder roles for the current scope:

-   `PRIMARY`
-   `CO_HOLDER`

Relationships:

`CUSTOMER 1 : N ACCOUNT_HOLDER`

`ACCOUNT 1 : N ACCOUNT_HOLDER`

Therefore:

`CUSTOMER N : M ACCOUNT`

For an individual account, one customer normally owns the account. For a
joint account, multiple customers can be connected to the same account.

------------------------------------------------------------------------

### ACCOUNT_PRODUCT

Defines the banking product and its rules.

Examples:

-   Global Savings
-   Student Savings
-   Premium Savings
-   Personal Current

Main fields:

  Field                 PostgreSQL Type   Java Type
  --------------------- ----------------- ---------------
  id                    BIGSERIAL         Long
  product_code          VARCHAR(30)       String
  product_name          VARCHAR(100)      String
  account_type          VARCHAR(20)       AccountType
  interest_rate         NUMERIC(8,5)      BigDecimal
  minimum_balance       NUMERIC(19,4)     BigDecimal
  overdraft_supported   BOOLEAN           Boolean
  status                VARCHAR(20)       ProductStatus

Current account types:

-   `SAVINGS`
-   `CURRENT`

Product status:

-   `ACTIVE`
-   `INACTIVE`

The product determines the broad account type and product-level rules.
The actual Account represents the customer's specific instance of that
product.

Relationship:

`ACCOUNT_PRODUCT 1 : N ACCOUNT`

------------------------------------------------------------------------

### ACCOUNT_BALANCE

Stores the current posted balance of an account.

Current V1 design:

  -----------------------------------------------------------------------
  Field             PostgreSQL Type   Java Type         Description
  ----------------- ----------------- ----------------- -----------------
  id                BIGSERIAL         Long              Primary key

  account_id        BIGINT            Account           FK + UNIQUE

  ledger_balance    NUMERIC(19,4)     BigDecimal        Current
                                                        officially posted
                                                        account balance
  -----------------------------------------------------------------------

Relationship:

`ACCOUNT 1 : 1 ACCOUNT_BALANCE`

CoreFlow V1 will not initially implement account holds. When pending or
approval-based financial operations are introduced, an `ACCOUNT_HOLD`
entity can be added rather than storing only a single unexplained hold
amount.

------------------------------------------------------------------------

### CURRENCY

Represents a currency supported by the banking system.

  Field            PostgreSQL Type   Java Type   Description
  ---------------- ----------------- ----------- ---------------------------------
  code             CHAR(3)           String      Primary currency identifier
  name             VARCHAR(100)      String      Currency name
  numeric_code     CHAR(3)           String      Numeric currency code
  decimal_places   SMALLINT          Short       Supported minor-unit precision
  symbol           VARCHAR(10)       String      Display symbol
  enabled          BOOLEAN           Boolean     Whether the currency is enabled

Examples:

``` text
USD | US Dollar | 840 | 2 | $
THB | Thai Baht | 764 | 2 | ฿
JPY | Japanese Yen | 392 | 0 | ¥
```

Relationship:

`CURRENCY 1 : N ACCOUNT`

Each account has exactly one currency. A customer who needs multiple
currencies opens separate accounts.

Exchange rates are **not** stored in the Currency entity. Foreign
exchange will belong to a future FX/Transaction domain.

------------------------------------------------------------------------

# 8. Combined Domain Relationship Model

``` text
CUSTOMER
   |
   +-- 1:1 --> CUSTOMER_PROFILE
   |
   +-- 1:N --> CUSTOMER_CONTACT
   |
   +-- 1:N --> CUSTOMER_ADDRESS
   |
   +-- 1:N --> IDENTITY_DOCUMENT
   |
   +-- 1:N --> KYC_RECORD
   |
   +-- 1:N --> ACCOUNT_HOLDER
                     |
                     | N:1
                     v
                  ACCOUNT
                     |
                     +-- N:1 --> ACCOUNT_PRODUCT
                     |
                     +-- N:1 --> CURRENCY
                     |
                     +-- 1:1 --> ACCOUNT_BALANCE
```

The `ACCOUNT_HOLDER` entity creates a many-to-many relationship between
Customer and Account while preserving ownership-specific information.

------------------------------------------------------------------------

# 9. Core Business Rules

## Customer Rules

-   Every customer must have a unique customer number.
-   External APIs should use UUIDs instead of exposing sequential
    database IDs.
-   A customer may have multiple contacts, addresses, identity
    documents, and KYC records.
-   A customer must pass the required KYC process before unrestricted
    banking operations are allowed.
-   Historical verification information should be preserved for
    auditability.

## Account Rules

-   Every account must have a unique account number.
-   Every account must reference one valid Account Product.
-   Every account must use exactly one currency.
-   One customer may own multiple accounts.
-   One account may have multiple holders when it is a joint account.
-   An individual account should have one holder.
-   A joint account should have two or more holders.
-   The same customer should not be attached to the same account more
    than once.
-   Every account should have one current Account Balance record.
-   Financial amounts must use `BigDecimal` in Java and `NUMERIC` in
    PostgreSQL.
-   `double` and `float` must not be used for monetary values.
-   Closed accounts cannot perform new financial transactions.
-   Frozen accounts must restrict outgoing financial operations
    according to banking rules.
-   Product rules determine whether overdraft functionality is
    supported.
-   An individual account's approved overdraft limit is stored
    separately from the product-level overdraft capability.

------------------------------------------------------------------------

# 10. Shared Audit Model

CoreFlow will use a reusable base entity for appropriate business
entities.

``` text
BaseEntity
-------------------------
active
created_at
updated_at
deleted_at
created_by
updated_by
deleted_by
```

Recommended timestamp mapping:

-   PostgreSQL: `TIMESTAMPTZ`
-   Java: `OffsetDateTime`

The `created_by`, `updated_by`, and `deleted_by` fields are intended to
reference authenticated system users once the Identity and Access domain
is implemented.

Reference/master data such as Currency does not necessarily need to
inherit the complete BaseEntity structure.

------------------------------------------------------------------------

# 11. Out of Scope for Current Phase

The following features are intentionally excluded from the current
implementation phase:

-   Organization/corporate customers
-   Corporate account authorities
-   Maker-checker approval
-   Deposits and withdrawals
-   Account-to-account transfers
-   International transfers
-   Foreign exchange conversion
-   Transaction holds
-   Card authorization
-   Double-entry ledger
-   Payment processing
-   Loans
-   Interest posting
-   Fees
-   End-of-day batch processing
-   Redis-based caching
-   Advanced fraud detection

These capabilities can be added incrementally after the Customer and
Account foundations are stable.

------------------------------------------------------------------------

# 12. Planned Future Domains

The proposed expansion path is:

``` text
Customer
    |
    v
Account
    |
    v
Transaction
    |
    v
Ledger
    |
    +--> Payment / Transfer
    +--> FX
    +--> Holds
    +--> Loans
    +--> Fees / Interest
    +--> EOD / Batch
```

### Transaction Domain

Will manage financial operations such as:

-   Deposit
-   Withdrawal
-   Internal transfer
-   Transaction status
-   Idempotency
-   Balance validation
-   Concurrency protection

### Ledger Domain

Will introduce proper accounting records and double-entry principles so
financial history becomes auditable and consistent.

### Security and Audit Domain

Will manage:

-   Authentication
-   Authorization
-   Banking roles
-   Audit events
-   Sensitive-operation tracking

------------------------------------------------------------------------

# 13. Current Project Milestone

## Milestone 1 --- Customer and Account Foundation

**Status: Design Phase Completed / Implementation Pending**

Deliverables:

-   [x] Customer domain identified
-   [x] Customer entities designed
-   [x] Customer relationships designed
-   [x] KYC and identity-document structure designed
-   [x] Account domain identified
-   [x] Account ownership model designed
-   [x] Joint account support designed
-   [x] Account Product model designed
-   [x] Savings and Current account types defined
-   [x] Multi-currency account structure designed
-   [x] Account Balance structure designed
-   [x] Core domain relationships reviewed
-   [ ] PostgreSQL schema implementation
-   [ ] Spring Boot JPA entity implementation
-   [ ] Repository layer
-   [ ] Service/business-rule implementation
-   [ ] REST API implementation
-   [ ] Unit and integration testing
-   [ ] React UI implementation

------------------------------------------------------------------------

# 14. Current Project Boundary

CoreFlow V1 currently answers two fundamental banking questions:

**Who is the bank serving?**

→ Customer Domain

**Where is the customer's money held?**

→ Account Domain

The next major phase will answer:

**What happens when money moves?**

→ Transaction Domain

This staged approach keeps the project understandable while providing a
foundation that can grow into a more complete core banking system.
