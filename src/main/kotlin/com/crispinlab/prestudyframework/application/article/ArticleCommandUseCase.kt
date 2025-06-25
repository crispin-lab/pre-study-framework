package com.crispinlab.prestudyframework.application.article

internal interface ArticleCommandUseCase {
    data class WriteArticleRequest(
        val title: String,
        val content: String,
        val author: Long,
        val password: String
    )

    class WriteArticleResponse private constructor(
        val code: Int,
        val message: String
    ) {
        companion object {
            fun success(): WriteArticleResponse {
                return WriteArticleResponse(code = 200, message = "success")
            }

            fun fail(message: String): WriteArticleResponse {
                return WriteArticleResponse(code = 300, message = message)
            }
        }
    }

    fun writeArticle(request: WriteArticleRequest): WriteArticleResponse
}
