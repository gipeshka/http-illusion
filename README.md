Http Illusion
==================

## What is it ##

The service is built to serve as a proxy decorator for HTTP interaction between services.
The idea is to define criteria and HTTP responses dynamically to mock third party service.

## What can it do ##

The service exposes an api to setup criteria and HTTP response.
Whenever it receives a http request the service checks if there is a matching criteria, if so returns the matching http response, otherwise just proxies request/response.

## Setup ##

See [Swagger documentation](http://htmlpreview.github.io/?https://github.com/gipeshka/http-illusion/blob/master/docs/index.html)

Criteria is defined by `request` object in a request.

Criteria has the following fields that can be used to describe http request:

- `path` string
- `method` string
- `headers` object, string to string map
- `query` object, string to string map. Matches get parameters of a request
- `bodyJson` object. Matches body of a request
- `bodyUrlEncoded` object, string to string map. Matches body of a request

All the matches check that given value is a subset of the value in a request.

Response is defined by `response` object in a request.

Response has the following fields hat can be used to describe http response:

- `status` int
- `body` Json value

## Examples ##

To setup mock

```bash
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

```bash
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

```bash
curl -X GET http://localhost:9090/setup
```

Example of response

```json
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

To search mocks

```bash
curl -X POST \
  http://localhost:9090/setup/search \
  -H 'Content-Type: application/json' \
  -d '{
	"request": {
		"headers": {
			"Authorization": "Bearer thesupertoken"
		}
	}
}'
```

Example of response

```json
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
    }
]
```

To delete mocks with criteria

```bash
curl -X DELETE \
  http://localhost:9090/setup/search \
  -H 'Content-Type: application/json' \
  -d '{
	"request": {
		"headers": {
			"Authorization": "Bearer thesupertoken"
		}
	}
}'
```

## Env vars ##

The application is using the following  env vars to setup config:
- FALLBACK_SERVICE_HOST host of the service to proxy request if no handler was able to process the request
- FALLBACK_SERVICE_PORT port of the service to proxy request if no handler was able to process the request
- FALLBACK_SERVICE_SCHEME scheme of the request to proxy request if no handler was able to process the request
- APPLICATION_HOST host to bind to
- APPLICATION_PORT port to bind to

When using couchbase as a storage
- COUCHBASE_URL_1
- COUCHBASE_URL_2 optional
- COUCHBASE_URL_3 optional
- COUCHBASE_URL_4 optional
- COUCHBASE_BUCKET_NAME name of a bucket to store configuration to
- COUCHBASE_BUCKET_PASSWORD password of a bucket to store configuration to
