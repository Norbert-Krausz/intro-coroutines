package tasks

import contributors.*

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit) {
    val repos = service
        .getOrgRepos(req.org) // Executes request and blocks the current thread
        .also { logRepos(req, it) }
        .bodyList()

    var allUsers = emptyList<User>()
    for ((index, repo) in repos.withIndex()){
        val users = service.getRepoContributors(req.org, repo.name) // Executes request and blocks the current thread
            .also { logUsers(repo, it) }
            .bodyList()

        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers, index == repos.lastIndex)
    }
}
//    repos.flatMap { repo ->
//        service
//            .getRepoContributors(req.org, repo.name) // Executes request and blocks the current thread
//            .also { logUsers(repo, it) }
//            .bodyList()
//    }.aggregate()
//    updateResults
// }
