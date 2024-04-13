# Fugitive Darkness

[![](https://img.shields.io/github/v/release/Zer0S2m/FugitiveDarkness.svg)](https://github.com/Zer0S2m/FugitiveDarkness/releases/latest "GitHub release")
![](http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat)
[![](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

Search engine from different sources:

- Indexing git sources and searching.
- DOCX Documents.

## Development

### Development - Server

Before starting, create an .env file and edit it:

```bash
cp ./.env.example ./.env
```

_[Parameter information](#env-params-server)_

Run docker:

```bash
make run-docker
```

Run system:

```bash
make run
```

Run migrations:

```bash
make run-migrate
```

Run full system:

```bash
make dev
```

### Development - Client

Go to working directory:

```bash
cd ./client
```

Before starting, create an .env file and edit it:

```bash
cp ./.env.example ./.env
```

_[Parameter information](#env-params-client)_

Install dependencies:

```bash
npm i
```

Run system:

```bash
npm run dev
```

## Env params

### Env params (Server)

| Env param                  | Value                                                  | Example                                       |
|----------------------------|--------------------------------------------------------|-----------------------------------------------|
| **`FD_ROOT_PATH_REPO`**    | Root folder for storing locally installed repositories | `/path/FD/repo`                               |
| **`FD_ROOT_PATH_DOCX`**    | Root folder for storing locally installed docx files   | `/path/FD/docx`                               |
| **`FD_ALLOW_ORIGIN`**      | Set the list of allowed origins.                       | `http://localhost:5173,http://localhost:5174` |
| **`FD_POSTGRES_PORT`**     | Indicates the port where the server is listening       | `localhost`                                   |
| **`FD_POSTGRES_HOST`**     | Is where the server host is located                    | `5432`                                        |
| **`FD_POSTGRES_PASSWORD`** | Password for connecting to the specified database      | `my_db_password`                              |
| **`FD_POSTGRES_USER`**     | Database user                                          | `my_db_user`                                  |
| **`FD_POSTGRES_DB`**       | Database name                                          | `my_db`                                       |

### Env params (Client)

| Env param              | Value                       | Example                 |
|------------------------|-----------------------------|-------------------------|
| **`VITE_FD_HOST_API`** | The host for the API module | `http://localhost:8080` |

## Build

Build client:

```shell
cd ./client && npm run build
```

Build server:

```shell
mvn clean package
```

Run server:

```shell
java -jar ./fugitive-darkness-api/target/fugitive-darkness-api-0.0.7-fat.jar
```
