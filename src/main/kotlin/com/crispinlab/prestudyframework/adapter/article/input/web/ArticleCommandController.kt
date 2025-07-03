package com.crispinlab.prestudyframework.adapter.article.input.web

import com.crispinlab.prestudyframework.adapter.article.input.web.extensions.getUserIdOrNull
import com.crispinlab.prestudyframework.adapter.article.input.web.request.DeleteArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.request.UpdateArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.request.WriteArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.response.ArticleResponse
import com.crispinlab.prestudyframework.application.article.ArticleCommandUseCase
import com.crispinlab.prestudyframework.common.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
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
        servletRequest: HttpServletRequest
    ): ArticleResponse<Unit> {
        val userId: Long =
            servletRequest.getUserIdOrNull()
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

    @PatchMapping(
        path = ["/articles/{id}"],
        produces = ["application/json", "application/vnd.pre-study.com-v1+json"]
    )
    fun updateArticle(
        @RequestBody @Valid request: UpdateArticleRequest,
        @PathVariable(required = true) id: Long,
        servletRequest: HttpServletRequest
    ): ArticleResponse<Unit> {
        val userId: Long =
            servletRequest.getUserIdOrNull()
                ?: return ArticleResponse.error(
                    code = ErrorCode.NOT_FOUND_SUBJECT.code,
                    message = ErrorCode.NOT_FOUND_SUBJECT.message
                )

        val response: ArticleCommandUseCase.Response =
            articleCommandUseCase.updateArticle(
                ArticleCommandUseCase.UpdateArticleRequest(
                    id = id,
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

    @DeleteMapping(
        path = ["/articles/{id}"],
        produces = ["application/json", "application/vnd.pre-study.com-v1+json"]
    )
    fun deleteArticle(
        @RequestBody @Valid request: DeleteArticleRequest,
        @PathVariable(required = true) id: Long,
        servletRequest: HttpServletRequest
    ): ArticleResponse<Unit> {
        val userId: Long =
            servletRequest.getUserIdOrNull()
                ?: return ArticleResponse.error(
                    code = ErrorCode.NOT_FOUND_SUBJECT.code,
                    message = ErrorCode.NOT_FOUND_SUBJECT.message
                )

        val response: ArticleCommandUseCase.Response =
            articleCommandUseCase.deleteArticle(
                ArticleCommandUseCase.DeleteArticleRequest(
                    id = id,
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
