package dev.anvith.binanceninja.data.remote.extensions

import dev.anvith.binanceninja.data.remote.ApiConstants
import dev.anvith.binanceninja.data.remote.core.ApiResult
import dev.anvith.binanceninja.data.remote.core.ApiResult.Failure
import dev.anvith.binanceninja.data.remote.core.ApiResult.Success
import dev.anvith.binanceninja.data.remote.models.ApiException
import dev.anvith.binanceninja.data.remote.models.UnauthorisedException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.content.TextContent
import io.ktor.http.contentType

suspend fun HttpClient.get(
  endpoint: String,
  headers: Map<String, Any> = emptyMap(),
  queryParams: Map<String, Any> = emptyMap(),
): HttpResponse {
  return get("${ApiConstants.BASE_URl}$endpoint") {
    contentType(ContentType.Application.Json)
    headers.map { (key, value) -> header(key, value) }
    queryParams.map { (key, value) -> parameter(key, value) }
  }
}

suspend fun HttpClient.post(
  endpoint: String,
  headers: Map<String, Any> = emptyMap(),
  queryParams: Map<String, Any> = emptyMap(),
  jsonBody: String,
): HttpResponse {
  return post("${ApiConstants.BASE_URl}$endpoint") {
    contentType(ContentType.Application.Json)
    headers.map { (key, value) -> header(key, value) }
    queryParams.map { (key, value) -> parameter(key, value) }
    setBody(TextContent(jsonBody, ContentType.Application.Json))
  }
}

suspend inline fun <reified T> HttpResponse.asResult(): ApiResult<T> {
  return when (status) {
    OK -> Success(body())
    Unauthorized -> Failure(UnauthorisedException(bodyAsText()))
    else -> Failure(ApiException(bodyAsText()))
  }
}
