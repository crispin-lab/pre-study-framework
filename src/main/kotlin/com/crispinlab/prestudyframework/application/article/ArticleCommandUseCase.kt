package com.crispinlab.prestudyframework.application.article

internal interface ArticleCommandUseCase {
    class Response private constructor(
        val code: Int,
        val message: String
    ) {
        companion object {
            fun success(): Response {
                return Response(code = 200, message = "success")
            }

            fun fail(message: String): Response {
                return Response(code = 400, message = message)
            }
        }
    }

    data class WriteArticleRequest(
        val title: String,
        val content: String,
        val author: Long,
        val password: String
    )

    data class UpdateArticleRequest(
        val id: Long,
        val title: String,
        val content: String
    )

    fun writeArticle(request: WriteArticleRequest): Response

    fun updateArticle(request: UpdateArticleRequest): Response
}
