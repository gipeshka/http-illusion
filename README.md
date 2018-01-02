Http Illusion
==================

## What is it ##

The service is built to serve as a proxy decorator for HTTP interaction between services.
The idea is to define criteria and HTTP responses dynamically to mock third party service.

## What can it do ##

The service exposes an api to setup criteria and HTTP response.
Whenever it receives a http request the service checks if there is a matching criteria, if so returns the matching http response, otherwise just proxies request/response.

## Examples ##

To setup mock

```
curl -X POST \
  http://localhost:9090/setup \
  -d '{
	"request": {
		"path": "/oauth/token",
		"method": "POST",
		"query": {
			"username": "1d76dc2cdf4b49d9bfd864c5d14a39f4"
		}
	},
	"response": {
		"status": 200,
		"body": {
			"scope": "noone cares",
			"access_token": "thesupertoken",
			"expires_in": 100,
			"token_type": "Bearer"
		}
	}
}'
```

To delete mock
```
curl -X DELETE \
  http://localhost:9090/setup \
  -d '{
	"request": {
		"path": "/oauth/token",
		"method": "POST",
		"query": {
			"username": "1d76dc2cdf4b49d9bfd864c5d14a39f4"
		}
	}
}'
```

To list mocks
```
curl -X GET http://localhost:9090/setup
```

Example of response
```
[
    {
        "request": {
            "headers": {
                "Authorization": "Bearer thesupertoken"
            }
        },
        "response": {
            "status": 200,
            "body": {
                "mock": "I'm just mocking all the requests to external service for the user"
            }
        }
    },
    {
        "request": {
            "method": "POST",
            "path": "/oauth/token",
            "query": {
                "username": "1d76dc2cdf4b49d9bfd864c5d14a39f4"
            }
        },
        "response": {
            "status": 200,
            "body": {
                "access_token": "thesupertoken",
                "token_type": "Bearer",
                "expires_in": 100,
                "scope": "noone cares"
            }
        }
    }
]
```
