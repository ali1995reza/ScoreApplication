# Overview

The abstract of this project is a simple HTTP server that will let users submit their scores for multiple applications.

Clients can search and get top score list related to any application.

# Server abstract

Server will handle request by following Rest-APIs and DTOs.

## DTOs

### - ClientToken  (Json)

| Field Name           | Format          | Description  |
| :------------------- | :-------------- | :------------|
|`token`               | `string`        | authentication token in string format for future use

<br/>

### - RankedScore  (Json)

| Field Name           | Format          | Description  |
| :------------------- | :-------------- | :------------|
|`id`                  | `string`        | id of the score
|`userId`              | `string`        | the user which this score related to
|`applicationId`       | `string`        | the application which this score related to
|`score`               | `long`          | last and highest submitted score fot the user in the application
|`rank`                | `long`          | user rank in the score list of this application

<br/>

### - SubmitRequest  (Json)

| Field Name           | Format          | Description  |
| :------------------- | :-------------- | :------------|
|`score`               | `long`          | the score which want to submit in score list of an specific application

<br/>

## REST APIs

### - Login

Client can login to application by requesting to this api.

| Url                                 | Http Method    | Request Body |Response Body |
| :--------------------------------- | :-------------- | :------------| :------------|
|`http://[host:port]/login/{userId}` |`GET`            |`-`           | `ClientToken`|

##### Path Parameters

| Name    | Format                         | Description  |
| :-------| :----------------------------- | :------------|
|`userId` | `Regx - ^([0-9a-zA-Z_\-]){3,}$`|  the user id which client want to login by

##### Possible HTTP status codes

| Status               | Description     |
| :------------------- | :-------------- |
|`200`                 | ok              |
|`400`                 | `userId` format is not valid|

<br/>

## - Submit Score

| Url                                                     | Http Method     | Request Body   |Response Body |
| :------------------------------------------------------ | :-------------- | :------------- | :------------|
|`http://[host:port]/applications/{applicationId}/scores` |`PUT`            |`SubmitRequest` | `RankedScore`|

##### Path Parameters

| Name           | Format                         | Description  |
| :------------- | :----------------------------- | :------------|
|`applicationId` | `Regx - ^([0-9a-zA-Z_\-]){3,}$`|  the user id which client want to login by

##### Request Headers

| Name            | Value                          | Description  |
| :-------------- | :----------------------------- | :------------|
|`X-CLIENT-TOKEN` | `authentication token`         |  the authentication token that provided after login

##### Possible HTTP status codes

| Status               | Description     |
| :------------------- | :-------------- |
|`200`                 | ok              |
|`400`                 | if `applicationId` format is not valid<br/>if `score` value is negative|
|`401`                 | `authentication token` expired or invalid|

<br/>

## - Get Top Scores

| Url                                                     | Http Method     | Request Body |Response Body       |
| :------------------------------------------------------ | :-------------- | :------------| :----------------- |
|`http://[host:port]/applications/{applicationId}/scores` |`GET`            |`-`           | `List[RankedScore]`|

##### Path Parameters

| Name           | Format                         | Description  |
| :------------- | :----------------------------- | :------------|
|`applicationId` | `Regx - ^([0-9a-zA-Z_\-]){3,}$`|  the user id which client want to login by

##### Query Parameters

| Name           | Format                         | Description  |
| :------------- | :----------------------------- | :------------|
|`offset`        | `long`                         | offset of top score list
|`size`          | `long`                         | size of top score list

##### Possible HTTP status codes

| Status               | Description     |
| :------------------- | :-------------- |
|`200`                 | ok              |
|`400`                 | if `applicationId` format is not valid<br/>if `size` or `offset` value is invalid

<br/>

## - Search Scores

| Url                                                            | Http Method     | Request Body |Response Body       |
| :------------------------------------------------------------- | :-------------- | :------------| :----------------- |
|`http://[host:port]/applications/{applicationId}/scores/search` |`GET`            |`-`           | `List[RankedScore]`|

##### Path Parameters

| Name           | Format                         | Description  |
| :------------- | :----------------------------- | :------------|
|`applicationId` | `Regx - ^([0-9a-zA-Z_\-]){3,}$`|  the user id which client want to login by

##### Query Parameters

| Name           | Format                         | Description  |
| :------------- | :----------------------------- | :------------|
|`userId`        | `string`                       | the user which will search in score list
|`top`           | `int`                          | number of score on top of the searched user
|`bottom`        | `int`                          | number of score at bottom of the searched user

##### Possible HTTP status codes

| Status               | Description     |
| :------------------- | :-------------- |
|`200`                 | ok              |
|`400`                 | if `applicationId` format is not valid<br/>if `top` or `bottom` is negative
|`404`                 | if there is not any score submitted for the user in the application

# Implements

## Server

There is 2 implements of this server by `Go` and `Java`.

### Go-Version Server

You can find source of this under `{project-root}/server/go-version/impl-score-app-server`.<br/>
To build this server you can use `batch` or `shell` script provided in `{project-root}/server/go-version`
directory.<br/>

- Use `build.[bat|sh]` to build from source
- Use `run.[bat|sh]` to run the built file
- Use `build_and_run.[bat|sh]` to build and run server

##### Requirements

- `Go-lang` version `1.17.x`
- Present `Go-lang` directory in `system path`
- Internet connection

<br/>

### Java-Version Server

You can find source of this under `{project-root}/server/java-version/`.<br/>
To build this server you can use `batch` or `shell` script provided in `{project-root}/server/java-version`
directory.<br/>

- Use `build.[bat|sh]` to build from source
- Use `run.[bat|sh]` to run the built file
- Use `build_and_run.[bat|sh]` to build and run server

##### Requirements

- `Java` version `11.0.x`
- Present `Java` directory in `system path`
- Installed `maven` version `3.x.y`
- Internet connection


***Note*** : Both version will ask user to config them (host, port, ...) on startup.

<br/>

## Client
There is just one implement of client in `Java` which can use both server versions.
You can find source of it under `{project-root}/client/client-app`.
To build client you can use `batch` or `shell` script provided in `{project-root}/client/`
directory.<br/>
This client will provide a command line interface to communicate with server.

- Use `build.[bat|sh]` to build from source
- Use `run.[bat|sh]` to run the built file
- Use `build_and_run.[bat|sh]` to build and run client

##### Requirements

- `Java` version `11.0.x`
- Present `Java` directory in `system path`
- Installed `maven` version `3.x.y`
- Internet connection

***Note*** : Client will ask user to config it (serve-host, server-port) on startup.
