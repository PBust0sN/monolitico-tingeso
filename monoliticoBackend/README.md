# Toolrent Backend

Para correr el backend del proyecto debe (ocupando intellij)

primero debe abrir las variables de entorno desde

```
Run->Edit Configurations...
```

Debe habilitar En el apartado modify options, la seccion de Enviroment Variables, en donde debe configurar las siguientes variables de entorno

```
# ip del host de la base de datos en mysql
DB_HOST=

# url del issuer de keycloak
KEYCLOAK_ISSUER_URI=

# url a donde redireccionará keycloak 
REDIRECT_URI=
```

Además debe crear una base de datos en mysql con el nombre

```
tingeso
```

y además esta debe estar corriendo en el puerto

```
3306
```

no se entrará mas en detalle de las configuraciones del keycloak, estas se tratarán en el readme principal del proyecto (ubicado en la raiz del proyecto)



