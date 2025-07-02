package com.crispinlab.prestudyframework.adapter.article.input.web

import com.crispinlab.prestudyframework.adapter.article.input.web.request.WriteArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.response.ArticleResponse
import com.crispinlab.prestudyframework.application.article.ArticleCommandUseCase
import com.crispinlab.prestudyframework.common.exception.ErrorCode
import jakarta.servlet.ServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
internal class ArticleCommandController(
    private val articleCommandUseCase: ArticleCommandUseCase
) {
    @PostMapping(
        path = ["/article"],
        produces = ["application/json", "application/vnd.pre-study.com-v1+json"]
    )
    fun writeArticle(
        @RequestBody @Valid request: WriteArticleRequest,
        servletRequest: ServletRequest
    ): ArticleResponse<Unit> {
        val userId: Long =
            servletRequest.getAttribute("userId") as? Long
                ?: return ArticleResponse.error(
                    code = ErrorCode.NOT_FOUND_SUBJECT.code,
                    message = ErrorCode.NOT_FOUND_SUBJECT.message
                )

        val response: ArticleCommandUseCase.Response =
            articleCommandUseCase.writeArticle(
                ArticleCommandUseCase.WriteArticleRequest(
                    title = request.title,
                    content = request.content,
                    author = userId,
                    password = request.password
                )
            )

        return ArticleResponse.success(
            code = response.code,
            message = response.message
        )
    }
}
