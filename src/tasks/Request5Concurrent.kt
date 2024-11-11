package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org) // Executes request and blocks the current thread
        .also { logRepos(req, it) }
        .body() ?: emptyList()

    val deferreds : List<Deferred<List<User>>> = repos.map { repo ->
        async {
            log("starting loading for ${repo.name}")
            delay(3000)
            service
                .getRepoContributors(req.org, repo.name) // Executes request and blocks the current thread
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}