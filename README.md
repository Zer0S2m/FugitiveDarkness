# Fugitive Darkness

Search engine from different sources:

- Indexing git sources and searching.
- DOCX Documents.

### Development - Server

Before starting, create an .env file and edit it:

```bash
cp ./.env.example ./.env
```

_[Parameter information](#env-params)_

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

Install dependencies:

```bash
npm i
```

Run system:

```bash
npm run dev
```

### Env params

| Env param                  | Value                                                  | Example                                       |
|----------------------------|--------------------------------------------------------|-----------------------------------------------|
| **`FD_ROOT_PATH_REPO`**    | Root folder for storing locally installed repositories | `/path/FD/repo`                               |
| **`FD_ALLOW_ORIGIN`**      | Set the list of allowed origins.                       | `http://localhost:5173,http://localhost:5174` |
| **`FD_POSTGRES_PORT`**     | Indicates the port where the server is listening       | `localhost`                                   |
| **`FD_POSTGRES_HOST`**     | Is where the server host is located                    | `5432`                                        |
| **`FD_POSTGRES_PASSWORD`** | Password for connecting to the specified database      | `my_db_password`                              |
| **`FD_POSTGRES_USER`**     | Database user                                          | `my_db_user`                                  |
| **`FD_POSTGRES_DB`**       | Database name                                          | `my_db`                                       |

### Build

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
java -jar ./fugitive-darkness-api/target/fugitive-darkness-api-0.0.5-fat.jar
```
