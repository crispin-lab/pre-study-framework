package com.crispinlab.prestudyframework.adapter.article.input.web

import com.crispinlab.prestudyframework.adapter.article.input.web.param.RetrieveArticlesParams
import com.crispinlab.prestudyframework.adapter.article.input.web.response.ArticleResponse
import com.crispinlab.prestudyframework.application.article.ArticleQueryUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
internal class ArticleQueryController(
    private val articleQueryUseCase: ArticleQueryUseCase
) {
    @GetMapping(
        path = ["/articles"],
        produces = ["application/json", "application/vnd.pre-study.com-v1+json"]
    )
    fun retrieveArticles(
        @ModelAttribute params: RetrieveArticlesParams
    ): ArticleResponse<ArticleQueryUseCase.RetrieveArticlesResponse> {
        val response: ArticleQueryUseCase.Response<ArticleQueryUseCase.RetrieveArticlesResponse> =
            articleQueryUseCase.retrieveArticles(
                ArticleQueryUseCase.RetrieveArticlesParams(
                    page = params.page,
                    pageSize = params.pageSize
                )
            )
        return ArticleResponse.success(
            code = response.code,
            message = response.message,
            result = response.data!!
        )
    }
}
