These are meant as a startup scripts for dev environments. One folder for each database. One for the mirid controller and one for the registry.

These can be loaded directly into a Postgres container by mounting the folder into the container's `/docker-entrypoint-initdb.d` folder.

They also assume the username is called `devusername` when giving permission toa access database.

Docker run example:
```shell
docker run --rm -e 'POSTGRES_PASSWORD=devpassword' -e 'POSTGRES_USER=devusername' -v './registry:/docker-entrypoint-initdb.d' -p '5432:5432' --name 'idorg-registry-db' postgres
```

See [registry's folder docker compose yaml](../webservices/registry/docker-compose-development.yml) for an example on how to use this in conjunction with other services.
