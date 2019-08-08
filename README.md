# DevOps Secrets Vault Jenkins Plugin
This plugin allows access to the Thycotic Vault API to access secrets used in the build process.
Use of the is plugin must be associated with a licensed version of the Thycotic Vault.

## Limitations
Currently this plugin only supports `json` formatted secrets within the vault. The secret data values must 
be simple json types, complex types such as arrays are not currently supported.

Supported by the plugin
```
{
    "id": "0a71c5c0-5198-4c17-b2e3-c9e8703ef03d",
    "path": "path:to:secret",
    "data": {
        "password": "somepassword1",
        "username": "someuser"
    }
}
```

Not supported
```
{
    "id": "0a71c5c0-5198-4c17-b2e3-c9e8703ef03d",
    "path": "path:to:secret",
    "data": {
        "password": ["somepassword1", "somepassword2"],
        "username": "someuser"
    }
}
```

## Building and Running

This plugin requires the use of Java 8 along with Maven 3.5+

### Helpful Commands
- Create plugin - `mvn clean install`
- Run tests - `mvn clean test`
- Run Plugin Locally - `mvn hpi:run`

### Example Usage in Jenkins

Additional examples are documented [Here](https://docs.thycotic.com/dsv-extension-jenkins) 

```
node {
    // define the secret key value and the env variables the key matches the attribute name in the secret json. 
    // Only simple json types are supported for the secret value.
    def secretValues = [
        [$class: 'ThycoticSecretValue', key: 'password', envVar: 'secret']
    ]
    
    // define the path to the secret stored in DevOps Secrets Vault
    def secrets = [
        [$class: 'ThycoticSecret', path: 'path/to/your/secret', secretValues: secretValues]
    ]

    // set the jenkins credential id used to connect to the vault
    def configuration = [$class: 'DevOpsSecretsVaultConfiguration',
                       thycoticCredentialId: '"dsv-auth-credentials"']

    // instantiate the build wrapper to access the populated environment variables
    wrap([$class: 'ThycoticVaultBuildWrapper', configuration: configuration, thycoticVaultSecrets: secrets]) {
        echo "my secret is $secret"
    }
}
```